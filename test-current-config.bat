@echo off
echo === Current LLM Configuration ===
echo.

echo Reading config.properties...
type config.properties | Select-String "llm."

echo.
echo === Google Gemini Integration Status ===
echo.
echo To test Google Gemini integration:
echo 1. Get API key from https://makersuite.google.com/app/apikey
echo 2. Update config.properties with your API key
echo 3. Run: mvn exec:java -Dexec.mainClass="demoproject.GoogleGeminiTest"
echo.
echo The system is configured for Google Gemini and will work once API key is set.
echo If Google Gemini is not available, it will use fallback code generation.
echo.
pause 