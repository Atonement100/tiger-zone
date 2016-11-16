import java.util.*;

public class TigerZone {


	public static void main(String[] args){
		System.out.print("Enter number of human players: ");
		Scanner scanner = new Scanner(System.in);
		int numPlayers = scanner.nextInt();
		GameController game = new GameController(numPlayers);

		game.gameLoop();

//		game.placeTile(game.gameTiles.get(0), new Location(0,0));
//		game.placeTile(game.gameTiles.get(1), new Location(1,0));
//		game.placeTile(game.gameTiles.get(5), new Location(1,1));
//		game.placeTile(game.gameTiles.get(6), new Location(10,10));
		//just change this to true if you want to see the board
		if(false) {
			for (int row = 0; row < 20; row++) {
				for (int col = 0; col < 20; col++) {

					if (game.board[row][col] == null) {
						System.out.print("     ");
					} else {
						System.out.print(" ");
						for (int nodeIndex = 0; nodeIndex < game.board[row][col].edges[1].nodes.length; nodeIndex++) {
							System.out.print(game.board[row][col].edges[1].nodes[nodeIndex].featureType.toChar());
						}
						System.out.print(" ");
					}
				}
				System.out.println("");
				for (int col = 0; col < 20; col++) {
					if (game.board[row][col] == null) {
						System.out.print("     ");
					} else {
						System.out.print(game.board[row][col].edges[0].nodes[2].featureType.toChar() + "   " + game.board[row][col].edges[2].nodes[0].featureType.toChar());
					}
				}
				System.out.println("");
				for (int col = 0; col < 20; col++) {
					if (game.board[row][col] == null) {
						System.out.print("     ");
					} else {
						System.out.print(game.board[row][col].edges[0].nodes[1].featureType.toChar() + " " + game.board[row][col].middle.featureType.toChar() + " " + game.board[row][col].edges[2].nodes[1].featureType.toChar());
					}
				}
				System.out.println("");
				for (int col = 0; col < 20; col++) {
					if (game.board[row][col] == null) {
						System.out.print("     ");
					} else {
						System.out.print(game.board[row][col].edges[0].nodes[0].featureType.toChar() + "   " + game.board[row][col].edges[2].nodes[2].featureType.toChar());
					}
				}
				System.out.println();
				for (int col = 0; col < 20; col++) {
					if (game.board[row][col] == null) {
						System.out.print("     ");
					} else {
						System.out.print(" ");
						for (int nodeIndex = game.board[row][col].edges[3].nodes.length - 1; nodeIndex >= 0; nodeIndex--) {
							System.out.print(game.board[row][col].edges[3].nodes[nodeIndex].featureType.toChar());
						}
						System.out.print(" ");
					}
				}
				System.out.println("");
			}
		}
	}
}
