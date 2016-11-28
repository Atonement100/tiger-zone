import java.net.*;
import java.io.*;

public class TigerZoneServer {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 1) {
            System.err.println("Usage: java TigerZoneServer <port number>");
          	return;
        }

        System.out.println("Server started");

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
            outputLine = "NEW CHALLENGE 1 YOU WILL PLAY 3 MATCHES";
            out.println(outputLine);
            int i = 0;
            while(i < 3){
            TigerZoneProtocol tzProtocol = new TigerZoneProtocol();
            outputLine = "BEGIN ROUND 1 OF 3";
            out.println(outputLine);
            outputLine = "YOUR OPPONENT IS PLAYER Blue";
            out.println(outputLine);
            outputLine = "STARTING TILE IS TLTJ- AT 0 0 0";
            out.println(outputLine);
            outputLine = tzProtocol.StartGame();
            out.println(outputLine);
            outputLine = "MATCH BEGINS IN 15 SECONDS";
            out.println(outputLine);
            
           /* try {
                Thread.sleep(15000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            } */
            
            boolean isGamePlaying = true;
            while(isGamePlaying)
            {
            	outputLine = tzProtocol.NotifyPlayer();
            	out.println(outputLine);
            	if(outputLine.equals("END OF ROUND"))
            		isGamePlaying = false;
            	else{
            	inputLine = in.readLine();
            	}
            	outputLine = tzProtocol.SendGameAMove(inputLine);
            	out.println(outputLine);
            	outputLine = tzProtocol.SendGameBMove(inputLine);
            	out.println(outputLine);
            			
            }i++;
            outputLine = "PLEASE WAIT FOR NEXT CHALLENGE TO BEGIN";
            out.println(outputLine);
            }
            outputLine = "THANK YOU FOR PLAYING! GOODBYE ";
            out.println(outputLine);
            
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }

        System.out.println("Server shutting down");

    }
}
