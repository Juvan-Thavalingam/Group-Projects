/**
 * This class controls and runs the whole game. It initialize the board, select the language and set field of the currentplayer.
 * Also the board will be printed here.
 */
public class Gameplay {

    private GameLogic logic = new GameLogic();
    private Board board = new Board();
    private OutputManager outputManager = new OutputManager();
    private InputManager inputManager = new InputManager();

    private String currentPlayerToken = "X";
    private String oldPlayer;

    private void initializeGame(){
        board.initializeGround();
        outputManager.printGround(board.getPlayBoard());
        outputManager.initializeLangListDE();
        outputManager.initializeLangListEN();
    }

    private boolean checkInput(int field){
        if (field < 0 || field > 8){
            outputManager.printInvalidFieldString();
            return false;
        }
        return true;
    }

    private boolean checkField(int field){
        if(!board.getPlayBoard().get(field).equals("   ")) {
            outputManager.printFieldTakenString();
            return false;
        }
        return true;
    }

    private void setField(int field){
        if(currentPlayerToken.equals("X")){
            board.setBoard(field, outputManager.colorText("  X", currentPlayerToken));
        } else {
            board.setBoard(field, outputManager.colorText("  O", currentPlayerToken));
        }
    }

    private void switchPlayer(){
        if(currentPlayerToken.equals("X")){
            oldPlayer = "X";
            currentPlayerToken = "O";
        } else {
            oldPlayer = "O";
            currentPlayerToken = "X";
        }
    }

    private boolean checkContinue(){
        if (logic.checkWin(board.getPlayBoard())){
            outputManager.printWinString(oldPlayer);
            return false;
        } else if(logic.checkDraw(board.getPlayBoard())){
           outputManager.printTieString();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method to simulate a round. First select a language, then write the input and set the field.
     * Print the ground and if game is not finished, then start another round.
     */
    public void play(){
        initializeGame();
        while (checkContinue()){
            outputManager.printSelectLanguageText();
            outputManager.langSelect(inputManager.enterInput());
            outputManager.printFieldInputString(currentPlayerToken);
            int field = inputManager.translateInput(inputManager.enterInput());
            if(checkInput(field) && checkField(field)){
                setField(field);
                switchPlayer();
                outputManager.printGround(board.getPlayBoard());
            }
        }
    }
}
