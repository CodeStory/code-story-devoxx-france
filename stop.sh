#!/bin/bash

jps | awk '($2=="net.codestory.CodeStoryServer"){print $1}' | xargs -I {} kill -9 {}
