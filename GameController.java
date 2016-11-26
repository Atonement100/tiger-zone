import java.util.*;
import java.util.ArrayList;
public class GameController {
    private static final char startingTileChar = 'S';
    static final int NUM_PLAYERS = 2;
    static final int NUM_MEEPLES = 7;
    GameBoard board;
    protected GuiAdapter guiAdapter;
    private PlayerController[] players = new PlayerController[NUM_PLAYERS];
    //private Meeple[][] playerMeeples = new Meeple[NUM_PLAYERS][NUM_MEEPLES];
    
    private ArrayList<Tile> gameTileReference; // Don't modify this one after constructor. Can be indexed in to with tile.ID.
    private ArrayList<Tile> gameTiles;
    private int currentPlayer = 0;
    public ScoreController scoreController;
    
    public GameController(int row, int col){
        board = new GameBoard(row, col);
    }
    
    public GameController(int numHumanPlayers){
        this.gameTiles = retrieveGameTiles();
        this.gameTileReference = retrieveGameTiles();
        board = new GameBoard(gameTiles.size(), gameTiles.size());
        guiAdapter= new GuiAdapter(gameTiles.size());
        for (int numCreated = 0; numCreated < NUM_PLAYERS; numCreated++){
            if (numCreated < numHumanPlayers){
                players[numCreated] = new HumanPlayerController(guiAdapter);
            }
            else {
                players[numCreated] = new ComputerPlayerController(guiAdapter, board);
            }
        }
        scoreController = new ScoreController(gameTileReference, board);
        
        Tile startingTile = prepareTiles();
        Location boardDimensions = board.getBoardDimensions();
        board.placeTile(startingTile, new Location( boardDimensions.Row / 2, boardDimensions.Col / 2 ), 0);
        guiAdapter.placeFirstTile(boardDimensions.Row / 2,boardDimensions.Col /2 , String.format("%s", startingTile.tileType));
        /*scoreController.localBoard.placeTile(new Tile(startingTile), new Location( boardDimensions.Row / 2, boardDimensions.Col / 2 ), 0);
         */
        for (PlayerController playerController : players) {
            playerController.processConfirmedMove(new Tile(startingTile), new MoveInformation(new Location(boardDimensions.Row / 2, boardDimensions.Col / 2), 0, -1), currentPlayer);
        }
    }
    
    private Tile drawTile(){
        Tile nextTile = gameTiles.get(gameTiles.size()-1);
        gameTiles.remove(gameTiles.size()-1);
        return nextTile;
    }
    
    int gameLoop(long minTurnMiliseconds){
        Tile currentTile;
        
        while(!gameTiles.isEmpty()){
            currentTile = drawTile();
            handleMove(currentTile, minTurnMiliseconds);           
        }       
        endOfGameScoring();
        
        return 0;
    }
    
    private void endOfGameScoring(){
        for(int playerIndex = 0; playerIndex < board.playerMeeples.length; playerIndex++){
            for (int meepleIndex = 0; meepleIndex < board.playerMeeples[playerIndex].length; meepleIndex++){
                if (board.playerMeeples[playerIndex][meepleIndex].status == MeepleStatusEnum.onMonastery){
                    scoreController.scoreIncompleteDen(board.playerMeeples[playerIndex][meepleIndex].location);
                }
                else if (board.playerMeeples[playerIndex][meepleIndex].status == MeepleStatusEnum.onCity){
                    Location meepleLocation = board.playerMeeples[playerIndex][meepleIndex].location;
                    Tile meepleTile = board.board[meepleLocation.Row][meepleLocation.Col];
                    for (Edge edge : meepleTile.edges){
                        for (Node node: edge.nodes){
                            if (node.meeple != null && node.meeple.equals(board.playerMeeples[playerIndex][meepleIndex])){
                                scoreController.scoreIncompleteCity(node);
                                break;
                            }
                        }
                    }
                }
                else if (board.playerMeeples[playerIndex][meepleIndex].status == MeepleStatusEnum.onRoad){
                    Location meepleLocation = board.playerMeeples[playerIndex][meepleIndex].location;
                    Tile meepleTile = board.board[meepleLocation.Row][meepleLocation.Col];
                    for (Edge edge : meepleTile.edges){
                        for (Node node: edge.nodes){
                            if (node.meeple != null && node.meeple.equals(board.playerMeeples[playerIndex][meepleIndex])){
                                scoreController.scoreField(node);
                                break;
                            }
                        }
                    }
                }
                else if (board.playerMeeples[playerIndex][meepleIndex].status == MeepleStatusEnum.onField){
                    Location meepleLocation = board.playerMeeples[playerIndex][meepleIndex].location;
                    Tile meepleTile = board.board[meepleLocation.Row][meepleLocation.Col];
                    for (Edge edge : meepleTile.edges){
                        for (Node node: edge.nodes){
                            if (node.meeple != null && node.meeple.equals(board.playerMeeples[playerIndex][meepleIndex])){
                                scoreController.scoreField(node);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void handleMove(Tile tileForPlayer, long minTurnMiliseconds){
        long startTime = System.currentTimeMillis();
        System.out.println("player " + currentPlayer + " has tile " + tileForPlayer.tileType + " to move with");
        //Tile.printTile(tileForPlayer); Unneeded for GUI mode.
        
        
        MoveInformation playerMoveInfo;
        boolean firstAttempt = true;
        do {
            if(!firstAttempt){
                System.out.println("Invalid move.");
            }
            playerMoveInfo = players[currentPlayer].processPlayerMove(tileForPlayer);
            firstAttempt = false;
        } while (!board.verifyTilePlacement(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tileRotation));
        
        System.out.println("Player " + currentPlayer + " has confirmed a move Row: " + playerMoveInfo.tileLocation.Row + " Col: " + playerMoveInfo.tileLocation.Col + " Rotation: " + playerMoveInfo.tileRotation + " Meeple Location: " + playerMoveInfo.meepleLocation);
        board.placeTile(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tileRotation);
        if (board.verifyMeeplePlacement(tileForPlayer, playerMoveInfo.meepleLocation, currentPlayer) ) {
            board.placeMeeple(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.meepleLocation, currentPlayer);
        }
        else{
            System.out.println("Bad meeple placement, discarding");
            System.out.println("Bad Meeple: " + playerMoveInfo.meepleLocation);
            playerMoveInfo.meepleLocation = -1;
            //Just throw away bad meeple placements so score ctrlr and players don't get false signal
        }
        
        
        ArrayList<Meeple> meeplesToReturn = scoreController.processConfirmedMove(new Tile(tileForPlayer), playerMoveInfo, currentPlayer, false);
        // Limit turn speed so that AI games can be seen progressing in the GUI.
        long elapsedTime = System.currentTimeMillis() - startTime;
        long remainingTime = minTurnMiliseconds - elapsedTime;
        if(remainingTime > 0){
            try{
                Thread.sleep((remainingTime));
            }catch(InterruptedException e){
                System.out.println("Thread interrupted while sleeping, whose idea was this???");
            }           
        }
        for (PlayerController playerController : players){
            playerController.processConfirmedMove(new Tile(tileForPlayer), playerMoveInfo, currentPlayer);
        }
        guiAdapter.proccessConfirmedMove(tileForPlayer, playerMoveInfo, currentPlayer);
        
        for (Meeple info : meeplesToReturn){
            board.freeMeeple(info.owner, info.ID);
            scoreController.processFreedMeeple(info.owner, info.ID);
            for (PlayerController playerController : players){
                playerController.processFreedMeeple(info.owner, info.ID);
            }
        }
        
        switchPlayerControl();
        return;
    }
    
    private void switchPlayerControl(){
        currentPlayer = (currentPlayer + 1) % NUM_PLAYERS;
    }
    
    private Tile prepareTiles(){	//Returns instance of starting tile
        Tile startingTile = null;
        for (int index = 0; index < gameTiles.size(); index++){
            if (gameTiles.get(index).tileType == startingTileChar){
                Collections.swap(gameTiles, index, gameTiles.size()-1);
                startingTile = gameTiles.get(gameTiles.size()-1);
                gameTiles.remove(gameTiles.size()-1);
                break;
            }
        }
        
        Collections.shuffle(gameTiles, new Random(System.currentTimeMillis()));
        
        if (startingTile == null) {
            System.out.println("Starting tile not found in the imported deck.");
        }
        return startingTile;
    }
    
    private ArrayList<Tile> retrieveGameTiles(){
        Scanner scanner = new Scanner(System.in);
        String filePath = "tileset.txt";// scanner.next();
        return new TileRetriever(filePath).tiles;
    }
    
}