# Contributing

## Prerequisites

- Java 11+ (build and test with 11 to ensure compatibility)
- Maven 3.9+

## Setup

```bash
git clone https://github.com/openguardrail/error-framework.git
cd error-framework
mvn clean install
```

## Development Guidelines

### Adding a new class

1. Place in `src/main/java/org/openguardrail/errorframework/`.
2. Add complete Javadoc on all public classes and methods.
3. Add corresponding tests in `src/test/java/org/openguardrail/errorframework/`.
4. Run `mvn clean verify` - must pass with source and javadoc generation.

### Code conventions

- All public classes must have Javadoc.
- No runtime dependencies. This library must remain pure Java.
- All public API must be backward-compatible within a major version.
- Prefer immutability. New classes should be `final` or records where appropriate.
- Error codes are validated at construction time. Do not add lazy validation.

### Testing

```bash
mvn test
```

All new code must have unit tests. Aim for branch coverage on validation logic.

## Pull Request Process

1. Fork the repository, branch from `main`.
2. One concern per PR. Do not bundle unrelated changes.
3. Write tests for all new functionality.
4. Ensure `mvn clean verify` passes (compiles, tests, generates source and javadoc JARs).
5. Write a clear PR description: what changed, why, and how to verify.
6. Address all review feedback before merge.

## Backward Compatibility

This is a library consumed by other projects. Breaking changes require a major version bump.

Breaking changes include:

- Removing or renaming a public class, method, or field
- Changing method signatures
- Changing the behavior of `GuardrailError` construction validation
- Removing an enum constant from a released version

Non-breaking changes:

- Adding new classes or methods
- Adding new enum constants
- Widening parameter types
- Adding default methods to interfaces

## Reporting Issues

Use [GitHub Issues](https://github.com/openguardrail/error-framework/issues).

For bugs:

- Java version
- Maven version
- Minimal code to reproduce
- Expected vs. actual behavior

## License

By contributing, you agree that your contributions will be licensed under Apache-2.0.
