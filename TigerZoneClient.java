import java.io.*;
import java.net.*;

public class TigerZoneClient {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 5) {
            System.err.println(
                "Usage: java TigerZoneClient <host name> <port number> <tournament password> <username> <password>");
           return;
        }

		System.err.println("Client opened");

		String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        String tournamentPassword = args[2];
        String username = args[3];
        String password = args[4];

        try (
            Socket tzSocket = new Socket(hostName, portNumber);
            PrintWriter networkOut = new PrintWriter(tzSocket.getOutputStream(), true);
            BufferedReader networkIn = new BufferedReader(new InputStreamReader(tzSocket.getInputStream()));
        ) {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String[] serverText = null;
            int totalNumberOfRounds = 0;
            boolean isVerified = false;
            boolean isWaiting = true;
            boolean isPlaying = true;
            // Getting verified by server
            while (!isVerified) 
            {
           		fromServer = networkIn.readLine();
           		System.out.println("Server: " + fromServer);
           		if(fromServer.equals("THIS IS SPARTA!")){
           			networkOut.println("JOIN " + tournamentPassword);
           		}
           		else if (fromServer.equals("HELLO!")){
           			isVerified = true;
           			networkOut.println("I AM " + username + " " + password);
           		}
           	}

			fromServer = networkIn.readLine();
			System.out.println("Server: " + fromServer);
			serverText = fromServer.split(" ");
			String pid = serverText[1];
            // Find out how many challenges will be played
            fromServer = networkIn.readLine();
       		serverText = fromServer.split(" ");
       		if(serverText[0].equalsIgnoreCase("NEW")){
       			totalNumberOfRounds = Integer.parseInt(serverText[6]);
       		}
       		int currentRound = 0;
       		while(currentRound < totalNumberOfRounds)
       		{
       		NetworkAdapter netAdapter = new NetworkAdapter();
				netAdapter.pid = pid;
            // Getting game info - Player ID - Starting Tile - Order of Tiles 
       			while(isWaiting){
       				fromServer = networkIn.readLine();
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
       				fromServer = networkIn.readLine();
       				System.out.println("Server: " + fromServer);
       				serverText = fromServer.split(" ");
       				switch(serverText[0]){
       					case "MAKE":
       						String returnMessage = netAdapter.parseMakeMove(fromServer);
							networkOut.println(returnMessage);

							//AI should probably call this method and client gets the string in another method
       						//String makeMove = netAdapter.sendMove(messageStatus, gid, tileID, x, y, orientation, zone);
       						//System.out just lets us know what we're sending.
       						//System.out.println("Client: " + makeMove);
       						// Sends move to Server
       						//networkOut.println(makeMove);
       						break;
       				
       					case "GAME":
       						//TODO: Update gameboard for AI for both players move
       						netAdapter.parseUpdateGameBoard(fromServer);
       						if(serverText[6].equalsIgnoreCase("FORFEITED:")){
       							gamesEnded++;
       						}
       						break;
       				
       					case "END":
       						netAdapter.endGame();
       						gamesEnded++;
       						break;
       				}
       				
   					if(gamesEnded >= 2){
   						// After Endgame / forfeited has happened twice, exit loop
   						isPlaying = false;
   					}
       			}
				// if its only 1 challenge end, if not go back to top for 2 or more challenges
       			currentRound++;
       			}
        	} 
        
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
        }

		System.err.println("Client shutting down");

	}
}
