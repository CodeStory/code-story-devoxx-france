#!/bin/bash

jps | awk '($2=="CodeStoryServer"){print $1}' | xargs -I {} kill -9 {}
