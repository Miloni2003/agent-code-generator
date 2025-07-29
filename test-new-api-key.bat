@echo off
echo ========================================
echo Testing New API Key with TDD Agent
echo ========================================
echo.

echo Current API Key: AIzaSyAxm3ARZByaUaOxZR76ErH8XC7DQ1RrcfQ
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

echo 2. Running TDD Agent with new API key...
echo This will test if the new API key generates real code instead of TODO templates.
echo.

call mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass="demoproject.TddAgent" -q

echo.
echo 3. Checking if real code was generated...
if exist generated-tests (
    echo ✅ Generated tests folder found
    echo.
    echo === Generated Files ===
    dir generated-tests /s
    echo === End Generated Files ===
    echo.
    
    if exist generated-tests\req-001\src\main\java\generated\code\*.java (
        echo ✅ Generated code files found
        echo.
        echo === Generated Code Content ===
        type generated-tests\req-001\src\main\java\generated\code\*.java
        echo === End Generated Code ===
        echo.
        
        findstr /C:"TODO" generated-tests\req-001\src\main\java\generated\code\*.java >nul
        if %errorlevel% equ 0 (
            echo ❌ Still contains TODO templates - API key may not be working
        ) else (
            echo ✅ Real code generated - API key is working!
        )
    ) else (
        echo ❌ No generated code files found
    )
) else (
    echo ❌ No generated tests folder found - TDD agent may have failed
)

echo.
echo ========================================
echo API Key Test Completed
echo ========================================
pause 