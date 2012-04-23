#!/bin/bash

set PORT=8085

mvn -q clean install -Dmaven.test.skip=true && java -cp "target/lib/*:target/*" net.codestory.CodeStoryServer
