package ie.atu.sw.menu;

import ie.atu.sw.console.ConsolePrint;

/**
 * Enum representing options for the settings menu with auto-generated or custom
 * shortcuts.
 */
public enum ConfigureMenuObject {

    EMBEDDINGS_FILE("Load New Word-Embeddings File", null),
    OUTPUT_FILE("Load New Data-Output File", null),
    SIMILARITIES_NUMBER("Number of Similarities to Find", null),
    SIMILARITIES_ALGORITHM("Select Similarity Metric to Use", null),
    TOGGLE_SIMILARITY_SCORE("Toggle Similarity Score in Output", null),
    TOGGLE_APPEND("Switch between Append/Overwrite Modes Data-Output File", null),
    EMPTY_OUTPUT_FILE("Clear the Output File", null),
    RESET("Reset Configure Options to Defaults", null),
    PRINT("Print Current Configuration Options", null),
    QUIT("Close Configure Options", "q");

    public final String key;
    public final String setting;

    /**
     * Constructor to create a menu item/option.
     *
     * @param setting - The title of the menu option to be displayed in the
     *                terminal.
     * @param key     - Use 'null' to auto-generate a shortcut based on position in
     *                the list,
     *                or provide a custom key, like 'q' for quit.
     */
    private ConfigureMenuObject(String setting, String key) {
        this.setting = setting;
        // Generate shortcut key: if custom key is provided, use it; otherwise, generate
        // based on ordinal position.
        this.key = key != null ? key : String.valueOf(this.ordinal() + 1);
    }

    /**
     * Looks up a keyboard shortcut and finds the corresponding menu item.
     * 
     * @param key - The keyboard shortcut to match.
     * @return The matching menu item.
     * @throws Exception if no matching menu item is found for the provided key
     */
    public static ConfigureMenuObject valueOfKey(String key) throws Exception {
        // Iterate through all enum values to find the one matching the provided key.
        for (ConfigureMenuObject item : ConfigureMenuObject.values())
            if (item.key.equals(key.toLowerCase()))
                return item; // Return the matching menu item.

        // Throw an exception if no matching menu item is found.
        throw new Exception("'" + key + "' is not a valid menu option.");
    }

    /**
     * Returns a string representation of the menu item, including its keyboard
     * shortcut.
     *
     * @return A string in the format "[key] setting", where 'key' is the keyboard
     *         shortcut
     *         and 'setting' is the title of the menu option.
     */
    @Override
    public String toString() {
        return "[" + this.key + "] " + this.setting;
    }

    /**
     * Prints the title of the configuration menu to the terminal.
     */
    public static void printConfigure() {
        ConsolePrint.printConfigure("Configure Options");
    }

    /**
     * Prints all menu options of the configuration menu along with their shortcuts
     * to the terminal.
     */
    public static void printConOptions() {
        String[] options = new String[ConfigureMenuObject.values().length];
        int index = 0;

        // Populate the array with each configuration menu option formatted as
        // "[shortcut] option"
        for (ConfigureMenuObject item : ConfigureMenuObject.values())
            options[index++] = item.toString();

        // Print the formatted menu options array to the terminal
        ConsolePrint.printMenuOptions(options);
    }
}
