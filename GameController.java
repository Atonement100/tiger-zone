import java.util.ArrayList;
public class GameController {
	//	   			  N, E, S, W
	static int[] dx = {-1,0,1,0};
	static int[] dy = {0,1,0,-1};
	Tile[][] board;
	
	ArrayList<Tile> player1Tiles;
	ArrayList<Tile> player2Tiles;
	
	public GameController(int row, int col){
		board = new Tile[row][col];
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
				case 0:										//check NORTH tile compatibility (north edge of this, south edge of other)
					if(!toPlace.edges[direction].isCompatible(neighborTiles[direction].edges[2])){
						compatible = false;
					}
					break;
				case 1:										//check EAST tile compatibility (east edge of this, west edge of other)
					if(!toPlace.edges[direction].isCompatible(neighborTiles[direction].edges[3])){
						compatible = false;
					}
					break;
				case 2:										//check SOUTH tile compatibility (south edge of this, north edge of other)
					if(!toPlace.edges[direction].isCompatible(neighborTiles[direction].edges[0])){
						compatible = false;
					}
					break;
				case 3:										//check WEST tile compatibility (west edge of this, east edge of other)
					if(!toPlace.edges[direction].isCompatible(neighborTiles[direction].edges[1])){
						compatible = false;
					}
					break;
			}
		}
		
		//if passed all compatibility checks, place tile
		if(compatible){
			board[row][col] = toPlace;
			return true;
		}
		
		//if tile was not compatible with its neighboring tiles, return false
		return false;
	}
}