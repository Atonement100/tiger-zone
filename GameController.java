import java.util.*;
import java.util.ArrayList;
public class GameController {
    protected GuiAdapter guiAdapter;
    private static final char startingTileChar = 'S';
    static final int NUM_PLAYERS = 2;
    static final int NUM_TIGERS = 7;
    GameBoard board;
    private PlayerController[] players = new PlayerController[NUM_PLAYERS];
    //private Tiger[][] playerTigers = new Tiger[NUM_PLAYERS][NUM_TIGERS];
    
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
        scoreController.notifyStartingTile(startingTile);

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
        boolean spectating = false;
        
        while(!gameTiles.isEmpty()){
            currentTile = drawTile();
            handleMove(currentTile);
            //board.printBoard();
           // System.out.println("Player 1 score: " + scoreController.player1Score);
           // System.out.println("Player 2 score: " + scoreController.player2Score);
            //guiAdapter.updateScores(scoreController.player1Score, scoreController.player2Score);
            if (spectating){
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
        for(int playerIndex = 0; playerIndex < board.playerTigers.length; playerIndex++){
            for (int tigerIndex = 0; tigerIndex < board.playerTigers[playerIndex].length; tigerIndex++){
                if (board.playerTigers[playerIndex][tigerIndex].status == TigerStatusEnum.onDen){
                    scoreController.scoreIncompleteDen(board.playerTigers[playerIndex][tigerIndex].location);
                }
                else if (board.playerTigers[playerIndex][tigerIndex].status == TigerStatusEnum.onLake){
                    Location tigerLocation = board.playerTigers[playerIndex][tigerIndex].location;
                    Tile tigerTile = board.board[tigerLocation.Row][tigerLocation.Col];
                    for (Edge edge : tigerTile.edges){
                        for (Node node: edge.nodes){
                            if (node.tiger != null && node.tiger.equals(board.playerTigers[playerIndex][tigerIndex])){
                                scoreController.scoreIncompleteLake(node);
                                break;
                            }
                        }
                    }
                }
                else if (board.playerTigers[playerIndex][tigerIndex].status == TigerStatusEnum.onTrail){
                    Location tigerLocation = board.playerTigers[playerIndex][tigerIndex].location;
                    Tile tigerTile = board.board[tigerLocation.Row][tigerLocation.Col];
                    for (Edge edge : tigerTile.edges){
                        for (Node node: edge.nodes){
                            if (node.tiger != null && node.tiger.equals(board.playerTigers[playerIndex][tigerIndex])){
                                scoreController.scoreJungle(node);
                                break;
                            }
                        }
                    }
                }
                else if (board.playerTigers[playerIndex][tigerIndex].status == TigerStatusEnum.onJungle){
                    Location tigerLocation = board.playerTigers[playerIndex][tigerIndex].location;
                    Tile tigerTile = board.board[tigerLocation.Row][tigerLocation.Col];
                    for (Edge edge : tigerTile.edges){
                        for (Node node: edge.nodes){
                            if (node.tiger != null && node.tiger.equals(board.playerTigers[playerIndex][tigerIndex])){
                                scoreController.scoreJungle(node);
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

        if (playerMoveInfo.tigerLocation != -1) {
            board.placeTiger(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tigerLocation, currentPlayer);
        }

        ArrayList<Tiger> tigersToReturn = scoreController.processConfirmedMove(tileForPlayer, playerMoveInfo, currentPlayer, false);

        for (PlayerController playerController : players){
            playerController.processConfirmedMove(new Tile(tileForPlayer), playerMoveInfo, currentPlayer);
        }

        for (Tiger tiger : tigersToReturn){
            board.freeTiger(tiger.owner, tiger.ID);
            scoreController.processFreedTiger(tiger.owner, tiger.ID);
            for (PlayerController playerController : players){
                playerController.processFreedTiger(tiger.owner, tiger.ID);
            }
        }
        board.printBoard();
    }

    private void handleMove(Tile tileForPlayer){
        //System.out.println("player " + currentPlayer + " has tile " + tileForPlayer.tileType + " to move with");
        //Tile.printTile(tileForPlayer);

        MoveInformation playerMoveInfo;
        boolean firstAttempt = true;
        do {
            if (!firstAttempt) System.out.println("Invalid move.");
            playerMoveInfo = players[currentPlayer].processPlayerMove(tileForPlayer);
            firstAttempt = false;
        } while (!board.verifyTilePlacement(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tileRotation));
        
        //System.out.println("Player " + currentPlayer + " has confirmed a move Row: " + playerMoveInfo.tileLocation.Row + " Col: " + playerMoveInfo.tileLocation.Col + " Rotation: " + playerMoveInfo.tileRotation + " Tiger Location: " + playerMoveInfo.tigerLocation);
        
        board.placeTile(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tileRotation);
        
        if (board.verifyTigerPlacement(tileForPlayer, playerMoveInfo.tigerLocation, currentPlayer) ) {
            board.placeTiger(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tigerLocation, currentPlayer);
        }
        else{
            playerMoveInfo.tigerLocation = -1;
            //Just throw away bad tiger placements so score ctrlr and players don't get false signal
        }
        
        
        ArrayList<Tiger> tigersToReturn = scoreController.processConfirmedMove(tileForPlayer, playerMoveInfo, currentPlayer, false);
        
        for (PlayerController playerController : players){
            playerController.processConfirmedMove(new Tile(tileForPlayer), playerMoveInfo, currentPlayer);
        }

        guiAdapter.proccessConfirmedMove(tileForPlayer, playerMoveInfo, currentPlayer);
        
        for (Tiger tiger : tigersToReturn){
            board.freeTiger(tiger.owner, tiger.ID);
            scoreController.processFreedTiger(tiger.owner, tiger.ID);
            for (PlayerController playerController : players){
                playerController.processFreedTiger(tiger.owner, tiger.ID);
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
        Tile.resetTileIdentify();
        Scanner scanner = new Scanner(System.in);
        String filePath = "tileset.txt";// scanner.next();
        return new TileRetriever(filePath).tiles;
    }

    Location getBoardCenter(){
        return new Location(board.board.length / 2, board.board[0].length / 2);
    }
}