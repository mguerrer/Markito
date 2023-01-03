set user=marcos150
set pwd=uGotcDU7y8nn9V8tnJcS
echo "Uploading apps to BrowserStack"
curl -u %user%:%pwd% -X POST "https://api-cloud.browserstack.com/app-automate/upload" -F "file=@apps/LocalSample.apk"
curl -u %user%:%pwd% -X POST "https://api-cloud.browserstack.com/app-automate/upload" -F "file=@apps/LocalSample.ipa"
curl -u %user%:%pwd% -X POST "https://api-cloud.browserstack.com/app-automate/upload" -F "file=@apps/WikipediaSample.apk"