import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the methods to take a given string and convert it onto the specified format.
 */
public class Format {

    private static final String PREFIX = " :| ";
    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;
    private static final int NO_INDEX_FOUND = -1;
    private static final int ONE_INDEX_POSITION = 1;

    private String createPrefix(int lineNumber) {
        return lineNumber + PREFIX;
    }

    private String convertStringToRawFormat(String userInputText, int lineNumber) {
        return createPrefix(lineNumber) + userInputText;
    }

    private String createSubString(String stringInput, int line_width) {
        String formattedString;

        if (stringInput.charAt(line_width) == ' ') {
            stringInput = stringInput.substring(FIRST_INDEX, line_width);
        } else if (stringInput.substring(FIRST_INDEX, line_width).lastIndexOf(' ') == NO_INDEX_FOUND) {
            stringInput = stringInput.substring(FIRST_INDEX, line_width);
        } else if (stringInput.substring(FIRST_INDEX, line_width).lastIndexOf(' ') != NO_INDEX_FOUND) {
            stringInput = stringInput.substring(FIRST_INDEX, stringInput.substring(FIRST_INDEX, line_width).lastIndexOf(' ') + ONE_INDEX_POSITION);
        }

        formattedString = stringInput;

        return formattedString;
    }

    private String convertStringToFixFormat(String userInputText, int line_width) {
        String formattedString;
        String stringToFormat = userInputText;
        String finalString = "";
        List<String> substrings = new ArrayList<>();

        while (stringToFormat.length() > 0) {
            if (stringToFormat.length() > line_width) {
                formattedString = createSubString(stringToFormat, line_width);
                stringToFormat = stringToFormat.substring(formattedString.length());

                substrings.add(formattedString);
            } else {
                substrings.add(stringToFormat);
                stringToFormat = "";
            }
        }

        for (int indexNumber = 0; indexNumber < substrings.size(); indexNumber++) {
            if (indexNumber < (substrings.size() - 1)) {
                if (!(substrings.get(indexNumber).matches(" "))) {
                    if (substrings.get(indexNumber).charAt(FIRST_INDEX) == ' ') {
                        finalString += substrings.get(indexNumber).substring(SECOND_INDEX) + System.lineSeparator();
                    } else {
                        finalString += substrings.get(indexNumber) + System.lineSeparator();
                    }
                }
            } else {
                if (substrings.get(indexNumber).charAt(FIRST_INDEX) == ' ') {
                    substrings.set(indexNumber, substrings.get(indexNumber).substring(SECOND_INDEX));
                }
                finalString += substrings.get(indexNumber);
            }

        }

        return finalString;
    }

    /**
     * This method takes in a bunch of different inputs, like the current user input (String), line number,
     * whether to use fix format and the line width. After that, it checks, if fix format should be used and
     * if line width is not 0. If both of them are true, it executes a method to convert the string into the fix format
     * and if not, then the raw format conversion method will be used.
     * @param userInputText is the string that the user types in.
     * @param lineNumber is the line number of the current edited string.
     * @param formatFix is a boolean to decide, whether fix format should be used or not by being true or false respectively.
     * @param line_width is the width, when the text should be indented.
     * @return the edited string in his corresponding format.
     */
    public String convertStringToFormat(String userInputText, int lineNumber, boolean formatFix, int line_width) {
        if (formatFix && line_width != 0) {
            return convertStringToFixFormat(userInputText, line_width);
        } else {
            return convertStringToRawFormat(userInputText, lineNumber);
        }
    }
}
