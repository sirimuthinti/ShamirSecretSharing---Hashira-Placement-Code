import java.math.BigInteger;
import java.util.*;

public class ShamirSecretSharing {

    /**
     * Represents a point (x, y) on the polynomial
     */
    static class Point {
        BigInteger x;
        BigInteger y;

        Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Convert a number from any base to decimal (BigInteger)
     */
    public static BigInteger convertToDecimal(String value, int base) {
        try {
            return new BigInteger(value, base);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number '" + value + "' for base " + base);
        }
    }

    /**
     * Perform Lagrange interpolation to find y at given x
     */
    public static BigInteger lagrangeInterpolation(List<Point> points, BigInteger x) {
        BigInteger result = BigInteger.ZERO;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            BigInteger xi = points.get(i).x;
            BigInteger yi = points.get(i).y;

            // Calculate numerator and denominator for Lagrange basis polynomial
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    BigInteger xj = points.get(j).x;
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
     * Find the secret (constant term) using Shamir's Secret Sharing
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
                Map<String, String> rootData = (Map<String, String>) jsonData.get(key);
                int base = Integer.parseInt(rootData.get("base"));
                String value = rootData.get("value");

                // Convert to decimal
                BigInteger x = BigInteger.valueOf(i);
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
        return lagrangeInterpolation(selectedPoints, BigInteger.ZERO);
    }

    /**
     * Main method with test cases
     */
    public static void main(String[] args) {
        System.out.println("Shamir's Secret Sharing - Polynomial Interpolation (Java)");
        System.out.println("=".repeat(60));

        // Test Case 1
        Map<String, Object> testCase1 = new HashMap<>();
        Map<String, Object> keys1 = new HashMap<>();
        keys1.put("n", 4);
        keys1.put("k", 3);
        testCase1.put("keys", keys1);

        Map<String, String> root1 = new HashMap<>();
        root1.put("base", "10");
        root1.put("value", "4");
        testCase1.put("1", root1);

        Map<String, String> root2 = new HashMap<>();
        root2.put("base", "2");
        root2.put("value", "111");
        testCase1.put("2", root2);

        Map<String, String> root3 = new HashMap<>();
        root3.put("base", "10");
        root3.put("value", "12");
        testCase1.put("3", root3);

        Map<String, String> root6 = new HashMap<>();
        root6.put("base", "4");
        root6.put("value", "213");
        testCase1.put("6", root6);

        // Test Case 2
        Map<String, Object> testCase2 = new HashMap<>();
        Map<String, Object> keys2 = new HashMap<>();
        keys2.put("n", 10);
        keys2.put("k", 7);
        testCase2.put("keys", keys2);

        Map<String, String> root1_2 = new HashMap<>();
        root1_2.put("base", "6");
        root1_2.put("value", "13444211440455345511");
        testCase2.put("1", root1_2);

        Map<String, String> root2_2 = new HashMap<>();
        root2_2.put("base", "15");
        root2_2.put("value", "aed7015a346d635");
        testCase2.put("2", root2_2);

        Map<String, String> root3_2 = new HashMap<>();
        root3_2.put("base", "15");
        root3_2.put("value", "6aeeb69631c227c");
        testCase2.put("3", root3_2);

        Map<String, String> root4_2 = new HashMap<>();
        root4_2.put("base", "16");
        root4_2.put("value", "e1b5e05623d881f");
        testCase2.put("4", root4_2);

        Map<String, String> root5_2 = new HashMap<>();
        root5_2.put("base", "8");
        root5_2.put("value", "316034514573652620673");
        testCase2.put("5", root5_2);

        Map<String, String> root6_2 = new HashMap<>();
        root6_2.put("base", "3");
        root6_2.put("value", "2122212201122002221120200210011020220200");
        testCase2.put("6", root6_2);

        Map<String, String> root7_2 = new HashMap<>();
        root7_2.put("base", "3");
        root7_2.put("value", "20120221122211000100210021102001201112121");
        testCase2.put("7", root7_2);

        Map<String, String> root8_2 = new HashMap<>();
        root8_2.put("base", "6");
        root8_2.put("value", "20220554335330240002224253");
        testCase2.put("8", root8_2);

        Map<String, String> root9_2 = new HashMap<>();
        root9_2.put("base", "12");
        root9_2.put("value", "45153788322a1255483");
        testCase2.put("9", root9_2);

        Map<String, String> root10_2 = new HashMap<>();
        root10_2.put("base", "7");
        root10_2.put("value", "1101613130313526312514143");
        testCase2.put("10", root10_2);

        // Solve Test Case 1
        System.out.println("\nTest Case 1:");
        System.out.println("Secret (constant term): " + findSecret(testCase1));

        // Solve Test Case 2
        System.out.println("\n" + "=".repeat(60));
        System.out.println("\nTest Case 2:");
        System.out.println("Secret (constant term): " + findSecret(testCase2));
    }
}
