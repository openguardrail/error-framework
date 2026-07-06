# OpenGuardrail Error Framework

Standardized error handling library for Java services.

[![License: Apache-2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![Java 11+](https://img.shields.io/badge/Java-11%2B-blue.svg)](https://openjdk.org/)

---

## Overview

A lightweight, zero-dependency Java library that provides structured error definitions with code range validation, HTTP status mapping, recoverability signaling, and a consistent API error response format.

Compatible with Java 11, 17, and 21.

## Installation

```xml
<dependency>
    <groupId>org.openguardrail</groupId>
    <artifactId>error-framework</artifactId>
    <version>1.0.0</version>
</dependency>
```

From source:

```bash
git clone https://github.com/openguardrail/error-framework.git
cd error-framework
mvn clean install
```

## Usage

### Define error groups

```java
public enum OrderErrorGroup implements ErrorGroup {
    VALIDATION(100, 199, "Order validation errors", "Validation"),
    PAYMENT(200, 299, "Payment processing errors", "Payment"),
    FULFILLMENT(300, 399, "Fulfillment errors", "Fulfillment"),
    INTERNAL(900, 999, "Internal errors", "Internal");

    private final int start;
    private final int end;
    private final String description;
    private final String label;

    OrderErrorGroup(int start, int end, String description, String label) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.label = label;
    }

    @Override public int getCodeRangeStart() { return start; }
    @Override public int getCodeRangeEnd() { return end; }
    @Override public String getDescription() { return description; }
    @Override public String getLabel() { return label; }
}
```

### Define errors

```java
public enum OrderValidationError implements ErrorBase {
    MISSING_ITEM(new ServiceError(100, OrderErrorGroup.VALIDATION,
            "Order must contain at least one item", "ORD-100", 400, false)),
    INVALID_QUANTITY(new ServiceError(101, OrderErrorGroup.VALIDATION,
            "Item quantity must be positive", "ORD-101", 400, false)),
    ADDRESS_INCOMPLETE(new ServiceError(102, OrderErrorGroup.VALIDATION,
            "Shipping address is incomplete", "ORD-102", 400, false));

    private final ServiceError error;

    OrderValidationError(ServiceError error) { this.error = error; }

    @Override
    public ServiceError getError() { return error; }
}
```

### Throw

```java
throw new ServiceException(
        "Quantity -3 is not valid for item SKU-42",
        OrderValidationError.INVALID_QUANTITY.getError());
```

### Handle

```java
@ExceptionHandler(ServiceException.class)
public ResponseEntity<ErrorResponse> handle(ServiceException ex) {
    ErrorResponse response = ErrorResponse.from(ex);
    return ResponseEntity.status(ex.getHttpStatus()).body(response);
}
```

### Response

```json
{
    "code": "ORD-101",
    "message": "Quantity -3 is not valid for item SKU-42",
    "status": 400,
    "recoverable": false,
    "timestamp": "2026-07-06T14:30:00.000Z"
}
```

## License

Apache License 2.0. See [LICENSE](LICENSE).
