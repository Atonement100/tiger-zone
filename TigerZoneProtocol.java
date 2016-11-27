import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

public class TigerZoneProtocol {
    private static final int WAITING = 0;
    private static final int SENTSPARTA = 1;
    private static final int SENTHELLO = 2;
    private static final int SENTWELCOME = 3;

    private int state = WAITING;
    private int currentTile = 0;
    private int moveNumber = 1;
    private String[] tiles = new String[] { "JJJJ-", "JJJJX", "JJJJX", "JJJJX", "JJJJX", 
    		"JJTJX", "JJTJX", "TTTT-", "TJTJ-", "TJTJ-", "TJTJ-", "TJTJ-",
    		"TJTJ-","TJTJ-","TJTJ-","TJTJ-", "TJJT-", "TJJT-", "TJJT-", "TJJT-", 
    		"TJJT-", "TJJT-", "TJJT-", "TJJT-", "TJJT-", "TJTT-", "TJTT-", "TJTT-", 
    		"TJTT-", "LLLL-", "JLLL-", "JLLL-", "JLLL-", "JLLL-", "LLJJ-", 
    		"LLJJ-", "LLJJ-", "LLJJ-", "LLJJ-", "JLJL-", "JLJL-", "JLJL-", 
    		"LJLJ-", "LJLJ-", "LJLJ-", "LJJJ-", "LJJJ-", "LJJJ-", "LJJJ-", 
    		"LJJJ-", "JLLJ-", "JLLJ-", "TLJT-", "TLJTP", "TLJTP", "JLTT-",
    		"JLTTB", "JLTTB", "TLTJ-", "TLTJ-", "TLTJD", "TLTJD", "TLLL-", 
    		"TLTT-", "TLTTP", "TLTTP", "TLLT-", "TLLT-", "TLLT-", "TLLTB",
    		"TLLTB", "LJTJ-", "LJTJD", "LJTJD", "TLLLC", "TLLLC"};
    private String gameA = "GAME A";
    private String gameB = "GAME B";
    private String[] parseInput = null;
    private boolean switchGames = true;
    
    public String VerifyAuthentication(String theInput) {
        String theOutput = null;

        if (state == WAITING) {
            theOutput = "THIS IS SPARTA!";
            state = SENTSPARTA;
        } else if (state == SENTSPARTA) {
            if (theInput.equalsIgnoreCase("JOIN PersiaRocks!")) {
                theOutput = "HELLO!";
                state = SENTHELLO;
            } else {
                theOutput = "Wrong password, try \"PersiaRocks!\"! ";
            }
        } else if (state == SENTHELLO) {
            if (theInput.equalsIgnoreCase("I AM Red Obiwan77")) {
                theOutput = "WELCOME Red PLEASE WAIT FOR THE NEXT CHALLENGE";
                state = SENTWELCOME;
            } else {
                theOutput = "Wrong User/Password - Red Obiwan77";
                state = SENTSPARTA;
            }
        } 
        return theOutput;
    }
    public String StartGame() {
        String theOutput = null;
        ArrayList<String> newTile = new ArrayList<String>();
        Collections.addAll(newTile, tiles);
        Collections.shuffle(newTile);
        theOutput = "THE REMAINING " + tiles.length + " TILES ARE " + Arrays.toString(newTile.toArray());
        return theOutput;
    }
    
    public String NotifyPlayer() {
    	String theOutput = null;
    	if(currentTile == tiles.length){
    		theOutput = "END OF ROUND";
    	}
    	else{
    		if(switchGames){
    			theOutput = "MAKE YOUR MOVE IN " + gameA + " WITHIN 1 SECOND: MOVE " +  moveNumber + " PLACE " + tiles[currentTile];
    			switchGames = false;
    		}
    		else{
    			theOutput = "MAKE YOUR MOVE IN " + gameB + " WITHIN 1 SECOND: MOVE " +  moveNumber + " PLACE " + tiles[currentTile];
    			switchGames = true;
    		}
    	}
    	return theOutput;
    }
    
    public String SendGameAMove(String theInput) {
    	parseInput = theInput.split(" ");
    	String theOutput = "GAME A MOVE " + moveNumber + " PLAYER Red PLACED " + parseInput[5] + " AT " + parseInput[7] + " " + parseInput[8] + " " + parseInput[9] + " " + parseInput[10] + " " + parseInput[11];
    	return theOutput;
    }

    public String SendGameBMove(String theInput) {
    	parseInput = theInput.split(" ");
    	String theOutput = "GAME B MOVE " + moveNumber + " PLAYER Blue PLACED " + parseInput[5] + " AT " + parseInput[7] + " " + parseInput[8] + " " + parseInput[9] + " " + parseInput[10] + " " + parseInput[11];
    	currentTile++;
    	moveNumber++;
    	return theOutput;
    }
}
