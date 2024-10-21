package ie.atu.sw.menu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;
import java.util.prefs.Preferences;

import ie.atu.sw.console.ConsolePrint;
import ie.atu.sw.embeddings.WordsEmbeddingsSearch;
import ie.atu.sw.util.VectorSimilarityMetrics;

/**
 * Manages user preferences and settings with persistent storage using
 * java.util.prefs.Preferences.
 * Default settings are provided and can be customized through user interaction.
 */
public class ConfigureOptionsMenu {

    private Scanner inputScanner; // Scanner for reading user input from the terminal
    private String embeddingFilePath; // File path for word embeddings
    private String outputFilePath; // File path for data output
    private Preferences preferences = Preferences.userNodeForPackage(ConfigureOptionsMenu.class); // User preferences
                                                                                                  // storage
    private boolean keepSettingsOpen; // Flag indicating whether settings menu should remain open
    private WordsEmbeddingsSearch wordsEmbeddings; // Handles operations related to word embeddings

    /**
     * Constructs a new instance of ConfigureOptionsMenu with the provided Scanner
     * object.
     * This class manages user preferences and settings using
     * java.util.prefs.Preferences.
     * 
     * @param inputScanner Scanner object to read user input from the terminal
     */
    public ConfigureOptionsMenu(Scanner inputScanner) {
        this.inputScanner = inputScanner;
    }

    /**
     * Get the WordsEmbeddings instance used in this class
     * 
     * @return the instance of WordsEmbeddings used in this class
     */
    public WordsEmbeddingsSearch getWordsEmbeddings() {
        return this.wordsEmbeddings;
    }

    /**
     * Get the stored file name used when the words-embeddings file was loaded
     * 
     * @return - the words-embeddings file name
     */
    private String getWordsEmbeddingsFileName() {
        return this.preferences.get("wordsEmbeddingsFileName", "./word-embeddings.txt");
    }

    /**
     * Set and store the words-embeddings file name
     * 
     * @param fileName - the words-embeddings file name
     */
    private void setWordsEmbeddingsFileName(String fileName) {
        this.preferences.put("wordsEmbeddingsFileName", fileName);
    }

    /**
     * Set the path for the embedding file based on user input.
     * Prompts the user to enter the file path and prints a confirmation message.
     */
    public void setEmbeddingFilePath() {
        System.out.print("Enter the path for the embedding file: ");
        this.embeddingFilePath = inputScanner.nextLine();
        ConsolePrint.printInfo("Embedding file path set to: " + this.embeddingFilePath);
    }

    /**
     * Set the path for the output file based on user input.
     * Prompts the user to enter the file path and prints a confirmation message.
     */
    public void setOutputFilePath() {
        System.out.print("Enter the path for the output file: ");
        this.outputFilePath = inputScanner.nextLine();
        ConsolePrint.printInfo("Output file path set to: " + this.outputFilePath);
    }

    /**
     * Retrieve the path of the embedding file currently set.
     * 
     * @return The path of the embedding file.
     */
    public String getEmbeddingFilePath() {
        return this.embeddingFilePath;
    }

    /**
     * Retrieve the path of the output file currently set.
     * 
     * @return The path of the output file.
     */
    public String getOutputFilePath() {
        return this.outputFilePath;
    }

    public void initWordsEmbeddings() throws Exception {
        // Check if the embedding file path is set and not empty
        if (this.embeddingFilePath == null || this.embeddingFilePath.isEmpty()) {
            throw new NoSuchFileException("Please set Embedding file.");
        }

        // Check if the wordsEmbeddings object is initialized, and if not, load a new
        // file
        if (this.wordsEmbeddings == null) {
            loadNewWordsEmbeddingsFile();
        }

        // Apply the currently set similarity metric to the current WordsEmbeddings
        // class
        this.wordsEmbeddings.setVectorSimilarity(getVectorSimilarity());
    }

    /**
     * Retrieve the stored number of similarities to find.
     * Defaults to 10 if not explicitly set.
     * 
     * @return The number of similarities to find.
     */
    public int getNumberOfSimilaritiesToFind() {
        return this.preferences.getInt("numberOfSimilaritiesToFind", 10);
    }

    /**
     * Set and store the number of similarities to find
     * 
     * @param number The number of similarities to find
     */
    private void setNumberOfSimilaritiesToFind(int number) {
        this.preferences.putInt("numberOfSimilaritiesToFind", number);
    }

    /**
     * Retrieve the stored similarity metric to use in word searches.
     * Defaults to cosine similarity, represented by the shortcut '4' in
     * VectorSimilarityMetricsObject.
     * 
     * @return The current similarity metric to use in word searches.
     * @throws Exception If the stored metric number is invalid or not found.
     */
    public VectorSimilarityMetrics getVectorSimilarity() throws Exception {
        String metricNumber = this.preferences.get("similarityMetricNumber", "4");
        return VectorSimilarityMetricsObject.valueOfKey(metricNumber);
    }

    /**
     * Set and store the similarity metric to use in word searches.
     * The metric is stored as an integer corresponding to a shortcut in
     * VectorSimilarityMetricsObject.
     * 
     * @param metric The similarity metric to use in word searches.
     */
    private void setVectorSimilarity(VectorSimilarityMetrics metrics) {
        String metricNumber = Integer.toString(metrics.ordinal() + 1);
        this.preferences.put("similarityMetricNumber", metricNumber);
    }

    /**
     * Retrieve the boolean representing whether the similarity score should be
     * included
     * in the data output.
     * Defaults to false if not explicitly set.
     * 
     * @return true if the similarity score should be included in the data output,
     *         false otherwise.
     */
    public boolean getAddSimilarityScore() {
        return this.preferences.getBoolean("addSimilarityScore", false);
    }

    /**
     * Set and store whether the similarity score should be included in the data
     * output.
     * 
     * @param addSimilarityScore - true to include the similarity score in the data
     *                           output, false otherwise
     */
    private void setAddSimilarityScore(boolean append) {
        this.preferences.putBoolean("addSimilarityScore", append);
    }

    /**
     * Retrieve the currently set data output file name.
     * 
     * @return The currently set data output file name, defaulting to
     *         "./output.txt".
     */
    public String getDataOutputFileName() {
        return this.preferences.get("dataOutputFileName", "./output.txt");
    }

    /**
     * Set the data output file name to use.
     * 
     * @param fileName The data output file name to use.
     */
    private void setDataOutputFileName(String fileName) {
        this.preferences.put("dataOutputFileName", fileName);
    }

    /**
     * Get a BufferedWriter pointing to the currently set data-output file.
     * 
     * @return A BufferedWriter pointing to the currently set data-output file.
     * @throws IOException If there's an error creating the BufferedWriter.
     */
    public BufferedWriter getDataOutputBufferedWriter() throws IOException {
        File dataOutputFile = new File(getDataOutputFileName());
        FileWriter dataOutputFileWriter = new FileWriter(dataOutputFile, getAppendDataOutputFile());
        BufferedWriter dataOutputBufferedWriter = new BufferedWriter(dataOutputFileWriter);
        return dataOutputBufferedWriter;
    }

    /**
     * Get a boolean indicating whether data should be appended (true) or
     * overwritten (false)
     * in the data-output file.
     * 
     * @return true if data should be appended, false if data should overwrite the
     *         existing content.
     */
    public boolean getAppendDataOutputFile() {
        return this.preferences.getBoolean("appendDataOutputFile", true);
    }

    /**
     * Set whether data should be appended (true) or overwritten (false) in the
     * data-output file.
     * 
     * @param append true to append data, false to overwrite existing content
     */
    public void setAppendDataOutputFile(boolean append) {
        this.preferences.putBoolean("appendDataOutputFile", append);
    }

    /**
     * Format the search parameter used for display as a heading, including other
     * relevant settings.
     * 
     * @param dataHeadingText text representing the search value(s) used, to appear
     *                        in the heading, along with other heading info
     * @param similar         true if searching for similar words, false for
     *                        dissimilar words
     * @return the formatted heading text
     * @throws Exception if there is an issue retrieving the similarity algorithm
     */
    public String getSettingsAsHeading(String dataHeadingText, boolean similar) throws Exception {
        String heading = getNumberOfSimilaritiesToFind() + " ";
        heading += getAddSimilarityScore()
                ? "Scores/"
                : "";
        heading += similar
                ? "Words Similar to '"
                : "Words Dissimilar to '";
        heading += dataHeadingText + "'";
        heading += " using " + getVectorSimilarity() + ":";

        return heading;
    }

    /**
     * Print the most recently searched WordsEmbeddings matches neatly to the stored
     * data-output file.
     * 
     * @param dataHeadingText text representing the search value(s) used, to appear
     *                        in the heading along with other heading info
     * @param similar         true if searching for similar words, false for
     *                        dissimilar words
     * @throws Exception if there is an issue retrieving data or writing to the
     *                   data-output file
     */
    public void printDataOutput(String dataHeadingText, boolean similar) throws Exception {
        BufferedWriter dataOutputBufferedWriter = getDataOutputBufferedWriter();

        String heading = getSettingsAsHeading(dataHeadingText, similar);

        ConsolePrint.printHeading(heading);
        dataOutputBufferedWriter.write(heading);
        dataOutputBufferedWriter.newLine();

        for (int i = 0; i < getWordsEmbeddings().getPreviousSimilarWords().length; i++) {
            if (getAddSimilarityScore()) {
                double score = getWordsEmbeddings().getPreviousSimilarWordsScores()[i];
                String formattedScore = String.format("%24.18f", score);
                System.out.print(formattedScore + " ");
                dataOutputBufferedWriter.write(formattedScore + " ");
            }

            System.out.print(getWordsEmbeddings().getPreviousSimilarWords()[i]);
            dataOutputBufferedWriter.write(getWordsEmbeddings().getPreviousSimilarWords()[i]);

            System.out.println();
            dataOutputBufferedWriter.newLine();
        }

        dataOutputBufferedWriter.newLine();
        dataOutputBufferedWriter.close();
    }

    /**
     * Prints the menu title, displays all configuration options to the user, and
     * processes user input.
     * This method initializes the settings menu, prints the configuration menu
     * title, prints all configuration
     * options with their respective shortcuts, and waits for user input to process
     * the selected option.
     *
     * @throws Exception if there's an error while processing user input or
     *                   executing the selected option.
     */
    public void launchMenu() throws Exception {
        keepSettingsOpen = true; // Ensure the settings menu remains open until explicitly closed

        ConfigureMenuObject.printConfigure(); // Print the title of the configure menu
        ConfigureMenuObject.printConOptions(); // Print all configuration options with their shortcuts

        processInput(); // Process user input to execute the selected configuration option
    }

    /**
     * Reads user input from the terminal and performs the corresponding action
     * based on the selected menu item.
     * This method handles user interactions within the configure options menu,
     * processing the input to execute
     * actions such as loading files, setting parameters, toggling options, or
     * quitting the menu.
     *
     * @throws Exception if there's an error during input processing or while
     *                   executing the selected action.
     */
    private void processInput() throws Exception {

        try {

            String input = this.inputScanner.nextLine();
            ConfigureMenuObject item = ConfigureMenuObject.valueOfKey(input);

            switch (item) {
                case EMBEDDINGS_FILE -> loadNewWordsEmbeddingsFile();
                case SIMILARITIES_NUMBER -> specifySimilaritiesNumber();
                case SIMILARITIES_ALGORITHM -> specifyVectorSimilarity();
                case TOGGLE_SIMILARITY_SCORE -> toggleAddSimilarityScore();
                case OUTPUT_FILE -> specifyNewDataOutputFileName();
                case TOGGLE_APPEND -> toggleAppendDataOutputFile();
                case EMPTY_OUTPUT_FILE -> emptyDataOutputFile();
                case RESET -> resetConfigureOptions();
                case PRINT -> printConfigureOptions();
                case QUIT -> quitConfigureOptions();
                default -> throw new Exception("Something is not right!");
            }

        } catch (Exception e) {
            ConsolePrint.printError(e.getMessage());
        } finally {
            if (this.keepSettingsOpen)
                launchMenu(); // Re-launch the menu if settings should remain open
        }

    }

    /**
     * Prompts the user to specify and load a new words-embeddings file from user
     * input in the terminal.
     * This method initializes a new WordsEmbeddings instance with the specified
     * file and updates the stored
     * file name in preferences.
     *
     * @throws Exception if there is an error loading the new file or processing
     *                   user input.
     */
    public void loadNewWordsEmbeddingsFile() throws Exception {
        ConsolePrint.printHeading("Load New Words-Embeddings File");

        // Prompt user to enter the new file name
        String wordsEmbeddingsFileName = scanFileName(getWordsEmbeddingsFileName());

        // Initialize WordsEmbeddings instance with the new file
        this.wordsEmbeddings = new WordsEmbeddingsSearch(wordsEmbeddingsFileName);

        // Update the stored file name in preferences
        setWordsEmbeddingsFileName(wordsEmbeddingsFileName);

        // Print confirmation message
        ConsolePrint.printInfo("Words-Embeddings file loaded: " + wordsEmbeddingsFileName);
    }

    /**
     * Prompts the user to enter a new number of similarities to find when
     * performing word-embedding searches,
     * and stores the input value in preferences.
     *
     * @throws Exception if there is an error processing user input or storing the
     *                   value.
     */
    private void specifySimilaritiesNumber() throws Exception {
        ConsolePrint.printHeading("Specify Number of Similarities to Find");

        // Prompt user to enter the new number of similarities
        int similarities = scanSimilarities();

        // Store the new number of similarities in preferences
        setNumberOfSimilaritiesToFind(similarities);

        // Print confirmation message
        ConsolePrint.printInfo("Number of Similarities To Find is set to: " + similarities);
    }

    /**
     * Prompts the user to enter a new similarity metrics to use when performing
     * word-embedding similarity searches,
     * and stores the input value in preferences.
     *
     * @throws Exception if there is an error processing user input or storing the
     *                   value.
     */
    private void specifyVectorSimilarity() throws Exception {
        VectorSimilarityMetricsObject.printSection();
        VectorSimilarityMetricsObject.printOptions();

        // Read user input for the similarity algorithm
        String input = this.inputScanner.nextLine();

        // Determine the similarity algorithm based on user input
        VectorSimilarityMetrics metrics = VectorSimilarityMetricsObject.valueOfKey(input);

        // Store the new similarity algorithm in preferences
        setVectorSimilarity(metrics);

        // Print confirmation message
        ConsolePrint.printInfo("Similarity Metric set to: " + metrics);
    }

    /**
     * Prompts the user to enter a file name, with an option to use a default file
     * name by hitting ENTER.
     * 
     * @param defaultFileName The default file name to use if the user hits ENTER.
     * @return The user-provided file name or the default file name if no input is
     *         provided.
     */
    private String scanFileName(String defaultFileName) {
        ConsolePrint.printInfo("Hit ENTER for default Output File: " + defaultFileName);
        System.out.print("Enter Output File name: ");
        String input = this.inputScanner.nextLine();

        if (input.equals(""))
            input = defaultFileName;

        return input;
    }

    /**
     * Prompts the user to enter a new number of similarities to find when
     * performing
     * word-embedding searches.
     * 
     * @return The user-defined number of similarities to find.
     * @throws Exception If the input number is invalid or exceeds the available
     *                   word count.
     */
    private int scanSimilarities() throws Exception {
        try {

            System.out.print("Enter an integer (greater than 0): ");
            String input = this.inputScanner.nextLine();

            int number = Integer.parseInt(input);

            if (number < 1)
                throw new Exception("Number must be greater than 0");

            if (wordsEmbeddings != null) {
                if (number > this.wordsEmbeddings.getNumberOfWords())
                    throw new Exception(
                            "Number to big. Maximum words available = " + this.wordsEmbeddings.getNumberOfWords());
            } else {
                ConsolePrint
                        .printWarning("Make sure to load a words-embeddings file with at least " + number + " word(s)");
            }

            return number;

        } catch (NumberFormatException e) {
            throw new Exception("Invalid number. Using previous value: " + getNumberOfSimilaritiesToFind());
        }

    }

    /**
     * Toggles the boolean flag defining whether or not to use similarity scores in
     * the data
     * output and stores the updated value.
     */
    private void toggleAddSimilarityScore() {
        setAddSimilarityScore(!getAddSimilarityScore());

        if (getAddSimilarityScore())
            ConsolePrint.printInfo("Similarity metric scores will be used in output data");
        else
            ConsolePrint.printInfo("Similarity metic scores will NOT be used in output data");
    }

    /**
     * Prompts the user to enter a new data-output file name and updates the stored
     * value.
     * If data is set to append, prints an informative message confirming the file
     * name.
     */
    public void specifyNewDataOutputFileName() {
        ConsolePrint.printHeading("Load New/Define Data-Output File");

        String dataOutputFileName = scanFileName(getDataOutputFileName());

        setDataOutputFileName(dataOutputFileName);

        if (getAppendDataOutputFile())
            ConsolePrint.printInfo("Data output will now be appended to " + getDataOutputFileName());
        else
            ConsolePrint.printInfo("Data output will now overwrite " + getDataOutputFileName());
    }

    /**
     * Toggles the boolean flag to append new results to the data-output file (true)
     * or overwrite it (false).
     * Prints an informative message confirming the current file mode after
     * toggling.
     */
    private void toggleAppendDataOutputFile() {
        setAppendDataOutputFile(!getAppendDataOutputFile());

        if (getAppendDataOutputFile())
            ConsolePrint.printInfo("Data output will now be appended to " + getDataOutputFileName());
        else
            ConsolePrint.printInfo("Data output will now overwrite " + getDataOutputFileName());
    }

    /**
     * Empties the currently-set data-output file by deleting it if it exists,
     * then recreating it as an empty file.
     *
     * @throws Exception if there is a problem with file handling, such as
     *                   permissions or IO errors.
     */
    private void emptyDataOutputFile() throws Exception {
        try {

            // Get the File object representing the data-output file
            File dataOutputFile = new File(getDataOutputFileName());

            // Attempt to delete the existing file and recreate it
            if (dataOutputFile.delete())
                dataOutputFile.createNewFile();

        } catch (Exception e) {
            throw new Exception("Problem emptying data-output file: " + getDataOutputFileName());
        }

        ConsolePrint.printInfo("Data-Output file is now empty: " + getDataOutputFileName());
    }

    /**
     * Resets all settings to their default values by clearing the preferences.
     * 
     * @throws Exception if there is an issue clearing preferences or accessing
     *                   default values.
     */
    private void resetConfigureOptions() throws Exception {
        // Clear all preferences to reset settings to defaults
        this.preferences.clear();

        // Print heading to indicate reset process
        ConsolePrint.printHeading("Resetting Configure Options...");
        // Print current configuration options after reset
        printConfigureOptions();
    }

    /**
     * Prints all current configuration settings to the console.
     * 
     * @throws Exception if there is an issue accessing configuration settings.
     */
    public void printConfigureOptions() throws Exception {

        // Retrieve the current words embeddings file name or indicate not set
        String wordsEmbeddingsFileName = this.wordsEmbeddings == null ? "NOT SET" : this.wordsEmbeddings.getFileName();
        // Determine if data output file is set to append or overwrite
        String appendOverwrite = getAppendDataOutputFile() ? "append" : "overwrite";

        // Print each configuration setting with descriptive labels
        ConsolePrint.printInfo("Embeddings File: " + wordsEmbeddingsFileName);
        ConsolePrint.printInfo("Number of Words to Find: " + getNumberOfSimilaritiesToFind());
        ConsolePrint.printInfo("Data-Output File: " + getDataOutputFileName());
        ConsolePrint.printInfo("Append/Overwrite Mode: " + appendOverwrite);
        ConsolePrint.printInfo("Include Similarity Score: " + getAddSimilarityScore());
        ConsolePrint.printInfo("Similarity Metrics: " + getVectorSimilarity());
        ConsolePrint.printInfo("Configuration Options resetted");
        System.out.println();
    }

    /**
     * Stops the settings loop by toggling the 'keepSettingsOpen' boolean.
     * Prints a heading and informational message indicating closure.
     */
    private void quitConfigureOptions() {
        // Set keepSettingsOpen to false to stop the settings loop
        this.keepSettingsOpen = false;

        // Print a heading indicating the action being taken
        ConsolePrint.printHeading("Closing Configure Options...");

        // Print an informational message confirming closure
        ConsolePrint.printInfo("Configure Options Closed");
    }
}