package ch.zhaw.catan;

import ch.zhaw.catan.Config.Faction;
import ch.zhaw.catan.Config.Resource;
import ch.zhaw.catan.Config.Land;

import java.awt.Point;

import java.util.*;

/**
 * This class performs all actions related to modifying the game state.
 * The whole logic of the game was implemented in this class.
 * @author Juvan Thavalinga, Aleksander Stojilkovic, Dominique Kaluscha
 */
public class SiedlerGame {
    public static final int FOUR_TO_ONE_TRADE_OFFER = 4;
    public static final int FOUR_TO_ONE_TRADE_WANT = 1;
    private int VictoryPointsToWin;


    private SiedlerBoard board;
    private Bank bank;
    private Thief thief;

    private List<Player> playerList;
    private Map<Faction, Map<Resource, Integer>> factionResource;

    private int actualPlayer = 0;

    //check to place road by settlement
    private Point checkPlaceRoad;

    private Random random;

    /**
     * Constructs a SiedlerGame game state object.
     * This constructor creates all the player, the gameboard, thieve and bank.
     * @param winPoints       the number of points required to win the game
     * @param numberOfPlayers the number of players
     * @throws IllegalArgumentException if winPoints is lower than
     *                                  three or players is not between two and four
     */
    public SiedlerGame(int winPoints, int numberOfPlayers) {
        if (winPoints < 3) {
            throw new IllegalArgumentException("Winpoints must be 3 or more!");
        }
        if (numberOfPlayers < Config.MIN_NUMBER_OF_PLAYERS || numberOfPlayers > 4) {
            throw new IllegalArgumentException("Number of Players must be between 2 and 4!");
        }

        board = new SiedlerBoard();
        playerList = new ArrayList<>();
        factionResource = new HashMap<>();
        bank = new Bank();
        thief = new Thief();
        initPlayerList(numberOfPlayers);
        VictoryPointsToWin = winPoints;
        random = new Random();
    }

    /**
     * Generate random int from a given range using two values as a range.
     *
     * @param min min int to be used as the bottom part of the range (being included).
     * @param max max int to be used as the top part of the range (being excluded)
     * @return random int between the given range
     */
    public int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private void initPlayerList(int numberOfPlayers) {
        for (int i = 0; i < numberOfPlayers; i++) {
            playerList.add(new Player(Faction.values()[i]));
        }
    }

    /**
     * Get the current player.
     *
     * @return the current player as Player.
     */
    public Player getActualPlayer() {
        return playerList.get(actualPlayer);
    }

    /**
     * Get the number of the current player of the playerlist.
     *
     * @return actualPlayer as int.
     */
    public int getActualPlayerNumber() {
        return actualPlayer;
    }

    private Map<Faction, Map<Resource, Integer>> initResourceOfAllFaction() {
        for (Player player : playerList) {
            factionResource.put(player.getFaction(), player.getPlayerResource());
        }

        return factionResource;
    }

    /**
     * Switches to the next player in the defined sequence of players.
     */
    public void switchToNextPlayer() {
        if (actualPlayer == playerList.size() - 1) {
            actualPlayer = 0;
        } else {
            actualPlayer++;
        }
    }

    /**
     * Switches to the previous player in the defined sequence of players.
     */
    public void switchToPreviousPlayer() {
        if (actualPlayer == 0) {
            actualPlayer = playerList.size() - 1;
        } else {
            actualPlayer--;
        }
    }

    /**
     * Returns the {@link Faction}s of the active players.
     *
     * <p>The order of the player's factions in the list must
     * correspond to the oder in which they play.
     * Hence, the player that sets the first settlement must be
     * at position 0 in the list etc.
     * </p><p>
     * <strong>Important note:</strong> The list must contain the
     * factions of active players only.</p>
     *
     * @return the list with player's factions
     * <p>
     */
    public List<Faction> getPlayerFactions() {
        List<Faction> playerList = new ArrayList<>();
        for (Player player : this.playerList) {
            playerList.add(player.getFaction());
        }
        return playerList;
    }


    /**
     * Returns the game board.
     *
     * @return board as Siedlerboard.
     */
    public SiedlerBoard getBoard() {
        return board;
    }

    /**
     * Returns the {@link Faction} of the current player.
     *
     * @return the faction of the current player
     */
    public Faction getCurrentPlayerFaction() {
        return getActualPlayer().getFaction();
    }

    /**
     * Returns how many resource cards of the specified type
     * the current player owns.
     *
     * @param resource the resource type
     * @return the number of resource cards of this type
     */
    public int getCurrentPlayerResourceStock(Resource resource) {
        return getActualPlayer().getPlayerResource().get(resource);
    }

    /**
     * Return the resources of the currentplayer.
     * @return the resources as a map.
     */
    public Map<Config.Resource, Integer> getCurrentPlayerResource() {
        return getActualPlayer().getPlayerResource();
    }

    /**
     * Places a settlement in the founder's phase (phase II) of the game.
     *
     * <p>The placement does not cost any resource cards. If payout is
     * set to true, for each adjacent resource-producing field, a resource card of the
     * type of the resource produced by the field is taken from the bank (if available) and added to
     * the players' stock of resource cards.</p>
     *
     * @param position the position of the settlement
     * @param payout   if true, the player gets one resource card per adjacent resource-producing field
     * @return true, if the placement was successful
     */
    public boolean placeInitialSettlement(Point position, boolean payout) {
        if (board.hasCorner(position) && board.getCorner(position) == null && checkWater(position)) {
            if (board.getNeighboursOfCorner(position).size() == 0) {
                checkPlaceRoad = position;
                if (getActualPlayer().checkAvailableSettlement()) {
                    board.setCorner(position, getActualPlayer().getSettlement());
                    getActualPlayer().addWinPoint(getActualPlayer().getSettlement().getVictoryPoints());
                }
                if (payout) {
                    for (Land field : board.getFields(position)) {
                        if (!field.equals(Land.WATER) && !field.equals(Land.DESERT)) {
                            bank.removeFromBankStock(field.getResource(), 1);
                            int resourceStock = getCurrentPlayerResourceStock(field.getResource()) + 1;
                            getCurrentPlayerResource().put(field.getResource(), resourceStock);
                        }
                    }
                }
                initResourceOfAllFaction();
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the fields of the Corner are only water.
     *
     * @param position of the Settlement
     * @return true if the fields doesn't contain only water.
     */
    public boolean checkWater(Point position) {
        for (int i = 0; i < board.getFields(position).size(); i++) {
            if (!board.getFields(position).get(i).equals(Land.WATER)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Places a road in the founder's phase (phase II) of the game.
     * The placement does not cost any resource cards.
     *
     * @param roadStart position of the start of the road
     * @param roadEnd   position of the end of the road
     * @return true, if the placement was successful
     */
    public boolean placeInitialRoad(Point roadStart, Point roadEnd) {
        if (board.hasEdge(roadStart, roadEnd)) {
            if (checkInitialRoad(roadStart, roadEnd) || checkInitialRoad(roadEnd, roadStart)) {
                if (getActualPlayer().checkAvailableRoads()) {
                    board.setEdge(roadStart, roadEnd, getActualPlayer().getRoad());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkInitialRoad(Point roadStart, Point roadEnd) {
        if (board.getCorner(roadStart) != null && board.getCorner(roadStart).equals(getActualPlayer().getSettlement())) {
            if (roadStart.equals(checkPlaceRoad)) {
                if (checkWater(roadEnd)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method takes care of actions depending on the dice throw result.
     * <p>
     * A key action is the payout of the resource cards to the players
     * according to the payout rules of the game. This includes the
     * "negative payout" in case a 7 is thrown and a player has more than
     * {@link Config#MAX_CARDS_IN_HAND_NO_DROP} resource cards.
     * </p><p>
     * If a player does not get resource cards, the list for this players'
     * {@link Faction} is <b>an empty list (not null)</b>!.
     * </p><p>
     * The payout rules of the game take into account factors such as, the number
     * of resource cards currently available in the bank, settlement types
     * (settlement or city), and the number of players that should get resource
     * cards of a certain type (relevant if there are not enough left in the bank).
     * </p>
     *
     * @param dicethrow the resource cards that have been distributed to the players
     * @return the resource cards added to the stock of the different players
     */
    public Map<Faction, List<Resource>> throwDice(int dicethrow) {
        Map<Faction, List<Resource>> dicePlayerResource = new HashMap<>();
        for (Player player : playerList) {
            dicePlayerResource.put(player.getFaction(), new ArrayList<>());
        }

        if (dicethrow != 7) {
            for (Point point : board.getFieldsForDiceValue(dicethrow)) {
                if (bank.getBankStockResource(Config.getStandardLandPlacement().get(point).getResource()) != 0) {
                    if (!board.getCornersOfField(point).isEmpty() && !thief.getThiefPosition().equals(point)) {
                        if(board.getCornersOfField(point).size() <= bank.getBankStockResource(Config.getStandardLandPlacement().get(point).getResource())){
                            for (Settlement settlement : board.getCornersOfField(point)) {
                                for (Faction faction : Faction.values()) {
                                    if (faction.equals(settlement.getFaction())) {
                                        List<Resource> resourceList = dicePlayerResource.get(faction);
                                        if(Character.isUpperCase(settlement.toString().charAt(0))){
                                            resourceList.add(Config.getStandardLandPlacement().get(point).getResource());
                                            resourceList.add(Config.getStandardLandPlacement().get(point).getResource());
                                            bank.removeFromBankStock(Config.getStandardLandPlacement().get(point).getResource(), 2);
                                        } else {
                                            resourceList.add(Config.getStandardLandPlacement().get(point).getResource());
                                            bank.removeFromBankStock(Config.getStandardLandPlacement().get(point).getResource(), 1);
                                        }
                                        dicePlayerResource.put(faction, resourceList);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            distributeResources(dicePlayerResource);
        } else {
            removeHalfResourcesAndReturnToBank(dicePlayerResource);
        }
        return dicePlayerResource;
    }

    private void distributeResources(Map<Faction, List<Resource>> dicePlayerResource) {
        // add throwdice Resource to player ResourceStock
        for (Faction faction : dicePlayerResource.keySet()) {
            for (Player player : playerList) {
                if (player.getFaction().equals(faction)) {
                    for (Resource resource : dicePlayerResource.get(faction)) {
                        player.addResource(resource);
                    }
                }
            }
        }
    }

    /**
     * Builds a settlement at the specified position on the board.
     *
     * <p>The settlement can be built if:
     * <ul>
     * <li> the player possesses the required resource cards</li>
     * <li> a settlement to place on the board</li>
     * <li> the specified position meets the build rules for settlements</li>
     * </ul>
     *
     * @param position the position of the settlement
     * @return true, if the placement was successful
     */
    public boolean buildSettlement(Point position) {
        //Check if corner is available
        if (!board.hasCorner(position) || board.getCorner(position) != null || !checkWater(position)) {
            return false;
        }

        //Check whether player has enough resources
        Map<Resource, Integer> actualPlayerResourceList = getCurrentPlayerResource();
        int resourcesWithValidAmount = 0;
        for (Resource resource : actualPlayerResourceList.keySet()) {
            if (!resource.equals(Resource.ORE) && actualPlayerResourceList.get(resource) >= 1) {
                resourcesWithValidAmount++;
                //if this value is 4, there's enough resources
            }
        }

        //check whether road is available.
        boolean isRoadAvailable = board.getAdjacentEdges(position).contains(getActualPlayer().getRoad());

        //check whether position has neighbouring settlements
        boolean areNeighbouringCornersObstructed = board.getNeighboursOfCorner(position).size() != 0;

        if (!getActualPlayer().checkAvailableSettlement()) {
            return false;
        }

        if (resourcesWithValidAmount == 4 && !areNeighbouringCornersObstructed && isRoadAvailable) {
            for (Resource resource : Resource.values()) {
                if (!resource.equals(Resource.ORE)) {
                    getCurrentPlayerResource().put(resource, getCurrentPlayerResource().get(resource) - 1);
                    bank.addToBankStock(resource, 1);
                }
            }

            board.setCorner(position, getActualPlayer().getSettlement());
            getActualPlayer().addWinPoint(getActualPlayer().getSettlement().getVictoryPoints());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Builds a city at the specified position on the board.
     *
     * <p>The city can be built if:
     * <ul>
     * <li> the player possesses the required resource cards</li>
     * <li> a city to place on the board</li>
     * <li> the specified position meets the build rules for cities</li>
     * </ul>
     *
     * @param position the position of the city
     * @return true, if the placement was successful
     */
    public boolean buildCity(Point position) {
        if (board.hasCorner(position) && board.getCorner(position) != null) {
            if (board.getCorner(position).equals(getActualPlayer().getSettlement())) {
                if (getCurrentPlayerResourceStock(Resource.ORE) >= 3) {
                    if (getCurrentPlayerResourceStock(Resource.GRAIN) >= 2) {
                        if (getActualPlayer().checkAvailableCities()) {
                            getCurrentPlayerResource().put(Resource.GRAIN, getCurrentPlayerResourceStock(Resource.GRAIN) - 2);
                            getCurrentPlayerResource().put(Resource.ORE, getCurrentPlayerResourceStock(Resource.ORE) - 3);
                            bank.addToBankStock(Resource.GRAIN, 2);
                            bank.addToBankStock(Resource.ORE, 3);
                            board.setCorner(position, getActualPlayer().getCity());
                            getActualPlayer().addWinPoint(getActualPlayer().getCity().getVictoryPoints());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Builds a road at the specified position on the board.
     *
     * <p>The road can be built if:
     * <ul>
     * <li> the player possesses the required resource cards</li>
     * <li> a road to place on the board</li>
     * <li> the specified position meets the build rules for roads</li>
     * </ul>
     *
     * @param roadStart the position of the start of the road
     * @param roadEnd   the position of the end of the road
     * @return true, if the placement was successful
     */
    public boolean buildRoad(Point roadStart, Point roadEnd) {
        if (!checkRoad(roadStart, roadEnd) && !checkRoad(roadEnd, roadStart)) {
            return false;
        }

        Map<Resource, Integer> actualPlayerResourceList = getCurrentPlayerResource();
        int resourcesWithValidAmount = 0;
        for (Resource resource : actualPlayerResourceList.keySet()) {
            if ((resource.equals(Resource.LUMBER) || resource.equals(Resource.BRICK)) && actualPlayerResourceList.get(resource) >= 1) {
                resourcesWithValidAmount++;
                //if this value is 2, there's enough resources
            }
        }

        if (resourcesWithValidAmount == 2) {
            if (!getActualPlayer().checkAvailableRoads()) {
                return false;
            }
            for (Resource resource : Resource.values()) {
                if (resource.equals(Resource.LUMBER) || resource.equals(Resource.BRICK)) {
                    getCurrentPlayerResource().put(resource, getCurrentPlayerResourceStock(resource) - 1);
                    bank.addToBankStock(resource, 1);
                }
            }
            board.setEdge(roadStart, roadEnd, getActualPlayer().getRoad());
            return true;
        }
        return false;
    }

    private boolean checkRoad(Point roadStart, Point roadEnd) {
        if (board.hasEdge(roadStart, roadEnd)) {
            if (board.getEdge(roadStart, roadEnd) != null) {
                return false;
            }

            if (!checkWater(roadStart) || !checkWater(roadEnd)) {
                return false;
            }

            if (board.getCorner(roadStart) == null) {
                if (board.getAdjacentEdges(roadStart) != null) {
                    for (Road road : board.getAdjacentEdges(roadStart)) {
                        if (road.equals(getActualPlayer().getRoad())) {
                            return true;
                        }
                    }
                }
                return false;
            } else {
                if (board.getCorner(roadStart).equals(getActualPlayer().getSettlement())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the winner of the game, if any.
     *
     * @return the winner of the game or null, if there is no winner (yet)
     */
    public Faction getWinner() {
        for (Player player : playerList) {
            if (player.getWinPoints() >= VictoryPointsToWin) {
                return player.getFaction();
            }
        }
        return null;
    }

    /**
     * <p>Trades in {@link #FOUR_TO_ONE_TRADE_OFFER} resource cards of the
     * offered type for {@link #FOUR_TO_ONE_TRADE_WANT} resource cards of the wanted type.
     * </p><p>
     * The trade only works when bank and player possess the resource cards
     * for the trade before the trade is executed.
     * </p>
     *
     * @param offer offered type
     * @param want  wanted type
     * @return true, if the trade was successful
     */
    public boolean tradeWithBankFourToOne(Resource offer, Resource want) {
        if (offer.equals(want)) {
            return false;
        }
        int currentResourceValueOfOfferResource = getCurrentPlayerResourceStock(offer);
        int currentResourceValueOfWantResource = getCurrentPlayerResourceStock(want);
        if (getCurrentPlayerResourceStock(offer) >= 4) {
            if (bank.getBankStockResource(want) >= 1) {
                currentResourceValueOfOfferResource -= FOUR_TO_ONE_TRADE_OFFER;
                getCurrentPlayerResource().put(offer, currentResourceValueOfOfferResource);
                currentResourceValueOfWantResource += FOUR_TO_ONE_TRADE_WANT;
                getCurrentPlayerResource().put(want, currentResourceValueOfWantResource);
                return true;
            }
        }
        return false;
    }

    private void removeHalfResourcesAndReturnToBank(Map<Faction, List<Resource>> dicePlayerResource) {
        Resource resourceToBeRemoved;
        for (Faction faction : dicePlayerResource.keySet()) {
            ArrayList<Resource> reducibleResources = new ArrayList<>();
            int totalResources = 0;
            Map<Resource, Integer> currentResourceListOfFaction = factionResource.get(faction);
            for (Resource resource : currentResourceListOfFaction.keySet()) { //Gets the total amount of resource cards the player has, which is important, if he has 8 or more.
                if (currentResourceListOfFaction.get(resource) > 0) {
                    totalResources += currentResourceListOfFaction.get(resource);
                    reducibleResources.add(resource); //puts all resources (which can be reduced) one by one into a list.
                }
            }
            if (!(totalResources < 8)) {
                if (totalResources % 2 == 1) {
                    totalResources--;
                }
                int amountOfCardsToRemove = totalResources / 2;
                while (amountOfCardsToRemove != 0) {
                    int randomNumber;
                    if (reducibleResources.size() > 1) {
                        randomNumber = getRandomNumberUsingNextInt(0, reducibleResources.size() - 1);
                    } else {
                        randomNumber = 0;
                    }
                    for (Player player : playerList) {
                        if (player.getFaction().equals(faction)) {
                            resourceToBeRemoved = reducibleResources.get(randomNumber);
                            ResourceExchangeInFavorBank(player, resourceToBeRemoved);

                            if (currentResourceListOfFaction.get(resourceToBeRemoved).equals(0)) {
                                reducibleResources.remove(resourceToBeRemoved);
                            }
                        }
                    }
                    amountOfCardsToRemove--;
                }
            }
        }
    }

    /**
     * Place the thief on the specified field and steal a resource card from a
     * random player (excluding the current player) with a settlement at that field (if there is a settlement).
     * Bank gets the stolen card.
     *
     * @param field the field to place the thief on
     */
    public boolean placeThiefAndStealCard(Point field) {
        HashMap<Player, ArrayList<Resource>> hMapWithPlayersWithResources;
        if (board.getFields().contains(field)) {
            if (!board.getField(field).equals(Land.WATER)) {
                if (!thief.getThiefPosition().equals(field)) {
                    ArrayList<Resource> unluckyPlayerResourceList;

                    Point oldAnnotationPosition = new Point(thief.getThiefPosition().x + 1, thief.getThiefPosition().y - 1);
                    board.addFieldAnnotation(thief.getThiefPosition(), oldAnnotationPosition, null);
                    thief.setThiefPosition(field);
                    Point newAnnotationPosition = new Point(field.x + 1, field.y - 1);
                    board.addFieldAnnotation(field, newAnnotationPosition, "TH");

                    List<Settlement> settlementsAtField = board.getCornersOfField(field);
                    settlementsAtField.remove(getActualPlayer().getSettlement());

                    hMapWithPlayersWithResources = createHMapWithPlayersWithResources(settlementsAtField);
                    if (hMapWithPlayersWithResources.size() > 0) {
                        List<Player> playersKeys = new ArrayList<>(hMapWithPlayersWithResources.keySet());
                        Player unluckyPlayer = playersKeys.get(random.nextInt(playersKeys.size()));
                        unluckyPlayerResourceList = hMapWithPlayersWithResources.get(unluckyPlayer);
                        Resource resourceToRemove = unluckyPlayerResourceList.get(random.nextInt(unluckyPlayerResourceList.size()));
                        ResourceExchangeInFavorBank(unluckyPlayer, resourceToRemove);
                    }

                    return true;
                }
            }
        }


        return false;
    }

    private HashMap<Player, ArrayList<Resource>> createHMapWithPlayersWithResources(List<Settlement> settlementsAtField) {
        HashMap<Player, ArrayList<Resource>> hMapWithPlayersWithResources = new HashMap<>();
        if (settlementsAtField.size() > 0) {
            for (Player player : playerList) {
                if (settlementsAtField.contains(player.getSettlement()) && !getActualPlayer().equals(player)) {
                    ArrayList<Resource> reducibleResources = new ArrayList<>();
                    for (Resource resource : player.getPlayerResource().keySet()) {
                        if (player.getPlayerResource().get(resource) > 0) {
                            reducibleResources.add(resource);
                        }
                    }
                    if (reducibleResources.size() > 0) {
                        hMapWithPlayersWithResources.put(player, reducibleResources);
                    }

                }
            }
        }

        return hMapWithPlayersWithResources;
    }

    private boolean ResourceExchangeInFavorBank(Player player, Resource resource) {
        return player.removeResource(resource) && bank.addToBankStock(resource, 1);
    }
}