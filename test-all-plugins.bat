@echo off
echo ========================================
echo Testing All Updated Maven Plugins
echo ========================================
echo.

echo 1. Testing Maven Clean Plugin...
call mvn clean
if %errorlevel% neq 0 (
    echo ❌ Clean plugin failed
    pause
    exit /b 1
)
echo ✅ Clean plugin successful
echo.

echo 2. Testing Maven Compiler Plugin...
call mvn compile
if %errorlevel% neq 0 (
    echo ❌ Compiler plugin failed
    pause
    exit /b 1
)
echo ✅ Compiler plugin successful
echo.

echo 3. Testing Maven Resources Plugin...
call mvn resources:resources
if %errorlevel% neq 0 (
    echo ❌ Resources plugin failed
    pause
    exit /b 1
)
echo ✅ Resources plugin successful
echo.

echo 4. Testing Maven JAR Plugin...
call mvn jar:jar
if %errorlevel% neq 0 (
    echo ❌ JAR plugin failed
    pause
    exit /b 1
)
echo ✅ JAR plugin successful
echo.

echo 5. Testing Maven Shade Plugin (Executable JAR)...
call mvn clean package
if %errorlevel% neq 0 (
    echo ❌ Shade plugin failed
    pause
    exit /b 1
)
echo ✅ Shade plugin successful
echo.

echo 6. Testing Maven Exec Plugin...
call mvn exec:java -Dexec.mainClass="demoproject.TddAgent" -q
if %errorlevel% neq 0 (
    echo ❌ Exec plugin failed
    pause
    exit /b 1
)
echo ✅ Exec plugin successful
echo.

echo 7. Testing Executable JAR...
if exist target\artifact-1.0.jar (
    java -jar target\artifact-1.0.jar
    if %errorlevel% neq 0 (
        echo ❌ JAR execution failed
        pause
        exit /b 1
    )
    echo ✅ JAR execution successful
) else (
    echo ❌ Executable JAR not found
    pause
    exit /b 1
)
echo.

echo 8. Testing Maven Install Plugin...
call mvn install -DskipTests
if %errorlevel% neq 0 (
    echo ❌ Install plugin failed
    pause
    exit /b 1
)
echo ✅ Install plugin successful
echo.

echo ========================================
echo All Plugin Tests Passed! ✅
echo ========================================
echo.
echo Available commands:
echo - mvn clean compile
echo - mvn exec:java -Dexec.mainClass="demoproject.TddAgent"
echo - mvn clean package
echo - java -jar target\artifact-1.0.jar
echo.
pause 