# LLM-Powered TDD Agent

A comprehensive Test-Driven Development (TDD) agent that automates the complete TDD workflow using LLM (Large Language Model) integration.

## 🎯 TDD Workflow Overview

```
+---------------------------------------------+
|           LLM-Powered TDD Agent             |
+---------------------------------------------+
| 1. RequirementParser       → Gemini/Claude  |
| 2. TestCaseGenerator       → JUnit Code     |
| 3. CodeGenerator           → Production Code|
| 4. RefactorEngine          → Cleanups       |
| 5. TestRunner              → mvn test       |
| 6. Validator               → Suggest gaps   |
+---------------------------------------------+
```

## 🚀 Features

### Complete TDD Cycle Implementation
- **Red Phase**: Generate failing tests based on requirements
- **Green Phase**: Generate minimal production code to make tests pass
- **Refactor Phase**: Improve code quality and maintainability
- **Validation Phase**: Analyze results and suggest improvements

### LLM Integration
- Support for OpenAI GPT models
- Support for Claude models
- Support for Gemini models
- Fallback templates when LLM is unavailable

### Multi-Language Support
- Java (JUnit 4/5, TestNG)
- Python (PyTest)
- JavaScript (Jest)

### Quality Assurance
- Automated code quality analysis
- Test coverage validation
- Performance monitoring
- Best practices enforcement

## 📁 Project Structure

```
artifact/
├── src/main/java/demoproject/
│   ├── TddAgent.java              # Main TDD orchestrator
│   ├── TestGenerationAgent.java   # Legacy test-only agent
│   ├── config/
│   │   └── AgentConfig.java       # Configuration management
│   ├── models/
│   │   ├── Requirement.java       # Requirement model
│   │   ├── GeneratedTest.java     # Generated test model
│   │   ├── ProductionCode.java    # Generated production code model
│   │   ├── TestResult.java        # Test execution results
│   │   └── ValidationReport.java  # Validation results
│   └── services/
│       ├── RequirementsReader.java # Requirement parser
│       ├── TestGenerator.java     # Test case generator
│       ├── CodeGenerator.java     # Production code generator
│       ├── RefactorEngine.java    # Code refactoring engine
│       ├── TestRunner.java        # Test execution engine
│       ├── Validator.java         # Results validator
│       ├── LlmService.java        # LLM integration
│       ├── TestTemplateService.java # Test templates
│       ├── CodeTemplateService.java # Code templates
│       └── GitService.java        # Git integration
├── requirements/
│   └── sample-requirements.md     # Sample requirements
├── generated-tests/               # Generated test files
├── config.properties              # Configuration file
└── README-TDD.md                  # This file
```

## 🛠️ Installation

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- Git (optional, for version control)

### Setup
1. Clone the repository
2. Navigate to the artifact directory
3. Build the project:
   ```bash
   mvn clean compile
   ```

## ⚙️ Configuration

### Basic Configuration
Create or modify `config.properties`:

```properties
# Requirements and Output Configuration
requirements.folder=./requirements
output.folder=./generated-tests

# Test Generation Configuration
test.framework=junit5
language=java
max.concurrent.tasks=4

# LLM Configuration (Optional)
llm.api.key=your-openai-api-key
llm.api.url=https://api.openai.com/v1/chat/completions
llm.model=gpt-4

# Git Configuration (Optional)
git.enabled=false
git.repository.url=https://github.com/your-username/your-repo.git
```

### Environment Variables
You can also configure using environment variables:

```bash
export TESTGEN_LLM_API_KEY="your-openai-api-key"
export TESTGEN_LLM_MODEL="gpt-4"
export TESTGEN_REQUIREMENTS_FOLDER="./requirements"
export TESTGEN_OUTPUT_FOLDER="./generated-tests"
```

## 🚀 Usage

### Running the TDD Agent

#### Basic Usage
```bash
java -cp target/classes demoproject.TddAgent
```

#### With Configuration File
```bash
java -cp target/classes demoproject.TddAgent --config config.properties
```

#### With Command Line Arguments
```bash
java -cp target/classes demoproject.TddAgent \
  --requirements-folder ./my-requirements \
  --output-folder ./my-output \
  --test-framework junit5 \
  --language java \
  --llm-api-key your-api-key
```

### Command Line Options

| Option | Short | Description |
|--------|-------|-------------|
| `--requirements-folder` | `-r` | Folder containing requirement files |
| `--output-folder` | `-o` | Output folder for generated files |
| `--test-framework` | | Test framework to use (junit5, junit4, testng, pytest, jest) |
| `--language` | `-l` | Programming language (java, python, javascript) |
| `--llm-api-key` | | LLM API key |
| `--llm-api-url` | | LLM API URL |
| `--llm-model` | | LLM model name |
| `--git-repo` | `-g` | Git repository URL |
| `--no-git` | | Disable Git integration |
| `--max-tasks` | | Maximum concurrent tasks |
| `--config` | `-c` | Configuration file path |
| `--help` | `-h` | Show help information |

## 📝 Requirements Format

### Markdown Format
Create requirement files in the `requirements` folder with `.md` extension:

```markdown
# REQ-001: User Authentication

## Description
Implement user authentication functionality with username and password.

## Priority
High

## Type
Feature

## Acceptance Criteria
- User can login with valid credentials
- User is rejected with invalid credentials
- Session is created upon successful login
- Session expires after 30 minutes

## Dependencies
- Database connection
- Password hashing service

## Module
Authentication
```

### Supported Fields
- **ID**: Unique requirement identifier (e.g., REQ-001)
- **Title**: Short description of the requirement
- **Description**: Detailed description
- **Priority**: High, Medium, Low
- **Type**: Feature, Bug, Enhancement, etc.
- **Acceptance Criteria**: List of acceptance criteria
- **Dependencies**: List of dependencies
- **Module**: Module or component name

## 🔄 TDD Workflow Steps

### 1. Requirement Parser
- Reads requirement files from the specified folder
- Parses markdown format into structured Requirement objects
- Extracts key information for test generation

### 2. Test Case Generator
- Analyzes requirements to understand functionality
- Generates comprehensive test cases using LLM
- Creates test files in the specified framework format
- Includes positive, negative, and edge case tests

### 3. Code Generator
- Analyzes generated tests to understand required functionality
- Generates minimal production code to make tests pass
- Follows TDD principles (implement only what's needed)
- Creates production code files

### 4. Refactor Engine
- Analyzes generated code for quality issues
- Identifies areas for improvement (naming, structure, etc.)
- Applies refactoring suggestions using LLM
- Improves code maintainability and readability

### 5. Test Runner
- Sets up test environment with generated files
- Executes tests using Maven (`mvn test`)
- Captures test execution results
- Reports success/failure statistics

### 6. Validator
- Analyzes test results and code quality
- Identifies gaps in test coverage
- Suggests improvements for tests and code
- Generates comprehensive validation reports

## 📊 Output Structure

After running the TDD agent, you'll find:

```
generated-tests/
├── src/
│   ├── main/java/generated/code/     # Production code
│   │   ├── REQ001Service.java
│   │   └── REQ002Service.java
│   └── test/java/generated/tests/    # Test code
│       ├── TestREQ001.java
│       └── TestREQ002.java
├── validation-reports/               # Validation reports
│   ├── validation-report-REQ001.txt
│   └── validation-report-REQ002.txt
└── pom.xml                          # Maven project file
```

## 🔧 Customization

### Adding New Test Frameworks
1. Create a new template in `TestTemplateService`
2. Add framework-specific configuration
3. Update the configuration validation

### Adding New Languages
1. Create language-specific templates in both `TestTemplateService` and `CodeTemplateService`
2. Add language-specific code generation logic
3. Update the configuration options

### Custom LLM Integration
1. Extend `LlmService` with new API endpoints
2. Add API-specific request/response handling
3. Update the configuration options

## 🧪 Testing

### Running Tests
```bash
mvn test
```

### Running Specific Tests
```bash
mvn test -Dtest=TestREQ001
```

### Test Reports
Test reports are generated in `target/surefire-reports/`

## 📈 Monitoring and Logging

### Log Levels
- `DEBUG`: Detailed execution information
- `INFO`: General workflow progress
- `WARN`: Non-critical issues
- `ERROR`: Critical failures

### Log Configuration
```properties
logging.level=INFO
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Troubleshooting

### Common Issues

#### LLM API Issues
- Verify API key is correct
- Check API URL is accessible
- Ensure sufficient API credits

#### Test Execution Issues
- Verify Maven is installed and in PATH
- Check Java version compatibility
- Ensure test dependencies are available

#### Git Integration Issues
- Verify Git credentials are configured
- Check repository URL is correct
- Ensure write permissions to repository

### Getting Help
- Check the logs for detailed error messages
- Verify configuration settings
- Test with a simple requirement first

## 🔮 Future Enhancements

- [ ] Support for more programming languages
- [ ] Integration with CI/CD pipelines
- [ ] Real-time collaboration features
- [ ] Advanced code analysis tools
- [ ] Performance benchmarking
- [ ] Code coverage reporting
- [ ] Integration with issue tracking systems 