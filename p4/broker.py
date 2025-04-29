# broker.py
import threading
import time
from collections import deque

class Broker:
    _instance = None
    _lock = threading.Lock()

    def __init__(self):
        self.queues = {}  # {nombre_cola: deque de mensajes}
        self.consumers = {}  # {nombre_cola: lista de callbacks}
        self.last_consumer_index = {}  # {nombre_cola: índice round robin}
        self.expirations = {}  # {nombre_cola: {mensaje: timestamp_expiracion}}
        self._start_cleanup_thread()

    @classmethod
    def get_instance(cls):
        with cls._lock:
            if cls._instance is None:
                cls._instance = Broker()
        return cls._instance

    def declarar_cola(self, nombre_cola):
        if nombre_cola not in self.queues:
            self.queues[nombre_cola] = deque()
            self.consumers[nombre_cola] = []
            self.last_consumer_index[nombre_cola] = 0
            self.expirations[nombre_cola] = {}
            print(f"[Broker] Cola '{nombre_cola}' creada.")

    def publicar(self, nombre_cola, mensaje):
        if nombre_cola not in self.queues:
            print(f"[Broker] Cola '{nombre_cola}' no existe. Mensaje descartado.")
            return
        timestamp_expiracion = time.time() + 300  # 5 minutos
        self.queues[nombre_cola].append(mensaje)
        self.expirations[nombre_cola][mensaje] = timestamp_expiracion
        self._entregar_mensajes(nombre_cola)

    def consumir(self, nombre_cola, callback):
        if nombre_cola not in self.queues:
            self.declarar_cola(nombre_cola)
        self.consumers[nombre_cola].append(callback)
        print(f"[Broker] Consumidor registrado en cola '{nombre_cola}'.")
        self._entregar_mensajes(nombre_cola)

    def _entregar_mensajes(self, nombre_cola):
        while self.queues[nombre_cola] and self.consumers[nombre_cola]:
            mensaje = self.queues[nombre_cola].popleft()
            idx = self.last_consumer_index[nombre_cola] % len(self.consumers[nombre_cola])
            callback = self.consumers[nombre_cola][idx]
            callback(mensaje)
            self.last_consumer_index[nombre_cola] += 1
            if mensaje in self.expirations[nombre_cola]:
                del self.expirations[nombre_cola][mensaje]

    def _start_cleanup_thread(self):
        thread = threading.Thread(target=self._cleanup_expired_messages, daemon=True)
        thread.start()

    def _cleanup_expired_messages(self):
        while True:
            time.sleep(10)  # revisar cada 10 segundos
            now = time.time()
            for nombre_cola, expiraciones in list(self.expirations.items()):
                mensajes_a_eliminar = [m for m, exp in expiraciones.items() if now > exp]
                for mensaje in mensajes_a_eliminar:
                    if mensaje in self.queues.get(nombre_cola, deque()):
                        self.queues[nombre_cola].remove(mensaje)
                        print(f"[Broker] Mensaje expirado eliminado de cola '{nombre_cola}'.")
                    del expiraciones[mensaje]
