import static org.junit.Assert.*;
import org.junit.Test;

public class NetworkAdapterTest {
	@Test
	public void testSendMoveNoTiger(){
	NetworkAdapter networkAdapter = new NetworkAdapter();
	String sendMove = networkAdapter.sendMove(0, "A", "A", 1, 0, 90, -1);
	String expected = "GAME A PLACE JJJJ- AT 1 0 90 NONE";
	assertEquals(expected, sendMove);	
	}
	@Test
	public void testSendMoveCrocodile(){
		NetworkAdapter networkAdapter = new NetworkAdapter();
		String sendMove = networkAdapter.sendMove(1, "B", "E", 5, 5, 180, -1);
		String expected = "GAME B PLACE TJTJ- AT 5 5 180 CROCODILE";
		assertEquals(expected, sendMove);
	}
	@Test
	public void testSendMoveTiger(){
		NetworkAdapter networkAdapter = new NetworkAdapter();
		String sendMove = networkAdapter.sendMove(2, "A", "U", 3, 3, 270, 6);
		String expected = "GAME A PLACE TLLL- AT 3 3 270 TIGER 6";
		assertEquals(expected, sendMove);
	}
	@Test 
	public void testSendPass(){
		NetworkAdapter networkAdapter = new NetworkAdapter();
		String sendMove = networkAdapter.sendMove(3, "A", "Z", 1, 1, 0, -1);
		String expected = "GAME A TILE LJTJ- UNPLACEABLE PASS";
		assertEquals(expected, sendMove);
	}
	@Test 
	public void testUnplaceableRetrieveTiger(){
		NetworkAdapter networkAdapter = new NetworkAdapter();
		String sendMove = networkAdapter.sendMove(4, "A", "D", -2, 1, 0, -1);
		String expected = "GAME A TILE TTTT- UNPLACEABLE RETRIEVE TIGER AT -2 1";
		assertEquals(expected, sendMove);
	}
	@Test 
	public void testUnplaceableAddTiger(){
		NetworkAdapter networkAdapter = new NetworkAdapter();
		String sendMove = networkAdapter.sendMove(5, "B", "S", 3, -4, 0, -1);
		String expected = "GAME B TILE TLTJ- UNPLACEABLE ADD ANOTHER TIGER TO 3 -4";
		assertEquals(expected, sendMove);
	}
}
