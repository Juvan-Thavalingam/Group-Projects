import java.util.LinkedList;
import java.util.Scanner;

/**
 * All commands executed from Texteditor are being processed in this class.
 */
public class Textprocessor {

    private Index index;
    private OutputManager outputManager;
    private LinkedList<String> editorsList = new LinkedList();

    private Scanner scanner = new Scanner(System.in);

    private static final int REAL_INDEX = 1;
    private static final char FILTER_CHAR = '°';
    private static final String FILTER_REGEX = "[\s\\wäöüÄÖÜ.,:;\\-!?’()\"%@+*\\[\\]{}/&#$\\\\]*";

    /**
     * Constructor of Textprocessor, which initializes Index class and OutputManager class.
     */
    public Textprocessor(){
        index = new Index();
        outputManager = new OutputManager();
    }

    /**
     * Method to for processing ADD command.
     * @param command Command to be processed.
     */
    public void add(String command) {
        outputManager.printMessageInput();
        String inputText = validateInput(scanner.nextLine());
        if (command.replaceAll("[^0-9.]", "").equals("")) {
            editorsList.add(inputText);
        } else {
            int line = Integer.parseInt(command.replaceAll("[^0-9.]", "")) - REAL_INDEX;
            if (line <= editorsList.size()) {
                editorsList.add(line, inputText);
            } else {
                outputManager.printMessageParagraphNumberTooBig();
            }
        }
    }

    /**
     * Method to for processing REPLACE command. Replacee and Replacer values are taken from the user.
     * Replacee values are replaced with Replacer values.
     * @param command Command to be processed.
     */
    public void replace(String command) {
        int line = editorsList.size() - REAL_INDEX;
        if (command.replaceAll("[^0-9.]", "").equals("")) {
            if (editorsList.size() == 0) {
                line = 0;
            }
        } else {
            line = Integer.parseInt(command.replaceAll("[^0-9.]", "")) - REAL_INDEX;
        }
        if (line >= editorsList.size() || editorsList.size() == 0) {
            outputManager.printMessageParagraphNumberTooBig();
        } else {
            String currentext = editorsList.get(line);
            outputManager.printMessageToReplaceText();
            String inputText = scanner.nextLine();
            if (inputText.equals("")) {
                outputManager.printMessageWordNotFound();
            } else if (!currentext.contains(inputText)) {
                outputManager.printMessageNoTextToReplace();
            } else {
                outputManager.printMessageReplacer();
                String replacerText = scanner.nextLine();
                if (replacerText.equals("")) {
                    outputManager.printMessageMissingReplacer();

                } else if (replacerText.equals(inputText)) {
                    outputManager.printMessageIdenticalReplace();
                } else {
                    editorsList.set(line, editorsList.get(line).replaceFirst(inputText, replacerText));
                }
            }
        }
    }

    /**
     * Method to for processing DEL command. Deletes the paragraph at the given line number.
     * @param command Command to be processed.
     */
    public void del(String command) {
        if (editorsList.size() == 0) {
            outputManager.printMessageNothingToDelete();
        } else {
            if (command.replaceAll("[^0-9.]", "").equals("")) {
                editorsList.remove(editorsList.size() - REAL_INDEX);
            } else {
                int line = Integer.parseInt(command.replaceAll("[^0-9.]", "")) - REAL_INDEX;
                if (line < editorsList.size()) {
                    editorsList.remove(line);
                } else {
                    outputManager.printMessageParagraphNumberTooBig();
                }
            }
        }
    }

    /**
     * Method to for processing dummy command. Replaces the current paragraph at the given line number with the predefined dummy text.
     * @param command Command to be processed.
     */
    public void dummy(String command) {
        if (command.replaceAll("[^0-9.]", "").equals("")) {
            editorsList.add(outputManager.getMessageDummy());
        } else {
            int line = Integer.parseInt(command.replaceAll("[^0-9.]", "")) - REAL_INDEX;
            if (line <= editorsList.size()) {
                editorsList.add(line, outputManager.getMessageDummy());
            } else {
                outputManager.printMessageParagraphNumberTooBig();
            }
        }
    }

    /**
     * Takes in passed through variables from Texteditor and executes printWorkSpace from the outputManager.
     * @param formatFix is a boolean to decide, whether fix format should be used or not by being true or false respectively.
     * @param line_width is the width, when the text should be indented.
     */
    public void print(boolean formatFix, int line_width) {
        outputManager.printWorkSpace(editorsList, formatFix, line_width);
    }

    /**
     * Method to validate FormatFix input.
     * @param command Command to be processed.
     */
    public boolean validateFormatFixInput(String command) {
        return command.matches(".*\\d.*");
    }

    private String validateInput(String text) {
        if (!text.matches(FILTER_REGEX)) {
            for (int inputNumber = 0; inputNumber < text.length(); inputNumber++) {
                if (!String.valueOf(text.charAt(inputNumber)).matches(FILTER_REGEX)) {
                    text = text.replace(text.charAt(inputNumber), FILTER_CHAR);
                }
            }
        }
        return text.replaceAll(String.valueOf(FILTER_CHAR), "");
    }

    /**
     * Method to print the full editorsList text.
     */
    public void printMapIndex() {
        outputManager.printIndex(index.getIndex(editorsList));
    }
}