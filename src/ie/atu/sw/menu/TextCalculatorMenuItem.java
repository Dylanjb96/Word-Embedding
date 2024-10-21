package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;

/**
 * Enum for text calculator menu options with auto-generated shortcuts.
 */
public enum TextCalculatorMenuItem {

    ADD("Add", "+"),
    SUBTRACT("Subtract", "-"),
    MULTIPLY("Multiply", "*"),
    DIVIDE("Divide", "/"),
    QUIT("Close 'Text Calculator'", "q");

    public final String key; // Keyboard shortcut for the menu option
    public final String title; // Title of the menu option

    /**
     * Constructor to create a menu item/option.
     * 
     * @param title - The title of the menu option to be displayed in the terminal.
     * @param key   - Use 'null' to auto-generate a shortcut based on position in
     *              list, or provide a custom key, like 'q' for quit.
     */
    private TextCalculatorMenuItem(String title, String key) {
        this.title = title;
        this.key = key != null ? key : String.valueOf(this.ordinal() + 1);
    }

    /**
     * Look up a keyboard shortcut and find the appropriate menu item.
     * 
     * @param key - The keyboard shortcut to match.
     * @return The matching menu item.
     * @throws Exception If no matching menu item is found.
     */
    public static TextCalculatorMenuItem valueOfKey(String key) throws Exception {
        // Loop through all enum values
        for (TextCalculatorMenuItem item : TextCalculatorMenuItem.values())
            // Compare the keyboard shortcut (case insensitive)
            if (item.key.equals(key.toLowerCase()))
                return item; // Return the matching menu item

        // Throw an exception if no matching menu item is found
        throw new Exception("'" + key + "' is not a valid option.");
    }

    /**
     * Return a string representation of the menu item, including its keyboard
     * shortcut.
     * 
     * @return A formatted string with the menu item's keyboard shortcut and title.
     */
    @Override
    public String toString() {
        return "[" + this.key + "] " + this.title;
    }

    /**
     * Print the title of the menu to the terminal.
     */
    public static void printTitle() {
        ConsolePrint.printTitle("Configuration");
    }

    /**
     * Print all menu options with their shortcuts to the terminal.
     */
    public static void printOptions() {
        // Create an array to store menu option strings
        String[] options = new String[TextCalculatorMenuItem.values().length];
        int index = 0;

        // Iterate through each enum value to construct menu option strings
        for (TextCalculatorMenuItem item : TextCalculatorMenuItem.values()) {
            options[index++] = item.toString(); // Convert enum item to string format
        }

        // Print the menu options using a utility method (assuming
        // ConsolePrint.printMenuOptions() exists)
        ConsolePrint.printMenuOptions(options);
    }
}
