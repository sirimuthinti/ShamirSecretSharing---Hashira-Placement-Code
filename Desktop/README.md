# Shamir's Secret Sharing - Polynomial Interpolation

This Python implementation solves Shamir's Secret Sharing problem by finding the constant term (secret) of a polynomial using Lagrange interpolation.

## Problem Description

In Shamir's Secret Sharing scheme, a secret is divided into multiple parts (shares) where:
- `n` is the total number of shares
- `k` is the minimum number of shares required to reconstruct the secret
- Each share is a point (x, y) on a polynomial of degree k-1
- The secret is the constant term (y-intercept) of the polynomial

## Algorithm

1. **Parse JSON Input**: Read the JSON data containing n, k, and the shares
2. **Base Conversion**: Convert each share value from its given base to decimal
3. **Point Extraction**: Create (x, y) coordinate pairs where x is the share number and y is the converted value
4. **Lagrange Interpolation**: Use the first k points to interpolate the polynomial at x = 0
5. **Secret Recovery**: The result of interpolation at x = 0 is the secret

## Implementation Details

### Functions

- `convert_to_decimal(value, base)`: Converts a number from any base to decimal
- `lagrange_interpolation(points, x)`: Performs Lagrange interpolation to find y at given x
- `find_secret(json_data)`: Main function that orchestrates the secret recovery process
- `main()`: Contains test cases and demonstrates the solution

### Supported Bases

The implementation supports all standard number bases from 2 to 36, including:
- Binary (base 2)
- Ternary (base 3)
- Quaternary (base 4)
- Octal (base 8)
- Decimal (base 10)
- Hexadecimal (base 16)
- And other bases like 6, 7, 12, 15 as used in the test cases

## Test Results

### Test Case 1
- **Input**: n=4, k=3, shares in bases 10, 2, 10, 4
- **Secret**: 3

### Test Case 2
- **Input**: n=10, k=7, shares in various bases (6, 15, 16, 8, 3, 12, 7)
- **Secret**: -6290016743746469796

## Usage

```bash
python shamir_secret_sharing.py
```

The script will automatically run both test cases and display the results.

## Mathematical Background

The solution uses Lagrange interpolation formula:

For points (x₁, y₁), (x₂, y₂), ..., (xₖ, yₖ), the interpolated value at x is:

L(x) = Σ yᵢ * Π (x - xⱼ)/(xᵢ - xⱼ) (for j ≠ i)

The secret is L(0), which gives the constant term of the polynomial.

## Error Handling

- Validates that at least k points are available
- Handles invalid base conversions
- Provides clear error messages for debugging

## Requirements

- Python 3.x
- No external dependencies required
