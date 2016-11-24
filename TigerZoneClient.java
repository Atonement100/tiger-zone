import java.io.*;
import java.net.*;

public class TigerZoneClient {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 2) {
            System.err.println(
                "Usage: java TigerZoneClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
            Socket tzSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(tzSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(tzSocket.getInputStream()));
        ) {
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;
            String opponentPlayerID;
            String[] parseText = null;
            int numberOfRounds = 0;
            boolean isVerified = false;
            boolean isWaiting = true;
            boolean isPlaying = true;
            while (!isVerified) 
            {
           		fromServer = in.readLine();
           		System.out.println("Server: " + fromServer);
        		fromUser = stdIn.readLine();
           		if(fromServer.equals("THIS IS SPARTA!")){
           			out.println("JOIN " + fromUser);
           		}
           		else if (fromServer.equals("HELLO!")){
           			isVerified = true;
           			out.println("I AM " + fromUser);
           		}
           	}
            
            // Find out how many challenges will be played
            fromServer = in.readLine();
       		parseText = fromServer.split(" ");
       		String firstWord = parseText[0];
       		if(firstWord.equalsIgnoreCase("NEW")){
       			numberOfRounds = Integer.parseInt(parseText[6]);
       		}
       		// while(number of rounds < total rounds) 
            // Getting game info - Player ID - Starting Tile - Order of Tiles 
            while(isWaiting){
           		fromServer = in.readLine();
           		parseText = fromServer.split(" ");
           		firstWord = parseText[0];
           		System.out.println("Server: " + fromServer);
           		if(firstWord.equals("YOUR")){
           			opponentPlayerID = parseText[4];
           		}
           		else if(firstWord.equals("REMAINING"))
           		{
           			// TODO: ADD TILES TO AI LIST
           		}
           		else if(firstWord.equals("MATCH"))
           		{
           			isWaiting = false;
           		}
            }
            // Where we start Receiving notification to send move / Send our moves. 
            while(isPlaying){
           		fromServer = in.readLine();
           		System.out.println("Server: " + fromServer);
           		parseText = fromServer.split(" ");
           		firstWord = parseText[0];
           		if(firstWord.equals("MAKE")){
           			// TODO: SEND MOVES FROM AI
           			String makeMove = "GAME " + parseText[5] + " MOVE " + parseText[10] + " PLACE " + parseText[12] + " AT 0 0 0 TIGER 8";
           			// System.out just lets us know what we're sending. 
           			System.out.println("Client: " + makeMove);
           			out.println(makeMove);
           		}
           		else if(firstWord.equals("GAME")){
           			// TODO: Update gameboard for AI for opponents move
           		}
           		else if (firstWord.equals("END")){
           			// if its only 1 challenge end, if not go back to top for 2 or more challenges
           		}
           		
            }
            
        } 
        
        catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } 
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
}
