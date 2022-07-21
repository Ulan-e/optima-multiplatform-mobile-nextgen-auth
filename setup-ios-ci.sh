#!/bin/bash
set -euo pipefail

GITLAB_TOKEN=$1
GITLAB_PROJECT_ID=$2
FASTLANE_PASSWORD=$3
MATCH_PASSWORD=$4

FASTLANE_USER="apple@icerockdev.com"
FASTLANE_TEAM_ID="RF9T8S9829"
MATCH_GIT_BRANCH="mobiledevelopment"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=FASTLANE_PASSWORD" --form "value=$FASTLANE_PASSWORD"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=FASTLANE_USER" --form "value=$FASTLANE_USER"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=MATCH_PASSWORD" --form "value=$MATCH_PASSWORD"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=FASTLANE_TEAM_ID_DEV" --form "value=$FASTLANE_TEAM_ID"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=FASTLANE_TEAM_ID_STAGE" --form "value=$FASTLANE_TEAM_ID"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=FASTLANE_TEAM_ID_PROD" --form "value=$FASTLANE_TEAM_ID"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=MATCH_GIT_BRANCH_DEV" --form "value=$MATCH_GIT_BRANCH"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=MATCH_GIT_BRANCH_STAGE" --form "value=$MATCH_GIT_BRANCH"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=MATCH_GIT_BRANCH_PROD" --form "value=$MATCH_GIT_BRANCH"
