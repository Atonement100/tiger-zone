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
//        l.possibleMove(new int []{3,4,5},  new int[]{6, 7, 8});
		System.out.print("Enter number of human players: ");
		Scanner scanner = new Scanner(System.in);
		int numPlayers = scanner.nextInt();
		GameController game = new GameController(numPlayers);
                // Need to limit turn speed if we want to watch AI only games in the GUI.
                long minTurnMiliseconds = 0;
                if(numPlayers == 0){
                    // Edit this value to change turn speed / remove speed limit.
                    minTurnMiliseconds = 500;
                    // Give time to find starting tile in GUI.
                    System.out.print("Press enter to begin game.");
                    scanner.nextLine();
                    scanner.nextLine();
                }                
		game.gameLoop(minTurnMiliseconds);
	}
}
