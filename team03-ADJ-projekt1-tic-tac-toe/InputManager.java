import java.util.Scanner;

/**
 * The user make an input or the input of setField, will be translated to have access to Arraylist in Board.
 */
public class InputManager {
    private Scanner scanner = new Scanner(System.in);

    /**
     * Player can make an input with the console, to set a field or select the language.
     * @return the input as String.
     */
    public String enterInput(){
        return scanner.nextLine();
    }

    /**
     * Takes a String as an input and converts it to the internal field naming scheme (o-8). Outputs it as int.
     * @param inputString is the string input, which is going to be converted.
     * @return Returns the respective integer output (0-8) from given string input (a1-c3). 404 for wrong inputs.
     */
    public int translateInput(String inputString){
        return switch (inputString) {
            case "a1", "A1" -> 0;
            case "a2", "A2" -> 1;
            case "a3", "A3" -> 2;
            case "b1", "B1" -> 3;
            case "b2", "B2" -> 4;
            case "b3", "B3" -> 5;
            case "c1", "C1" -> 6;
            case "c2", "C2" -> 7;
            case "c3", "C3" -> 8;
            default -> 404;
        };
    }
}
