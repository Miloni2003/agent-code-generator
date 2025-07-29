@echo off
echo === Testing API Key Integration ===
echo.

echo Compiling...
call mvn clean compile -q

echo.
echo Testing API key...
call mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass="demoproject.DebugAgent" 2>&1

echo.
echo Test completed.
pause 