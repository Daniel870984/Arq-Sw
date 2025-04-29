import pika

# Conexión al servidor RabbitMQ (local por defecto)
connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

# Asegura que la cola exista
channel.queue_declare(queue='hello')

# Envía un mensaje
channel.basic_publish(exchange='',
                      routing_key='hello',
                      body='Hello World!')
print(" [x] Sent 'Hello World!'")

# Cierra conexión
connection.close()
