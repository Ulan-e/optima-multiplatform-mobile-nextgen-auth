#!/bin/bash
set -euo pipefail

PROJECT_ID=$1
APPLICATION_ID=$2
PROD_APPLICATION_ID=$3

firebase login

# Android applications
firebase apps:create --project $PROJECT_ID -a ${PROD_APPLICATION_ID} ANDROID "Android Prod Release"
firebase apps:create --project $PROJECT_ID -a ${PROD_APPLICATION_ID}.debug ANDROID "Android Prod Debug"
firebase apps:create --project $PROJECT_ID -a ${APPLICATION_ID}.stage ANDROID "Android Stage Release"
firebase apps:create --project $PROJECT_ID -a ${APPLICATION_ID}.stage.debug ANDROID "Android Stage Debug"
firebase apps:create --project $PROJECT_ID -a ${APPLICATION_ID}.dev ANDROID "Android Dev Release"
firebase apps:create --project $PROJECT_ID -a ${APPLICATION_ID}.dev.debug ANDROID "Android Dev Debug"

# iOS applications
firebase apps:create --project $PROJECT_ID -b ${PROD_APPLICATION_ID} -s '' IOS "iOS Prod"
firebase apps:create --project $PROJECT_ID -b ${APPLICATION_ID}.stage -s '' IOS "iOS Stage"
firebase apps:create --project $PROJECT_ID -b ${APPLICATION_ID}.dev -s '' IOS "iOS Dev"