#!/bin/sh

echo "Building project..."
./gradlew clean build

echo "Running services..."
docker compose up


