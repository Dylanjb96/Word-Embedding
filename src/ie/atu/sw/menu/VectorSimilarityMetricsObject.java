package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;
import ie.atu.sw.util.VectorSimilarityMetrics;

/**
 * A utility class representing a menu for vector similarity metrics.
 */
public class VectorSimilarityMetricsObject {

    /**
     * Look up a keyboard shortcut and find the corresponding similarity metric.
     * 
     * @param key - The keyboard shortcut to match.
     * @return The matching similarity metric.
     * @throws Exception if no matching similarity metric is found.
     */
    public static VectorSimilarityMetrics valueOfKey(String key) throws Exception {
        // Iterate through all available similarity metrics
        for (VectorSimilarityMetrics item : VectorSimilarityMetrics.values()) {
            // Check if the ordinal (index + 1) matches the provided key
            if (Integer.toString(item.ordinal() + 1).equals(key)) {
                return item; // Return the matching similarity metric
            }
        }
        // If no match is found, throw an exception indicating an invalid input
        throw new Exception("'" + key + "' is not a valid similarity metric.");
    }

    /**
     * Print the section title for similarity metrics to the terminal.
     */
    public static void printSection() {
        ConsolePrint.printSection("Similarity Metrics");
    }

    /**
     * Print all the menu options for similarity algorithms with their shortcuts to
     * the terminal.
     */
    public static void printOptions() {
        String[] options = new String[VectorSimilarityMetrics.values().length];
        int index = 0;

        // Populate the options array with formatted strings of each similarity
        // algorithm
        for (VectorSimilarityMetrics item : VectorSimilarityMetrics.values()) {
            options[index++] = "[" + (item.ordinal() + 1) + "] " + item.toString();
        }

        // Print the menu options using a utility method or class (assuming ConsolePrint
        // is used for printing)
        ConsolePrint.printMenuOptions(options);
    }

}
