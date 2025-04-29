# broker.py

import socket  
import threading  
import time  
from collections import deque

# Configuración de red
HOST = "127.0.0.1"  # IP de localhost
PORT = 5000         # Puerto donde el broker escucha

class Broker:
    """
    Clase Broker:
    Gestiona productores y consumidores, colas de mensajes,
    distribución de mensajes a consumidores usando fair dispatch.
    """

    def __init__(self):
        self.queues = {"task_queue": deque()}  # Diccionario de colas: nombre -> deque de mensajes
        self.consumers = []  # Lista de consumidores conectados: (socket, ocupado)
        self.lock = threading.Lock()  # Lock para sincronizar acceso a datos compartidos

    def start(self):
        """
        Arranca el servidor broker:
        - Abre un socket TCP.
        - Acepta conexiones entrantes y crea un hilo por cada conexión.
        """
        server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_socket.bind((HOST, PORT))
        server_socket.listen()
        print(f"[Broker] Escuchando en {HOST}:{PORT}")

        while True:
            conn, addr = server_socket.accept()
            print(f"[Broker] Nueva conexión desde {addr}")
            threading.Thread(target=self.handle_client, args=(conn,), daemon=True).start()

    def handle_client(self, conn):
        """
        Maneja una nueva conexión:
        - Identifica si el cliente es productor o consumidor.
        - Redirige la conexión al handler correspondiente.
        """
        try:
            role = conn.recv(1024).decode()
            if role == "PRODUCER":
                self.handle_producer(conn)
            elif role == "CONSUMER":
                self.handle_consumer(conn)
            else:
                conn.close()
        except Exception as e:
            print(f"[Broker] Error: {e}")
            conn.close()

    def handle_producer(self, conn):
        """
        Gestiona la comunicación con un productor:
        - Recibe mensajes de publicación o comandos especiales (LIST, DELETE).
        - Inserta mensajes en las colas correspondientes.
        """
        while True:
            try:
                data = conn.recv(1024).decode()
                if not data:
                    break

                if data == "LIST":
                    with self.lock:
                        colas = list(self.queues.keys())
                    print(f"[Broker] Listado de colas: {colas}")
                    conn.sendall(("Colas existentes: " + ", ".join(colas)).encode())

                elif data.startswith("DELETE:"):
                    nombre_cola = data.split(":", 1)[1]
                    with self.lock:
                        if nombre_cola in self.queues:
                            del self.queues[nombre_cola]
                            print(f"[Broker] Cola '{nombre_cola}' eliminada.")
                            conn.sendall(f"Cola '{nombre_cola}' eliminada.".encode())
                        else:
                            conn.sendall(f"No existe la cola '{nombre_cola}'.".encode())

                else:
                    # Publicar mensaje normal en la cola "task_queue"
                    with self.lock:
                        if "task_queue" not in self.queues:
                            self.queues["task_queue"] = deque()
                        self.queues["task_queue"].append(data)
                    print(f"[Broker] Mensaje recibido de productor: {data}")
                    self.distribute_messages()

            except Exception as e:
                print(f"[Broker] Error en productor: {e}")
                break
        conn.close()

    def handle_consumer(self, conn):
        """
        Gestiona la comunicación con un consumidor:
        - Añade el consumidor a la lista de consumidores (marcado como libre inicialmente).
        - Escucha mensajes ACK enviados por el consumidor.
        """
        with self.lock:
            self.consumers.append((conn, False))  # Consumidor inicialmente libre

        try:
            while True:
                data = conn.recv(1024)
                if not data:
                    print("[Broker] Consumidor desconectado.")
                    break
                decoded = data.decode()
                if decoded.startswith("ACK:"):
                    print(f"[Broker] Recibido ACK de consumidor: {decoded[4:]}")
                    self.mark_consumer_free(conn)
        except Exception as e:
            print(f"[Broker] Error en consumidor: {e}")
        finally:
            conn.close()

    def mark_consumer_free(self, conn):
        """
        Marca a un consumidor como libre después de recibir un ACK:
        - Busca el consumidor en la lista y actualiza su estado.
        - Intenta repartir mensajes pendientes ahora que hay un consumidor disponible.
        """
        with self.lock:
            for idx, (c, ocupado) in enumerate(self.consumers):
                if c == conn:
                    self.consumers[idx] = (c, False)
                    break
        self.distribute_messages()

    def distribute_messages(self):
        """
        Distribuye mensajes desde las colas a consumidores:
        - Solo asigna mensajes a consumidores libres.
        - Marca al consumidor como ocupado después de enviar el mensaje.
        """
        with self.lock:
            # Verifica si hay mensajes y consumidores libres
            while self.queues.get("task_queue") and any(not ocupado for _, ocupado in self.consumers):
                mensaje = self.queues["task_queue"].popleft()

                n = len(self.consumers)
                for i in range(n):
                    conn, ocupado = self.consumers.pop(0)
                    if not ocupado:
                        try:
                            conn.sendall(mensaje.encode())
                            # Marcar consumidor como ocupado
                            self.consumers.append((conn, True))
                            print("[Broker] Mensaje enviado a consumidor.")
                            break  # Mensaje enviado correctamente
                        except:
                            print("[Broker] Error al enviar a consumidor, cerrando conexión.")
                            conn.close()
                    else:
                        # Consumidor ocupado, se vuelve a poner al final
                        self.consumers.append((conn, ocupado))

if __name__ == "__main__":
    broker = Broker()
    broker.start()
