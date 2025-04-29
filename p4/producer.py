# producer.py
from broker import Broker
import sys

def main():
    broker = Broker.get_instance()
    cola = "task_queue"
    broker.declarar_cola(cola)
    mensaje = ' '.join(sys.argv[1:]) or "Hello World!"
    broker.publicar(cola, mensaje)
    print(f"[Producer] Mensaje enviado: {mensaje}")

if __name__ == "__main__":
    main()
