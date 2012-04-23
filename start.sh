#!/bin/bash

killall java

nohup mvn -q clean install -Dmaven.test.skip=true && java -classpath "target/lib/*:target/*" net.codestory.CodeStoryServer < /dev/null > /dev/null 2>&1 &

/usr/local/bin/wget -O - --inet4-only --retry-connrefused -t 10 --waitretry=5 "http://localhost:8085/" > /dev/null

rm nohup.out

echo "Deploy successful"
exit 0
