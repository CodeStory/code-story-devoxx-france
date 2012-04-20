#!/bin/bash

killall java

mvn -q clean install -Dmaven.test.skip=true && java -classpath "target/lib/*:target/*" net.codestory.CodeStoryServer >/dev/null 2>&1 &

/usr/local/bin/wget -O - --inet4-only --retry-connrefused -t 10 --waitretry=5 "http://localhost:8080/" > /dev/null
