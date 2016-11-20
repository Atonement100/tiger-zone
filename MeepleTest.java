import static org.junit.Assert.*;

import org.junit.Test;

public class MeepleTest {
	
	@Test
	public void testMeeple() throws Exception {
		Meeple m = new Meeple();
		assertEquals(-1, m.owner);
		assertTrue(m.location.isEqual(new Location(-1,-1)));
		assertEquals(-1, m.status);
	}

	@Test
	public void testMeepleInt() throws Exception {
		Meeple m = new Meeple(1, 1);
		assertEquals(1, m.owner);
		assertTrue(m.location.isEqual(new Location(-1,-1)));
		assertEquals(0, m.status);
	}

	@Test
	public void testUpdateLocation() throws Exception {
		Meeple m = new Meeple(1, 1);
		m.updateLocation(new Location(1,1));
		assertTrue(m.location.isEqual(new Location(1,1)));
	}

	@Test
	public void testUpdateStatus() throws Exception {
		Meeple m = new Meeple(1, 1);
		m.setStatus(1);
		assertEquals(1, m.status);
	}

}
