@echo off
echo === Testing TDD Agent (Maven Version) ===
echo.

echo Compiling the project...
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ Compilation failed
    pause
    exit /b 1
)
echo ✅ Compilation successful
echo.

echo Running TDD Agent with Maven...
call mvn exec:java -Dexec.mainClass="demoproject.TddAgent" -q

echo.
echo Test completed.
pause 