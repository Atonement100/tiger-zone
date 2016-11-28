import java.util.ArrayDeque;
import java.util.HashSet;

public class GameBoard {
    Tile[][] board;
    
    private static final int NUM_PLAYERS = GameController.NUM_PLAYERS;
    private static final int NUM_MEEPLES = GameController.NUM_MEEPLES;
    private static int[] dx = {0,-1,0,1}; // W, N, E, S
    private static int[] dy = {-1,0,1,0};
    private HashSet<Location> possibleTargets = new HashSet<>();
    
    Meeple[][] playerMeeples = new Meeple[NUM_PLAYERS][NUM_MEEPLES];
    
    GameBoard(int numRows, int numCols){
        board = new Tile[numRows][numCols];
        
        for (int playerIndex = 0; playerIndex < NUM_PLAYERS; playerIndex++){
            for (int meepleIndex = 0; meepleIndex < NUM_MEEPLES; meepleIndex++){
                playerMeeples[playerIndex][meepleIndex] = new Meeple(playerIndex, meepleIndex);
            }
        }
    }
    
    Location getBoardDimensions(){
        return new Location(board.length, board[board.length-1].length);
    }
    
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
        
        updateMeepleInfoForNewTile(targetLocation);
        updateAvailablePlacements(targetLocation);
    }

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
        
        updateMeepleInfoForNewTile(targetLocation);
    }
    
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
                node.meeplePlacedInFeature = false;
            }
        }

        board[targetLocation.Row][targetLocation.Col] = null;
        tileToRemove.rotateAntiClockwise(tileToRemove.rotations);
    }
    
    private void updateMeepleInfoForNewTile(Location tileLocation){
        Tile tileToUpdate = board[tileLocation.Row][tileLocation.Col];

        for (Edge edge : tileToUpdate.edges) {
            for (Node node : edge.nodes){           //For each node...
                if (node.meeplePlacedInFeature) continue; //If this node already knows it has a meeple, continue
                
                /* So this looks kinda hacky, but it should actually be pretty efficient in practice.
                 *  Visited queue only includes tiles that are not marked as having a meeple.
                 *  If a node is known to have a meeple, its neighbors are not added to the nodeQueue.
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
                            if (neighbor.meeplePlacedInFeature) {
                                shouldMarkVisited = true;
                            } else if (!visitedQueue.contains(neighbor) && neighbor.owningTileId == currNode.owningTileId) {     //Only add to nodequeue if we haven't seen it before
                                nodeQueue.add(neighbor);
                            }
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
    
    private void updateMeepleInfoForTemporaryTile(Location tileLocation){
        Tile tileToUpdate = board[tileLocation.Row][tileLocation.Col];

        for (Edge edge : tileToUpdate.edges) {
            for (Node node : edge.nodes){           //For each node...
                if (node.meeplePlacedInFeature) continue; //If this node already knows it has a meeple, continue

                ArrayDeque<Node> nodeQueue = new ArrayDeque<>();
                ArrayDeque<Node> visitedQueue = new ArrayDeque<>();
                
                boolean shouldMarkVisited = false;
                
                nodeQueue.add(node);
                while (!nodeQueue.isEmpty()) {      //While there are some neighbors that have not been visited...
                    Node currNode = nodeQueue.removeFirst();
                    visitedQueue.add(currNode);
                    for (Node neighbor : currNode.neighbors) {
                        if (currNode.featureType.isSameFeature(neighbor.featureType)) {
                            if (neighbor.meeplePlacedInFeature) {
                                shouldMarkVisited = true;
                            } else if (!visitedQueue.contains(neighbor)) {     //Only add to nodequeue if we haven't seen it before
                                nodeQueue.add(neighbor);
                            }
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
    
    private void updateMeepleInfoForNewMeeple(Location targetLocation, int meepleLocation){
        if (meepleLocation == 12) return;
        
        int edge = meepleLocation / 3; //Nodes per edge
        int node = meepleLocation % 3;
        Tile tile = board[targetLocation.Row][targetLocation.Col];
        
        if (tile == null) return;
        
        ArrayDeque<Node> nodeQueue = new ArrayDeque<>();
        ArrayDeque<Node> visitedQueue = new ArrayDeque<>();
        nodeQueue.add(tile.edges[edge].nodes[node]);
        while (!nodeQueue.isEmpty()){
            Node currNode = nodeQueue.removeFirst();
            visitedQueue.add(currNode);
            for (Node neighbor : currNode.neighbors){
                if (currNode.featureType.isSameFeature(neighbor.featureType) && !neighbor.meeplePlacedInFeature && !visitedQueue.contains(neighbor)){
                    neighbor.meeplePlacedInFeature = true;
                    nodeQueue.add(neighbor);
                }
            }
        }
    }
    
    boolean isValidMeeplePlacementOnNode(Location targetLocation, int meepleLocation){
        if (meepleLocation == 12) return true;
        else if (meepleLocation < 0 || meepleLocation > 12) return false;
        
        int edge = meepleLocation / 3; //Nodes per edge
        int node = meepleLocation % 3;
        Tile tile = board[targetLocation.Row][targetLocation.Col];
        if (tile == null) return false;

        if(tile.edges[edge].nodes[node].featureType == FeatureTypeEnum.InnerWall){
            node = 1;
        }
        
        Node startingNode = tile.edges[edge].nodes[node];
        if (startingNode.meeplePlacedInFeature) return false;
        
        ArrayDeque<Node> nodeQueue = new ArrayDeque<>();
        ArrayDeque<Node> visitedQueue = new ArrayDeque<>();
        nodeQueue.add(startingNode);
        while (!nodeQueue.isEmpty()){
            Node currNode = nodeQueue.removeFirst();
            visitedQueue.add(currNode);
            for (Node neighbor : currNode.neighbors){
                if (neighbor.featureType.isSameFeature(currNode.featureType)) {
                    if (neighbor.meeplePlacedInFeature) {
                        System.out.println("Potential error from isValidMeeplePlacementOnNode - starting node should be marked true when the tile is placed");
                        return false;
                    } else if (!visitedQueue.contains(neighbor)) {
                        nodeQueue.add(neighbor);
                    }
                }
            }
        }
        
        return true;
    }

    boolean isValidMeeplePlacementOnNode(Tile tile, int meepleLocation){
        if (meepleLocation == 12) return true;
        else if (meepleLocation < 0 || meepleLocation > 12) return false;

        int edge = meepleLocation / 3; //Nodes per edge
        int node = meepleLocation % 3;

        if (tile == null) return false;

        if(tile.edges[edge].nodes[node].featureType == FeatureTypeEnum.InnerWall){
            node = 1;
        }

        Node startingNode = tile.edges[edge].nodes[node];
        if (startingNode.meeplePlacedInFeature) return false;


        ArrayDeque<Node> nodeQueue = new ArrayDeque<>();
        ArrayDeque<Node> visitedQueue = new ArrayDeque<>();
        nodeQueue.add(startingNode);
        while (!nodeQueue.isEmpty()){
            Node currNode = nodeQueue.removeFirst();
            visitedQueue.add(currNode);
            for (Node neighbor : currNode.neighbors){
                if (neighbor.featureType.isSameFeature(currNode.featureType)) {
                    if (neighbor.meeplePlacedInFeature) {
                        System.out.println("Potential error from isValidMeeplePlacementOnNode - starting node should be marked true when the tile is placed");
                        return false;
                    } else if (!visitedQueue.contains(neighbor)) {
                        nodeQueue.add(neighbor);
                    }
                }
            }
        }

        return true;
    }
    
    void freeMeeple(int ownerID, int meepleID){
        this.playerMeeples[ownerID][meepleID].status = MeepleStatusEnum.onNone;
        this.playerMeeples[ownerID][meepleID].location = new Location(-1,-1);
    }
    
    //places a meeple in the valid position
    void placeMeeple(Tile tileToPlace, Location targetLocation, int placement, int currentPlayer){
    	
        if (placement < 0 || placement > 12) return;
        
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
                    //if placement is on an inner wall, change the placement to the center node on the edge
                    if(tileToPlace.edges[edge].nodes[node].featureType == FeatureTypeEnum.InnerWall){
                        node = 1;
                    }
                    board[targetLocation.Row][targetLocation.Col].edges[edge].nodes[node].meeplePlacedInFeature = true;
                    board[targetLocation.Row][targetLocation.Col].edges[edge].nodes[node].meeple = playerMeeples[currentPlayer][meepleIndex];
                    updateMeepleInfoForNewMeeple(targetLocation, placement);    //No need to propagate changes for monastery, only do it here
                }
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
    
    /* void dummyFunc(int stat){
    	switch (stat){
    	case 0: System.out.println("Placement value is not within -1 to 12");
    		throw new IllegalStateException();
    	case 1: System.out.println("Meeple already placed in this feature, cannot place new meeple");
    		throw new IllegalStateException();
    	case 2: System.out.println("You are out of meeples to place, cannot place a meeple");
    		throw new IllegalStateException();
    	}
    } */
    
    //checks if a meeple can be placed at the spot indicated
    boolean verifyMeeplePlacement(Tile tileToPlace, int meeplePlacement, int currentPlayer){
        if (meeplePlacement < 0 || meeplePlacement > 11){
        	if(meeplePlacement == 12 && tileToPlace.middle.featureType != FeatureTypeEnum.None){
        		return true; //It can be larger than 11 only if it is 12, which must also be a monastery placement
        		//Monasteries are also not connected to anything, so they don't need to be verified for adjacency.
        	}
        	if(meeplePlacement == -1){
        		//System.out.println("Placement = -1 meaning don't want to place a meeple");
        		return false;
        	}
        	//dummyFunc(0);
        }
        
        //initializes necessary values
        int edge = meeplePlacement / 3;
        int node = meeplePlacement % 3;
        
        if(tileToPlace.edges[edge].nodes[node].featureType == FeatureTypeEnum.InnerWall){
            node = 1;
        }
        
        //checks if there is already a meeple on the feature that the player is trying to place a meeple on
        if(tileToPlace.edges[edge].nodes[node].meeplePlacedInFeature){
        	//dummyFunc(1);
            return false;
        }
        //checks if the player has any meeples to place
        for(int meepleIndex = 0; meepleIndex < NUM_MEEPLES; meepleIndex++){
            if (playerMeeples[currentPlayer][meepleIndex].getStatus() == MeepleStatusEnum.onNone){
                return true;
            }
        }
        
        //dummyFunc(2);
        return false;
    }

    //checks if a meeple can be placed at the spot indicated
    boolean aiVerifyMeeplePlacement(Tile tileToPlace, int meeplePlacement, int currentPlayer){
  /*      boolean noMeepleAvailable = true;
        for(int meepleIndex = 0; meepleIndex < NUM_MEEPLES; meepleIndex++){
            if (playerMeeples[currentPlayer][meepleIndex].getStatus() == MeepleStatusEnum.onNone){
                noMeepleAvailable = false;
            }
        }

        if (noMeepleAvailable) return false;

        return isValidMeeplePlacementOnNode(tileToPlace, meeplePlacement);
*/
        if (meeplePlacement < 0 || meeplePlacement > 11){
            if(meeplePlacement == 12 && tileToPlace.middle.featureType != FeatureTypeEnum.None){
                return true; //It can be larger than 11 only if it is 12, which must also be a monastery placement
                //Monasteries are also not connected to anything, so they don't need to be verified for adjacency.
            }
            if(meeplePlacement == -1){
                return false;
            }
        }

        //initializes necessary values
        int edge = meeplePlacement / 3;
        int node = meeplePlacement % 3;

        if(tileToPlace.edges[edge].nodes[node].featureType == FeatureTypeEnum.InnerWall){
            node = 1;
        }

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

    void updateAvailablePlacements(Location moveLocation){
        for (Location loc : getEmptyNeighboringLocations(moveLocation)){
            if (loc != null){
                possibleTargets.add(loc);
            }
        }
        possibleTargets.remove(moveLocation);
    }
    
    void printBoard(){
        int lower = 23, upper = 53;
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
