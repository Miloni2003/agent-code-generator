@echo off
echo === Testing TDD Agent ===
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

echo Running TDD Agent...
call mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass="demoproject.TddAgent" 2>&1
if %errorlevel% neq 0 (
    echo ❌ TDD Agent failed with error code: %errorlevel%
    echo.
    echo Please check:
    echo 1. Is the Google API key valid?
    echo 2. Are there requirements files in the requirements folder?
    echo 3. Check the logs above for specific errors
) else (
    echo ✅ TDD Agent completed successfully
)

echo.
echo Test completed.
pause 