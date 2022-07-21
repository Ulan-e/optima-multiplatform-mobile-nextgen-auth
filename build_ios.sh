export BUILD_NUMBER=$(echo $BITBUCKET_TAG | rev | cut -d '/' -f1 | rev)
fastlane run changelog_from_git_commits tag_match_pattern:build/* | grep -E '^([a-zA-Z]*-.*)' | sed 's/^\([a-zA-Z]*-[0-9]*\).*$/\1/' | sort | uniq |  tr '\n' ',' | sed 's/\(.*\),/\1/' > ${CHANGELOG_FILE} || true
cd ./ios-app
pod install --repo-update
cd ..
./gradlew syncMultiPlatformLibraryReleaseFrameworkIosArm64
cd ./ios-app
mkdir fastlane || true
curl $FASTFILE_URL --output fastlane/Fastfile
fastlane build

cd ..
mkdir –p dist
cp $CHANGELOG_FILE dist/
cp ios-app/$XCZIP_NAME dist/