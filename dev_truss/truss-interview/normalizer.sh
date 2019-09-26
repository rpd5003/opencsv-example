#!/bin/bash

if [ $# -ne 2 ]
  then
    echo "Usage: ./normalizer full_path_to_input.csv full_path_to_output.csv"
	exit 1
fi

cd app
mvn clean install
java -jar target/normalizer.jar $1 $2
