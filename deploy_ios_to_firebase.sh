mv dist/$CHANGELOG_FILE ios-app/
cp dist/$XCZIP_NAME ios-app/

cd ios-app
mkdir fastlane || true
curl $FASTFILE_URL --output fastlane/Fastfile
fastlane deploy_firebase