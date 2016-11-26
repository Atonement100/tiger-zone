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
            String[] serverText = null;
            int totalNumberOfRounds = 0;
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
       		serverText = fromServer.split(" ");
       		if(serverText[0].equalsIgnoreCase("NEW")){
       			totalNumberOfRounds = Integer.parseInt(serverText[6]);
       		}
       		int currentRound = 0;
       		while(currentRound < totalNumberOfRounds)
       		{
       		NetworkAdapter netAdapter = new NetworkAdapter();
            // Getting game info - Player ID - Starting Tile - Order of Tiles 
       			while(isWaiting){
       				fromServer = in.readLine();
       				serverText = fromServer.split(" ");
       				System.out.println("Server: " + fromServer);
       				if(serverText[0].equals("MATCH"))
       				{
       					isWaiting = false;
       				}
       				else {
       				netAdapter.parseMatchProtocol(fromServer);
       				}
       			}
       			int gamesEnded = 0;
       			// Where we start Receiving notification to send move / Send our moves. 
       			while(isPlaying){
       				fromServer = in.readLine();
       				System.out.println("Server: " + fromServer);
       				serverText = fromServer.split(" ");
       				
       				if(serverText[0].equals("MAKE")){
       					//TODO: SEND MOVES FROM AI
       					netAdapter.parseMakeMove(fromServer);
       					//just need to update fields
       					String makeMove = netAdapter.sendMove(messageStatus, gid, tileID, x, y, orientation, zone);
       					//System.out just lets us know what we're sending. 
       					System.out.println("Client: " + makeMove);
       					// Sends move to Server
       					out.println(makeMove);
       				}
       				else if(serverText[0].equals("GAME")){
       					 //TODO: Update gameboard for AI for opponents move
       					netAdapter.parseUpdateGameBoard(fromServer);
       					if(serverText[6].equalsIgnoreCase("FORFEITED:")){
       						gamesEnded++;
       					}
       				}
       				else if (serverText[0].equals("END")){
       					// end specific game
       					netAdapter.endGame();
       					gamesEnded++;
       				}
   					if(gamesEnded == 2){
   						isPlaying = false;
   					}
       			}
				// if its only 1 challenge end, if not go back to top for 2 or more challenges
       			currentRound++;
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
