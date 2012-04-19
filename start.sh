#!/bin/bash

mvn -q clean install -Dmaven.test.skip=true && java -classpath "target/lib/*:target/*" CodeStoryServer
