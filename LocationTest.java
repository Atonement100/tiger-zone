import static org.junit.Assert.*;

import org.junit.Test;

public class LocationTest {

	@Test
	public void testLocation() throws Exception {
		Location l = new Location(1,1);
		assertEquals(1, l.Row);
		assertEquals(1, l.Col);
	}

}
