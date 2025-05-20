#!/bin/bash

minikube start \
  --profile=medimap-cluster \
  --kubernetes-version=v1.32.0 \
  --driver=docker \
  --cpus=2 \
  --memory=4g

