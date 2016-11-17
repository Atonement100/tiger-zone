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
		this.gameTileReference = this.gameTiles;
		board = new GameBoard(gameTiles.size(), gameTiles.size());

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

		if (board.verifyMeeplePlacement(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.meepleLocation, currentPlayer) ) {
			board.placeMeeple(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.meepleLocation, currentPlayer);
		}

		for (PlayerController playerController : players){
			playerController.processConfirmedMove(tileForPlayer, playerMoveInfo);
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
		String filePath = scanner.next();
		return new TileRetriever(filePath).tiles;
	}

}