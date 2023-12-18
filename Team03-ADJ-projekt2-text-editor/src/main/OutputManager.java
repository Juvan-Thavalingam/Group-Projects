import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * All outputs printed in the console, will be managed in this class.
 */
public class OutputManager {

    private Format format;

    /**
     * Constructor initializes Format class.
     */
    public OutputManager() {
        format = new Format();
    }

    /**
     * PrintWorkSpace: Method to print all text in workspace.
     * Used by the Print command.
     */
    public void printWorkSpace(List<String> list, boolean formatFix, int line_width) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(format.convertStringToFormat(list.get(i), i+1, formatFix, line_width));
        }
    }

    /**
     * printIndex: For each line, print content and key
     * @param indexMap Hashmap containing indexes of words, being used more than 3 times. Used to print the index.
     */
    public void printIndex(HashMap<String, HashSet<Integer>> indexMap){
        for (String key : indexMap.keySet()){
            System.out.println(key + " " + indexMap.get(key));
        }
    }

    /**
     * printMessageCommand: Print the message prompting to enter a command.
     */
    public void printMessageCommand() {
        System.out.println("Befehl Eingabe [n]: ");
    }

    /**
     * printMessageInput: Print the message prompting to enter a text.
     */
    public void printMessageInput() {
        System.out.println("Text Eingabe: [n]: ");
    }

    /**
     * printMessageToReplaceText: Print the message asking which text to replace.
     */
    public void printMessageToReplaceText() {
        System.out.println("Welcher text soll ersetzt werden?: ");
    }

    /**
     * printMessageReplacer: Print the message asking for the replacement text.
     */
    public void printMessageReplacer() {
        System.out.println("Mit was soll der text ersetzt werden?: ");
    }

    /**
     * printMessageMissingFormatNumber: Print the message if the format command is missing a number.
     */
    public void printMessageMissingFormatNumber() {
        System.err.println("Zeilenbreite nicht angegeben!");
    }

    /**
     * printMessageProgEnd: Print the message if the program ends.
     */
    public void printMessageProgEnd() {
        System.out.println("Programm wurde beendet!");
    }

    /**
     * printMessageError: Print the message if the command is not valid.
     */
    public void printMessageError() {
        System.err.println("ERROR: Text ungültig!");
    }

    /**
     * printMessageParagraphNumberTooBig: Print the message if the paragraph number is too big.
     */
    public void printMessageParagraphNumberTooBig() {
        System.err.println("Fehler: Absatznummer ist zu gross!");
    }

    /**
     * printMessageWordNotFound: Print the message if the word is not found.
     */
    public void printMessageWordNotFound() {
        System.err.println("Fehler: Wort nicht gefunden!");
    }

    /**
     * printMessageNoTextToReplace: Print the message if there is no text to replace.
     */
    public void printMessageNoTextToReplace() {
        System.err.println("Fehler: Kein Text zum Ersetzen!");
    }

    /**
     * printMessageMissingReplacer: Print the message if the replacer is missing.
     */
    public void printMessageMissingReplacer() {
        System.err.println("Fehler: Kein Ersatztext!");
    }

    /**
     * printMessageIdenticalReplace: Print the message if the replacer is identical to the text to replace.
     */
    public void printMessageIdenticalReplace() {
        System.err.println("Fehler: Ersatztext ist identisch mit dem zu ersetzenden Text!");
    }

    /**
     * printMessageNothingToDelete: Print the message if there is nothing to delete.
     */
    public void printMessageNothingToDelete() {
        System.err.println("Keine Texte mehr verfügbar zum löschen.");
    }

    /**
     * getMessageDummy: return a dummy message as String.
     */
    public String getMessageDummy() {
        return  "This is a DUMMY placeholder.";
    }
}