@echo off
echo ========================================
echo Running TDD Agent End-to-End
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

echo 2. Running TDD Agent...
call mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass="demoproject.TddAgent" -q
if %errorlevel% neq 0 (
    echo ❌ TDD Agent failed
    pause
    exit /b 1
)
echo ✅ TDD Agent completed
echo.

echo 3. Checking generated files...
if exist generated-tests (
    echo ✅ Generated tests folder found
    dir generated-tests /s
) else (
    echo ❌ No generated tests folder found
)
echo.

echo 4. Checking generated code quality...
if exist generated-tests\req-001\src\main\java\generated\code\*.java (
    echo ✅ Generated code files found
    echo.
    echo === Generated Code Content ===
    type generated-tests\req-001\src\main\java\generated\code\*.java
    echo === End Generated Code ===
) else (
    echo ❌ No generated code files found
)
echo.

echo ========================================
echo End-to-End Test Completed
echo ========================================
pause 