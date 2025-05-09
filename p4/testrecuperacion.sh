#!/bin/bash
# test_durability.sh
# Este script prueba el sistema de mensajería con énfasis en la durabilidad

set -e

echo "===== FASE 1: Iniciar sistema y enviar mensajes normales y durables ====="

echo "Arrancando contenedores en modo detach..."
docker-compose up -d

echo "Esperando a que los contenedores se inicien..."
sleep 3

echo "Enviando comandos a producer1 (mensajes normales)..."
docker exec -i producer1 bash -c "printf 'CREATE:cola_prueba\nLIST\nTarea-P1..\nMensaje-P1 .\nexit\n' | python producer.py" &

echo "Enviando comandos a producer2 (mensajes durables)..."
docker exec -i producer2 bash -c "printf 'LIST\nDURABLE:cola_importante:Mensaje durable 1\nDURABLE:cola_importante:Mensaje durable 2......\nTarea-P2.\nexit\n' | python producer.py" &

echo "Esperando a que se distribuyan y procesen los mensajes..."
sleep 7

echo "===== Logs antes del reinicio ====="
echo "--- broker ---"
docker-compose logs broker
echo "--- Consumidores ---"
docker-compose logs consumer1 | tail -n 10
docker-compose logs consumer2 | tail -n 10

echo "===== FASE 2: Reinicio del broker para comprobar durabilidad ====="

echo "Deteniendo el broker..."
docker-compose stop broker

echo "Esperando 3 segundos..."
sleep 3

echo "Reiniciando el broker..."
docker-compose start broker

echo "Esperando a que el broker se reinicie..."
sleep 5

echo "Enviando comandos a producer3 (después del reinicio)..."
docker exec -i producer3 bash -c "printf 'LIST\nTarea-P3.\nTrabajo-P3 .\nexit\n' | python producer.py" &

echo "Esperando a que se distribuyan y procesen los mensajes..."
sleep 7

echo "===== Logs después del reinicio ====="
echo "--- broker (después del reinicio) ---"
docker-compose logs --since=5m broker | tail -n 30
echo "--- Consumidores (después del reinicio) ---"
docker-compose logs consumer3 | tail -n 10
docker-compose logs consumer4 | tail -n 10

echo "===== Prueba completada ====="