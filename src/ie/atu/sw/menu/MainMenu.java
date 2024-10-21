package ie.atu.sw.menu;

import java.nio.file.NoSuchFileException;
import java.util.Scanner;

import ie.atu.sw.console.ConsolePrint;

/**
 * Handles user interactions and application launches.
 *
 * When launching an application, prompts for a words-embeddings file if not
 * loaded.
 *
 * Options:
 * - [1] 'Specify Embedding File'
 * - [2] Specify an Output File (default: ./output.txt)
 * - [3] Enter a Word or Text for 'Similiar Words'
 * - [4] Enter a Word or Text for 'Dissimilar Words'
 * - [5] Run 'Text Calculator'
 * - [6] Configure Options
 * - [q] Quit
 */

public class MainMenu {
    // Scanner to read user input
    private Scanner inputScanner = new Scanner(System.in);

    // ConfigureOptionsMenu to handle settings related tasks
    private ConfigureOptionsMenu settingsMenu = new ConfigureOptionsMenu(this.inputScanner);

    /**
     * Display options to the user and process their choice.
     * 
     * This method prints the menu title and available options,
     * then waits for the user's input to process it accordingly.
     */
    public void launchMenu() {
        MainMenuObject.printTitle();
        MainMenuObject.printOptions();

        processInput();
    }

    /**
     * Accept user input from the terminal, process it, and launch the appropriate
     * action or application.
     * Handle all errors that may occur during input processing and application
     * launching.
     */
    private void processInput() {
        try {
            // Read user input
            String input = this.inputScanner.nextLine();
            // Determine the corresponding MainMenuObject based on user input
            MainMenuObject item = MainMenuObject.valueOfKey(input);

            // Perform actions based on the selected MainMenuObject
            switch (item) {
                case SPECIFY_EMBEDDING_FILE -> settingsMenu.setEmbeddingFilePath();
                case SPECIFY_OUTPUT_FILE -> settingsMenu.specifyNewDataOutputFileName();
                case SIMILAR_WORDS -> launchSimilarWords();
                case DISSIMILAR_WORDS -> launchDissimilarWords();
                case TEXT_CALCULATOR -> launchTextCalculator();
                case CONFIGURE_OPTIONS -> launchConfigureOptions();
                case QUIT -> quitProgram();
                default -> throw new Exception("This should never happen!");
            }

        } catch (NoSuchFileException e) {
            // Handle file not found exception
            ConsolePrint.printError("File not found: '" + e.getMessage() + "'");
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            ConsolePrint.printError(e.getMessage());
        } finally {
            // After processing, relaunch the main menu
            launchMenu();
        }

    }

    /**
     * Launches the functionality to 'Find Similar Words'.
     * 
     * @throws Exception if an error occurs during the operation
     */
    private void launchSimilarWords() throws Exception {
        launchSimilarWords(true); // Default behavior to find similar words
    }

    /**
     * Launches the functionality to 'Find Dissimilar Words'.
     * 
     * @throws Exception if an error occurs during the operation
     */
    private void launchDissimilarWords() throws Exception {
        launchSimilarWords(false); // Launch with behavior to find dissimilar words
    }

    /**
     * Finds similar or dissimilar words based on user input in the terminal.
     * Customizations can be configured via the settings menu.
     * 
     * @param similar true to find similar words, false to find dissimilar words
     * @throws Exception if an error occurs during word similarity calculation or
     *                   output
     */
    private void launchSimilarWords(boolean similar) throws Exception {
        settingsMenu.initWordsEmbeddings(); // Ensure word embeddings are initialized

        // Print appropriate section header based on similarity type
        if (similar)
            ConsolePrint.printSection("Find Similar Words");
        else
            ConsolePrint.printSection("Find Dissimilar Words");

        settingsMenu.printConfigureOptions(); // Print configuration options to the terminal

        System.out.print("Enter word(s): ");

        String input = this.inputScanner.nextLine();
        String sanitizedInput = input.replaceAll("[\s]+", " ").toLowerCase();
        ConsolePrint.printInfo("Interpreting input as: '" + sanitizedInput + "'");
        String[] words = sanitizedInput.split(" ");

        // Process each word to find similar or dissimilar words
        try {
            // Find similar or dissimilar words for the current word
            settingsMenu.getWordsEmbeddings().getSimilarWords(
                    words[0],
                    settingsMenu.getNumberOfSimilaritiesToFind(),
                    similar);

            // Print the results to the configured output
            settingsMenu.printDataOutput(words[0], similar);

        } catch (Exception e) {
            ConsolePrint.printError(e.getMessage());
        }

        // Reset append mode to its original value after processing all words
        boolean appendData = settingsMenu.getAppendDataOutputFile();
        settingsMenu.setAppendDataOutputFile(true);
        for (int i = 1; i < words.length; i++) {
            try {

                settingsMenu.getWordsEmbeddings().getSimilarWords(
                        words[i],
                        settingsMenu.getNumberOfSimilaritiesToFind(),
                        similar);

                settingsMenu.printDataOutput(words[i], similar);

            } catch (Exception e) {
                ConsolePrint.printError(e.getMessage());
            }
        }
        settingsMenu.setAppendDataOutputFile(appendData);
    }

    /**
     * Launches the 'Text Calculator' application, allowing users to perform
     * calculations or operations on text data.
     * 
     * @throws Exception if an error occurs during the launch or operation of the
     *                   text calculator
     */
    private void launchTextCalculator() throws Exception {
        // Launches the TextCalculatorMenu instance, passing the input scanner and
        // settings menu
        new TextCalculatorMenu(this.inputScanner, this.settingsMenu).launchMenu();
    }

    /**
     * Launches the settings menu, allowing users to configure various options
     * and settings of the application.
     * 
     * @throws Exception if an error occurs during the launch or operation of the
     *                   settings menu
     */
    private void launchConfigureOptions() throws Exception {
        // Launches the settings menu associated with this instance
        settingsMenu.launchMenu();
    }

    /**
     * Terminates the program execution gracefully by closing the input scanner,
     * printing a quit message to the console, and exiting the application.
     */
    private void quitProgram() {
        // Close the input scanner to release system resources
        this.inputScanner.close();
        // Print a heading indicating the program is quitting
        ConsolePrint.printHeading("Quitting...");
        // Print an informational message indicating that the program has quit
        ConsolePrint.printInfo("Quit");
        // Print a newline for better console output formatting
        System.out.println();
        // Exit the program with a status code of 0, indicating successful termination
        System.exit(0);
    }
}
