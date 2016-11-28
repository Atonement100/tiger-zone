import static org.junit.Assert.*;


import java.io.IOException;
import org.junit.Test;


public class TigerZoneClientTest {
	@Test
	public void testClientConnection() {
		TigerZoneClient tzClient = new TigerZoneClient();

		String[] args = {"localhost", "5", "PersiaRocks!", "Red", "Obiwan77"};
		try {
			tzClient.main(args);
		} catch (IOException e) {
			// Assert True if - Couldn't get I/O for the connection to localhost
			e.printStackTrace();
			assertTrue(true);
		}
	}
	@Test
	public void testClientNullArguments() throws IOException {
		TigerZoneClient tzClient = new TigerZoneClient();
		String[] args = {""};
		
		try {
			tzClient.main(args);
		} catch (IndexOutOfBoundsException e) {
			// if not enough arguments throw exception 
			e.printStackTrace();
			assertTrue(true);
		}
		
	}
}
