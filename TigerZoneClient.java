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
			String fromServer;
			String[] serverText = null;
			boolean isWaiting = true;
			boolean isPlaying = true;
			boolean startingMatch = true;
			// Getting verified by server

			fromServer = networkIn.readLine();
			System.out.println("Server: " + fromServer);
			if(fromServer.equals("THIS IS SPARTA!")){
				networkOut.println("JOIN " + tournamentPassword);
			}
			fromServer = networkIn.readLine();
			System.out.println("Server: " + fromServer);
			if (fromServer.equals("HELLO!")){
				networkOut.println("I AM " + username + " " + password);
			}

			while(startingMatch){
				NetworkAdapter netAdapter = new NetworkAdapter();
				// Getting game info - Player ID - Starting Tile - Order of Tiles
				isWaiting = true;
				while(isWaiting){

					fromServer = networkIn.readLine();
					if (null == fromServer) continue; 
					serverText = fromServer.split(" ");
					System.out.println("Server: " + fromServer);
					
					if(serverText[0].equals("THANK")){
						startingMatch = false;
						System.exit(0);
					}
					
					if(serverText[0].equals("MATCH"))
					{
						isWaiting = false;
					}
					
					else {
						netAdapter.parseMatchProtocol(fromServer);
					}
				}
				// Where we start Receiving notification to send move / Send our moves.
				isPlaying = true;
				while(isPlaying){
					fromServer = networkIn.readLine();

					System.out.println("Server: " + fromServer);
					serverText = fromServer.split(" ");
					switch(serverText[0]){
						case "MAKE":
							String returnMessage = netAdapter.parseMakeMove(fromServer);
							System.out.println(returnMessage);
							networkOut.println(returnMessage);
							break;

						case "GAME":
							if(serverText[6].equals("FORFEITED:")){
							}
							else if(serverText[2].equals("OVER")){
							}
							else{
								netAdapter.parseUpdateGameBoard(fromServer);
							}
							break;

						case "END":
							isPlaying = false;
							break;
					}
				}
			}
		}


		catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " +
					hostName);
		}

		System.err.println("Client shutting down");

	}
}