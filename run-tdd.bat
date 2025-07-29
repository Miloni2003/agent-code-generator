@echo off
echo ========================================
echo    LLM-Powered TDD Agent
echo ========================================
echo.

REM Check if Java is available
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 11 or higher
    pause
    exit /b 1
)

REM Check if Maven is available
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo Building project...
call mvn clean compile -q
if errorlevel 1 (
    echo ERROR: Build failed
    pause
    exit /b 1
)

echo.
echo Starting TDD Agent...
echo.

REM Run the TDD agent with default configuration
java -cp target/classes demoproject.TddAgent %*

echo.
echo TDD Agent completed.
pause 