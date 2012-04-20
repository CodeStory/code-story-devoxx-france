#!/bin/bash

ssh -t david@jean-laurent.code-story.net "cd CodeStory; git fetch; git reset --hard origin/master; ./start.sh"
