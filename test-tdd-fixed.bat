@echo off
echo === Testing TDD Agent (Fixed Version) ===
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
java -cp "target/classes;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.17.1\jackson-databind-2.17.1.jar;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.17.1\jackson-annotations-2.17.1.jar;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.17.1\jackson-core-2.17.1.jar;%USERPROFILE%\.m2\repository\org\apache\httpcomponents\client5\httpclient5\5.3.1\httpclient5-5.3.1.jar;%USERPROFILE%\.m2\repository\org\apache\httpcomponents\core5\httpcore5\5.2.4\httpcore5-5.2.4.jar;%USERPROFILE%\.m2\repository\org\slf4j\slf4j-api\1.7.36\slf4j-api-1.7.36.jar;%USERPROFILE%\.m2\repository\ch\qos\logback\logback-classic\1.2.11\logback-classic-1.2.11.jar;%USERPROFILE%\.m2\repository\ch\qos\logback\logback-core\1.2.11\logback-core-1.2.11.jar" demoproject.TddAgent

echo.
echo Test completed.
pause 