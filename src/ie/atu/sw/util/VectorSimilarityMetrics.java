package ie.atu.sw.util;

/**
 * Enum defining different similarity algorithms for vector comparisons.
 * Each enum constant implements a calculate method to compute the similarity
 * measure between two vectors.
 */
public enum VectorSimilarityMetrics {
    DOT_PRODUCT("Dot Product") {
        /**
         * Calculates the dot product similarity between two vectors.
         * 
         * @param vector1 the first vector
         * @param vector2 the second vector
         * @return the dot product similarity between vector1 and vector2
         * @throws Exception if the vectors have different lengths
         */
        @Override
        public double calculate(double[] vector1, double[] vector2) throws Exception {
            return VectorOperations.dotProduct(vector1, vector2);
        }
    },
    EUCLIDEAN_DISTANCE_NO_SQRT("Euclidean Distance (No Square Root)") {
        /**
         * Calculates the squared Euclidean distance between two vectors.
         * 
         * @param vector1 the first vector
         * @param vector2 the second vector
         * @return the squared Euclidean distance between vector1 and vector2
         * @throws Exception if the vectors have different lengths
         */
        @Override
        public double calculate(double[] vector1, double[] vector2) throws Exception {
            return VectorOperations.euclideanDistanceNoSqrt(vector1, vector2);
        }
    },
    EUCLIDEAN_DISTANCE("Euclidean Distance") {
        /**
         * Calculates the Euclidean distance between two vectors.
         * 
         * @param vector1 the first vector
         * @param vector2 the second vector
         * @return the Euclidean distance between vector1 and vector2
         * @throws Exception if the vectors have different lengths
         */
        @Override
        public double calculate(double[] vector1, double[] vector2) throws Exception {
            return VectorOperations.euclideanDistance(vector1, vector2);

        }
    },
    COSINE_SIMILARITY("Cosine Similarity") {
        /**
         * Calculates the cosine similarity between two vectors.
         * 
         * @param vector1 the first vector
         * @param vector2 the second vector
         * @return the cosine similarity between vector1 and vector2
         * @throws Exception if the vectors have different lengths
         */
        @Override
        public double calculate(double[] vector1, double[] vector2) throws Exception {
            return VectorOperations.cosineSimilarity(vector1, vector2);
        }
    };

    /**
     * Represents an enum defining various similarity metrics for vector
     * comparisons.
     * Each enum constant encapsulates a specific similarity metric and implements
     * a method to calculate similarity between two vectors.
     */
    private String name;

    /**
     * Constructor for VectorSimilarityMetrics enum constants.
     * Initializes the display name or label of the similarity metric.
     *
     * @param name the name or label of the similarity metric
     */
    VectorSimilarityMetrics(String name) {
        this.name = name;
    }

    /**
     * Returns the name or label of the similarity metric.
     *
     * @return the name of the similarity metric
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Abstract method that calculates the similarity between two vectors.
     *
     * @param vector1 the first vector
     * @param vector2 the second vector
     * @return the similarity measure between vector1 and vector2
     * @throws Exception if there is an issue calculating the similarity
     */
    public abstract double calculate(double[] vector1, double[] vector2) throws Exception;
}
