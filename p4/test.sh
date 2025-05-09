#!/bin/bash
# test_flow.sh
# Este script levanta los contenedores, envía comandos a todos los productores y luego muestra los logs

set -e

echo "Arrancando contenedores en modo detach..."
docker-compose up -d

echo "Esperando a que los contenedores se inicien..."
sleep 2

echo "Enviando comandos a producer1..."
docker exec -i producer1 bash -c "printf 'LIST\nTarea-P1..............\nMensaje-P1 .\nTrabajo-P1 ...\nFin-P1 ...\nexit\n' | python producer.py" &

echo "Enviando comandos a producer2..."
docker exec -i producer2 bash -c "printf 'LIST\nTarea-P2...............\nMensaje-P2 .\nTrabajo-P2 .\nFin-P2 .\nexit\n' | python producer.py" &

echo "Enviando comandos a producer3..."
docker exec -i producer3 bash -c "printf 'LIST\nTarea-P3...............\nMensaje-P3 ..\nTrabajo-P3 .\nFin-P3 .\nexit\n' | python producer.py" &

echo "Enviando comandos a producer4..."
docker exec -i producer4 bash -c "printf 'LIST\nTarea-P4...\nMensaje-P4 ..\nTrabajo-P4 ..nFin-P4 .\nexit\n' | python producer.py" &

echo "Esperando a que se distribuyan y procesen los mensajes..."
sleep 15

echo "=== Logs del contenedor broker ==="
docker-compose logs broker

echo "=== Logs de los productores ==="
echo "--- producer1 ---"
docker-compose logs producer1
echo "--- producer2 ---"
docker-compose logs producer2
echo "--- producer3 ---"
docker-compose logs producer3
echo "--- producer4 ---"
docker-compose logs producer4

echo "=== Logs de los consumidores ==="
echo "--- consumer1 ---"
docker-compose logs consumer1
echo "--- consumer2 ---"
docker-compose logs consumer2
echo "--- consumer3 ---"
docker-compose logs consumer3
echo "--- consumer4 ---"
docker-compose logs consumer4