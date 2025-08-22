#!/bin/bash

if [ $# -ne 2 ]; then
    echo "Usage: ./startClient.sh <hostname> <port>"
    exit 1
fi

HOSTNAME=$1
PORT=$2

java -cp ~/ SirexaClient $HOSTNAME $PORT
