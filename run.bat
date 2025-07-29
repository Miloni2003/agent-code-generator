@echo off
REM Test Generation Agent Runner for Windows
REM Usage: run.bat [options]

echo Starting Test Generation Agent...

REM Check if Java is available
"C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot\bin\java.exe" -version >nul 2>&1
if errorlevel 1 (
    echo Error: Java is not available at expected location
    echo Please ensure Java is installed at: C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot\
    pause
    exit /b 1
)

REM Check if Maven is available
mvn -version >nul 2>&1
if errorlevel 1 (
    echo Warning: Maven not in PATH, trying to use full path...
    "C:\apache-maven-3.9.11\bin\mvn.cmd" -version >nul 2>&1
    if errorlevel 1 (
        echo Error: Maven is not available
        echo Please ensure Maven is installed at: C:\apache-maven-3.9.11\
        pause
        exit /b 1
    )
)

REM Build the project first
echo Building project...
mvn -version >nul 2>&1
if errorlevel 1 (
    call "C:\apache-maven-3.9.11\bin\mvn.cmd" clean compile
) else (
    call mvn clean compile
)
if errorlevel 1 (
    echo Error: Build failed
    pause
    exit /b 1
)

REM Run the agent with provided arguments
echo Running Test Generation Agent...
if "%1"=="" (
    REM No arguments provided, use default configuration with Git disabled
    "C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot\bin\java.exe" -cp "target/classes;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.17.0\jackson-databind-2.17.0.jar;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.17.0\jackson-annotations-2.17.0.jar;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.17.0\jackson-core-2.17.0.jar;%USERPROFILE%\.m2\repository\net\bytebuddy\byte-buddy\1.14.9\byte-buddy-1.14.9.jar;%USERPROFILE%\.m2\repository\commons-io\commons-io\2.15.1\commons-io-2.15.1.jar;%USERPROFILE%\.m2\repository\org\slf4j\slf4j-api\2.0.12\slf4j-api-2.0.12.jar;%USERPROFILE%\.m2\repository\ch\qos\logback\logback-classic\1.4.14\logback-classic-1.4.14.jar;%USERPROFILE%\.m2\repository\ch\qos\logback\logback-core\1.4.14\logback-core-1.4.14.jar;%USERPROFILE%\.m2\repository\org\apache\httpcomponents\client5\httpclient5\5.3.1\httpclient5-5.3.1.jar;%USERPROFILE%\.m2\repository\org\apache\httpcomponents\core5\httpcore5\5.2.4\httpcore5-5.2.4.jar;%USERPROFILE%\.m2\repository\org\apache\httpcomponents\core5\httpcore5-h2\5.2.4\httpcore5-h2-5.2.4.jar;%USERPROFILE%\.m2\repository\com\google\code\gson\gson\2.10.1\gson-2.10.1.jar" demoproject.TestGenerationAgent --no-git
) else (
    REM Arguments provided, pass them to the agent
    "C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot\bin\java.exe" -cp "target/classes;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.17.0\jackson-databind-2.17.0.jar;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.17.0\jackson-annotations-2.17.0.jar;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.17.0\jackson-core-2.17.0.jar;%USERPROFILE%\.m2\repository\net\bytebuddy\byte-buddy\1.14.9\byte-buddy-1.14.9.jar;%USERPROFILE%\.m2\repository\commons-io\commons-io\2.15.1\commons-io-2.15.1.jar;%USERPROFILE%\.m2\repository\org\slf4j\slf4j-api\2.0.12\slf4j-api-2.0.12.jar;%USERPROFILE%\.m2\repository\ch\qos\logback\logback-classic\1.4.14\logback-classic-1.4.14.jar;%USERPROFILE%\.m2\repository\ch\qos\logback\logback-core\1.4.14\logback-core-1.4.14.jar;%USERPROFILE%\.m2\repository\org\apache\httpcomponents\client5\httpclient5\5.3.1\httpclient5-5.3.1.jar;%USERPROFILE%\.m2\repository\org\apache\httpcomponents\core5\httpcore5\5.2.4\httpcore5-5.2.4.jar;%USERPROFILE%\.m2\repository\org\apache\httpcomponents\core5\httpcore5-h2\5.2.4\httpcore5-h2-5.2.4.jar;%USERPROFILE%\.m2\repository\com\google\code\gson\gson\2.10.1\gson-2.10.1.jar" demoproject.TestGenerationAgent %*
)

if errorlevel 1 (
    echo Error: Test Generation Agent failed
    pause
    exit /b 1
)

echo Test Generation Agent completed successfully!
pause 