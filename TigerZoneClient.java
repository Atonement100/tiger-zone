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
            String[] parseText = null;
            boolean isVerified = false;
            boolean isWaiting = true;
            boolean isPlaying = true;
            while (!isVerified) 
            {
           		fromServer = in.readLine();
           		System.out.println("Server: " + fromServer);
           		if (fromServer.equals("HELLO!"))
           			isVerified = true;
               
           		fromUser = stdIn.readLine();
           		if (fromUser != null) 
           		{
           			out.println(fromUser);
           		}
           	}
            
            while(isWaiting)
            {
           		fromServer = in.readLine();
           		parseText = fromServer.split(" ");
           		String firstWord = parseText[0];
           		System.out.println("Server: " + fromServer);
           		if(firstWord.equals("REMAINING"))
           		{
           			// TODO: ADD TILES TO AI LIST
           		}
           		else if(firstWord.equals("MATCH"))
           		{
           			isWaiting = false;
           		}
            }
            
            while(isPlaying)
            {
           		fromServer = in.readLine();
           		System.out.println("Server: " + fromServer);
           		// TODO: SEND MOVES
           		fromUser = stdIn.readLine();
           		if (fromUser != null) 
           		{
           			out.println(fromUser);
           		}
            }
            
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
}
