import socket
import time

HOST = "broker"
PORT = 5000

def main():
    """
    Producer persistente:
    - Se conecta una sola vez al broker.
    - Permite enviar múltiples mensajes seguidos.
    - Cierra la conexión cuando el usuario lo decide.
    """
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect((HOST, PORT))

    sock.sendall("PRODUCER".encode())  # Enviar rol al broker
    time.sleep(0.5)  # Pequeña espera para asegurar la identificación

    print("[Producer] Conectado al broker. Escribe mensajes para enviar. Escribe 'exit' para salir.")
    while True:
        mensaje = input(">> ").strip()
        if mensaje.lower() == "exit":
            print("[Producer] Saliendo...")
            break
        if mensaje:
            sock.sendall(mensaje.encode())
            print(f"[Producer] Mensaje enviado: {mensaje}")
            if mensaje.upper().startswith("LIST") or mensaje.upper().startswith("DELETE"):
                response = sock.recv(1024).decode()
                print(f"[Producer] Respuesta del broker: {response}")

    sock.close()

if __name__ == "__main__":
    main()