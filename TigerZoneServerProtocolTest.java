import static org.junit.Assert.*;

import org.junit.Test;

public class TigerZoneServerProtocolTest {
	@Test
	public void testAuthenticationProtocol(){
		TigerZoneProtocol tzProtocol = new TigerZoneProtocol();
		String tournamentPassword = null;
		
		String sparta = tzProtocol.VerifyAuthentication(tournamentPassword);
		assertEquals("THIS IS SPARTA!", sparta);
		tournamentPassword = "JOIN PersiaRocks!";
		String hello = tzProtocol.VerifyAuthentication(tournamentPassword);
		assertEquals("HELLO!", hello);
	}
	@Test
	public void testAuthenticationFailure(){
		TigerZoneProtocol tzProtocol = new TigerZoneProtocol();
		String tournamentPassword = null;
		String sparta = tzProtocol.VerifyAuthentication(tournamentPassword);
		assertEquals("THIS IS SPARTA!", sparta);
		tournamentPassword = "JOIN ILikeCats!";
		String wrong = tzProtocol.VerifyAuthentication(tournamentPassword);
		assertEquals("Wrong password, try \"PersiaRocks!\"! ", wrong);
	}
	@Test
	public void testStartGame(){
		TigerZoneProtocol tzProtocol = new TigerZoneProtocol();
		String getTiles = tzProtocol.StartGame();
		String[] remainingTiles  = getTiles.split(" ");
		assertEquals("76", remainingTiles[2]);
	}
	@Test
	public void testNotifyPlayer(){
		TigerZoneProtocol tzProtocol = new TigerZoneProtocol();
		String notification = tzProtocol.NotifyPlayer();
		String expected = "MAKE YOUR MOVE IN GAME A WITHIN 1 SECOND: MOVE 1 PLACE JJJJ-";
		assertEquals(expected, notification);
	}
	@Test
	public void testSendGameAMove(){
		TigerZoneProtocol tzProtocol = new TigerZoneProtocol();
		String gameAMove = tzProtocol.SendGameAMove("GAME A PLACE JJJJ- AT 1 1 90 TIGER 5");
		String expected = "GAME A MOVE 1 PLAYER Red PLACED JJJJ- AT 1 1 90 TIGER 5";
		assertEquals(expected, gameAMove);
	}
	@Test 
	public void testSendGameBMove(){
		TigerZoneProtocol tzProtocol = new TigerZoneProtocol();
		String gameBMove = tzProtocol.SendGameBMove("GAME A PLACE JJJJ- AT 1 1 90 TIGER 5");
		String expected = "GAME B MOVE 1 PLAYER Blue PLACED JJJJ- AT 1 1 90 TIGER 5";
		assertEquals(expected, gameBMove);
	}
}
