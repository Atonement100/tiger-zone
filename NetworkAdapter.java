import java.util.ArrayList;
import java.util.Scanner;
/* Needs: Tile.java, TileRetriever.java, tileset.txt, AI.java */

//Takes strings read from server and converts them to usable information
public class NetworkAdapter{
	private ArrayList<Tile> shuffledTiles = new ArrayList<Tile>();
	private ArrayList<Tile> staticTiles = new ArrayList<Tile>();
	private String pid; //Our playerID
	private String oid; // Opponent playerID
	/* Initialize AI's with game ID 
	Don't know if hard coding gameID is a good idea or not */
	//AI ai1 = new AI("A");
	//AI ai2 = new AI("B");
	Scanner s = new Scanner(System.in);
	
	//Creates array of local tiles from input file
	public NetworkAdapter(){
		String filePath = "uniquetileset.txt";
		staticTiles = new TileRetriever(filePath).tiles;
	}

	//Parses the string and decides which method to call
	public void parseMatchProtocol(String message){
		String[] tokens = message.split(" ");
		String tileID, gid, x, y, orientation;
		switch(tokens[0]){
			case "STARTING": //Place starting tile on AI's board
				tileID = convertTileToID(tokens[3]);
				Tile startingTile = convertFromTileID(tileID);
				x = tokens[5];
				y = tokens[6];
				orientation = tokens[7];
				//Place on both AI boards
				break;
				
			//Set opponent's player ID
			case "YOUR": 
				oid = tokens[4];
				break;
				
				// THE REMAINING TILES ARE : - should push array of tiles into AI here
			case "THE": //Create shuffled array of tiles
				initializeShuffledTiles(Integer.parseInt(tokens[2]), tokens);
				break;
		}
	}
	
	// Make Move string is parsed, should probably send AI the tile and GameID we get from parsing here
	public void parseMakeMove(String message){
		String[] tokens = message.split(" ");
		String tileID, gid;
		tileID = convertTileToID(tokens[12]);
		Tile nextTile = convertFromTileID(tileID);
		gid = tokens[5];
	}
	// Update gameboard string for both games is parsed - should update gameboards for our gamemove and opponent's game move on AI's representation of board
	public void parseUpdateGameBoard(String message){
		String[] tokens = message.split(" ");
		String tileID, gid, x, y, orientation;
		//Read in move that was legally made for both players & place tile on board
		gid = tokens[1];
		String playerID = tokens[5]; //player that made the move
		if (tokens[6].equals("PLACED")){
			Tile tilePlaced = convertFromTileID(convertTileToID(tokens[7]));
			x = tokens[9];
			y = tokens[10];
			orientation = tokens[11];
			String piece = "";
			String zone = "";
			if(!tokens[12].equals("NONE")){
			piece = tokens[12];
			zone = tokens[13];
			}
		}
		else{
		//Game was forfeited from an illegal move
		//End AI for this match
		}
	}
	
	public void endGame(){
		//Round: Wait for next match, Challenges: wait for next challenge
		//Empty shuffledTiles array
	}

	//Creates message to be sent through the network for a tile placement
	//messageStatus denotes type of move
	public String sendMove(int messageStatus, String gid, String tileID, int x, int y, int orientation, int zone){
		String message = "";
		switch(messageStatus){
			case 0: message = "GAME " + gid + " PLACE " + convertTileToString(tileID) + " AT " + x + " " + y + " " + orientation + " NONE";
					break;
			case 1: message = "GAME " + gid + " PLACE " + convertTileToString(tileID) + " AT " + x + " " + y + " " + orientation + " CROCODILE";
					break;
			case 2: message = "GAME " + gid + " PLACE " + convertTileToString(tileID) + " AT " + x + " " + y + " " + orientation + " TIGER " + zone;
					break;
			case 3: message = "GAME " + gid + " TILE " + convertTileToString(tileID) + " UNPLACEABLE PASS";
					break;
			case 4: message = "GAME " + gid + " TILE " + convertTileToString(tileID) + " UNPLACEABLE RETRIEVE TIGER AT " + x + " " + y;
					break;
			case 5: message = "GAME " + gid + " TILE " + convertTileToString(tileID) + " UNPLACEABLE ADD ANOTHER TIGER TO " + x + " " + y;
					break;
		}
		return message;
	}

	//Takes string from server and creates the shuffled tile array from string
	private void initializeShuffledTiles(int numberOfTiles, String[] input){
		for(int i = 0; i < numberOfTiles; i++){
			shuffledTiles.add(convertFromTileID(convertTileToID(input[6 + i])));
		}
	}


	//Converts tile string from server to local tile arrangement
	private String convertTileToID(String tileString){
		switch(tileString){
			case "JJJJ-": return ("A");
			case "JJJJX": return ("B");
			case "JJTJX": return ("C");
			case "TTTT-": return ("D");
			case "TJTJ-": return ("E");
			case "TJJT-": return ("F");
			case "TJTT-": return ("G");
			case "LLLL-": return ("H");
			case "JLLL-": return ("I");
			case "LLJJ-": return ("J");
			case "JLJL-": return ("K");
			case "LJLJ-": return ("L");
			case "LJJJ-": return ("M");
			case "JLLJ-": return ("N");
			case "TLJT-": return ("O");
			case "TLJTP": return ("P");
			case "JLTT-": return ("Q");
			case "JLTTB": return ("R");
			case "TLTJ-": return ("S");
			case "TLTJD": return ("T");
			case "TLLL-": return ("U");
			case "TLTT-": return ("V");
			case "TLTTP": return ("W");
			case "TLLT-": return ("X");
			case "TLLTB": return ("Y");
			case "LJTJ-": return ("Z");
			case "LJTJD": return ("a");
			case "TLLLC": return ("b");
			default: return "";
		}
	}

	//Converts tile string from local tile arrangement to server tile
	private String convertTileToString(String tileID){
		switch(tileID){
			case "A": return ("JJJJ-");
			case "B": return ("JJJJX");
			case "C": return ("JJTJX");
			case "D": return ("TTTT-");
			case "E": return ("TJTJ-");
			case "F": return ("TJJT-");
			case "G": return ("TJTT-");
			case "H": return ("LLLL-");
			case "I": return ("JLLL-");
			case "J": return ("LLJJ-");
			case "K": return ("JLJL-");
			case "L": return ("LJLJ-");
			case "M": return ("LJJJ-");
			case "N": return ("JLLJ-");
			case "O": return ("TLJT-");
			case "P": return ("TLJTP");
			case "Q": return ("JLTT-");
			case "R": return ("JLTTB");
			case "S": return ("TLTJ-");
			case "T": return ("TLTJD");
			case "U": return ("TLLL-");
			case "V": return ("TLTT-");
			case "W": return ("TLTTP");
			case "X": return ("TLLT-");
			case "Y": return ("TLLTB");
			case "Z": return ("LJTJ-");
			case "a": return ("LJTJD");
			case "b": return ("TLLLC");
			default: return "";
		}
	}

	private Tile convertFromTileID(String tileID){
		switch(tileID){
			case "A": return (staticTiles.get(0));
			case "B": return (staticTiles.get(1));
			case "C": return (staticTiles.get(2));
			case "D": return (staticTiles.get(3));
			case "E": return (staticTiles.get(4));
			case "F": return (staticTiles.get(5));
			case "G": return (staticTiles.get(6));
			case "H": return (staticTiles.get(7));
			case "I": return (staticTiles.get(8));
			case "J": return (staticTiles.get(9));
			case "K": return (staticTiles.get(10));
			case "L": return (staticTiles.get(11));
			case "M": return (staticTiles.get(12));
			case "N": return (staticTiles.get(13));
			case "O": return (staticTiles.get(14));
			case "P": return (staticTiles.get(15));
			case "Q": return (staticTiles.get(16));
			case "R": return (staticTiles.get(17));
			case "S": return (staticTiles.get(18));
			case "T": return (staticTiles.get(19));
			case "U": return (staticTiles.get(20));
			case "V": return (staticTiles.get(21));
			case "W": return (staticTiles.get(22));
			case "X": return (staticTiles.get(23));
			case "Y": return (staticTiles.get(24));
			case "Z": return (staticTiles.get(25));
			case "a": return (staticTiles.get(26));
			case "b": return (staticTiles.get(27));
			default: return null;
		}
	}
}
