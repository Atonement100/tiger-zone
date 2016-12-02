import java.util.ArrayDeque;
import java.util.HashSet;

public class GameBoard {
	/*
	 * GameBoard stores all tiles and the board array of tiles
	 * Actually places tiles and tigers
	 * Updates status of tigers and holds number of tigers and crocodiles for each player
	 * 
	 */
    Tile[][] board;
    
    private static final int NUM_PLAYERS = GameController.NUM_PLAYERS;
    private static final int NUM_TIGERS = GameController.NUM_TIGERS;
    private static int[] dx = {0,-1,0,1}; // W, N, E, S
    private static int[] dy = {-1,0,1,0};
    private HashSet<Location> possibleTargets = new HashSet<>();
    
    Tiger[][] playerTigers = new Tiger[NUM_PLAYERS][NUM_TIGERS];
    
    GameBoard(int numRows, int numCols){
        board = new Tile[numRows][numCols];
        
        for (int playerIndex = 0; playerIndex < NUM_PLAYERS; playerIndex++){
            for (int tigerIndex = 0; tigerIndex < NUM_TIGERS; tigerIndex++){
                playerTigers[playerIndex][tigerIndex] = new Tiger(playerIndex, tigerIndex);
            }
        }
    }
    
    //Returns the size of the board
    Location getBoardDimensions(){
        return new Location(board.length, board[board.length-1].length);
    }
    
    //Places a tile on the board and makes connections between the tiles
    void placeTile(Tile tileToPlace, Location targetLocation, int clockwiseRotations){
        tileToPlace.rotateClockwise(clockwiseRotations);
        board[ targetLocation.Row ][ targetLocation.Col ] = tileToPlace;
        
        for(int direction = 0; direction < 4; direction++){
            Tile[] neighborTiles = getNeighboringTiles(targetLocation);
            
            if(neighborTiles[direction] == null) continue;			//if there is no tile, no check is necessary, continue
            
            for (int nodeIndex = 0; nodeIndex < tileToPlace.edges[direction].nodes.length; nodeIndex++){
                tileToPlace.edges[direction].nodes[nodeIndex].neighbors.add(neighborTiles[direction].edges[(direction + 2) % 4].nodes[2-nodeIndex]);
                neighborTiles[direction].edges[(direction + 2) % 4].nodes[2-nodeIndex].neighbors.add(tileToPlace.edges[direction].nodes[nodeIndex]);
            }
        }
        
        updateTigerInfoForNewTile(targetLocation);
        updateAvailablePlacements(targetLocation);
    }

    //Places a temporary tile that will be removed for AI checking
    void placeTemporaryTile(Tile tileToPlace, Location targetLocation, int rotations){
        //This previously differed from the original placetile function, leave in place in the event things need to be different again
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

        updateTigerInfoForTemporaryTile(targetLocation);
    }

    //Removes the temporary tile that was placed for AI checking
    void removeTemporaryTile(Location targetLocation){
        Tile tileToRemove = board[targetLocation.Row][targetLocation.Col];

        for(int direction = 0; direction < 4; direction++){
            Tile[] neighborTiles = getNeighboringTiles(targetLocation);

            if(neighborTiles[direction] == null) continue;			//if there is no tile, no check is necessary, continue

            for (int nodeIndex = 0; nodeIndex < tileToRemove.edges[direction].nodes.length; nodeIndex++){
                tileToRemove.edges[direction].nodes[nodeIndex].neighbors.remove(neighborTiles[direction].edges[(direction + 2) % 4].nodes[2-nodeIndex]);
                neighborTiles[direction].edges[(direction + 2) % 4].nodes[2-nodeIndex].neighbors.remove(tileToRemove.edges[direction].nodes[nodeIndex]);
            }
        }

        for (Edge edge: tileToRemove.edges){
            for (Node node : edge.nodes){
                node.tigerPlacedInFeature = false;
            }
        }

        board[targetLocation.Row][targetLocation.Col] = null;
        tileToRemove.rotateAntiClockwise(tileToRemove.rotations);
    }
    
    //Updates the tiger information on a new tile to say that all neighbors of the feature have a tiger on them
    private void updateTigerInfoForNewTile(Location tileLocation){
        Tile tileToUpdate = board[tileLocation.Row][tileLocation.Col];

        for (Edge edge : tileToUpdate.edges) {
            for (Node node : edge.nodes){           //For each node...
                if (node.tigerPlacedInFeature) continue; //If this node already knows it has a tiger, continue
                
                /* So this looks kinda hacky, but it should actually be pretty efficient in practice.
                 *  Visited queue only includes tiles that are not marked as having a tiger.
                 *  If a node is known to have a tiger, its neighbors are not added to the nodeQueue.
                 *  We can't break early from the loop to ensure that we get all connected unmarked nodes
                 *  This is to cover the case where a tile placed connects two independent feature zones.
                 */
                ArrayDeque<Node> nodeQueue = new ArrayDeque<>();
                ArrayDeque<Node> visitedQueue = new ArrayDeque<>();
                
                boolean shouldMarkVisited = false;
                
                nodeQueue.add(node);
                while (!nodeQueue.isEmpty()) {      //While there are some neighbors that have not been visited...
                    Node currNode = nodeQueue.removeFirst();
                    visitedQueue.add(currNode);
                    for (Node neighbor : currNode.neighbors) {
                        if (currNode.featureType.isSameFeature(neighbor.featureType)) {
                            if (neighbor.tigerPlacedInFeature) {
                                shouldMarkVisited = true;
                            } else if (!visitedQueue.contains(neighbor)  && neighbor.owningTileId == currNode.owningTileId) {     //Only add to nodequeue if we haven't seen it before
                                nodeQueue.add(neighbor);
                            }
                        }
                    }
                }
                
                if (shouldMarkVisited) {
                    for (Node visitedNode : visitedQueue) {
                        visitedNode.tigerPlacedInFeature = true;
                    }
                }
            }
        }
    }

    //Updates tiger info for a temporary tile that will be removed
    private void updateTigerInfoForTemporaryTile(Location tileLocation){
        Tile tileToUpdate = board[tileLocation.Row][tileLocation.Col];

        for (Edge edge : tileToUpdate.edges) {
            for (Node node : edge.nodes){           //For each node...
                if (node.tigerPlacedInFeature) continue; //If this node already knows it has a tiger, continue

                ArrayDeque<Node> nodeQueue = new ArrayDeque<>();
                ArrayDeque<Node> visitedQueue = new ArrayDeque<>();

                boolean shouldMarkVisited = false;

                nodeQueue.add(node);
                while (!nodeQueue.isEmpty()) {      //While there are some neighbors that have not been visited...
                    Node currNode = nodeQueue.removeFirst();
                    visitedQueue.add(currNode);
                    for (Node neighbor : currNode.neighbors) {
                        if (currNode.featureType.isSameFeature(neighbor.featureType)) {
                            if (neighbor.tigerPlacedInFeature) {
                                shouldMarkVisited = true;
                            } else if (!visitedQueue.contains(neighbor) && neighbor.owningTileId == currNode.owningTileId) {     //Only add to nodequeue if we haven't seen it before
                                nodeQueue.add(neighbor);
                            }
                        }
                    }
                }

                if (shouldMarkVisited) {
                    for (Node visitedNode : visitedQueue) {
                        visitedNode.tigerPlacedInFeature = true;
                    }
                }
            }
        }
    }
    
    //Updates features in tiles to say they have a tiger on them
    private void updateTigerInfoForNewTiger(Location targetLocation, int tigerLocation){
        if (tigerLocation == 12) return;
        
        int edge = tigerLocation / 3; //Nodes per edge
        int node = tigerLocation % 3;
        Tile tile = board[targetLocation.Row][targetLocation.Col];
        
        if (tile == null) return;
        
        ArrayDeque<Node> nodeQueue = new ArrayDeque<>();
        ArrayDeque<Node> visitedQueue = new ArrayDeque<>();
        nodeQueue.add(tile.edges[edge].nodes[node]);
        while (!nodeQueue.isEmpty()){
            Node currNode = nodeQueue.removeFirst();
            visitedQueue.add(currNode);
            for (Node neighbor : currNode.neighbors){
                if (currNode.featureType.isSameFeature(neighbor.featureType) && !neighbor.tigerPlacedInFeature && !visitedQueue.contains(neighbor)){
                    neighbor.tigerPlacedInFeature = true;
                    nodeQueue.add(neighbor);
                }
            }
        }
    }
    
    //Removes a tiger from a board and updates its status
    void freeTiger(int ownerID, int tigerID){
        this.playerTigers[ownerID][tigerID].status = TigerStatusEnum.onNone;
        this.playerTigers[ownerID][tigerID].location = new Location(-1,-1);
    }
    
    //places a tiger in the valid position
    void placeTiger(Tile tileToPlace, Location targetLocation, int placement, int currentPlayer){
        if (placement < 0 || placement > 12) return;
        
        int edge = placement / 3; //Nodes per edge
        int node = placement % 3;
        FeatureTypeEnum feature;
        
        //finds the feature that the tiger is being placed in
        if (placement == 12){
            feature = tileToPlace.middle.featureType;
        }
        else {
            feature = tileToPlace.edges[edge].nodes[node].featureType;
        }
        
        //finds next free tiger in array and updates that tiger's status and location values
        for(int tigerIndex = 0; tigerIndex < NUM_TIGERS; tigerIndex++){
            if (playerTigers[currentPlayer][tigerIndex].status == TigerStatusEnum.onNone){
                //Places tiger on board and lets the node acknowledge it
                if(placement == 12) {
                    board[targetLocation.Row][targetLocation.Col].middle.tigerPlacedInFeature = true;
                    board[targetLocation.Row][targetLocation.Col].middle.tiger = playerTigers[currentPlayer][tigerIndex];
                }
                else{
                    //if placement is on an inner wall, change the placement to the center node on the edge
                    if(tileToPlace.edges[edge].nodes[node].featureType == FeatureTypeEnum.InnerShore){
                        node = 1;
                    }
                    board[targetLocation.Row][targetLocation.Col].edges[edge].nodes[node].tigerPlacedInFeature = true;
                    board[targetLocation.Row][targetLocation.Col].edges[edge].nodes[node].tiger = playerTigers[currentPlayer][tigerIndex];
                    updateTigerInfoForNewTiger(targetLocation, placement);    //No need to propagate changes for monastery, only do it here
                }

                //Updates location and status of tiger
                playerTigers[currentPlayer][tigerIndex].location.Row = targetLocation.Row;
                playerTigers[currentPlayer][tigerIndex].location.Col = targetLocation.Col;
                playerTigers[currentPlayer][tigerIndex].setStatus(feature.toInt()); // This processes the int value of the feature and sets it to the appropriate status :)
                
                break;
            }
        }
    }

    //Checks that a tile placement is legal
    boolean verifyTilePlacement(Tile tileToPlace, Location targetLocation, int rotations){
        tileToPlace.rotateClockwise(rotations);

        int row = targetLocation.Row;
        int col = targetLocation.Col;
        
        int rowBoundary = board.length;
        int colBoundary = board[0].length;
        
        //****************************************************************************************
        //logic(simple) CHECKS
        
        if(row < 0 || row >= rowBoundary || col < 0 || col >= colBoundary){
            tileToPlace.rotateClockwise(-rotations);
            return false;			//placement out of bounds;
        }
        
        if(board[row][col] != null){
            tileToPlace.rotateClockwise(-rotations);
            return false;			//if tile exists at loc
        }
        
        
        //****************************************************************************************
        //game rules CHECKS
        
        //start out optimistic
        boolean isCompatible = true;
        //get all neighboring tiles
        
        boolean noNeighboringTile = true; //CHANGE THIS TO FALSE/TRUE FOR TESTING PURPOSES
        Tile[] neighborTiles = getNeighboringTiles(targetLocation);
        for (Tile nTile : neighborTiles){
            if (nTile != null){
                noNeighboringTile = false;
            }
        }
        if (noNeighboringTile) {
            tileToPlace.rotateClockwise(-rotations);
            return false;
        }
        
        //check compatibility with neighboring tiles
        for(int direction = 0; direction < 4; direction++){
            if (neighborTiles[direction] != null){
                if(!tileToPlace.edges[direction].isCompatible(neighborTiles[direction].edges[(direction + 2) % 4])){
                    isCompatible = false;
                    break;
                }
            }
        }
        
        tileToPlace.rotateClockwise(-rotations);
        return isCompatible;
    }
    
    /* This was originally used to throw an exception when testing the AI's placement of tigers
     * 
     * void dummyFunc(int stat){
    	switch (stat){
    	case 0: System.out.println("Placement value is not within -1 to 12");
    		throw new IllegalStateException();
    	case 1: System.out.println("Tiger already placed in this feature, cannot place new tiger");
    		throw new IllegalStateException();
    	case 2: System.out.println("You are out of tigers to place, cannot place a tiger");
    		throw new IllegalStateException();
    	}
    } */
    
    //checks if a tiger can be placed at the spot indicated
    boolean verifyTigerPlacement(Tile tileToPlace, int tigerPlacement, int currentPlayer){
        if (tigerPlacement < 0 || tigerPlacement > 11){
        	if(tigerPlacement == 12 && tileToPlace.middle.featureType != FeatureTypeEnum.None){
        		return true; //It can be larger than 11 only if it is 12, which must also be a monastery placement
        		//Monasteries are also not connected to anything, so they don't need to be verified for adjacency.
        	}
        	if(tigerPlacement == -1){
        		//System.out.println("Placement = -1 meaning don't want to place a tiger");
        		return false;
        	}
        	//dummyFunc(0);
        }
        
        //initializes necessary values
        int edge = tigerPlacement / 3;
        int node = tigerPlacement % 3;
        
        if(tileToPlace.edges[edge].nodes[node].featureType == FeatureTypeEnum.InnerShore){
            node = 1;
        }
        
        //checks if there is already a tiger on the feature that the player is trying to place a tiger on
        if(tileToPlace.edges[edge].nodes[node].tigerPlacedInFeature){
        	//dummyFunc(1);
            return false;
        }
        
        //checks if the player has any tigers to place
        for(int tigerIndex = 0; tigerIndex < NUM_TIGERS; tigerIndex++){
            if (playerTigers[currentPlayer][tigerIndex].getStatus() == TigerStatusEnum.onNone){
                return true;
            }
        }
        
        //dummyFunc(2);
        return false;
    }

    //checks if a tiger can be placed at the spot indicated
    boolean aiVerifyTigerPlacement(Tile tileToPlace, int tigerPlacement, int currentPlayer){
 /*       boolean noTigerAvailable = true;
        for(int tigerIndex = 0; tigerIndex < NUM_TIGERS; tigerIndex++){
            if (playerTigers[currentPlayer][tigerIndex].getStatus() == TigerStatusEnum.onNone){
                noTigerAvailable = false;
            }
        }

        if (noTigerAvailable) return false;

        return isValidTigerPlacementOnNode(tileToPlace, tigerPlacement);
*/
        if (tigerPlacement < 0 || tigerPlacement > 11){
            if(tigerPlacement == 12 && tileToPlace.middle.featureType != FeatureTypeEnum.None){
                return true; //It can be larger than 11 only if it is 12, which must also be a monastery placement
                //Monasteries are also not connected to anything, so they don't need to be verified for adjacency.
            }
            if(tigerPlacement == -1){
                return false;
            }
        }

        //initializes necessary values
        int edge = tigerPlacement / 3;
        int node = tigerPlacement % 3;

        if(tileToPlace.edges[edge].nodes[node].featureType == FeatureTypeEnum.InnerShore){
            node = 1;
        }

        //checks if there is already a tiger on the feature that the player is trying to place a tiger on
        if(tileToPlace.edges[edge].nodes[node].tigerPlacedInFeature){
            return false;
        }

        //checks if the player has any tigers to place
        for(int tigerIndex = 0; tigerIndex < NUM_TIGERS; tigerIndex++){
            if (playerTigers[currentPlayer][tigerIndex].getStatus() == TigerStatusEnum.onNone){
                return true;
            }
        }
        return false;



    }
    
    //Finds the neighboring tiles for a tile
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

    //Finds the locations around a tile that don't already have a tile
    Location[] getEmptyNeighboringLocations(Location tileLocation){
        int row = tileLocation.Row;
        int col = tileLocation.Col;
        Location[] neighborLocations = new Location[4];

        for(int direction = 0; direction < 4; direction++){
            //if within bounds and null
            if(row + dx[direction] >= 0 && row + dx[direction] < board.length && col + dy[direction] >= 0 && col + dy[direction] < board[0].length &&
                    board[row + dx[direction]][col + dy[direction]] == null) {
                neighborLocations[direction] = new Location(row + dx[direction], col + dy[direction]);
            }
        }

        return neighborLocations;
    }

    //Determines if it is possible to place a tile at any point on the board
    boolean isPossibleToPlaceTileSomewhere(Tile tile){
        for (Location possibleLoc : possibleTargets){
            for (int rotation = 0; rotation < 4; rotation++){
                if (verifyTilePlacement(tile, possibleLoc, rotation)) {
                    return true;
                }
            }
        }

        return false;
    }

    //Updates the possible placements array 
    private void updateAvailablePlacements(Location moveLocation){
        for (Location loc : getEmptyNeighboringLocations(moveLocation)){
            if (loc != null){
                possibleTargets.add(loc);
            }
        }
        possibleTargets.remove(moveLocation);
    }
    
    //Prints the board as a local text UI
    void printBoard(){
        int lower = 67, upper = 87;
        for (int row = lower; row < upper; row++) {
            for (int col = lower; col < upper; col++) {
                
                if (board[row][col] == null) {
                    System.out.print("     ");
                } else {
                    System.out.print(" ");
                    for (int nodeIndex = 0; nodeIndex < board[row][col].edges[1].nodes.length; nodeIndex++) {
                        System.out.print(board[row][col].edges[1].nodes[nodeIndex].featureType.toChar());
                    }
                    System.out.print(" ");
                }
            }
            System.out.println("");
            for (int col = lower; col < upper; col++) {
                if (board[row][col] == null) {
                    System.out.print("     ");
                } else {
                    System.out.print(board[row][col].edges[0].nodes[2].featureType.toChar() + "   " + board[row][col].edges[2].nodes[0].featureType.toChar());
                }
            }
            System.out.println("");
            for (int col = lower; col < upper; col++) {
                if (board[row][col] == null) {
                    System.out.print("     ");
                } else {
                    System.out.print(board[row][col].edges[0].nodes[1].featureType.toChar() + " " + board[row][col].ID + (board[row][col].ID < 10 ? " " : "") + board[row][col].edges[2].nodes[1].featureType.toChar());
                }
            }
            System.out.println("");
            for (int col = lower; col < upper; col++) {
                if (board[row][col] == null) {
                    System.out.print("     ");
                } else {
                    System.out.print(board[row][col].edges[0].nodes[0].featureType.toChar() + "   " + board[row][col].edges[2].nodes[2].featureType.toChar());
                }
            }
            System.out.println();
            for (int col = lower; col < upper; col++) {
                if (board[row][col] == null) {
                    System.out.print("     ");
                } else {
                    System.out.print(" ");
                    for (int nodeIndex = board[row][col].edges[3].nodes.length - 1; nodeIndex >= 0; nodeIndex--) {
                        System.out.print(board[row][col].edges[3].nodes[nodeIndex].featureType.toChar());
                    }
                    System.out.print(" ");
                }
            }
            System.out.println("");
        }
    }
}
