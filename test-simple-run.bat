@echo off
echo Testing TDD Agent with Java 21...
echo.

REM Set Java 21 path
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo Java version:
java -version

echo.
echo Compiling project...
call mvn clean compile -q

echo.
echo Running TestGenerationAgent...
call mvn exec:java -Dexec.mainClass="demoproject.TestGenerationAgent" -q

echo.
echo Test completed.
pause 