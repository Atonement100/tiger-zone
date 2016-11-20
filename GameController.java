import java.util.*;
import java.util.ArrayList;
public class GameController {
    private static final char startingTileChar = 'S';
    static final int NUM_PLAYERS = 2;
    static final int NUM_MEEPLES = 7;
    GameBoard board;
    private PlayerController[] players = new PlayerController[NUM_PLAYERS];
    //	private Meeple[][] playerMeeples = new Meeple[NUM_PLAYERS][NUM_MEEPLES];
    
    private ArrayList<Tile> gameTileReference; // Don't modify this one after constructor. Can be indexed in to with tile.ID.
    private ArrayList<Tile> gameTiles;
    private int currentPlayer = 0;
    public ScoreController scoreController;
    
    private static int[] dxFULL = {0,-1,0,1,-1,-1,1,1};
    private static int[] dyFULL = {-1,0,1,0,-1,1,-1,1};
    
    public GameController(int row, int col){
        board = new GameBoard(row, col);
    }
    
    public GameController(int numHumanPlayers){
        
        
        
        for (int numCreated = 0; numCreated < NUM_PLAYERS; numCreated++){
            if (numCreated < numHumanPlayers){
                players[numCreated] = new HumanPlayerController();
            }
            else {
                players[numCreated] = new ComputerPlayerController();
            }
        }
        
        this.gameTiles = retrieveGameTiles();
        this.gameTileReference = retrieveGameTiles();
        
        
        board = new GameBoard(gameTiles.size(), gameTiles.size());
        
        scoreController = new ScoreController(gameTileReference, board);
        
        Tile startingTile = prepareTiles();
        Location boardDimensions = board.getBoardDimensions();
        board.placeTile(startingTile, new Location( boardDimensions.Row / 2, boardDimensions.Col / 2 ), 0);
    }
    
    private Tile drawTile(){
        Tile nextTile = gameTiles.get(gameTiles.size()-1);
        gameTiles.remove(gameTiles.size()-1);
        return nextTile;
    }
    
    int gameLoop(){
        Tile currentTile;
        
        while(!gameTiles.isEmpty()){
            currentTile = drawTile();
            handleMove(currentTile);
        }
        
        return 0;
    }
    
    private void handleMove(Tile tileForPlayer){
        System.out.println("player " + currentPlayer + " has tile " + tileForPlayer.tileType + " to move with");
        
        MoveInformation playerMoveInfo;
        do {
            playerMoveInfo = players[currentPlayer].processPlayerMove(tileForPlayer);
        } while (!board.verifyTilePlacement(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tileRotation));
        
        System.out.println("Player " + currentPlayer + " has confirmed a move Row: " + playerMoveInfo.tileLocation.Row + " Col: " + playerMoveInfo.tileLocation.Col + " Rotation: " + playerMoveInfo.tileRotation);
        
        board.placeTile(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tileRotation);
        
        //**************************************************************************************************************
        //FACILITATE CHECK FOR ANY SURROUNDING DENS AND CHECK IF THIS IS A SCOARABLE TILE IN CASE IT IS A TILE WITH A DEN
        int row = playerMoveInfo.tileLocation.Row;
        int col = playerMoveInfo.tileLocation.Col;
        
        boolean fullySurrounded = true;
        
        ArrayList<Location> denLocations = new ArrayList<Location>();
        for(int direction = 0; direction < 8; direction++){
            if(row + dxFULL[direction] >= 0 && row + dxFULL[direction] < board.board.length && col + dyFULL[direction] >= 0 && col + dyFULL[direction] < board.board[0].length){ //Checks within board boundary
                if(board.board[row+dxFULL[direction]][col+dyFULL[direction]] == null) fullySurrounded = false;
                else if(board.board[row+dxFULL[direction]][col+dyFULL[direction]].hasMonastery) denLocations.add(new Location(row+dxFULL[direction],col+dyFULL[direction]));
            }
        }
        
        if(fullySurrounded && tileForPlayer.hasMonastery){
            scoreController.scoreDen(tileForPlayer.middle);
        }
        
        while(!denLocations.isEmpty()){
            Location buffer = denLocations.remove(0);
            row = buffer.Row;
            col = buffer.Col;
            
            fullySurrounded = true;
            for(int direction = 0; direction < 8; direction++){
                if(row + dxFULL[direction] >= 0 && row + dxFULL[direction] < board.board.length && col + dyFULL[direction] >= 0 && col + dyFULL[direction] < board.board[0].length){ //Checks within board boundary
                    if(board.board[row+dxFULL[direction]][col+dyFULL[direction]] == null) fullySurrounded = false;
                }
            }
            
            //kinda redundant to check if these have monastery here since I am adding to the arrayList only tiles with monasteries
            if(fullySurrounded && board.board[row][col].hasMonastery){
                scoreController.scoreDen(board.board[row][col].middle);
            }
            
        }
        
        //**************************************************************************************************************
        
        
        
        if (board.verifyMeeplePlacement(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.meepleLocation, currentPlayer) ) {
            board.placeMeeple(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.meepleLocation, currentPlayer);
        }
        
        for (PlayerController playerController : players){
            playerController.processConfirmedMove(tileForPlayer, playerMoveInfo, currentPlayer);
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