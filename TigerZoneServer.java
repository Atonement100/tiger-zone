import java.net.*;
import java.io.*;

public class TigerZoneServer {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try ( 
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) {
        
            String inputLine, outputLine;
            
            // Initiate conversation with client
            TigerZoneProtocol tzp = new TigerZoneProtocol();
            outputLine = tzp.VerifyAuthentication(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = tzp.VerifyAuthentication(inputLine);
                out.println(outputLine);
                if (outputLine.equals("WELCOME Red PLEASE WAIT FOR THE NEXT CHALLENGE"))
                    break;
            }
            outputLine = "NEW CHALLENGE 1 Red YOU WILL PLAY 1 MATCH";
            out.println(outputLine);
            outputLine = "BEGIN ROUND 1 OF 1";
            out.println(outputLine);
            outputLine = "YOUR OPPONENT IS PLAYER Blue";
            out.println(outputLine);
            outputLine = "STARTING TILE IS TLTJ- AT 0 0 0";
            out.println(outputLine);
            outputLine = tzp.StartGame();
            out.println(outputLine);
            outputLine = "MATCH BEGINS IN 15 SECONDS";
            out.println(outputLine);
            boolean isGamePlaying = true;
            while(isGamePlaying)
            {
            	outputLine = tzp.NotifyPlayer();
            	out.println(outputLine);
            	if(outputLine.equals("GAME OVER"))
            		isGamePlaying = false;
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
