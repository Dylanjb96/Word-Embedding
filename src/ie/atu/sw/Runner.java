package ie.atu.sw;

import ie.atu.sw.menu.MainMenu;

/**
 * The Runner class serves as the entry point for the Words Embeddings Text
 * Interface application.
 * It initializes and launches the main menu for user interaction.
 */
public class Runner {

    /**
     * The main method is the entry point of the application. It initializes and
     * launches the main menu.
     * 
     * @param args command line arguments
     * @throws Exception if an error occurs during the execution of the application
     */
    public static void main(String[] args) throws Exception {
        // Launch the main menu of the application
        new MainMenu().launchMenu();
    }
}
