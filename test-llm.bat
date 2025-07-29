@echo off
echo Testing LLM Integration...
echo.

echo Compiling the project...
call mvn clean compile -q

echo.
echo Running TDD Agent...
call mvn exec:java -Dexec.mainClass="demoproject.TddAgent" -q

echo.
echo Test completed.
pause 