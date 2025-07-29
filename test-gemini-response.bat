@echo off
echo === Testing Google Gemini API Response Structure ===
echo.

echo Testing API key: AIzaSyDiCinm73irvEr1SA7vBfiN9ifuVkwuQtk
echo.

curl -X POST "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=AIzaSyDiCinm73irvEr1SA7vBfiN9ifuVkwuQtk" ^
  -H "Content-Type: application/json" ^
  -d "{\"contents\":[{\"parts\":[{\"text\":\"Write a simple Java method that adds two integers and returns the result. Only return the Java code, no explanations.\"}]}],\"generationConfig\":{\"temperature\":0.2,\"topK\":40,\"topP\":0.8,\"maxOutputTokens\":2048}}" ^
  --connect-timeout 10 ^
  --silent ^
  --show-error

echo.
echo Test completed.
pause 