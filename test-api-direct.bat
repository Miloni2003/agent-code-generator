@echo off
echo ========================================
echo Testing Google Gemini API Directly
echo ========================================
echo.

echo Testing API key: AIzaSyDiCinm73irvEr1SA7vBfiN9ifuVkwuQtk
echo.

echo Sending test request to Google Gemini API...
echo.

powershell -Command "Invoke-RestMethod -Uri 'https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=AIzaSyDiCinm73irvEr1SA7vBfiN9ifuVkwuQtk' -Method POST -ContentType 'application/json' -Body '{\"contents\":[{\"parts\":[{\"text\":\"Write a simple Java method that adds two integers. Return only the code.\"}]}],\"generationConfig\":{\"temperature\":0.2,\"maxOutputTokens\":2048}}'"

echo.
echo ========================================
echo Direct API Test Completed
echo ========================================
pause 