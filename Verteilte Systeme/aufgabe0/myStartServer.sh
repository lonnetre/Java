#!/bin/bash

# TODO: Set port with first argument of bash script
PORT=$1

if [[ -z $PORT ]]; then
  echo "Port must be set!"
  exit 1
fi

# TODO: Add java command to start chat server
java -cp /proj/i4vs/pub/aufgabe0/sirexa.jar vsue.chat.VSSirexa $PORT
