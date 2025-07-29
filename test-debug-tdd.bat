@echo off
echo ========================================
echo Testing Debug TDD Agent
echo ========================================
echo.

echo 1. Compiling project...
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ Compilation failed
    pause
    exit /b 1
)
echo ✅ Compilation successful
echo.

echo 2. Running DebugTddAgent...
echo This will test each step of the TDD process
echo.

java -cp "target/classes;target/dependency/*" demoproject.DebugTddAgent

echo.
echo 3. Checking if files were generated...
if exist generated-tests (
    echo ✅ Generated tests folder found
    echo.
    echo === Generated Files ===
    dir generated-tests /s
    echo === End Generated Files ===
) else (
    echo ❌ No generated tests folder found
)

echo.
echo ========================================
echo Debug TDD Test Completed
echo ========================================
pause 