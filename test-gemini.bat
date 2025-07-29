@echo off
echo === Testing Google Gemini Integration ===
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

echo Testing Google Gemini configuration...
call mvn exec:java -Dexec.mainClass="demoproject.GoogleGeminiTest" -q
if %errorlevel% neq 0 (
    echo ❌ Google Gemini test failed
    echo.
    echo Please check:
    echo 1. Is your Google API key set in config.properties?
    echo 2. Is the API key valid and has access to Gemini API?
    echo 3. Check your network connection
) else (
    echo ✅ Google Gemini test completed
)

echo.
echo === Next Steps ===
echo 1. If test failed, get API key from: https://makersuite.google.com/app/apikey
echo 2. Update config.properties with your API key
echo 3. Run this test again
echo 4. Once working, run: mvn exec:java -Dexec.mainClass="demoproject.TddAgent"
echo.
pause 