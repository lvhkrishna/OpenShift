#!/bin/bash

# This deploy hook gets executed after dependencies are resolved and the
# build hook has been run but before the application has been started back
# up again.

# create the uploads directory if it doesn't exist
if [ ! -d ${OPENSHIFT_DATA_DIR}upload ]; then
    mkdir ${OPENSHIFT_DATA_DIR}upload
fi

# create symlink to uploads directory
ln -s ${OPENSHIFT_DATA_DIR}upload ${OPENSHIFT_REPO_DIR}webapps/