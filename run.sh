#!/bin/sh

echo "Initializing docker compose"
docker compose -f others/docker/docker-compose.yaml up -d