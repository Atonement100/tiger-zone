import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TileTest {
    Tile allTrueTile = new Tile(true, true, true, 1, new Integer[]{0, 1, 2, 2}, 'T');
    Tile allFalseTile = new Tile(false, false, false, 2, new Integer[]{2, 1, 0, 1}, 'F');

    @Test
    public void ctorTrueTileWorks() throws Exception {
        assertTrue(allTrueTile.hasMonastery);
        assertTrue(allTrueTile.citiesAreIndependent);
        assertTrue(allTrueTile.roadsEnd);
        assertTrue(allTrueTile.animalType == 1);
    }

    @Test
    public void ctorFalseTileWorks() throws Exception {
        assertFalse(allTrueTile.animalType == 2);
        assertFalse(allFalseTile.hasMonastery);
        assertFalse(allFalseTile.citiesAreIndependent);
        assertFalse(allFalseTile.roadsEnd);
    }

    @Test
    public void ctorEdgesWorks() throws Exception {
        assertArrayEquals(allTrueTile.edgeValues, new Integer[]{0,1,2,2});
        assertArrayEquals(allFalseTile.edgeValues, new Integer []{2,1,0,1});
    }

    @Test
    public void ctorTileTypeWorks() throws Exception {
        assertEquals(allFalseTile.tileType, 'F');
        assertNotEquals(allFalseTile.tileType, 'T');
    }

    @Test
    public void isEqual() throws Exception {
        assertFalse(allFalseTile.isEqual(allTrueTile));
        assertTrue(allTrueTile.isEqual(allTrueTile));
    }

    @Test
    public void rotateClockwise1() throws Exception {
        allTrueTile.rotateClockwise(1);
        assertArrayEquals(allTrueTile.edgeValues, new Integer[]{2,0,1,2});
    }

    @Test
    public void rotateClockwise2() throws Exception {
        allTrueTile.rotateClockwise(2);
        assertArrayEquals(allTrueTile.edgeValues, new Integer[]{2,2,0,1});
    }

    @Test
    public void rotateClockwise3() throws Exception {
        allTrueTile.rotateClockwise(3);
        assertArrayEquals(allTrueTile.edgeValues, new Integer[]{1,2,2,0});
    }

    @Test
    public void rotateClockwise4() throws Exception {
        allTrueTile.rotateClockwise(4);
        assertArrayEquals(allTrueTile.edgeValues, new Integer[]{0,1,2,2});
    }

    @Test
    public void rotateClockwise5() throws Exception {
        Tile trueCopy = allTrueTile;
        allTrueTile.rotateClockwise(5);
        trueCopy.rotateClockwise(1);
        assertArrayEquals(allTrueTile.edgeValues, trueCopy.edgeValues);
    }


    @Test
    public void rotateAntiClockwise1() throws Exception {
        Tile trueCopy = allTrueTile;
        allTrueTile.rotateClockwise(-1);
        trueCopy.rotateClockwise(3);
        assertArrayEquals(allTrueTile.edgeValues, trueCopy.edgeValues);
    }

    @Test
    public void rotateAntiClockwise3() throws Exception {
        Tile trueCopy = allTrueTile;
        allTrueTile.rotateClockwise(-3);
        trueCopy.rotateClockwise(1);
        assertArrayEquals(allTrueTile.edgeValues, trueCopy.edgeValues);
    }

}