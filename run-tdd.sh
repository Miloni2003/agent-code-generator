#!/bin/bash

echo "========================================"
echo "   LLM-Powered TDD Agent"
echo "========================================"
echo

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 11 or higher"
    exit 1
fi

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher"
    exit 1
fi

echo "Building project..."
mvn clean compile -q
if [ $? -ne 0 ]; then
    echo "ERROR: Build failed"
    exit 1
fi

echo
echo "Starting TDD Agent..."
echo

# Run the TDD agent with default configuration
java -cp target/classes demoproject.TddAgent "$@"

echo
echo "TDD Agent completed." 