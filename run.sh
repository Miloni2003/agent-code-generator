#!/bin/bash

# Test Generation Agent Runner for Unix/Linux
# Usage: ./run.sh [options]

echo "Starting Test Generation Agent..."

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 17 or higher"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "Error: Java 17 or higher is required. Found version: $JAVA_VERSION"
    exit 1
fi

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher"
    exit 1
fi

# Build the project first
echo "Building project..."
if ! mvn clean compile; then
    echo "Error: Build failed"
    exit 1
fi

# Run the agent with provided arguments
echo "Running Test Generation Agent..."
if [ $# -eq 0 ]; then
    # No arguments provided, use default configuration
    mvn exec:java -Dexec.mainClass="demoproject.TestGenerationAgent"
else
    # Arguments provided, pass them to the agent
    mvn exec:java -Dexec.mainClass="demoproject.TestGenerationAgent" -Dexec.args="$*"
fi

if [ $? -ne 0 ]; then
    echo "Error: Test Generation Agent failed"
    exit 1
fi

echo "Test Generation Agent completed successfully!" 