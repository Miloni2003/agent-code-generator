@echo off
echo ========================================
echo Running Simple TDD Test
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

echo 2. Running SimpleTddTest...
echo This will test if the TDD agent can run and generate files
echo.

java -cp "target/classes;target/dependency/*" demoproject.SimpleTddTest

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
echo Simple TDD Test Completed
echo ========================================
pause 