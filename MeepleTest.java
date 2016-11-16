import static org.junit.Assert.*;

import org.junit.Test;

public class MeepleTest {
	
	@Test
	public void testMeeple() throws Exception {
		Meeple m = new Meeple();
		assertEquals(-1, m.owner);
		assertEquals(-1, m.coordX);
		assertEquals(-1, m.coordY);
		assertEquals(-1, m.status);
	}

	@Test
	public void testMeepleInt() throws Exception {
		Meeple m = new Meeple(1);
		assertEquals(1, m.owner);
		assertEquals(-1, m.coordX);
		assertEquals(-1, m.coordY);
		assertEquals(0, m.status);
	}

	@Test
	public void testUpdateLocation() throws Exception {
		Meeple m = new Meeple(1);
		m.updateLocation(1, 1);
		assertEquals(1, m.coordX);
		assertEquals(1, m.coordY);
	}

	@Test
	public void testUpdateStatus() throws Exception {
		Meeple m = new Meeple(1);
		m.updateStatus(1);
		assertEquals(1, m.status);
	}

}
