import java.util.Scanner;
/**
 * Texteditor: Method managing the texteditor.
 * Managing Input validation, identifying commands and processing them.
 */
public class Texteditor {

    private Scanner scanner = new Scanner(System.in);
    private OutputManager outputManager;
    private Textprocessor textprocessor;
    private final String regexNumberFilter;
    private int line_width;
    private boolean formatFix;

    /**
     * Constructor for Texteditor.
     * regexNumberFilter: Regex for filtering numbers.
     * formatFix: Boolean for checking if format is fix.
     */
    public Texteditor() {
        outputManager = new OutputManager();
        textprocessor = new Textprocessor();
        regexNumberFilter = " *([1-9]*|[1-9]+[0-9]*)";
        formatFix = false;
    }

    private void startInput() {
        boolean exit = false;
        while (!exit) {
            outputManager.printMessageCommand();
            String inputCommand = scanner.nextLine().toLowerCase();
            if (inputCommand.matches("add" + regexNumberFilter)) {
                textprocessor.add(inputCommand);
            } else if (inputCommand.matches("del" + regexNumberFilter)) {
                textprocessor.del(inputCommand);
            } else if (inputCommand.matches("dummy" + regexNumberFilter)) {
                textprocessor.dummy(inputCommand);
            } else if (inputCommand.matches("replace" + regexNumberFilter)) {
                textprocessor.replace(inputCommand);
            } else if(inputCommand.matches("format raw\s*")){
                formatFix = false;
            } else if(inputCommand.matches("format fix\s*" + regexNumberFilter)){
                if (textprocessor.validateFormatFixInput(inputCommand)){
                    formatFix = true;
                    line_width = Integer.parseInt(inputCommand.replaceAll("[^0-9.]", ""));
                } else {
                    outputManager.printMessageMissingFormatNumber();
                }
            } else if(inputCommand.matches("index\s*")){
                textprocessor.printMapIndex();
            } else if (inputCommand.matches("exit\s*")) {
                exit = true;
                outputManager.printMessageProgEnd();
            } else if (inputCommand.matches("print")) {
                textprocessor.print(formatFix, line_width);
            } else {
                outputManager.printMessageError();
            }
        }
    }

    /**
     * Main method to start the program.
     */
    public static void main(String[] args) {
        Texteditor texteditor = new Texteditor();
        texteditor.startInput();
    }
}

