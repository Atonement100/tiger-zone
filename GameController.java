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

        players[0] = new ComputerPlayerController(board);
        players[1] = new HumanPlayerController(board); // Humans don't waste processor power

        this.gameTileReference = retrieveGameTiles();
        scoreController = new ScoreController(gameTileReference, board);
    }
    
    public GameController(int row, int col, ArrayList<Tile> tiles){
        board = new GameBoard(row, col);

        players[0] = new ComputerPlayerController(board);
        players[1] = new HumanPlayerController(board); // Humans don't waste processor power

        this.gameTileReference = tiles;
        scoreController = new ScoreController(gameTileReference, board);
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
        Tile nextTile;
        do {
            nextTile = gameTiles.get(gameTiles.size() - 1);
            gameTiles.remove(gameTiles.size() - 1);
        } while (!board.isPossibleToPlaceTileSomewhere(nextTile));


        return nextTile;
    }
    
    int gameLoop(){
        Tile currentTile;
        // Edit this to watch AI games play out move by move.
        boolean spectating = false;
        while(!gameTiles.isEmpty()){
            currentTile = drawTile();
            handleMove(currentTile);
            
            System.out.println("Player 0 score: " + scoreController.player1Score);
            System.out.println("Player 1 score: " + scoreController.player2Score);
            guiAdapter.updateScores(scoreController.player1Score, scoreController.player2Score);
            if(spectating){
                System.out.print("Press enter to continue.");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
            }
        }
        
        
        endOfGameScoring();
        guiAdapter.updateScores(scoreController.player1Score, scoreController.player2Score);
        return 0;
    }
    
    private void endOfGameScoring(){
    	
    	System.out.println("END OF GAME SCORING");
    	
        for(int playerIndex = 0; playerIndex < board.playerMeeples.length; playerIndex++){
            for (int meepleIndex = 0; meepleIndex < board.playerMeeples[playerIndex].length; meepleIndex++){
            	
            	
            	
            	
            	
                if (board.playerMeeples[playerIndex][meepleIndex].status == MeepleStatusEnum.onMonastery){
                	
                	System.out.println("END OF GAME SCORING");
                	System.out.println("BEFORE SCORING INCOMPLETE DEN at" +board.playerMeeples[playerIndex][meepleIndex].location.Row + " " + board.playerMeeples[playerIndex][meepleIndex].location.Col );
                	System.out.println("Player 0 " + scoreController.player1Score);
                	System.out.println("Player 1 " + scoreController.player2Score);
                	
                	
                	scoreController.scoreIncompleteDen(board.playerMeeples[playerIndex][meepleIndex].location);
                    
                	System.out.println("AFTER SCORING INCOMPLETE DEN at" +board.playerMeeples[playerIndex][meepleIndex].location.Row + " " + board.playerMeeples[playerIndex][meepleIndex].location.Col );

                	
                	System.out.println("Player 0 " + scoreController.player1Score);
                	System.out.println("Player 1 " + scoreController.player2Score);
                    
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
                            	
                            	
                            	
                                scoreController.scoreRoad(node);
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
                            	
                            	System.out.println("BEFORE SCORING FARM AT " + board.playerMeeples[playerIndex][meepleIndex].location.Row + " " + board.playerMeeples[playerIndex][meepleIndex].location.Col);
                            	System.out.println("player 0 " + scoreController.player1Score);
                            	System.out.println("player 1 " + scoreController.player2Score);
                            	
                                scoreController.scoreField(node);
                                
                                System.out.println("AFter SCORING FARM AT " + board.playerMeeples[playerIndex][meepleIndex].location.Row + " " + board.playerMeeples[playerIndex][meepleIndex].location.Col);
                            	System.out.println("player 0 " + scoreController.player1Score);
                            	System.out.println("player 1 " + scoreController.player2Score);
                                
                            	System.out.println("");
                            	
                                break;
                            }
                        }
                    }
                }
                
            }
        }
    }

    void processNetworkStart(Tile startingTile, MoveInformation startMoveInfo){
        board.placeTile(startingTile, startMoveInfo.tileLocation, startMoveInfo.tileRotation);
        players[0].processConfirmedMove(startMoveInfo);
    }

    MoveInformation processNetworkedPlayerMove(Tile tileForPlayer){
        return players[0].processPlayerMove(tileForPlayer);
    }

    void processConfirmedNetworkedMove(Tile tileForPlayer, MoveInformation playerMoveInfo, int currentPlayer){
        board.placeTile(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tileRotation);

        if (playerMoveInfo.meepleLocation != -1) {
            board.placeMeeple(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.meepleLocation, currentPlayer);
        }

        ArrayList<Meeple> meeplesToReturn = scoreController.processConfirmedMove(tileForPlayer, playerMoveInfo, currentPlayer, false);

        for (PlayerController playerController : players){
            playerController.processConfirmedMove(new Tile(tileForPlayer), playerMoveInfo, currentPlayer);
        }

        for (Meeple meeple : meeplesToReturn){
            board.freeMeeple(meeple.owner, meeple.ID);
            scoreController.processFreedMeeple(meeple.owner, meeple.ID);
            for (PlayerController playerController : players){
                playerController.processFreedMeeple(meeple.owner, meeple.ID);
            }
        }
        board.printBoard();
    }

    private void handleMove(Tile tileForPlayer){
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
        } while (!board.verifyTilePlacement(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tileRotation)
        		);
        
        System.out.println("Player " + currentPlayer + " has confirmed a move Row: " + playerMoveInfo.tileLocation.Row + " Col: " + playerMoveInfo.tileLocation.Col + " Rotation: " + playerMoveInfo.tileRotation + " Meeple Location: " + playerMoveInfo.meepleLocation);
        board.placeTile(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tileRotation);
        if (board.verifyMeeplePlacement(tileForPlayer, playerMoveInfo.meepleLocation, currentPlayer) ) {
            board.placeMeeple(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.meepleLocation, currentPlayer);
        }
        else{
            playerMoveInfo.meepleLocation = -1;
            //Just throw away bad meeple placements so score ctrlr and players don't get false signal
        }
        
        
        ArrayList<Meeple> meeplesToReturn = scoreController.processConfirmedMove(tileForPlayer, playerMoveInfo, currentPlayer, false);
        
        for (PlayerController playerController : players){
            playerController.processConfirmedMove(new Tile(tileForPlayer), playerMoveInfo, currentPlayer);
        }
        guiAdapter.proccessConfirmedMove(tileForPlayer, playerMoveInfo, currentPlayer);
        
        for (Meeple meeple : meeplesToReturn){
            board.freeMeeple(meeple.owner, meeple.ID);
            scoreController.processFreedMeeple(meeple.owner, meeple.ID);
            for (PlayerController playerController : players){
                playerController.processFreedMeeple(meeple.owner, meeple.ID);
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

    Location getBoardCenter(){
        return new Location(board.board.length / 2, board.board[0].length / 2);
    }
}
