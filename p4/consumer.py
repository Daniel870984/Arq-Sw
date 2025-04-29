# consumer.py
from broker import Broker
import time

def procesar_mensaje(mensaje):
    print(f"[Consumer] Mensaje recibido: {mensaje}")

def main():
    broker = Broker.get_instance()
    cola = "task_queue"
    broker.declarar_cola(cola)
    broker.consumir(cola, procesar_mensaje)

    # Mantener al consumidor vivo
    while True:
        time.sleep(1)

if __name__ == "__main__":
    main()
