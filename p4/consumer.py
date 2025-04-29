# consumer.py

import socket  
import time    

# Configuración del host y puerto donde está escuchando el broker
HOST = "127.0.0.1"
PORT = 5000

def tarea_lenta(sock, mensaje):
    """
    Función que simula el procesamiento de un mensaje:
    - Duerme (sleep) tantos segundos como puntos (.) tenga el mensaje.
    - Envía un ACK al broker cuando termina de procesar.
    """
    print(f"[Consumer] Empezando mensaje: {mensaje}")
    trabajo = mensaje.count('.')  # Número de segundos = número de puntos en el mensaje
    time.sleep(trabajo)            # Simula el trabajo
    print(f"[Consumer] Trabajo terminado después de {trabajo} segundos.")

    # Enviar ACK al broker indicando que el mensaje fue procesado
    try:
        ack_mensaje = f"ACK:{mensaje}"
        sock.sendall(ack_mensaje.encode())
    except Exception as e:
        print(f"[Consumer] Error al enviar ACK: {e}")

def main():
    """
    Función principal:
    - Crea un socket TCP y se conecta al broker.
    - Se registra como consumidor.
    - Escucha indefinidamente mensajes del broker para procesarlos.
    """
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # Crea el socket TCP
    sock.connect((HOST, PORT))                                # Se conecta al broker

    sock.sendall("CONSUMER".encode())  # Se identifica enviando el rol "CONSUMER" al broker

    while True:
        try:
            data = sock.recv(1024)  # Espera a recibir un mensaje del broker
            if not data:
                # Si no se recibe datos (el broker cierra la conexión), salir del bucle
                print("[Consumer] Broker cerró conexión. Cerrando consumidor...")
                break
            mensaje = data.decode()  # Decodifica el mensaje recibido
            tarea_lenta(sock, mensaje)  # Procesa el mensaje recibido
        except Exception as e:
            # Captura errores de conexión o recepción
            print(f"[Consumer] Error: {e}")
            break

    sock.close()  # Cierra el socket TCP al salir del bucle

if __name__ == "__main__":
    main()  # Ejecuta la función principal si el script es ejecutado directamente
