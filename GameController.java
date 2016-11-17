import java.util.*;
import java.util.ArrayList;
public class GameController {
	private static final char startingTileChar = 'S';
	private static final int NUM_PLAYERS = 2;
	private static final int NUM_MEEPLES = 7;
	private static int[] dx = {0,-1,0,1}; // W, N, E, S
	private static int[] dy = {-1,0,1,0};
	private Tile[][] board;
	private PlayerController[] players = new PlayerController[NUM_PLAYERS];
	private Meeple[][] playerMeeples = new Meeple[NUM_PLAYERS][NUM_MEEPLES];

	private ArrayList<Tile> gameTileReference; // Don't modify this one after constructor. Can be indexed in to with tile.ID.
	ArrayList<Tile> gameTiles;

	int currentPlayer = 0;

	public GameController(int row, int col){
		board = new Tile[row][col];
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

		for (int playerIndex = 0; playerIndex < NUM_PLAYERS; playerIndex++){
			for (int meepleIndex = 0; meepleIndex < NUM_MEEPLES; meepleIndex++){
				playerMeeples[playerIndex][meepleIndex] = new Meeple(playerIndex);
			}
		}

		this.gameTiles = retrieveGameTiles();
		this.gameTileReference = this.gameTiles;
		board = new Tile[gameTiles.size()][gameTiles.size()];

		Tile startingTile = prepareTiles();
		placeTile(startingTile, new Location( board.length/2, board[ board.length/2 ].length/2), 0);
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
		//System.out.println("player " + currentPlayer + " has tile " + tileForPlayer.tileType + " to move with");
		//Scanner scanner = new Scanner(System.in);
		//scanner.next();
		MoveInformation playerMoveInfo;
		do {
			playerMoveInfo = players[currentPlayer].processPlayerMove(tileForPlayer);
		} while (!verifyTilePlacement(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tileRotation) || !verifyMeeplePlacement(tileForPlayer, playerMoveInfo.meepleLocation));

		System.out.println("Player " + currentPlayer + " has confirmed a move Row: " + playerMoveInfo.tileLocation.Row + " Col: " + playerMoveInfo.tileLocation.Col + " Rotation: " + playerMoveInfo.tileRotation);

		placeTile(tileForPlayer, playerMoveInfo.tileLocation, playerMoveInfo.tileRotation);

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

	private void placeTile(Tile tileToPlace, Location targetLocation, int rotations){
		tileToPlace.rotateClockwise(rotations);
		board[ targetLocation.Row ][ targetLocation.Col ] = tileToPlace;


		for(int direction = 0; direction < 4; direction++){
			Tile[] neighborTiles = getNeighboringTiles(targetLocation);

			if(neighborTiles[direction] == null) continue;			//if there is no tile, no check is necessary, continue

			for (int nodeIndex = 0; nodeIndex < tileToPlace.edges.length; nodeIndex++){
				tileToPlace.edges[direction].nodes[nodeIndex].neighbors.add(neighborTiles[direction].edges[(direction + 2) % 4].nodes[2-nodeIndex]);
				neighborTiles[direction].edges[(direction + 2) % 4].nodes[2-nodeIndex].neighbors.add(tileToPlace.edges[direction].nodes[nodeIndex]);
			}

			//Place starting tile forcibly at the middle of the board

			/*
			switch(direction){
				case 0:										//(west edge of this, east edge of other)
					for(int i = 0; i < 3; i++){
						toPlace.edges[direction].nodes[i].neighbors.add(neighborTiles[direction].edges[2].nodes[i]);
						neighborTiles[direction].edges[2].nodes[i].neighbors.add(toPlace.edges[direction].nodes[i]);
					}

					break;
				case 1:										//(north edge of this, south edge of other)
					for(int i = 0; i < 3; i++){
						toPlace.edges[direction].nodes[i].neighbors.add(neighborTiles[direction].edges[3].nodes[i]);
						neighborTiles[direction].edges[3].nodes[i].neighbors.add(toPlace.edges[direction].nodes[i]);
					}
					break;
				case 2:										//(east edge of this, west edge of other)
					for(int i = 0; i < 3; i++){
						toPlace.edges[direction].nodes[i].neighbors.add(neighborTiles[direction].edges[0].nodes[i]);
						neighborTiles[direction].edges[0].nodes[i].neighbors.add(toPlace.edges[direction].nodes[i]);
					}
					break;
				case 3:										//(south edge of this, north edge of other)
					for(int i = 0; i < 3; i++){
						toPlace.edges[direction].nodes[i].neighbors.add(neighborTiles[direction].edges[1].nodes[i]);
						neighborTiles[direction].edges[1].nodes[i].neighbors.add(toPlace.edges[direction].nodes[i]);
					}
					break;
			}
			*/
		}


	}
	
	private boolean verifyTilePlacement(Tile tileToPlace, Location targetLocation, int rotations){
		tileToPlace.rotateClockwise(rotations);

		int row = targetLocation.Row;
		int col = targetLocation.Col;

		int rowBoundary = board.length;
		int colBoundary = board[0].length;
		
		//****************************************************************************************
		//logic(simple) CHECKS
		
		if(row < 0 || row >= rowBoundary || col < 0 || col >= colBoundary){
			
			return false;			//placement out of bounds;
		}
		
		if(board[row][col] != null){
			return false;			//if tile exists at loc
		}
		
		
		//****************************************************************************************
		//game rules CHECKS
		
		//start out optimistic
		boolean isCompatible = true;
		//get all neighboring tiles

		boolean noNeighboringTile = false; //CHANGE THIS TO FALSE/TRUE FOR TESTING PURPOSES
		Tile[] neighborTiles = getNeighboringTiles(targetLocation);
		for (Tile nTile : neighborTiles){
			if (nTile != null){
				noNeighboringTile = false;
			}
		}
		if (noNeighboringTile) return false;

		/*
		boolean noNeighboringTile = false;		//CHANGE THIS TO FALSE/TRUE FOR TESTING PURPOSES
		Tile[] neighborTiles = new Tile[4];		//N, E, S, W
		for(int direction = 0; direction < 4; direction++){
			if(row + dx[direction] >= 0 && row + dx[direction] < rowBoundary && col + dy[direction] >= 0 && col + dy[direction] < colBoundary){
				neighborTiles[direction] = board[row + dx[direction]][col + dy[direction]];
				if(neighborTiles[direction] != null){
					noNeighboringTile = false;
				}
			}
		}
		
		//if there is no neighboring tile, it's an invalid placement
		if(noNeighboringTile){
			return false;
		}
		*/

		//check compatibility with neighboring tiles
		for(int direction = 0; direction < 4; direction++){

			if (neighborTiles[direction] != null){
				if(!tileToPlace.edges[direction].isCompatible(neighborTiles[direction].edges[(direction + 2) % 4])){ //Could change 4 to constant - EDGES_PER_TILE. 2 is EDGES_PER_TILE / 2
					isCompatible = false;
					break;
				}
			}

			/*
			if(neighborTiles[direction] == null) continue;			//if there is no tile, no check is necessary, continue

			switch(direction){
				case 0:										//check WEST tile compatibility (north edge of this, south edge of other)
					if(!tileToPlace.edges[direction].isCompatible(neighborTiles[direction].edges[2])){
						isCompatible = false;
					}
					break;
				case 1:										//check NORTH tile compatibility (east edge of this, west edge of other)
					if(!tileToPlace.edges[direction].isCompatible(neighborTiles[direction].edges[3])){
						isCompatible = false;
					}
					break;
				case 2:										//check EAST tile compatibility (south edge of this, north edge of other)
					if(!tileToPlace.edges[direction].isCompatible(neighborTiles[direction].edges[0])){
						isCompatible = false;
					}
					break;
				case 3:										//check SOUTH tile compatibility (west edge of this, east edge of other)
					if(!tileToPlace.edges[direction].isCompatible(neighborTiles[direction].edges[1])){
						isCompatible = false;
					}
					break;
			}
			*/
		}

		return isCompatible;
	}

	private boolean verifyMeeplePlacement(Tile tileToPlace, int placement){
		//checks if the meeple can be placed at the spot that the player indicated
		boolean isCompatible = true;
		
		return isCompatible;
	}
	
	private Tile[] getNeighboringTiles(Location tileLocation){
		int row = tileLocation.Row;
		int col = tileLocation.Col;

		Tile[] neighborTiles = new Tile[4];		//N, E, S, W
		for(int direction = 0; direction < 4; direction++){
			if(row + dx[direction] >= 0 && row + dx[direction] < board.length && col + dy[direction] >= 0 && col + dy[direction] < board[0].length){ //Checks within board boundary
				neighborTiles[direction] = board[row + dx[direction]][col + dy[direction]];
			}
		}

		return neighborTiles;
	}
}