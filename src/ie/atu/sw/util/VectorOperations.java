package ie.atu.sw.util;

/**
 * Utility class with static methods for vector operations like addition,
 * subtraction,
 * dot product, etc.
 */
public class VectorOperations {
    /**
     * Assert that two vectors have equal lengths.
     * 
     * @param vector1 the first vector
     * @param vector2 the second vector
     * @throws Exception if the vectors have different lengths
     */
    public static void assertEqualLengths(double[] vector1, double[] vector2) throws Exception {
        if (vector1.length != vector2.length)
            throw new Exception("The two vector arrays must be of the same length.");
    }

    /**
     * Adds two vectors element-wise.
     * 
     * @param vector1 the first vector to add
     * @param vector2 the second vector to add
     * @return the result of the vector addition operation
     * @throws Exception if the vectors have different lengths
     */
    public static double[] add(double[] vector1, double[] vector2) throws Exception {
        // Ensure vectors have equal lengths
        assertEqualLengths(vector1, vector2);

        // Initialize the result vector with the same length as vector1
        double[] result = new double[vector1.length];

        // Perform element-wise addition
        for (int i = 0; i < vector1.length; i++) {
            result[i] = vector1[i] + vector2[i];
        }

        // Return the resulting vector
        return result;
    }

    /**
     * Performs element-wise subtraction of two vectors.
     * 
     * @param vector1 the vector from which vector2 will be subtracted
     * @param vector2 the vector to subtract from vector1
     * @return a new vector containing the element-wise difference of vector1 and
     *         vector2
     * @throws Exception if vector1 and vector2 have different lengths
     */
    public static double[] subtract(double[] vector1, double[] vector2) throws Exception {
        // Ensure vectors have equal lengths
        assertEqualLengths(vector1, vector2);

        // Initialize the result vector with the same length as vector1
        double[] result = new double[vector1.length];

        // Perform element-wise subtraction
        for (int i = 0; i < vector1.length; i++) {
            result[i] = vector1[i] - vector2[i];
        }

        // Return the resulting vector
        return result;
    }

    /**
     * Performs element-wise multiplication of two vectors.
     * 
     * @param vector1 the first vector to multiply
     * @param vector2 the second vector to multiply
     * @return a new vector containing the element-wise product of vector1 and
     *         vector2
     * @throws Exception if vector1 and vector2 have different lengths
     */
    public static double[] multiply(double[] vector1, double[] vector2) throws Exception {
        // Ensure vectors have equal lengths
        assertEqualLengths(vector1, vector2);

        // Initialize the result vector with the same length as vector1
        double[] result = new double[vector1.length];

        // Perform element-wise multiplication
        for (int i = 0; i < vector1.length; i++) {
            result[i] = vector1[i] * vector2[i];
        }

        // Return the resulting vector
        return result;
    }

    /**
     * Performs element-wise division of one vector by another.
     * 
     * @param vector1 the vector to be divided
     * @param vector2 the vector by which to divide
     * @return a new vector resulting from element-wise division of vector1 by
     *         vector2
     * @throws Exception if vector1 and vector2 have different lengths
     */
    public static double[] divide(double[] vector1, double[] vector2) throws Exception {
        // Ensure vectors have equal lengths
        assertEqualLengths(vector1, vector2);

        // Initialize an array to store the result of division
        double[] result = new double[vector1.length];

        // Perform element-wise division
        for (int i = 0; i < vector1.length; i++) {
            result[i] = vector1[i] / vector2[i];
        }

        // Return the resulting vector
        return result;
    }

    /**
     * Performs element-wise squaring of a vector.
     * 
     * @param vector the vector to be squared
     * @return a new vector resulting from element-wise squaring of the input vector
     */
    public static double[] square(double[] vector) {
        // Initialize an array to store the squared values
        double[] result = new double[vector.length];

        // Perform element-wise squaring
        for (int i = 0; i < vector.length; i++) {
            result[i] = vector[i] * vector[i];
        }

        // Return the resulting vector
        return result;
    }

    /**
     * Calculates the sum of all values in a vector.
     * 
     * @param vector the vector whose values are to be summed
     * @return the sum of all values in the vector
     */
    public static double sum(double[] vector) {
        double vectorSum = 0.0;

        // Iterate through each element in the vector and accumulate the sum
        for (double d : vector) {
            vectorSum += d;
        }

        // Return the calculated sum
        return vectorSum;
    }

    /**
     * Calculates the dot product of two vectors.
     * 
     * @param vector1 the first vector
     * @param vector2 the second vector
     * @return the dot product of vector1 and vector2
     * @throws Exception if the vectors have different lengths
     */
    public static double dotProduct(double[] vector1, double[] vector2) throws Exception {
        // Calculate the dot product by summing the element-wise multiplication of
        // vector1 and vector2
        return sum(multiply(vector1, vector2));
    }

    /**
     * Calculates the squared Euclidean distance between two vectors.
     * 
     * @param vector1 the first vector
     * @param vector2 the second vector
     * @return the squared Euclidean distance between vector1 and vector2
     * @throws Exception if the vectors have different lengths
     */
    public static double euclideanDistanceNoSqrt(double[] vector1, double[] vector2) throws Exception {
        // Calculate the squared Euclidean distance by subtracting vector2 from vector1,
        // squaring the result,
        // and summing the squared values of each dimension.
        return sum(square(subtract(vector1, vector2)));
    }

    /**
     * Calculates the Euclidean distance between two vectors.
     * 
     * @param vector1 the first vector
     * @param vector2 the second vector
     * @return the Euclidean distance between vector1 and vector2
     * @throws Exception if the vectors have different lengths
     */
    public static double euclideanDistance(double[] vector1, double[] vector2) throws Exception {
        // Calculate the squared Euclidean distance using the euclideanDistanceNoSqrt
        // method,
        // and then take the square root of the result to get the Euclidean distance.
        return Math.sqrt(euclideanDistanceNoSqrt(vector1, vector2));
    }

    /**
     * Calculates the cosine similarity between two vectors.
     * 
     * @param vector1 the first vector
     * @param vector2 the second vector
     * @return the cosine similarity between vector1 and vector2
     * @throws Exception if the vectors have different lengths
     */
    public static double cosineSimilarity(double[] vector1, double[] vector2) throws Exception {
        return dotProduct(vector1, vector2) / Math.sqrt(sum(square(vector1)) * sum(square(vector2)));
    }
}
