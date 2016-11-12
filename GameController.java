import java.util.ArrayList;
public class GameController {
	//	   			  N, E, S, W
	static int[] dx = {-1,0,1,0};
	static int[] dy = {0,1,0,-1};
	Tile[][] board;
	
	ArrayList<Tile> player1Tiles;
	ArrayList<Tile> player2Tiles;
	
	public GameController(int r, int c){
		board = new Tile[r][c];
	}
	
	boolean placeTile(Tile toPlace, Location loc){
		int r = loc.R;
		int c = loc.C;

		int rB = board.length;
		int cB = board[0].length;
		
		//****************************************************************************************
		//logic(simple) CHECKS
		
		if(r < 0 || r >= rB || c < 0 || c >= cB){
			
			return false;			//placement out of bounds;
		}
		
		if(board[r][c] != null){
			return false;			//if tile exists at loc
		}
		
		
		//****************************************************************************************
		//game rules CHECKS
		
		//start out optimistic
		boolean compatible = true;
		
		//get all neighboring tiles
		
		boolean noNeighboringTile = false;		//CHANGE THIS TO FALSE/TRUE FOR TESTING PURPOSES
		Tile[] neighborTiles = new Tile[4];		//N, E, S, W
		for(int i = 0; i < 4; i++){
			if(r+dx[i] >= 0 && r + dx[i] < rB && c + dy[i] >= 0 && c + dy[i] < cB){
				neighborTiles[i] = board[r+dx[i]][c+dy[i]];
				if(neighborTiles[i] != null){
					noNeighboringTile = false;
				}
			}
		}
		
		//if there is no neighboring tile, it's an invalid placement
		if(noNeighboringTile){
			return false;
		}
		
		//check compatibility with neighboring tiles
		for(int i = 0; i < 4; i++){
			
			if(neighborTiles[i] == null) continue;			//if there is no tile, no check is necessary, continue
			
			switch(i){
				case 0:										//check NORTH tile compatibility (north edge of this, south edge of other)
					if(!toPlace.edges[i].isCompatible(neighborTiles[i].edges[2])){
						compatible = false;
					}
					break;
				case 1:										//check EAST tile compatibility (east edge of this, west edge of other)
					if(!toPlace.edges[i].isCompatible(neighborTiles[i].edges[3])){
						compatible = false;
					}
					break;
				case 2:										//check SOUTH tile compatibility (south edge of this, north edge of other)
					if(!toPlace.edges[i].isCompatible(neighborTiles[i].edges[0])){
						compatible = false;
					}
					break;
				case 3:										//check WEST tile compatibility (west edge of this, east edge of other)
					if(!toPlace.edges[i].isCompatible(neighborTiles[i].edges[1])){
						compatible = false;
					}
					break;
			}
		}
		
		//if passed all compatibility checks, place tile
		if(compatible){
			board[r][c] = toPlace;	
			return true;
		}
		
		//if tile was not compatible with its neighboring tiles, return false
		return false;
	}
}