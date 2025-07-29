@echo off
echo ========================================
echo Testing TDD Agent - Simple Run
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
echo This should generate Java files in generated-tests folder
echo.

call mvn exec:java

echo.
echo 3. Checking generated files...
if exist generated-tests (
    echo ✅ Generated tests folder found
    echo.
    echo === Generated Files Structure ===
    dir generated-tests /s
    echo === End Generated Files ===
    echo.
    
    if exist generated-tests\req-001\src\main\java\generated\code\*.java (
        echo ✅ Generated code files found
        echo.
        echo === First Generated Code File ===
        for %%f in (generated-tests\req-001\src\main\java\generated\code\*.java) do (
            echo File: %%f
            type "%%f"
            goto :found
        )
        :found
        echo === End Generated Code ===
    ) else (
        echo ❌ No generated code files found
        echo.
        echo === Checking what was created ===
        dir generated-tests\req-001\src\main\java /s
        echo === End Check ===
    )
) else (
    echo ❌ No generated tests folder found
    echo.
    echo === Current Directory Contents ===
    dir
    echo === End Directory Contents ===
)

echo.
echo ========================================
echo TDD Agent Test Completed
echo ========================================
pause 