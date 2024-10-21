package ie.atu.sw.util;

/**
 * Utility class with static methods for finding the indexes of the
 * minimum or maximum values in arrays of doubles.
 */
public class Array {
    /**
     * Find the indexes of 'numValues' maximum values in an array.
     * 
     * @param array     The array to search for maximum values.
     * @param numValues How many maximum values to find.
     * @return An array of the indexes of the 'numValues' maximum values.
     * @throws Exception If the array length is shorter than 'howMany'.
     */
    public static int[] getMaxValuesIndexes(double[] array, int numValues) throws Exception {
        if (numValues > array.length)
            throw new Exception(
                    "Can't find largest " + numValues + " elements of array with length of " + array.length);

        // Create an array to store the indexes of maximum values
        int[] maxValuesIndexes = new int[numValues];

        // Make a copy of the original array to avoid modifying the original
        double[] arrayCopy = new double[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);

        // Find and store 'numValues' maximum values' indexes
        for (int i = 0; i < maxValuesIndexes.length; i++)
            maxValuesIndexes[i] = getMaxValueIndex(arrayCopy);

        return maxValuesIndexes;
    }

    /**
     * Find the index of the maximum value in the array and mark it as removed from
     * future searches.
     * 
     * @param array - the array to search
     * @return the index of the maximum value found in the array
     */
    public static int getMaxValueIndex(double[] array) {
        int maxValueIndex = 0; // Initialize with the index of the first element

        // Iterate through the array to find the index of the maximum value
        for (int i = 1; i < array.length; i++)
            if (array[i] > array[maxValueIndex])
                maxValueIndex = i; // Update maxValueIndex if a larger value is found

        // Mark the maximum value as removed by setting it to a very low value
        array[maxValueIndex] = -Double.MAX_VALUE;

        return maxValueIndex; // Return the index of the maximum value in the array
    }

    /**
     * Find the indexes of 'numValues' minimum values in the array.
     * 
     * @param array     - the array to search
     * @param numValues - how many results to search for
     * @return an array of the indexes of the 'numValues' smallest values in the
     *         array
     * @throws Exception if the array length is shorter than 'howMany'
     */
    public static int[] getMinValuesIndexes(double[] array, int numValues) throws Exception {
        if (numValues > array.length)
            throw new Exception(
                    "Can't find smallest " + numValues + " elements of array with length of " + array.length);

        int[] minValuesIndexes = new int[numValues];

        double[] arrayCopy = new double[array.length];
        System.arraycopy(array, 0, arrayCopy, 0, array.length);

        for (int i = 0; i < minValuesIndexes.length; i++)
            minValuesIndexes[i] = getMinValueIndex(arrayCopy);

        return minValuesIndexes; // Return the index of the minimum value in the array
    }

    /**
     * Find the index of the minimum value in the array and mark it with the highest
     * possible value,
     * effectively removing it from future searches.
     *
     * @param array - the array to search
     * @return the index of the minimum value in the array
     */
    public static int getMinValueIndex(double[] array) {
        int minValueIndex = 0;

        // Iterate through the array to find the index of the minimum value
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[minValueIndex]) {
                minValueIndex = i;
            }
        }

        // Mark the found minimum value with the maximum possible value for future
        // exclusion
        array[minValueIndex] = Double.MAX_VALUE;

        return minValueIndex;
    }
}
