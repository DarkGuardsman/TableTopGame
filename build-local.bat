@echo off
cd ..
gradlew clean build -Plocal=true --refresh-dependencies
echo "Exit?"
pause > nul