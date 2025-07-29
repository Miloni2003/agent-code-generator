# AI Code Generation Agent (TDD + Gemini/OpenAI + Google ADK)

## Overview

This agent automatically turns requirements into a working, test-driven Java Maven project using AI (LLM/GenAI) and Google ADK.  
It reads requirement files, generates JUnit tests (TDD), builds controllers, models, and services, refactors code, writes everything as a Maven project (MVC) *outside* its own folder, and includes an agent interface for further automation.

---

## Features

- Reads `.json`, `.md`, `.yaml`, `.txt` requirements
- TDD-first: generates tests, then code (≥80% coverage)
- Positive, negative, and edge case tests
- Pluggable with Gemini or OpenAI (needs API key)
- Outputs Maven Java MVC projects (Controller, Service, Model, Tests, README)
- Google ADK (App.java) agent included
- Clear logging & error handling—no fake/fallback code
- Clean external output (see `outputFolder` in config)
- README and Git versioning support

---

## Configuration

Create or open: `config/agent-config.yaml`

