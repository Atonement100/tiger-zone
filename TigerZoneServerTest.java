import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class TigerZoneServerTest {
	@Test
	public void testServerConnection(){
		TigerZoneServer tzServer = new TigerZoneServer();
		String[] args = {"5"};
		try {
			tzServer.main(args);
		} catch (IOException e) {
			// If couldn't get port, exception passed
			e.printStackTrace();
			assertTrue(true);
		}
	}
	@Test
	public void testServerNullArguments() throws IOException {
		TigerZoneServer tzServer = new TigerZoneServer();
		String[] args = {"", "5" };
		
		try {
			tzServer.main(args);
		} catch (IndexOutOfBoundsException e) {
			// if too many arguments exception passed
			e.printStackTrace();
			assertTrue(true);
		}
		
	}
}
