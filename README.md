# Warehouse Monitoring System

A distributed system for monitoring temperature and humidity across multiple warehouses.

## Overview
The system consists of several Spring Boot microservices communicating via RabbitMQ and Redis.

### Components
- **warehouse** – receives UDP sensor data and publishes readings to RabbitMQ
- **central** – consumes messages from RabbitMQ and stores latest values in Redis
- **analytics** – provides a REST API to view current readings from Redis
- **rabbitmq** – message broker
- **redis** – data store

## Architecture

[Sensor] → UDP → [Warehouse] → RabbitMQ → [Central] → Redis → [Analytics API]


## Running with Docker

docker compose up --build

## Test that everything is working

1) **Send a test UDP packet**

    `echo "sensor_id=123;value=42.5" | nc -u localhost 3344`

2) **Viewing Alerts**

    When a temperature or humidity reading exceeds a configured threshold
    (defaults: TEMP_THRESHOLD=35, HUM_THRESHOLD=50),
    the central service logs a warning like this:

    `(!) TEMP ALERT 42.5>35.0 @ wh1`

    To view alerts:

    `docker compose logs -f central`

3) **View analytics(TBD)** 

    http://localhost:8090/api/readings

**Author**: Oleksii Saiko  
**Date**: 15 October 2025


