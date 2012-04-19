#!/bin/bash

ssh david@jean-laurent.code-story.net "cd CodeStory && git fetch && git reset --hard origin/master && ./stop.sh  && ./start.sh" 
