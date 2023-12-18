import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

/**
 * All outputs which will be printed in the console, will be managed in this class.
 */
public class OutputManager {

    private String currentLang;
    private String german = "German";
    private String english = "English";
    private List<String> langListDE = new ArrayList<>();
    private List<String> langListEN = new ArrayList<>();

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RED = "\u001b[31m";
    private static final String ANSI_YELLOW_BRIGHT_BOLD = "\033[1;93m";

    private String coloredText = null;

    private void colorTextX(String textToColorize) {
        coloredText = ANSI_BLUE + textToColorize + ANSI_RESET;
    }

    private void colorTextO(String textToColorize) {
        coloredText = ANSI_GREEN + textToColorize + ANSI_RESET;
    }

    private String colorTextTie(String textToColorize) {
        coloredText = ANSI_YELLOW_BRIGHT_BOLD + textToColorize + ANSI_RESET;
        return coloredText;
    }

    /**
     * Method to choose which color method to use, based of the playerToken argument.
     * @param textToColorize is a string, which should be colorized.
     * @param playerToken is a string, containing the current Player (either X or O).
     * @return the colored string.
     */
    public String colorText(String textToColorize, String playerToken) {
        if (playerToken.equals("X")) {
            colorTextX(textToColorize);
        } else {
            colorTextO(textToColorize);
        }
        return coloredText;
    }

    private String colorTextError(String textToColorize) {
        return ANSI_RED + textToColorize + ANSI_RESET;
    }

    /**
     * Put all german sentences in an Arraylist.
     */
    public void initializeLangListDE() {
        langListDE.add("In welcher Sprache wollen sie spielen?");
        langListDE.add("Bitte 1 fuer Deutsch oder eine beliebige Taste fuer Englisch waehlen!");
        langListDE.add("Ok, lass uns auf Deutsch spielen");
        langListDE.add("Geben Sie ein Feld an (von a1 bis c3)!");
        langListDE.add("Fehler: Eingabe darf nur ein gegebenes Feld (a1 bis c3) betragen!");
        langListDE.add("Fehler: Feld besetzt!");
        langListDE.add("Der Gewinner ist: ");
        langListDE.add("Unentschieden!");
    }

    /**
     * Put all english sentences in an Arraylist.
     */
    public void initializeLangListEN() {
        langListEN.add("What language would you like to play in?");
        langListEN.add("Please select 1 for german or any key for english!");
        langListEN.add("Ok, let's play in english!");
        langListEN.add("select a field (from a1 to c3)!");
        langListEN.add("Error: Please enter a valid field (a1 to c3)!");
        langListEN.add("Error: Field already taken!");
        langListEN.add("The Winner is: ");
        langListEN.add("It's a tie!");
    }

    /**
     * Print german and english text, to tell the player, to select a language.
     */
    public void printSelectLanguageText(){
        System.out.println(langListDE.get(0) + " \n" + langListEN.get(0));
        System.out.println(langListDE.get(1) + " \n" + langListEN.get(1));
    }

    /**
     * Method to select a language.
     */
    public void langSelect(String languageSelect) {
        if (Objects.equals(languageSelect, "1")) {
            currentLang = german;
            System.out.println(langListDE.get(2));
        } else {
            currentLang = english;
            System.out.println(langListEN.get(2));
        }
    }

    /**
     * Prints out colorful text, which says, to select a field. Takes one argument, which is the current player (either X or O).
     * @param playerTokenPlaceholder Argument, which is passed to the colorText() method, so it can color the text respectively.
     */
    public void printFieldInputString(String playerTokenPlaceholder) {
        String text = "";
        if (Objects.equals(currentLang, german)) {
            text = langListDE.get(3);
        } else if (Objects.equals(currentLang, english)) {
            text = langListEN.get(3);
        }
        System.out.println(colorText(text, playerTokenPlaceholder));
    }

    /**
     * Prints text, which is a german or english string, who says, that the inputField doesn't exist in the playground.
     */
    public void printInvalidFieldString() {
        String text = "";
        if (Objects.equals(currentLang, german)) {
            text = langListDE.get(4);
        } else if (Objects.equals(currentLang, english)) {
            text = langListEN.get(4);
        }
        System.out.println(colorTextError(text));
    }

    /**
     * Prints text, which says, that the field is already taken.
     */
    public void printFieldTakenString() {
        String text = "";
        if (Objects.equals(currentLang, german)) {
            text = langListDE.get(5);
        } else if (Objects.equals(currentLang, english)) {
            text = langListEN.get(5);
        }
        System.out.println(colorTextError(text));
    }

    /**
     * Prints text, that the game has a winner.
     */
    public void printWinString(String playerToken) {
        if(playerToken.equals("X")){
            if (Objects.equals(currentLang, german)) {
                System.out.println(langListDE.get(6) + colorText("X", playerToken) + "!");
            } else {
                System.out.println(langListEN.get(6) + colorText("X", playerToken) + "!");
            }
        }  else {
            colorTextO(playerToken);
            if (Objects.equals(currentLang, german)) {
                System.out.println(langListDE.get(6) + colorText("O", playerToken) + "!");
            } else {
                System.out.println(langListEN.get(6) + colorText("O", playerToken) + "!");
            }
        }
    }

    /**
     * Prints text, that the game has been finished in a tie.
     */
    public void printTieString() {
        String text = "";
        if (Objects.equals(currentLang, german)) {
            text = langListDE.get(7);
        } else if (Objects.equals(currentLang, english)) {
            text = langListEN.get(7);
        }
        System.out.println(colorTextTie(text));
    }

    /**
     * Get the ArrayList and print it in shape of Tic Tac Toe.
     */
    public void printGround(List<String> ground){
        System.out.println("    1     2     3  ");
        System.out.println("a " + ground.get(0) + "  |" + ground.get(1) + "  |" + ground.get(2));
        System.out.println("  -----|-----|-----");
        System.out.println("b " + ground.get(3) + "  |" + ground.get(4) + "  |" + ground.get(5));
        System.out.println("  -----|-----|-----");
        System.out.println("c " + ground.get(6) + "  |" + ground.get(7) + "  |" + ground.get(8));
        System.out.println();
    }
}