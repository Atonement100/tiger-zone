import java.util.Arrays;

public class TigerZoneProtocol {
    private static final int WAITING = 0;
    private static final int SENTSPARTA = 1;
    private static final int SENTHELLO = 2;
    private static final int SENTWELCOME = 3;

    private int state = WAITING;
    private int currentTile = 0;
    private int moveNumber = 1;
    private String[] tiles = { "TLTTP", "LJTJ-", "JLJL-", "JJTJX", "LTTJB", "TLLT" };
    private String gameA = "GAME A";
    private String gameB = "GAME B";
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
                theOutput = "Wrong password, try \"JOIN PersiaRocks!\"! ";
            }
        } else if (state == SENTHELLO) {
            if (theInput.equalsIgnoreCase("I AM Red Obiwan77")) {
                theOutput = "WELCOME Red PLEASE WAIT FOR THE NEXT CHALLENGE";
                state = SENTWELCOME;
            } else {
                theOutput = "Wrong User/Password - I AM Red Obiwan77";
                state = SENTSPARTA;
            }
        } 
        return theOutput;
    }
    public String StartGame() {
        String theOutput = null;
        theOutput = "THE REMAINING " + tiles.length + " TILES ARE " + Arrays.toString(tiles);
        return theOutput;
    }
    
    public String NotifyPlayer() {
    	String theOutput = null;
    	if(currentTile == tiles.length)
    	{
    		theOutput = "GAME OVER";
    	}
    	else
    	{
    		if(switchGames)
    		{
    			theOutput = "MAKE YOUR MOVE IN " + gameA + " WITHIN 1 SECOND: MOVE " +  moveNumber + " PLACE " + tiles[currentTile];
    			switchGames = false;
    		}
    		else
    		{
    			theOutput = "MAKE YOUR MOVE IN " + gameB + " WITHIN 1 SECOND: MOVE " +  moveNumber + " PLACE " + tiles[currentTile];
    			switchGames = true;
    		}
    	}
    	currentTile++;
    	moveNumber++;
    	return theOutput;
    }
   
}
