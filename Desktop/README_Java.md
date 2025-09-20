# Shamir's Secret Sharing - Java Implementation

This Java implementation solves Shamir's Secret Sharing problem by finding the constant term (secret) of a polynomial using Lagrange interpolation.

## Features

- **BigInteger Support**: Uses Java's BigInteger class for arbitrary-precision arithmetic
- **Multiple Base Support**: Handles numbers in bases 2, 3, 4, 6, 7, 8, 10, 12, 15, 16
- **Lagrange Interpolation**: Implements polynomial interpolation to reconstruct the secret
- **Robust Error Handling**: Validates input and provides clear error messages
- **Test Cases Included**: Both test cases are embedded and tested

## Algorithm Overview

1. **Parse JSON Input**: Read the JSON data containing n, k, and the shares
2. **Base Conversion**: Convert each share value from its given base to decimal using BigInteger
3. **Point Extraction**: Create (x, y) coordinate pairs where x is the share number and y is the converted value
4. **Lagrange Interpolation**: Use the first k points to interpolate the polynomial at x = 0
5. **Secret Recovery**: The result of interpolation at x = 0 is the secret

## Compilation and Execution

### Compile
```bash
javac ShamirSecretSharing.java
```

### Run
```bash
java ShamirSecretSharing
```

## Test Results

### Test Case 1
- **Input**: n=4, k=3, shares in bases 10, 2, 10, 4
- **Secret**: 3

### Test Case 2
- **Input**: n=10, k=7, shares in various bases (6, 15, 16, 8, 3, 12, 7)
- **Secret**: -6290016743746469796

## Mathematical Background

The solution uses Lagrange interpolation formula:

For points (x₁, y₁), (x₂, y₂), ..., (xₖ, yₖ), the interpolated value at x is:

L(x) = Σ yᵢ * Π (x - xⱼ)/(xᵢ - xⱼ) (for j ≠ i)

The secret is L(0), which gives the constant term of the polynomial.

## Implementation Details

### Key Classes

- **ShamirSecretSharing**: Main class containing all methods
- **Point**: Inner class representing (x, y) coordinate pairs

### Key Methods

- `convertToDecimal(String value, int base)`: Converts numbers between different bases
- `lagrangeInterpolation(List<Point> points, BigInteger x)`: Performs Lagrange interpolation
- `findSecret(Map<String, Object> jsonData)`: Main method that orchestrates secret recovery
- `main(String[] args)`: Contains test cases and demonstrates the solution

## Requirements

- Java 8 or higher (for BigInteger and other modern features)
- No external dependencies required

## Repository

This implementation is hosted at: https://github.com/sirimuthinti/ShamirSecretSharing---Hashira-Placement-Code

## Usage Example

```java
// The main method contains complete working examples
// Simply compile and run to see both test cases solved
```

## Error Handling

- Validates that at least k points are available for interpolation
- Handles invalid base conversions with clear error messages
- Provides detailed error information for debugging
