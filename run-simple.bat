@echo off
echo === Running TDD Agent (Simple Method) ===
echo.

echo Compiling...
call mvn clean compile -q

echo.
echo Running TDD Agent...
call mvn exec:java -Dexec.mainClass="demoproject.TddAgent" -q

echo.
echo Completed.
pause 