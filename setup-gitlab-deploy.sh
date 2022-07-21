#!/bin/bash
set -euo pipefail

GITLAB_TOKEN=$1
GITLAB_PROJECT_ID=$2
GITLAB_DISTRIB_TOKEN=$3

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=GITLAB_DIST_TOKEN" --form "value=$GITLAB_DISTRIB_TOKEN"
