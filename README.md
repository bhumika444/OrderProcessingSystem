## Kafka (Local)

> Make sure Docker Desktop is running before executing the commands below.
 ```bash
> docker ps
```
### Start Kafka + Zookeeper
```bash

docker compose -f docker/docker-compose.yml up -d
docker ps
```

## Create topic: orders.created
```bash
docker exec -it order-kafka kafka-topics \
  --bootstrap-server localhost:9092 \
  --create --topic orders.created --partitions 1 --replication-factor 1
```

## List topics
```bash
docker exec -it order-kafka kafka-topics --bootstrap-server localhost:9092 --list
```

## Consume messages (Terminal 1)
```bash
docker exec -it order-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic orders.created \
  --from-beginning
```

## Produce messages (Terminal 2)
```bash
docker exec -it order-kafka kafka-console-producer \
  --bootstrap-server localhost:9092 \
  --topic orders.created
```

##Stop
```bash
docker compose -f docker/docker-compose.yml down
```
