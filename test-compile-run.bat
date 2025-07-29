@echo off
echo === Testing Updated Maven Plugins ===
echo.

echo 1. Cleaning and compiling...
call mvn clean compile
if %errorlevel% neq 0 (
    echo ❌ Compilation failed
    pause
    exit /b 1
)
echo ✅ Compilation successful
echo.

echo 2. Running with exec plugin...
call mvn exec:java -Dexec.mainClass="demoproject.TddAgent"
if %errorlevel% neq 0 (
    echo ❌ Exec plugin failed
    pause
    exit /b 1
)
echo ✅ Exec plugin successful
echo.

echo 3. Creating executable JAR...
call mvn clean package
if %errorlevel% neq 0 (
    echo ❌ JAR creation failed
    pause
    exit /b 1
)
echo ✅ JAR creation successful
echo.

echo 4. Running executable JAR...
java -jar target/artifact-1.0.jar
if %errorlevel% neq 0 (
    echo ❌ JAR execution failed
    pause
    exit /b 1
)
echo ✅ JAR execution successful
echo.

echo All tests passed! ✅
pause 