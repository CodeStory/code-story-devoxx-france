#!/bin/bash

export NODE_PATH=/usr/local/lib/node_modules
export PATH=$PATH:/usr/local/bin
export PORT=$2

mocha --ui qunit $1
