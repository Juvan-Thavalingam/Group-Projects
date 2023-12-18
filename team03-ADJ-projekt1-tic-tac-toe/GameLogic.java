import java.util.List;

/**
 * This class check the status of the game, if someone has won or if it's a tie.
 */
public class GameLogic {

    private boolean win;

    /**
     * This method checks, whether a player has 3 of their symbols placed horizontally in a row.
     * @param board Ground is a list containing all fields and their respective occupation status.
     * @return Returns a boolean. If a player is winning, it returns true. If no one is winning, false.
     */
    public boolean checkHorizontal(List<String> board) {
        if (!board.get(0).equals("   ") && board.get(0).equals(board.get(1)) && board.get(1).equals(board.get(2))) {
            win = true;
        } else if (!board.get(3).equals("   ") && board.get(3).equals(board.get(4)) && board.get(4).equals(board.get(5))) {
            win = true;
        } else if (!board.get(6).equals("   ") && board.get(6).equals(board.get(7)) && board.get(7).equals(board.get(8))) {
            win = true;
        }
        return win;
    }

    /**
     * This method checks, whether a player has 3 of their symbols placed vertically in a row.
     *
     * @param board Ground is a list containing all fields and their respective occupation status.
     * @return Returns a boolean. If a player is winning, it returns true. If no one is winning, false.
     */
    public boolean checkVertical(List<String> board) {
        if (!board.get(0).equals("   ") && board.get(0).equals(board.get(3)) && board.get(3).equals(board.get(6))) {
            win = true;
        } else if (!board.get(1).equals("   ") && board.get(1).equals(board.get(4)) && board.get(4).equals(board.get(7))) {
            win = true;
        } else if (!board.get(2).equals("   ") && board.get(2).equals(board.get(5)) && board.get(5).equals(board.get(8))) {
            win = true;
        }
        return win;
    }

    /**
     * This method checks, whether a player has 3 of their symbols placed horizontally in a row.
     *
     * @param board Ground is a list containing all fields and their respective occupation status.
     * @return Returns a boolean. If a player is winning, it returns true. If no one is winning, false.
     */
    public boolean checkDiagonal(List<String> board) {
        if (!board.get(0).equals("   ") && board.get(0).equals(board.get(4)) && board.get(4).equals(board.get(8))) {
            win = true;
        } else if (!board.get(2).equals("   ") && board.get(2).equals(board.get(4)) && board.get(4).equals(board.get(6))) {
            win = true;
        }
        return win;
    }

    private boolean checkIfGroundFull(List<String> board) {
        int fieldsOccupied = 0;
        for (String field : board) {
            if (!(field.equals("   "))) {
                fieldsOccupied++;
            }
        }
        return fieldsOccupied == 9;
    }

    /**
     * This method takes checkHorizontal(), checkVertical() and checkDiagonal() and combines them into one. It checks, if either one of those submethods is true.
     *
     * @param board Ground is a list containing all fields and their respective occupation status.
     * @return Returns true, if one of the submethods is true and false, if none is true.
     */
    public boolean checkWin(List<String> board) {
        return checkHorizontal(board) || checkVertical(board) || checkDiagonal(board);
    }

    /**
     * This method checks, whether a draw condition is present by using the checkIfGroundFull() and checkWin() methods. checkWin() must be false in this case.
     *
     * @param board Ground is a list containing all fields and their respective occupation status.
     * @return Returns true, if a draw condition is present. False, if not.
     */
    public boolean checkDraw(List<String> board) {
        return checkIfGroundFull(board) && !checkWin(board);
    }
}
