import java.util.*;

public class TigerZone {
	
	
    public static void main(String[] args){
        
    	Scanner scanner = new Scanner(System.in);
    	
        GameController game = new GameController(60,60);
        
        String path = scanner.next();
        TileRetriever retriever = new TileRetriever(path);
        game.player1Tiles = retriever.tiles;
        game.player2Tiles = retriever.tiles;
        
        game.placeTile(game.player1Tiles.remove(0), new Location(0,0));
        game.placeTile(game.player1Tiles.remove(0), new Location(1,0));
        
        /*
         * 
         * if(!game.placeTile(new Tile(edgesBuffer,middleBuffer),new Location(row,col))){
	            	System.out.println("invalid placement at " + row + " " + col);
	       }
         * 
         */
        
        for(int row = 0; row < 60; row++){
            for(int col = 0; col < 60; col++){

                if(game.board[row][col] == null){
                    System.out.print("     ");
                }
                else{
                    System.out.print(" ");
                    for(int nodeIndex = 0; nodeIndex < game.board[row][col].edges[0].nodes.length; nodeIndex++){
                        System.out.print(game.board[row][col].edges[0].nodes[nodeIndex].featureType.toChar());
                    }
                    System.out.print(" ");
                }
            }
            System.out.println("");
            for(int col = 0; col < 60; col++){
            	if(game.board[row][col] == null){
            		System.out.print("     ");
            	}
            	else{
            		System.out.print(game.board[row][col].edges[3].nodes[0].featureType.toChar() + "   " + game.board[row][col].edges[1].nodes[0].featureType.toChar());
            	}
            }
            System.out.println("");
            for(int col = 0; col < 60; col++){
            	if(game.board[row][col] == null){
            		System.out.print("     ");
            	}
            	else{
            		System.out.print(game.board[row][col].edges[3].nodes[1].featureType.toChar() + " "+ game.board[row][col].middle.featureType.toChar() +" " + game.board[row][col].edges[1].nodes[1].featureType.toChar());
            	}
            }
            System.out.println("");
            for(int col = 0; col < 60; col++){
            	if(game.board[row][col] == null){
            		System.out.print("     ");
            	}
            	else{
            		System.out.print(game.board[row][col].edges[3].nodes[2].featureType.toChar() + "   " + game.board[row][col].edges[1].nodes[2].featureType.toChar());
            	}
            }
            System.out.println();
            for(int col = 0; col < 60; col++){
                if(game.board[row][col] == null){
                    System.out.print("     ");
                }
                else{
                    System.out.print(" ");
                    for(int nodeIndex = 0; nodeIndex < game.board[row][col].edges[2].nodes.length; nodeIndex++){
                        System.out.print(game.board[row][col].edges[2].nodes[nodeIndex].featureType.toChar());
                    }
                    System.out.print(" ");
                }
            }
            System.out.println("");
        }
        scanner.close();
    }
}
