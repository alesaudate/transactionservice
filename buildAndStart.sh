#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

gradle_return_code=0
"$SCRIPT_DIR"/gradlew -b "$SCRIPT_DIR/build.gradle.kts" clean build check bootBuildImage --imageName=alesaudate/transactionservice:latest;gradle_return_code=$?

if [ $gradle_return_code = 0 ]; then
  docker-compose -f "$SCRIPT_DIR/docker-compose.yml" up
fi