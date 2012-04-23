#!/bin/bash

mvn -q clean install -Dmaven.test.skip=true && java -cp "target/lib/*:target/*" net.codestory.CodeStoryServer