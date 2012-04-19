#!/bin/bash

mvn -q clean install -Dmaven.test.skip=true && java -classpath "target/lib/*:target/*" net.codestory.CodeStoryServer &

wget --retry-connrefused --waitretry=5 "http://localhost:8080/"
