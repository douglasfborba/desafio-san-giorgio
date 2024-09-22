#!/bin/sh

echo "Building project..."
cd san_giorgio-api && ./gradlew build

echo "Running services..."
docker compose up


