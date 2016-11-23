import java.util.Arrays;

public class TigerZoneProtocol {
    private static final int WAITING = 0;
    private static final int SENTSPARTA = 1;
    private static final int SENTHELLO = 2;
    private static final int SENTWELCOME = 3;

    private static final int NUMJOKES = 5;

    private int state = WAITING;
    private int currentJoke = 0;
    private String[] tiles = { "TLTTP", "LJTJ-", "JLJL-", "JJTJX", "LTTJB", "TLLT" };


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
        else if (state == SENTWELCOME) {
            if (theInput.equalsIgnoreCase("y")) {
                theOutput = "Knock! Knock!";
                if (currentJoke == (NUMJOKES - 1))
                    currentJoke = 0;
                else
                    currentJoke++;
                state = SENTSPARTA;
            } else {
                theOutput = "Bye.";
                state = WAITING;
            }
        }
        return theOutput;
    }
    public String StartGame() {
        String theOutput = null;
        theOutput = "THE REMAINING " + tiles.length + " TILES ARE " + Arrays.toString(tiles);
        return theOutput;
    }

    
    
}