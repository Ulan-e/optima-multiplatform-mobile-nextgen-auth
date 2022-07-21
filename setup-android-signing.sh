#!/bin/bash
set -euo pipefail

KEY_ALIAS=$1
GITLAB_TOKEN=$2
GITLAB_PROJECT_ID=$3

PASSWORD=$(openssl rand -base64 16)
KEYSTORE_PATH="android-app/signing/release.jks"

mkdir -p "android-app/signing" || true

keytool -genkey -v -keystore $KEYSTORE_PATH -storepass "$PASSWORD" \
 -alias "$KEY_ALIAS" -keypass "$PASSWORD" -dname "o=IceRock Development, c=RU" \
 -keyalg RSA -sigalg SHA256withRSA -keysize 2048 -validity 10000

echo "created store at: $KEYSTORE_PATH"
echo "key alias: $KEY_ALIAS"
echo "store password: $PASSWORD"
echo "key password: $PASSWORD"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=RELEASE_KEY_ALIAS" --form "value=$KEY_ALIAS"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=RELEASE_STORE_PASSWORD" --form "value=$PASSWORD"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=RELEASE_KEY_PASSWORD" --form "value=$PASSWORD"
