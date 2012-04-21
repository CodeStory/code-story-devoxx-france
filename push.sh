#!/bin/bash

function alert_user {
	echo "${1}"
	which -s growlnotify && growlnotify "Build incassable" -m "${1}" -H jean-laurent.code-story.net -P polka --image /Users/dgageot/Dropbox/Photos/code_story.jpg -d CodeStory ${2}
}

function exit_ko {
	alert_user "${1}"; exit 1
}

function exit_ok {
	alert_user "${1}"; exit 0
}

alert_user "Building CodeStory..." "-s"

LOCATION=$(pwd)
REMOTE=${1:-origin}
REMOTE_URL=$(git remote show -n ${REMOTE} | awk '/Fetch/ {print $3}')
BRANCH=$(git symbolic-ref -q HEAD)
BRANCH=${BRANCH##refs/heads/}

# Git black magic to pull rebase even with uncommited work in progress
git fetch ${REMOTE}
git add -A; git ls-files --deleted -z | xargs -0 -I {} git rm {}; git commit -m "wip"
git rebase ${REMOTE}/${BRANCH}

if [ $? -ne 0 ]; then
	git rebase --abort
	git log -n 1 | grep -q -c "wip" && git reset HEAD~1
	exit_ko "Unable to rebase. please pull or rebase and fix conflicts manually."
fi
git log -n 1 | grep -q -c "wip" && git reset HEAD~1

# Private build
rm -Rf ../privatebuild
git clone -slb "${BRANCH}" . ../privatebuild
cd ../privatebuild

# Build with maven
mvn install
if [ $? -ne 0 ]; then
	exit_ko "Unable to build"
fi

# Push
git push ${REMOTE_URL} ${BRANCH}
if [ $? -ne 0 ]; then
	exit_ko "Unable to push"
fi

# Update working directory
cd ${LOCATION} && git fetch ${REMOTE}
exit_ok "Yet another successful build!"
