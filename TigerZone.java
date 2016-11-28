import javax.swing.*;
import java.util.*;

public class TigerZone {


	public static void main(String[] args){
		String[] imgId = {"JJJJ-", "JJJJX", "JJTJX", "TTTT-", "TJTJ-", "TJTT-", "LLLL-",
				"JLLL-", "LLJJ-", "JLJL-", "LJLJ-", "LJJJ-", "JLLJ-", "TLJT-",
				"TLJTP", "JLTT-", "JLTTB", "TLTJ-", "TLTJD", "TLLL-", "TLTT-",
				"TLTTP", "TLLT-", "TLLTB", "LJTJ-", "LJTJD"};
		//LabelDemo l= new LabelDemo();
		// l.placeFirstTile(10,10,"JJJJ-");
		// l.getImgID(imgId[1]);
		// l.possibleMove(new int []{3,4,5},  new int[]{6, 7, 8});
		System.out.print("Enter number of human players: ");
		Scanner scanner = new Scanner(System.in);
		int numPlayers = scanner.nextInt();

		//int totalScore = 0, highestScore = 0;

		//for (int i = 0; i < 100; i++) {
			GameController game = new GameController(numPlayers);

			game.gameLoop();

			System.out.println("END GAME SCORE");
			System.out.println(game.scoreController.player1Score);
			System.out.println(game.scoreController.player2Score);

		//	if (game.scoreController.player2Score > highestScore)
		//		highestScore = game.scoreController.player2Score;
		//	if (game.scoreController.player1Score > highestScore)
		//		highestScore = game.scoreController.player1Score;

		//	totalScore += game.scoreController.player1Score + game.scoreController.player2Score;
		//}

		//System.out.println("All games have completed. Average score was " + totalScore/100 + " highest score was " + highestScore);
	}
}
