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
        
        for(int r = 0; r < 60; r++){
            for(int c = 0; c < 60; c++){

                if(game.board[r][c] == null){
                    System.out.print("     ");
                }
                else{
                    System.out.print(" ");
                    for(int n = 0; n < game.board[r][c].edges[0].nodes.length; n++){
                        System.out.print(game.board[r][c].edges[0].nodes[n].territory);
                    }
                    System.out.print(" ");
                }
            }
            System.out.println("");
            for(int c = 0; c < 60; c++){
            	if(game.board[r][c] == null){
            		System.out.print("     ");
            	}
            	else{
            		System.out.print(game.board[r][c].edges[3].nodes[0].territory + "   " + game.board[r][c].edges[1].nodes[0].territory);
            	}
            }
            System.out.println("");
            for(int c = 0; c < 60; c++){
            	if(game.board[r][c] == null){
            		System.out.print("     ");
            	}
            	else{
            		System.out.print(game.board[r][c].edges[3].nodes[1].territory + " "+ game.board[r][c].middle.territory +" " + game.board[r][c].edges[1].nodes[1].territory);
            	}
            }
            System.out.println("");
            for(int c = 0; c < 60; c++){
            	if(game.board[r][c] == null){
            		System.out.print("     ");
            	}
            	else{
            		System.out.print(game.board[r][c].edges[3].nodes[2].territory + "   " + game.board[r][c].edges[1].nodes[2].territory);
            	}
            }
            System.out.println();
            for(int c = 0; c < 60; c++){
                if(game.board[r][c] == null){
                    System.out.print("     ");
                }
                else{
                    System.out.print(" ");
                    for(int n = 0; n < game.board[r][c].edges[2].nodes.length; n++){
                        System.out.print(game.board[r][c].edges[2].nodes[n].territory);
                    }
                    System.out.print(" ");
                }
            }
            System.out.println("");
        }
        scanner.close();
    }
}
