package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;

/**
 * Enum representing the main menu options.
 * 
 * Each enum constant corresponds to a menu option that can be displayed
 * and selected in the main menu. Each option has a title that describes
 * the action and an optional keyboard shortcut key.
 */
public enum MainMenuObject {

    SPECIFY_EMBEDDING_FILE("Specify Embedding File", null),
    SPECIFY_OUTPUT_FILE("Specify an Output File (default: ./output.txt)", null),
    SIMILAR_WORDS("Enter a Word or Text for 'Similiar Words'", null),
    DISSIMILAR_WORDS("Enter a Word or Text for 'Dissimilar Words'", null),
    TEXT_CALCULATOR("Run 'Text Calculator'", null),
    CONFIGURE_OPTIONS("Configure Options", null),
    QUIT("Quit", "q");

    public final String key;
    public final String title;

    /**
     * Creates a menu option.
     *
     * @param title the title of the menu option to be displayed in the terminal.
     * @param key   the shortcut key for the menu option. Use 'null' to
     *              auto-generate
     *              a shortcut based on its position in the list, or provide a
     *              custom
     *              key (e.g., 'q' for quit).
     */
    private MainMenuObject(String title, String key) {
        this.title = title;
        this.key = key != null ? key : String.valueOf(this.ordinal() + 1);
    }

    /**
     * Looks up a keyboard shortcut and finds the corresponding menu item.
     * 
     * @param key - the keyboard shortcut to match.
     * @return the matching menu item.
     * @throws Exception if no matching menu item is found.
     */
    public static MainMenuObject valueOfKey(String key) throws Exception {
        for (MainMenuObject item : MainMenuObject.values())
            if (item.key.equals(key.toLowerCase()))
                return item;

        throw new Exception("'" + key + "' is an unvalid option. There are only 6 options.");
    }

    /**
     * Print the menu item along with it's keyboard shortcut.
     * 
     * * Returns a string representation of the menu item.
     * The format is: [key] title
     * 
     * @return a string in the format of "[key] title"
     */

    @Override
    public String toString() {
        return "[" + this.key + "] " + this.title;
    }

    /**
     * Print the menu's title to the terminal.
     */
    public static void printTitle() {
        String titleBlock = "************************************************************\n" +
                "*     ATU - Dept. of Computer Science & Applied Physics    *\n" +
                "*                                                          *\n" +
                "*          Similarity Search with Word Embeddings          *\n" +
                "*                                                          *\n" +
                "************************************************************\n";
        ConsolePrint.printTitle(titleBlock);
    }

    /**
     * Prints all available main menu options to the console.
     * 
     * This method retrieves all the values from the MainMenuObject enum,
     * converts them to their string representation, and then prints them
     * using the ConsolePrint utility.
     */
    public static void printOptions() {
        String[] options = new String[MainMenuObject.values().length];
        int index = 0;

        for (MainMenuObject item : MainMenuObject.values())
            options[index++] = item.toString();

        ConsolePrint.printMenuOptions(options);
    }
}
