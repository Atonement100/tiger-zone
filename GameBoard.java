import java.util.ArrayDeque;

public class GameBoard {
    Tile[][] board;

    private static final int NUM_PLAYERS = GameController.NUM_PLAYERS;
    private static final int NUM_MEEPLES = GameController.NUM_MEEPLES;
    private static int[] dx = {0,-1,0,1}; // W, N, E, S
    private static int[] dy = {-1,0,1,0};

    private Meeple[][] playerMeeples = new Meeple[NUM_PLAYERS][NUM_MEEPLES];

    GameBoard(int numRows, int numCols){
        board = new Tile[numRows][numCols];

        for (int playerIndex = 0; playerIndex < NUM_PLAYERS; playerIndex++){
            for (int meepleIndex = 0; meepleIndex < NUM_MEEPLES; meepleIndex++){
                playerMeeples[playerIndex][meepleIndex] = new Meeple(playerIndex);
            }
        }
    }

    Location getBoardDimensions(){
        return new Location(board.length, board[board.length-1].length);
    }

    void placeTile(Tile tileToPlace, Location targetLocation, int rotations){
        //NEED TO ADD WHEN PLACING A TILE A CHECK TO SEE IF A MEEPLE HAS BEEN PLACED ON THE FEATURE BEING CONNECTED
        //IF A MEEPLE HAS BEEN PLACED ON THE CONNECTING FEATURE UPDATE THE TILE TO "HAS MEEPLE" AND ADD THE MEEPLE BEING PLACED
        //IF THERE ARE MULTIPLE MEEPLES BECAUSE OF CONNECTION NEED TO KEEP TRACK OF MEEPLE COUNT
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        tileToPlace.rotateClockwise(rotations);
        board[ targetLocation.Row ][ targetLocation.Col ] = tileToPlace;

        for(int direction = 0; direction < 4; direction++){
            Tile[] neighborTiles = getNeighboringTiles(targetLocation);

            if(neighborTiles[direction] == null) continue;			//if there is no tile, no check is necessary, continue

            for (int nodeIndex = 0; nodeIndex < tileToPlace.edges[direction].nodes.length; nodeIndex++){
                tileToPlace.edges[direction].nodes[nodeIndex].neighbors.add(neighborTiles[direction].edges[(direction + 2) % 4].nodes[2-nodeIndex]);
                neighborTiles[direction].edges[(direction + 2) % 4].nodes[2-nodeIndex].neighbors.add(tileToPlace.edges[direction].nodes[nodeIndex]);
            }
        }

        updateMeepleInformationForConnectedNodes(targetLocation);
    }

    private void updateMeepleInformationForConnectedNodes(Location tileLocation){
        Tile tileToUpdate = board[tileLocation.Row][tileLocation.Col];

        ArrayDeque<Node> nodeQueue = new ArrayDeque<>();
        ArrayDeque<Node> visitedQueue = new ArrayDeque<>();
        for (Edge edge : tileToUpdate.edges) {
            for (Node node : edge.nodes){           //For each node...
                if (node.meeplePlacedInFeature) continue; //If this node already knows it has a meeple, continue

                /* So this looks kinda hacky, but it should actually be pretty efficient in practice.
                *  Visited queue only includes tiles that are not marked as having a meeple.
                *  If a node is known to have a meeple, its neighbors are not added to the nodeQueue.
                *  We can't break early from the loop to ensure that we get all connected unmarked nodes
                *  This is to cover the case where a tile placed connects two independent feature zones.
                */

                boolean shouldMarkVisited = false;

                nodeQueue.add(node);
                while (!nodeQueue.isEmpty()) {      //While there are some neighbors that have not been visited...
                    Node currNode = nodeQueue.removeFirst();
                    visitedQueue.add(currNode);
                    for (Node neighbor : currNode.neighbors) {
                        if (neighbor.meeplePlacedInFeature){
                            shouldMarkVisited = true;
                        }
                        else if (!visitedQueue.contains(neighbor)){     //Only add to nodequeue if we haven't seen it before
                            nodeQueue.add(neighbor);
                        }
                    }
                }

                if (shouldMarkVisited) {
                    for (Node visitedNode : visitedQueue) {
                        visitedNode.meeplePlacedInFeature = true;
                    }
                }
            }
        }
    }

    //places a meeple in the valid position
    void placeMeeple(Tile tileToPlace, Location targetLocation, int placement, int currentPlayer){
        int edge = placement / 3; //Nodes per edge
        int node = placement % 3;
        FeatureTypeEnum feature;

        //finds the feature that the meeple is being placed in
        if (placement == 12){
            feature = tileToPlace.middle.featureType;
        }
        else {
            feature = tileToPlace.edges[edge].nodes[node].featureType;
        }

        //finds next free meeple in array and updates that meeple's status and location values
        for(int meepleIndex = 0; meepleIndex < NUM_MEEPLES; meepleIndex++){
            if (playerMeeples[currentPlayer][meepleIndex].status == MeepleStatusEnum.onNone){
                //Places meeple on board and lets the node acknowledge it
                if(placement == 12) {
                    board[targetLocation.Row][targetLocation.Col].middle.meeplePlacedInFeature = true;
                    board[targetLocation.Row][targetLocation.Col].middle.meeple = playerMeeples[currentPlayer][meepleIndex];
                }
                else{
                    board[targetLocation.Row][targetLocation.Col].edges[edge].nodes[node].meeplePlacedInFeature = true;
                    board[targetLocation.Row][targetLocation.Col].edges[edge].nodes[node].meeple = playerMeeples[currentPlayer][meepleIndex];
                }

                //NEED TO PUT AN UPDATE ALL NODES IN THE FEATURE TO MEEPLEPLACEDINFEATURE = TRUE
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


                //Updates location and status of meeple
                playerMeeples[currentPlayer][meepleIndex].location.Row = targetLocation.Row;
                playerMeeples[currentPlayer][meepleIndex].location.Col = targetLocation.Col;
                playerMeeples[currentPlayer][meepleIndex].setStatus(feature.toInt()); // This processes the int value of the feature and sets it to the appropriate status :)

                break;
            }
        }
    }

    boolean verifyTilePlacement(Tile tileToPlace, Location targetLocation, int rotations){
        tileToPlace.rotateClockwise(rotations);

        int row = targetLocation.Row;
        int col = targetLocation.Col;

        int rowBoundary = board.length;
        int colBoundary = board[0].length;

        //****************************************************************************************
        //logic(simple) CHECKS

        if(row < 0 || row >= rowBoundary || col < 0 || col >= colBoundary){

            return false;			//placement out of bounds;
        }

        if(board[row][col] != null){
            return false;			//if tile exists at loc
        }


        //****************************************************************************************
        //game rules CHECKS

        //start out optimistic
        boolean isCompatible = true;
        //get all neighboring tiles

        boolean noNeighboringTile = false; //CHANGE THIS TO FALSE/TRUE FOR TESTING PURPOSES
        Tile[] neighborTiles = getNeighboringTiles(targetLocation);
        for (Tile nTile : neighborTiles){
            if (nTile != null){
                noNeighboringTile = false;
            }
        }
        if (noNeighboringTile) return false;

        //check compatibility with neighboring tiles
        for(int direction = 0; direction < 4; direction++){

            if (neighborTiles[direction] != null){
                if(!tileToPlace.edges[direction].isCompatible(neighborTiles[direction].edges[(direction + 2) % 4])){ //Could change 4 to constant - EDGES_PER_TILE. 2 is EDGES_PER_TILE / 2
                    isCompatible = false;
                    break;
                }
            }
        }

        return isCompatible;
    }

    //checks if a meeple can be placed at the spot indicated
    boolean verifyMeeplePlacement(Tile tileToPlace, int placement, int currentPlayer){
        if (placement < 0 || placement > 11){
            return (placement == 12 && tileToPlace.middle.featureType != FeatureTypeEnum.None); //It can be larger than 11 only if it is 12, which must also be a monastery placement
            //Monasteries are also not connected to anything, so they don't need to be verified for adjacency.
        }
        //initializes necessary values
        int edge = placement / 3;
        int node = placement % 3;

        //checks if there is already a meeple on the feature that the player is trying to place a meeple on
        if(tileToPlace.edges[edge].nodes[node].meeplePlacedInFeature){
            return false;
        }

        //checks if the player has any meeples to place
        for(int meepleIndex = 0; meepleIndex < NUM_MEEPLES; meepleIndex++){
            if (playerMeeples[currentPlayer][meepleIndex].getStatus() == MeepleStatusEnum.onNone){
                return true;
            }
        }

        return false;
    }

    private Tile[] getNeighboringTiles(Location tileLocation){
        int row = tileLocation.Row;
        int col = tileLocation.Col;

        Tile[] neighborTiles = new Tile[4];		//N, E, S, W
        for(int direction = 0; direction < 4; direction++){
            if(row + dx[direction] >= 0 && row + dx[direction] < board.length && col + dy[direction] >= 0 && col + dy[direction] < board[0].length){ //Checks within board boundary
                neighborTiles[direction] = board[row + dx[direction]][col + dy[direction]];
            }
        }

        return neighborTiles;
    }
}
