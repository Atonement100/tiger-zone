import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TileTest {
    Tile allTrueTile = new Tile(true, true, true, 1, new Integer[]{0, 1, 2, 2}, 'T');
    Tile allFalseTile = new Tile(false, false, false, 2, new Integer[]{2, 1, 0, 1}, 'F');

    @Test
    public void ctorTrueTileWorks() throws Exception {
        assertTrue(allTrueTile.hasDen);
        assertTrue(allTrueTile.lakesAreIndependent);
        assertTrue(allTrueTile.trailsEnd);
        assertTrue(allTrueTile.animalType == 1);
    }

    @Test
    public void ctorFalseTileWorks() throws Exception {
        assertFalse(allTrueTile.animalType == 2);
        assertFalse(allFalseTile.hasDen);
        assertFalse(allFalseTile.lakesAreIndependent);
        assertFalse(allFalseTile.trailsEnd);
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

    @Test
    public void nodeConnectionTileTypeK() throws Exception {
        Tile tile = new Tile(false, false, false, 0, new Integer[]{2,0,2,0}, 'K');
        Edge[] edges = tile.edges;

        assertSame(edges[0].nodes[0].neighbors.get(0), edges[2].nodes[2]);
        assertSame(edges[0].nodes[0].neighbors.get(1), edges[3].nodes[2]);
        assertSame(edges[0].nodes[1].neighbors.get(0), edges[2].nodes[1]);
        assertSame(edges[0].nodes[2].neighbors.get(0), edges[2].nodes[0]);
        assertSame(edges[0].nodes[2].neighbors.get(1), edges[1].nodes[0]);

        assertSame(edges[1].nodes[0].neighbors.get(0), edges[0].nodes[2]);
        assertSame(edges[1].nodes[0].neighbors.get(1), edges[1].nodes[1]);
        assertSame(edges[1].nodes[1].neighbors.get(0), edges[1].nodes[0]);
        assertSame(edges[1].nodes[1].neighbors.get(1), edges[1].nodes[2]);
        assertSame(edges[1].nodes[2].neighbors.get(0), edges[1].nodes[1]);
        assertSame(edges[1].nodes[2].neighbors.get(1), edges[2].nodes[0]);

        assertSame(edges[2].nodes[0].neighbors.get(0), edges[0].nodes[2]);
        assertSame(edges[2].nodes[0].neighbors.get(1), edges[1].nodes[2]);
        assertSame(edges[2].nodes[1].neighbors.get(0), edges[0].nodes[1]);
        assertSame(edges[2].nodes[2].neighbors.get(0), edges[0].nodes[0]);
        assertSame(edges[2].nodes[2].neighbors.get(1), edges[3].nodes[0]);

        assertSame(edges[3].nodes[0].neighbors.get(0), edges[2].nodes[2]);
        assertSame(edges[3].nodes[0].neighbors.get(1), edges[3].nodes[1]);
        assertSame(edges[3].nodes[1].neighbors.get(0), edges[3].nodes[0]);
        assertSame(edges[3].nodes[1].neighbors.get(1), edges[3].nodes[2]);
        assertSame(edges[3].nodes[2].neighbors.get(0), edges[3].nodes[1]);
        assertSame(edges[3].nodes[2].neighbors.get(1), edges[0].nodes[0]);
    }

    /* These may safely be deleted, they were used only to test java functionality
    @Test
    public void copyConstructor() throws Exception {
        Tile tileToCopy = new Tile(false, false, false, 0, new Integer[]{0, 1, 2, 2}, 'K');

        rotate(tileToCopy, 1);

        assertArrayEquals(new Integer[]{0, 1, 2, 2}, tileToCopy.edgeValues);
    }

    void rotate(Tile tile, int rotations){
        tile.rotateClockwise(rotations);
    }
    */
}