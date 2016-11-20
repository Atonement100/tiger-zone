
import Gui.LabelDemo;

import javax.swing.*;
import java.util.*;

public class TigerZone {


	public static void main(String[] args){
         String[] imgId = {"JJJJ-", "JJJJX", "JJTJX", "TTTT-", "TJTJ-", "TJTT-", "LLLL-",
                "JLLL-", "LLJJ-", "JLJL-", "LJLJ-", "LJJJ-", "JLLJ-", "TLJT-",
                "TLJTP", "JLTT-", "JLTTB", "TLTJ-", "TLTJD", "TLLL-", "TLTT-",
                "TLTTP", "TLLT-", "TLLTB", "LJTJ-", "LJTJD"};
		LabelDemo l= new LabelDemo();
        l.getImgID(imgId[7]);
      //  l.getImgID("JJJJX");
       // l.possibleMove(new int []{3,4,5},  new int[]{6, 7, 8});
		//System.out.print("Enter number of human players: ");
	//	Scanner scanner = new Scanner(System.in);
		//int numPlayers = scanner.nextInt();
	//	GameController game = new GameController(numPlayers);

	///	game.gameLoop();
	//	game.board.printBoard();
	}
}
