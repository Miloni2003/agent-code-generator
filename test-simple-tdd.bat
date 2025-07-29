@echo off
echo ========================================
echo Testing Simple TDD Agent
echo ========================================
echo.

echo 1. Compiling project...
call mvn clean compile -q

echo.
echo 2. Running Simple TDD Agent...
call mvn exec:java -Dexec.mainClass="demoproject.SimpleTddAgent" -q

echo.
echo 3. Checking generated files...
if exist generated-tests\req-001\src\test\java\*.java (
    echo ✅ Generated test files found!
    echo.
    echo === Generated Files ===
    dir generated-tests\req-001\src\test\java\*.java
    echo.
    echo === First Test File Content ===
    for %%f in (generated-tests\req-001\src\test\java\*.java) do (
        echo File: %%f
        type "%%f"
        goto :found
    )
    :found
    echo === End Test File ===
) else (
    echo ❌ No test files generated
    echo.
    echo === Directory Structure ===
    dir generated-tests /s
    echo === End Directory Structure ===
)

echo.
echo ========================================
echo Test completed.
pause 