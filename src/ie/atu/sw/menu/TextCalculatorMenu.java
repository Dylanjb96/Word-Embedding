package ie.atu.sw.menu;

import java.io.IOException;
import java.util.Scanner;

import ie.atu.sw.console.ConsolePrint;

/**
 * Handle user interactions which perform vector operations on words, and return
 * word-search results.
 */
public class TextCalculatorMenu {
    private Scanner inputScanner; // Scanner to read user input from the terminal
    private ConfigureOptionsMenu settingsMenu; // Settings menu where user preferences are stored
    private StringBuilder currentWordCalculation = new StringBuilder(); // StringBuilder to store current word
                                                                        // calculation
    private double[] currentEmbedding; // Current word embedding vector
    private boolean keepWordCalculatorOpen; // Flag to control whether the calculator loop continues

    /**
     * Initialize the TextCalculatorMenu class and ensure that a words-embeddings
     * file has been initialized.
     * 
     * @param inputScanner - Scanner to accept user input from the terminal
     * @param settingsMenu - The settings menu where all user preferences are stored
     * @throws Exception if there is an issue initializing the words-embeddings file
     */
    public TextCalculatorMenu(Scanner inputScanner, ConfigureOptionsMenu settingsMenu) throws Exception {
        this.inputScanner = inputScanner; // Initialize the inputScanner
        this.settingsMenu = settingsMenu; // Initialize the settingsMenu

        // Initialize the words-embeddings file in the settings menu
        this.settingsMenu.initWordsEmbeddings();
    }

    /**
     * When launched, initiates the text calculator menu.
     * Prompts the user to enter a word to begin calculations, using its vector
     * representation.
     * Retrieves similar words based on the entered word's embedding and prints
     * them.
     * Processes user input for performing vector operations on words until the user
     * exits.
     * 
     * @throws Exception if there is an issue with user input or processing
     */
    public void launchMenu() throws Exception {
        this.keepWordCalculatorOpen = true; // Flag to control the calculator loop

        ConsolePrint.printSection("Text Calculator"); // Print section header for Text Calculator
        this.settingsMenu.printConfigureOptions(); // Print current configuration options

        String input = processWordInput(); // Prompt user for a word to start calculations

        // Append the chosen word to currentWordCalculation
        this.currentWordCalculation.append(input);
        this.currentWordCalculation.append(";");

        // Retrieve the word embedding vector for the chosen word
        this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().getWordEmbedding(input);

        // Retrieve and print similar words based on the chosen word's embedding
        settingsMenu.getWordsEmbeddings().getSimilarWords(
                this.currentEmbedding,
                settingsMenu.getNumberOfSimilaritiesToFind());

        // Print data output with the current word calculation and similar words
        settingsMenu.printDataOutput(this.currentWordCalculation.toString(), true);

        // Print menu options and process user input for vector operations
        printOptionsAndProcessInput();
    }

    /**
     * Display a heading including all previous user inputs, present options for
     * further calculations, execute the selected action, and print data output
     * based
     * on user settings.
     * 
     * @throws Exception if there is an issue with user input or processing
     */
    private void printOptionsAndProcessInput() throws Exception {
        try {
            // Print a section header with current word calculation details
            ConsolePrint.printSection("Text Calculator\n" + this.currentWordCalculation.toString());

            // Print available menu options for text calculator operations
            TextCalculatorMenuItem.printOptions();

            // Read user input to determine the operation to perform
            String input = this.inputScanner.nextLine();
            TextCalculatorMenuItem item = TextCalculatorMenuItem.valueOfKey(input);

            // Perform the selected operation based on user input
            switch (item) {
                case ADD -> {
                    input = processWordInput();
                    // Perform addition operation on current embedding with input word embedding
                    this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().add(
                            this.currentEmbedding,
                            input);
                    // Calculate and print output for addition operation
                    calculateAndPrintOutput(" + " + input);
                }
                case SUBTRACT -> {
                    input = processWordInput();
                    // Perform subtraction operation on current embedding with input word embedding
                    this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().subtract(
                            this.currentEmbedding,
                            input);
                    // Calculate and print output for subtraction operation
                    calculateAndPrintOutput(" - " + input);
                }
                case MULTIPLY -> {
                    input = processWordInput();
                    // Perform multiplication operation on current embedding with input word
                    // embedding
                    this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().multiply(
                            this.currentEmbedding,
                            input);
                    // Calculate and print output for multiplication operation
                    calculateAndPrintOutput(" * " + input);
                }
                case DIVIDE -> {
                    input = processWordInput();
                    // Perform division operation on current embedding with input word embedding
                    this.currentEmbedding = this.settingsMenu.getWordsEmbeddings().divide(
                            this.currentEmbedding,
                            input);
                    // Calculate and print output for division operation
                    calculateAndPrintOutput(" / " + input);
                }
                case QUIT -> quitWordCalculator(); // Quit the text calculator menu
            }

        } catch (IOException e) {
            ConsolePrint.printError("File Input/Output Error: " + e.getMessage());
        } catch (Exception e) {
            ConsolePrint.printError(e.getMessage());
        } finally {
            // If keepWordCalculatorOpen flag is true, continue to prompt for operations
            if (this.keepWordCalculatorOpen)
                printOptionsAndProcessInput();
        }
    }

    /**
     * A consistent way to prompt the user for a word input.
     * 
     * @return A sanitized version of the word input (a single lowercase word).
     */
    private String processWordInput() {
        System.out.print("Enter a word: ");

        // Read user input
        String input = this.inputScanner.nextLine();

        // Sanitize input: Remove extra spaces, convert to lowercase, and extract the
        // first word
        String sanitizedInput = input.replaceAll("[\\s]+", " ").toLowerCase().split(" ")[0];

        // Print information about the sanitized input
        ConsolePrint.printInfo("Deciphering input: '" + sanitizedInput + "'");

        return sanitizedInput;
    }

    /**
     * Find similar-or-dissimilar words based on user input and settings, and print
     * data output.
     * 
     * @param input - The calculation-type and word that were input by the user
     *              (used for user feedback)
     * @throws Exception if there is an issue during the calculation or printing
     *                   process
     */
    private void calculateAndPrintOutput(String input) throws Exception {
        // Append the input to the current word calculation
        this.currentWordCalculation.append(input);
        this.currentWordCalculation.append(";");

        // Retrieve similar words based on the current word embedding and user settings
        settingsMenu.getWordsEmbeddings().getSimilarWords(
                this.currentEmbedding,
                settingsMenu.getNumberOfSimilaritiesToFind());

        // Print the data output based on the current word calculation and settings
        settingsMenu.printDataOutput(this.currentWordCalculation.toString(), true);
    }

    /**
     * Allow settings loop to stop running by toggling 'keepWordCalculatorOpen'
     * boolean.
     */
    private void quitWordCalculator() {
        this.keepWordCalculatorOpen = false;

        ConsolePrint.printHeading("Closing Text Calculator...");
        ConsolePrint.printInfo("Text Calculator Closed");
    }
}