@echo off
echo ========================================
echo Update Google Gemini API Key
echo ========================================
echo.

echo Current API Key: AIzaSyDiCinm73irvEr1SA7vBfiN9ifuVkwuQtk
echo.

echo To get a new API key:
echo 1. Go to: https://aistudio.google.com/app/apikey
echo 2. Sign in with your Google account
echo 3. Click "Create API Key"
echo 4. Copy the new key (starts with AIzaSy...)
echo.

set /p NEW_API_KEY="Enter your new API key: "

if "%NEW_API_KEY%"=="" (
    echo ❌ No API key entered
    pause
    exit /b 1
)

echo.
echo Updating config.properties with new API key...
echo.

powershell -Command "(Get-Content config.properties) -replace 'llm.api.key=.*', 'llm.api.key=%NEW_API_KEY%' | Set-Content config.properties"

echo ✅ API key updated successfully!
echo.
echo New API key: %NEW_API_KEY%
echo.

echo Testing the new API key...
call mvn clean compile -q
call mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass="demoproject.ApiKeyTest"

echo.
echo API key update completed!
pause 