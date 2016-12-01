import static org.junit.Assert.*;

import org.junit.Test;

public class TigerTest {
	
	@Test
	public void testTiger() throws Exception {
		Tiger m = new Tiger();
		assertEquals(-1, m.owner);
		assertTrue(m.location.isEqual(new Location(-1,-1)));
		assertEquals(-1, m.status);
	}

	@Test
	public void testTigerInt() throws Exception {
		Tiger m = new Tiger(1, 1);
		assertEquals(1, m.owner);
		assertTrue(m.location.isEqual(new Location(-1,-1)));
		assertEquals(0, m.status);
	}

	@Test
	public void testUpdateLocation() throws Exception {
		Tiger m = new Tiger(1, 1);
		m.updateLocation(new Location(1,1));
		assertTrue(m.location.isEqual(new Location(1,1)));
	}

	@Test
	public void testUpdateStatus() throws Exception {
		Tiger m = new Tiger(1, 1);
		m.setStatus(1);
		assertEquals(1, m.status);
	}

}
