#!/bin/sh
docker build . -t lambda_parameter_store_expl
mkdir -p build
docker run --rm --entrypoint cat lambda_parameter_store_expl /home/application/function.zip > build/function.zip
