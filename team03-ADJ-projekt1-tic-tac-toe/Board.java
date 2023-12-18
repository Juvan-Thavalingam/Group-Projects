import java.util.ArrayList;
import java.util.List;

/**
 * Generate a Board, to play Tic Tac Toe.
 */
public class Board {
    private List<String> playBoard = new ArrayList<>();
    public final static int MAX_AMOUNT_OF_FIELDS = 9;

    /**
     * Before the game starts, fill the ArrayList with empty Strings.
     */
    public void initializeGround(){
        for (int i = 0; i < MAX_AMOUNT_OF_FIELDS; i++){
            playBoard.add("   ");
        }
    }

    public List<String> getPlayBoard() {
        return playBoard;
    }

    public void setBoard(int field, String playerToken){
        playBoard.set(field, playerToken);
    }
}
