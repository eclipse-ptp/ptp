#!/bin/bash

set -u # run with unset flag error so that missing parameters cause build failure
set -e # error out on any failed commands
set -x # echo all commands used for debugging purposes

SSH="ssh genie.ptp@projects-storage.eclipse.org"
DOWNLOAD=download.eclipse.org/tools/ptp/$PTP_PUBLISH_LOCATION
DOWNLOAD_MOUNT=/home/data/httpd/$DOWNLOAD
ARTIFACTS=https://ci.eclipse.org/ptp/job/${PROJECT}-build/$PTP_BUILD_NUMBER/artifact/

if [ "$PROJECT" == "ptp" ]; then
    ARTIFACTS_REPO_TARGET=$ARTIFACTS/releng/org.eclipse.ptp.repo/target
elif [ "$PROJECT" == "remote" ]; then
    ARTIFACTS_REPO_TARGET=$ARTIFACTS/releng/org.eclipse.remote.repo/target
elif [ "$PROJECT" == "photran" ]; then
    ARTIFACTS_REPO_TARGET=$ARTIFACTS/org.eclipse.photran.repo/target
else
    echo "Missing repo location of $PROJECT"
    exit 1
fi

echo Using download location root of "https://$DOWNLOAD"
echo Using artifacts location root of $ARTIFACTS

echo Testing to make sure we are publishing to a new directory
$SSH "test ! -e $DOWNLOAD_MOUNT"

echo Testing to make sure artifacts location is sane
wget -q --output-document=/dev/null $ARTIFACTS_REPO_TARGET

ECHO=echo
if [ "$DRY_RUN" == "false" ]; then
    ECHO=""
else
    echo Dry run of build:
fi

$ECHO $SSH "mkdir -p $DOWNLOAD_MOUNT"

$ECHO $SSH "cd $DOWNLOAD_MOUNT && \
    wget -q $ARTIFACTS_REPO_TARGET/repository/*zip*/repository.zip && \
    unzip -q repository.zip && \
    mv repository/* . && \
    rm -r repository repository.zip"

if [ "$DRY_RUN" == "false" ]; then
    echo Release uploaded to "https://$DOWNLOAD"
else
    echo Dry run completed.
fi
