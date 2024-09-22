#!/bin/sh

echo "Initializing AWS SQS queues..."

awslocal sqs create-queue --queue-name total-payment-queue
awslocal sqs create-queue --queue-name partial-payment-queue
awslocal sqs create-queue --queue-name surplus-payment-queue

echo "AWS SQS queues initialized."