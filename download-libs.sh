BASE_URL=https://dl.cryptlex.com/downloads
VERSION="v4.12.0";

mkdir -p tmp/windows
curl -O ${BASE_URL}/${VERSION}/LexFloatClient-Win.zip
unzip LexFloatClient-Win.zip -d ./tmp/windows
cp ./tmp/windows/libs/vc14/x64/LexFloatClient.dll src/main/resources/win32-x86-64/
cp ./tmp/windows/libs/vc14/x86/LexFloatClient.dll src/main/resources/win32-x86/
cp ./tmp/windows/libs/vc17/arm64/LexFloatClient.dll src/main/resources/win32-aarch64/

mkdir -p tmp/macos
curl -O ${BASE_URL}/${VERSION}/LexFloatClient-Mac.zip
unzip LexFloatClient-Mac.zip -d ./tmp/macos
cp ./tmp/macos/libs/clang/x86_64/libLexFloatClient.dylib src/main/resources/darwin-x86-64/
cp ./tmp/macos/libs/clang/arm64/libLexFloatClient.dylib src/main/resources/darwin-aarch64/

mkdir -p tmp/linux
curl -O ${BASE_URL}/${VERSION}/LexFloatClient-Linux.zip
unzip LexFloatClient-Linux.zip -d ./tmp/linux
cp ./tmp/linux/libs/gcc/amd64/libLexFloatClient.so src/main/resources/linux-x86-64/
cp ./tmp/linux/libs/gcc/i386/libLexFloatClient.so src/main/resources/linux-x86/
cp ./tmp/linux/libs/gcc/arm64/libLexFloatClient.so src/main/resources/linux-aarch64/

rm -f LexFloatClient-Win.zip
rm -f LexFloatClient-Mac.zip
rm -f LexFloatClient-Linux.zip
rm -R -f ./tmp
