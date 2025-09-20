import java.util.*;
import java.math.BigInteger;

public class ShamirSecretSharing {

    /**
     * Convert a number from any base to decimal.
     *
     * @param value The number as a string in the given base
     * @param base The base of the number system
     * @return The decimal equivalent of the number
     */
    public static BigInteger convertToDecimal(String value, int base) {
        try {
            return new BigInteger(value, base);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number '" + value + "' for base " + base + ": " + e.getMessage());
        }
    }

    /**
     * Perform Lagrange interpolation to find y at given x.
     *
     * @param points List of (x, y) coordinate pairs
     * @param x The x value to interpolate at
     * @return The interpolated y value at x
     */
    public static BigInteger lagrangeInterpolation(List<Point> points, BigInteger x) {
        BigInteger result = BigInteger.ZERO;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            Point currentPoint = points.get(i);
            BigInteger xi = currentPoint.x;
            BigInteger yi = currentPoint.y;

            // Calculate numerator and denominator for Lagrange basis polynomial
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    Point otherPoint = points.get(j);
                    BigInteger xj = otherPoint.x;

                    numerator = numerator.multiply(x.subtract(xj));
                    denominator = denominator.multiply(xi.subtract(xj));
                }
            }

            // Add the term to the result
            result = result.add(yi.multiply(numerator).divide(denominator));
        }

        return result;
    }

    /**
     * Find the secret (constant term) using Shamir's Secret Sharing.
     *
     * @param jsonData Map containing the JSON data with keys and roots
     * @return The secret (constant term of the polynomial)
     */
    public static BigInteger findSecret(Map<String, Object> jsonData) {
        // Extract n and k values
        @SuppressWarnings("unchecked")
        Map<String, Object> keys = (Map<String, Object>) jsonData.get("keys");
        int n = (Integer) keys.get("n");
        int k = (Integer) keys.get("k");

        // Extract and convert points
        List<Point> points = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            String key = String.valueOf(i);
            if (jsonData.containsKey(key)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> rootData = (Map<String, Object>) jsonData.get(key);
                int base = Integer.parseInt((String) rootData.get("base"));
                String value = (String) rootData.get("value");

                // Convert to decimal
                BigInteger x = BigInteger.valueOf(i); // x coordinate is the key number
                BigInteger y = convertToDecimal(value, base);

                points.add(new Point(x, y));
            }
        }

        // We need at least k points for interpolation
        if (points.size() < k) {
            throw new IllegalArgumentException("Need at least " + k + " points, but only " + points.size() + " provided");
        }

        // Use the first k points for interpolation
        List<Point> selectedPoints = points.subList(0, k);

        // Find the constant term (secret) by interpolating at x = 0
        BigInteger secret = lagrangeInterpolation(selectedPoints, BigInteger.ZERO);

        return secret;
    }

    /**
     * Point class to represent (x, y) coordinates
     */
    static class Point {
        BigInteger x, y;

        Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    /**
     * Create test case 1 data
     */
    public static Map<String, Object> createTestCase1() {
        Map<String, Object> testCase = new HashMap<>();
        Map<String, Object> keys = new HashMap<>();
        keys.put("n", 4);
        keys.put("k", 3);
        testCase.put("keys", keys);

        Map<String, Object> root1 = new HashMap<>();
        root1.put("base", "10");
        root1.put("value", "4");
        testCase.put("1", root1);

        Map<String, Object> root2 = new HashMap<>();
        root2.put("base", "2");
        root2.put("value", "111");
        testCase.put("2", root2);

        Map<String, Object> root3 = new HashMap<>();
        root3.put("base", "10");
        root3.put("value", "12");
        testCase.put("3", root3);

        Map<String, Object> root6 = new HashMap<>();
        root6.put("base", "4");
        root6.put("value", "213");
        testCase.put("6", root6);

        return testCase;
    }

    /**
     * Create test case 2 data
     */
    public static Map<String, Object> createTestCase2() {
        Map<String, Object> testCase = new HashMap<>();
        Map<String, Object> keys = new HashMap<>();
        keys.put("n", 10);
        keys.put("k", 7);
        testCase.put("keys", keys);

        Map<String, Object> root1 = new HashMap<>();
        root1.put("base", "6");
        root1.put("value", "13444211440455345511");
        testCase.put("1", root1);

        Map<String, Object> root2 = new HashMap<>();
        root2.put("base", "15");
        root2.put("value", "aed7015a346d635");
        testCase.put("2", root2);

        Map<String, Object> root3 = new HashMap<>();
        root3.put("base", "15");
        root3.put("value", "6aeeb69631c227c");
        testCase.put("3", root3);

        Map<String, Object> root4 = new HashMap<>();
        root4.put("base", "16");
        root4.put("value", "e1b5e05623d881f");
        testCase.put("4", root4);

        Map<String, Object> root5 = new HashMap<>();
        root5.put("base", "8");
        root5.put("value", "316034514573652620673");
        testCase.put("5", root5);

        Map<String, Object> root6 = new HashMap<>();
        root6.put("base", "3");
        root6.put("value", "2122212201122002221120200210011020220200");
        testCase.put("6", root6);

        Map<String, Object> root7 = new HashMap<>();
        root7.put("base", "3");
        root7.put("value", "20120221122211000100210021102001201112121");
        testCase.put("7", root7);

        Map<String, Object> root8 = new HashMap<>();
        root8.put("base", "6");
        root8.put("value", "20220554335330240002224253");
        testCase.put("8", root8);

        Map<String, Object> root9 = new HashMap<>();
        root9.put("base", "12");
        root9.put("value", "45153788322a1255483");
        testCase.put("9", root9);

        Map<String, Object> root10 = new HashMap<>();
        root10.put("base", "7");
        root10.put("value", "1101613130313526312514143");
        testCase.put("10", root10);

        return testCase;
    }

    /**
     * Main method to solve the Shamir's Secret Sharing problem.
     */
    public static void main(String[] args) {
        System.out.println("Shamir's Secret Sharing - Polynomial Interpolation");
        System.out.println("=".repeat(50));

        // Solve Test Case 1
        System.out.println("\nTest Case 1:");
        Map<String, Object> testCase1 = createTestCase1();

        try {
            BigInteger secret1 = findSecret(testCase1);
            System.out.println("Secret (constant term): " + secret1);
        } catch (Exception e) {
            System.out.println("Error solving Test Case 1: " + e.getMessage());
        }

        // Solve Test Case 2
        System.out.println("\n" + "=".repeat(50));
        System.out.println("\nTest Case 2:");
        Map<String, Object> testCase2 = createTestCase2();

        try {
            BigInteger secret2 = findSecret(testCase2);
            System.out.println("Secret (constant term): " + secret2);
        } catch (Exception e) {
            System.out.println("Error solving Test Case 2: " + e.getMessage());
        }
    }
}
