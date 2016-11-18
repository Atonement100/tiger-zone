import java.util.*;

public class TigerZone {


	public static void main(String[] args){
		System.out.print("Enter number of human players: ");
		Scanner scanner = new Scanner(System.in);
		int numPlayers = scanner.nextInt();
		GameController game = new GameController(numPlayers);

		game.gameLoop();
		game.board.printBoard();
	}
}
