import java.io.IOException;
import java.util.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
public class GameController {
	private static final char startingTileChar = 'S';
	private static final int NUM_PLAYERS = 2;
	private static int[] dx = {0,-1,0,1}; // W, N, E, S
	private static int[] dy = {-1,0,1,0};
	Tile[][] board;
	PlayerController[] players = new PlayerController[2];

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
			else{
				players[numCreated] = new ComputerPlayerController();
			}
		}

		this.gameTiles = retrieveGameTiles();
		this.gameTileReference = this.gameTiles;
		board = new Tile[gameTiles.size()][gameTiles.size()];

		Tile startingTile = prepareTiles();
		placeStartingTile(startingTile);
	}

	Tile drawTile(){
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

	void handleMove(Tile tileForPlayer){
		//System.out.println("player " + currentPlayer + " has tile " + tileForPlayer.tileType + " to move with");
		//Scanner scanner = new Scanner(System.in);
		//scanner.next();
		players[currentPlayer].processMove();

		switchPlayerControl();
		return;
	}

	void switchPlayerControl(){
		currentPlayer = (currentPlayer + 1) % 2; //NUM_PLAYERS == 2.. <3 magic numbers <3
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

	void placeStartingTile(Tile startingTile){
		//Place starting tile forcibly at the middle of the board
		board[ board.length/2 ][ board[board.length/2].length/2 ] = startingTile;
	}
	
	boolean placeTile(Tile toPlace, Location loc){
		int row = loc.Row;
		int col = loc.Col;

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
		boolean compatible = true;
		
		//get all neighboring tiles
		
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
		
		//check compatibility with neighboring tiles
		for(int direction = 0; direction < 4; direction++){
			
			if(neighborTiles[direction] == null) continue;			//if there is no tile, no check is necessary, continue
			
			switch(direction){
				case 0:										//check WEST tile compatibility (north edge of this, south edge of other)
					if(!toPlace.edges[direction].isCompatible(neighborTiles[direction].edges[2])){
						compatible = false;
					}
					break;
				case 1:										//check NORTH tile compatibility (east edge of this, west edge of other)
					if(!toPlace.edges[direction].isCompatible(neighborTiles[direction].edges[3])){
						compatible = false;
					}
					break;
				case 2:										//check EAST tile compatibility (south edge of this, north edge of other)
					if(!toPlace.edges[direction].isCompatible(neighborTiles[direction].edges[0])){
						compatible = false;
					}
					break;
				case 3:										//check SOUTH tile compatibility (west edge of this, east edge of other)
					if(!toPlace.edges[direction].isCompatible(neighborTiles[direction].edges[1])){
						compatible = false;
					}
					break;
			}
		}
		
		//if passed all compatibility checks, place tile, connect nodes
		if(compatible){
			board[row][col] = toPlace;
			
			for(int direction = 0; direction < 4; direction++){
				
				if(neighborTiles[direction] == null) continue;			//if there is no tile, no check is necessary, continue
				
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
			}
			return true;
		}
		
		//if tile was not compatible with its neighboring tiles, return false
		return false;
	}
}