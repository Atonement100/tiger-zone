import java.lang.reflect.Array;
import java.util.*;

public class ScoreController {
    
    // W,N,E,S,NW,NE,SW,SE
    private static int[] dxFULL = {0,-1,0,1,-1,-1,1,1};
    private static int[] dyFULL = {-1,0,1,0,-1,1,-1,1};
    
    GameBoard localBoard;
    int player1Score;
    int player2Score;
    private static int lakeIdentifier = 0;
    private static int denIdentifier = 0;
    private static int roadIdentifier = 0;
    ArrayList<Tile> gameTileReference;
    
    ScoreController(ArrayList<Tile> gameTileReference, Location boardDimensions){
        this.player1Score = 0;
        this.player2Score = 0;
        this.gameTileReference = gameTileReference;
        this.localBoard = new GameBoard(boardDimensions.Row, boardDimensions.Col);
    }
    
    
    ScoreController(ArrayList<Tile> gameTileReference, GameBoard board){
        this.player1Score = 0;
        this.player2Score = 0;
        this.gameTileReference = gameTileReference;
        this.localBoard = board;
    }
    
    ArrayList<Tiger> processConfirmedMove(Tile confirmedTile, MoveInformation moveInfo, int playerConfirmed, boolean usingLocalBoard){
        ArrayList<Tiger> tigersToReturn = new ArrayList<>();
        
        tigersToReturn.addAll(handleDens(confirmedTile, moveInfo));
        
        for (Edge edge : confirmedTile.edges){
            if (edge.nodes[1].featureType.isSameFeature(FeatureTypeEnum.Trail) && isTrailScorable(edge.nodes[1])){
                tigersToReturn.addAll(scoreTrail(edge.nodes[1]));
            }
            
            for(int cornerNodeIndex = 0; cornerNodeIndex < edge.nodes.length; cornerNodeIndex += 2){
                ArrayList<Node> cycleBuffer = getShoreCycleNodes(edge.nodes[cornerNodeIndex]);
                
                if (cycleBuffer.isEmpty()) continue;
                else {
                    tigersToReturn.addAll(scoreCompleteLake(edge.nodes[cornerNodeIndex]));
                }
            }
        }
        return tigersToReturn;
    }
    
    void processFreedTiger(int ownerID, int tigerID){
        localBoard.freeTiger(ownerID, tigerID);
    }
    
    private ArrayList<Tiger> handleDens(Tile confirmedTile, MoveInformation moveInfo){
        ArrayList<Tiger> tigersToReturn = new ArrayList<>();
        //**************************************************************************************************************
        //FACILITATE CHECK FOR ANY SURROUNDING DENS AND CHECK IF THIS IS A SCORABLE TILE IN CASE IT IS A TILE WITH A DEN
        int row = moveInfo.tileLocation.Row;
        int col = moveInfo.tileLocation.Col;
        
        boolean fullySurrounded = true;
        
        ArrayList<Location> denLocations = new ArrayList<Location>();
        for(int direction = 0; direction < 8; direction++){
            if(row + dxFULL[direction] >= 0 && row + dxFULL[direction] < localBoard.board.length && col + dyFULL[direction] >= 0 && col + dyFULL[direction] < localBoard.board[0].length){ //Checks within board boundary
                if(localBoard.board[row+dxFULL[direction]][col+dyFULL[direction]] == null) fullySurrounded = false;
                else if(localBoard.board[row+dxFULL[direction]][col+dyFULL[direction]].hasDen) denLocations.add(new Location(row+dxFULL[direction],col+dyFULL[direction]));
            }
        }
        
        if(fullySurrounded && confirmedTile.hasDen && confirmedTile.middle.tiger != null){
            scoreCompleteDen(confirmedTile.middle);
            tigersToReturn.add(new Tiger(localBoard.board[row][col].middle.tiger.owner, localBoard.board[row][col].middle.tiger.ID));
        }
        
        while(!denLocations.isEmpty()){
            Location buffer = denLocations.remove(0);
            row = buffer.Row;
            col = buffer.Col;
            
            fullySurrounded = true;
            for(int direction = 0; direction < 8; direction++){
                if(row + dxFULL[direction] >= 0 && row + dxFULL[direction] < localBoard.board.length && col + dyFULL[direction] >= 0 && col + dyFULL[direction] < localBoard.board[0].length){ //Checks within board boundary
                    if(localBoard.board[row+dxFULL[direction]][col+dyFULL[direction]] == null) fullySurrounded = false;
                }
            }
            
            //kinda redundant to check if these have monastery here since I am adding to the arrayList only tiles with monasteries
            if(fullySurrounded && localBoard.board[row][col].hasDen && localBoard.board[row][col].middle.tiger != null){
                scoreCompleteDen(localBoard.board[row][col].middle);
                tigersToReturn.add(new Tiger(localBoard.board[row][col].middle.tiger.owner, localBoard.board[row][col].middle.tiger.ID));
            }
            
        }
        
        return tigersToReturn;
    }
    
    void scoreJungle(Node start){
        ArrayDeque<Node> nodeQueue = new ArrayDeque<>();

        HashSet<Integer> uniqueCities = new HashSet<Integer>();
        HashSet<Integer> uniqueDens = new HashSet<Integer>();

        int[] tigersReturned = new int[2];

        nodeQueue.add(start);

        while (!nodeQueue.isEmpty()){
            Node currNode = nodeQueue.removeFirst();
            currNode.visited = true;
            if (currNode.tiger != null) tigersReturned[currNode.tiger.owner]++;

            for (Node neighbor : currNode.neighbors){
                if (!neighbor.visited){
                    if (neighbor.featureType == FeatureTypeEnum.Jungle){
                        nodeQueue.add(neighbor);
                    }
                    else if (neighbor.featureType == FeatureTypeEnum.Den && neighbor.featureID != -1){
                        if (!uniqueDens.contains(neighbor.featureID)){
                            uniqueDens.add(neighbor.featureID);
                        }
                    }
                    else if (neighbor.featureType == FeatureTypeEnum.Shore && neighbor.featureID != -1){
                        if (!uniqueCities.contains(neighbor.featureID)){
                            uniqueCities.add(neighbor.featureID);
                        }
                    }
                }
            }
        }
        
        if(tigersReturned[0] != 0 || tigersReturned[1] != 0){
            int featureValue = 3*uniqueCities.size() + 5*uniqueDens.size();
            if(tigersReturned[0] == tigersReturned[1]){
                this.player1Score += featureValue;
                this.player2Score += featureValue;
            }
            else if(tigersReturned[0] > tigersReturned[1]){
                this.player1Score += featureValue;
            }
            else{
                this.player2Score += featureValue;
            }
        }
    }
    
    private void scoreCompleteDen(Node den){
        //handleDens will take care of returning tigers appropriately
        denIdentifier++;
        den.featureID = denIdentifier;
        
        if(den.tigerPlacedInFeature && den.tiger != null){
            if(den.tiger.owner == 0){
                player1Score += 9;
            }
            else if(den.tiger.owner == 1){
                player2Score += 9;
            }
        }
        
    }
    
    void scoreIncompleteDen(Location denLocation){
        int row = denLocation.Row;
        int col = denLocation.Col;
        
        int numSurroundingTiles = 0;
        for(int direction = 0; direction < 8; direction++){
            if(row + dxFULL[direction] >= 0 && row + dxFULL[direction] < localBoard.board.length && col + dyFULL[direction] >= 0 && col + dyFULL[direction] < localBoard.board[0].length){ //Checks within board boundary
                if(localBoard.board[row+dxFULL[direction]][col+dyFULL[direction]] != null) numSurroundingTiles++;
            }
        }
        
        if(localBoard.board[row][col].middle.tiger.owner == 0){
            player1Score += numSurroundingTiles + 1;
        }
        else if (localBoard.board[row][col].middle.tiger.owner == 1){
            player2Score += numSurroundingTiles + 1;
        }
    }
    
    
    private ArrayList<Tiger> scoreTrail(Node start){
        ArrayList<Tiger> tigersToReturn = new ArrayList<>();
        int[] tigersReturned = new int[2];
        roadIdentifier++;
        start.featureID = roadIdentifier;
        
        HashSet<Integer> uniqueTiles = new HashSet<Integer>();
        HashSet<Integer> uniqueAnimals = new HashSet<Integer>();
        
        Queue<Node> bfsQueue = new LinkedList<Node>();
        bfsQueue.add(start);
        
        uniqueTiles.add(start.owningTileId);
        if(start.tiger != null){
            tigersReturned[start.tiger.owner]++;
            tigersToReturn.add(new Tiger(start.tiger.owner, start.tiger.ID));
            /*
             if(start.tiger.owner == 0){
             tigersToReturn.add(new Tiger(start.tiger.owner, start.tiger.ID)); //Not sure if it's safe to put these outside of tiger owner because start.tigerPlacedInFeature
             tigersReturned[0]++;                                                           //will be true even if no tiger is on that node
             }
             else if(start.tiger.owner == 1){
             tigersToReturn.add(new Tiger(start.tiger.owner, start.tiger.ID));
             tigersReturned[1]++;
             }
             */
        }
        int crocodilesMet = 0, preyAnimalsMet = 0;
        while(!bfsQueue.isEmpty()){
            Node buffer = bfsQueue.poll();

            if(gameTileReference.get(start.owningTileId).animalType < 4 && gameTileReference.get(start.owningTileId).animalType > 0 && !uniqueTiles.contains(start.owningTileId)){
                preyAnimalsMet++;
            }
            else if (gameTileReference.get(start.owningTileId).animalType == 4 && !uniqueTiles.contains(start.owningTileId)){
                crocodilesMet++;
            }
            uniqueTiles.add(buffer.owningTileId);

            buffer.featureID = roadIdentifier;
            buffer.visited = true;
            for(int i = 0; i < buffer.neighbors.size(); i++){
                if(!buffer.neighbors.get(i).visited &&
                   (buffer.neighbors.get(i).featureType.isSameFeature(FeatureTypeEnum.Trail)))
                {
                    bfsQueue.add(buffer.neighbors.get(i));
                    uniqueTiles.add(buffer.neighbors.get(i).owningTileId);

                    if(buffer.neighbors.get(i).tiger != null){
                        tigersReturned[buffer.neighbors.get(i).tiger.owner]++;
                        tigersToReturn.add(new Tiger(buffer.neighbors.get(i).tiger.owner, buffer.neighbors.get(i).tiger.ID));
                    }
                    
                }
            }
        }
        
        if(tigersReturned[0] != 0 || tigersReturned[1] != 0){
            int scoreToAdd = ((preyAnimalsMet - crocodilesMet > 0) ? preyAnimalsMet - crocodilesMet : 0);
            if(tigersReturned[0] == tigersReturned[1]){
                this.player1Score += scoreToAdd;
                this.player2Score += scoreToAdd;
            }
            else if(tigersReturned[0] > tigersReturned[1]){
                this.player1Score += scoreToAdd;
            }
            else{
                this.player2Score += scoreToAdd;
            }
        }
        
        
        return tigersToReturn;
    }
    
    private boolean isTrailScorable(Node start){
        if (start.featureID != -1) return false; //Already been scored, skip
        
        ArrayDeque<Node> nodeQueue = new ArrayDeque<>();
        ArrayDeque<Node> nodeCameFrom = new ArrayDeque<>(); //This goes hand in hand with nodequeue and indicates the previous node for each node in the nodequeue.
        ArrayDeque<Node> visitedNodes = new ArrayDeque<>();
        ArrayList<Integer> visitedTiles = new ArrayList<>();
        int endpointsReached = 0;
        boolean cycleDetected = false;
        
        nodeQueue.add(start);
        nodeCameFrom.add(start);
        if (start.featureType == FeatureTypeEnum.TrailEnd) endpointsReached++;
        
        while (!nodeQueue.isEmpty()){
            Node currNode = nodeQueue.removeFirst();
            Node currParent = nodeCameFrom.removeFirst();
            visitedNodes.add(currNode);
            if (!visitedTiles.contains(currNode.owningTileId)){
                visitedTiles.add(currNode.owningTileId);
            }
            
            for (Node neighbor : currNode.neighbors){
                if (visitedNodes.contains(neighbor)){
                    if (neighbor != currParent) {
                        cycleDetected = true;
                    }
                    continue;
                }
                
                if (neighbor.featureType == FeatureTypeEnum.Trail){
                    nodeQueue.add(neighbor);
                    nodeCameFrom.add(currNode);
                }
                else if (neighbor.featureType == FeatureTypeEnum.TrailEnd){
                    endpointsReached++;
                    if (!visitedTiles.contains(neighbor.owningTileId)){ //this should never be false, since endpoints are always only one node in
                        visitedTiles.add(neighbor.owningTileId);
                    }
                }
            }
        }

        return ((endpointsReached == 0 && cycleDetected) || endpointsReached == 2); //Either 2 endpoints need to have been reached or a cycle was detected.
    }
    
    ArrayList<Tiger> scoreIncompleteLake(Node start){
        ArrayList<Tiger> tigersToReturn = new ArrayList<>();
        lakeIdentifier++;
        int[] tigersReturned = new int[2];


        HashSet<Integer> uniqueTiles = new HashSet<Integer>();

        Queue<Node> bfsQueue = new LinkedList<Node>();
        bfsQueue.add(start);

        if(start.tiger != null){
            tigersReturned[start.tiger.owner]++;
            tigersToReturn.add(new Tiger(start.tiger.owner, start.tiger.ID));
        }

        int crocodilesMet = 0;
        int preyAnimalsMet = 0;

        while(!bfsQueue.isEmpty()){
            Node buffer = bfsQueue.poll();

            if(gameTileReference.get(start.owningTileId).animalType < 4 && gameTileReference.get(start.owningTileId).animalType > 0 && !uniqueTiles.contains(buffer.owningTileId)){
                preyAnimalsMet++;
            }
            else if(gameTileReference.get(start.owningTileId).animalType == 4 && !uniqueTiles.contains(start.owningTileId)){
                crocodilesMet++;
            }

            uniqueTiles.add(buffer.owningTileId);
            buffer.visited = true;
            buffer.featureID = lakeIdentifier;
            for(int i = 0; i < buffer.neighbors.size(); i++){
                if(!buffer.neighbors.get(i).visited &&
                        (buffer.neighbors.get(i).featureType.toChar() == 'W' || buffer.neighbors.get(i).featureType.toChar() == 'I'
                                ||buffer.neighbors.get(i).featureType.toChar() == 'C'))
                {
                    bfsQueue.add(buffer.neighbors.get(i));
                    uniqueTiles.add(buffer.neighbors.get(i).owningTileId);

                    if(buffer.neighbors.get(i).tiger != null){

                        tigersReturned[buffer.neighbors.get(i).tiger.owner]++;
                        tigersToReturn.add(new Tiger(buffer.neighbors.get(i).tiger.owner, buffer.neighbors.get(i).tiger.ID));

                    }
                }
            }
        }

        if(tigersReturned[0] != 0 || tigersReturned[1] != 0){
            if(tigersReturned[0] == tigersReturned[1]){
                this.player1Score += uniqueTiles.size() *(1+ ((preyAnimalsMet - crocodilesMet > 0) ? preyAnimalsMet - crocodilesMet : 0));
                this.player2Score += uniqueTiles.size() *(1+ ((preyAnimalsMet - crocodilesMet > 0) ? preyAnimalsMet - crocodilesMet : 0));
            }
            else if(tigersReturned[0] > tigersReturned[1]){
                this.player1Score += uniqueTiles.size() *(1+ ((preyAnimalsMet - crocodilesMet > 0) ? preyAnimalsMet - crocodilesMet : 0));
            }
            else{
                this.player2Score += uniqueTiles.size() *(1+ ((preyAnimalsMet - crocodilesMet > 0) ? preyAnimalsMet - crocodilesMet : 0));
            }
        }

        return tigersToReturn;
    }
    
    
    private ArrayList<Tiger> scoreCompleteLake(Node start){
        ArrayList<Tiger> tigersToReturn = new ArrayList<>();
        lakeIdentifier++;
        int[] tigersReturned = new int[2];


        HashSet<Integer> uniqueTiles = new HashSet<Integer>();
        HashSet<Integer> uniqueAnimals = new HashSet<Integer>();
        HashSet<Node> nodesToUnvisit = new HashSet<Node>();

        Queue<Node> bfsQueue = new LinkedList<Node>();
        bfsQueue.add(start);


        if(start.tiger != null){
            tigersReturned[start.tiger.owner]++;
            tigersToReturn.add(new Tiger(start.tiger.owner, start.tiger.ID));
        }

        int crocodilesMet = 0;

        while(!bfsQueue.isEmpty()){
            Node buffer = bfsQueue.poll();
            nodesToUnvisit.add(buffer);
            if(gameTileReference.get(start.owningTileId).animalType < 4 && gameTileReference.get(start.owningTileId).animalType > 0){
                uniqueAnimals.add(gameTileReference.get(start.owningTileId).animalType);
            }
            else if(gameTileReference.get(start.owningTileId).animalType < 4 && gameTileReference.get(start.owningTileId).animalType > 0 && !uniqueTiles.contains(start.owningTileId)){
                crocodilesMet++;
            }

            uniqueTiles.add(buffer.owningTileId);
            buffer.visited = true;
            buffer.featureID = lakeIdentifier;
            for(int i = 0; i < buffer.neighbors.size(); i++){
                if(!buffer.neighbors.get(i).visited &&
                        (buffer.neighbors.get(i).featureType.isSameFeature(FeatureTypeEnum.Lake)))
                {
                    bfsQueue.add(buffer.neighbors.get(i));
                    uniqueTiles.add(buffer.neighbors.get(i).owningTileId);

                    if(buffer.neighbors.get(i).tiger != null){

                        tigersReturned[buffer.neighbors.get(i).tiger.owner]++;
                        tigersToReturn.add(new Tiger(buffer.neighbors.get(i).tiger.owner, buffer.neighbors.get(i).tiger.ID));

                    }
                }
            }
        }

        if(tigersReturned[0] != 0 || tigersReturned[1] != 0){
            if(tigersReturned[0] == tigersReturned[1]){
                this.player1Score += 2*uniqueTiles.size() *(1+ ((uniqueAnimals.size() - crocodilesMet > 0) ? uniqueAnimals.size() - crocodilesMet : 0));
                this.player2Score += 2*uniqueTiles.size() *(1+ ((uniqueAnimals.size() - crocodilesMet > 0) ? uniqueAnimals.size() - crocodilesMet : 0));
            }
            else if(tigersReturned[0] > tigersReturned[1]){
                this.player1Score += 2*uniqueTiles.size() *(1+ ((uniqueAnimals.size() - crocodilesMet > 0) ? uniqueAnimals.size() - crocodilesMet : 0));
            }
            else{
                this.player2Score += 2*uniqueTiles.size() *(1+ ((uniqueAnimals.size() - crocodilesMet > 0) ? uniqueAnimals.size() - crocodilesMet : 0));
            }
        }

        for(Node nodeToUnvisitBuffer : nodesToUnvisit){
            nodeToUnvisitBuffer.visited = false;
        }

        return tigersToReturn;
    }
    
    private ArrayList<Node> getShoreCycleNodes(Node start){
        if (start.featureID != -1) return new ArrayList<>(); //Already been scored, skip
        
        //attempting to find a wall cycle from a non-wall node
        if (start.featureType != FeatureTypeEnum.InnerShore && start.featureType != FeatureTypeEnum.Shore) return new ArrayList<>();
        
        //tentative list of nodesInCycle
        ArrayList<Node> nodesInCycle = new ArrayList<Node>();
        
        //first mark start as visited, and add it in the cycle list
        start.visited = true;
        nodesInCycle.add(start);

        //get one of start node's neighbors of the same feature type and add it in the cycle list
        for(int neighborIndex = 0; neighborIndex < start.neighbors.size(); neighborIndex++)
        {
            if(start.neighbors.get(neighborIndex).featureType.toChar() == 'W' || start.neighbors.get(neighborIndex).featureType.toChar() == 'I')
            {
                nodesInCycle.add(start.neighbors.get(neighborIndex));
                start.neighbors.get(neighborIndex).visited = true;
                break;
            }
            
        }
        
        //if there were no neighbors of the same feature type cycle is impossible,
        // mark start node as unvisited and return an empty cycle List
        if(nodesInCycle.size() < 2)
        {
            nodesInCycle.remove(0).visited = false;				//this would be the starting node; remove it from the list, unvisit, and return
            return nodesInCycle;
        }
        
        //get last node added (one node away from starting node)
        Node oneLevelDepthNode = nodesInCycle.get(nodesInCycle.size()-1);
        
        //get one of the one level depth node's neighbors of the same feature type and add it in the cycle list
        for(int neighborIndex = 0; neighborIndex < oneLevelDepthNode.neighbors.size(); neighborIndex++)
        {
            if(!oneLevelDepthNode.neighbors.get(neighborIndex).visited &&
               (oneLevelDepthNode.neighbors.get(neighborIndex).featureType.toChar() == 'W'
                ||oneLevelDepthNode.neighbors.get(neighborIndex).featureType.toChar() == 'I'))
            {
                nodesInCycle.add(oneLevelDepthNode.neighbors.get(neighborIndex));
                oneLevelDepthNode.neighbors.get(neighborIndex).visited = true;
                break;
            }
        }
        
        //if there were no neighbors of the same feature type cycle is impossible,
        //mark everything in cycle List unvisited and return an empty List
        if(nodesInCycle.size() < 3){
            while(!nodesInCycle.isEmpty()){
                nodesInCycle.remove(0).visited = false;
            }
            return nodesInCycle;						//empty
        }
        
        //get last node added(two nodes away from starting node)
        //we will start searching at this point, so we will remove it from the nodesInCycle list
        //so that it doesn't exist two times in the list when it gets added back in the traversal
        Node twoLevelDepthNode = nodesInCycle.remove(nodesInCycle.size()-1);
        
        //create queue to traverse along edges
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(twoLevelDepthNode);
        
        boolean cycle = false;
        while(!queue.isEmpty()){
            Node buffer = queue.poll();
            buffer.visited = true;
            nodesInCycle.add(buffer);
            for(int i = 0; i < buffer.neighbors.size(); i++){
                if(buffer.neighbors.get(i).visited && buffer.neighbors.get(i) == start)
                {
                    cycle = true;
                }
                else if(!buffer.neighbors.get(i).visited &&
                        (buffer.neighbors.get(i).featureType == FeatureTypeEnum.Shore || buffer.neighbors.get(i).featureType == FeatureTypeEnum.InnerShore))
                {
                    queue.add(buffer.neighbors.get(i));
                }
            }
        }
        
        if(!cycle){
            while(!nodesInCycle.isEmpty()){
                nodesInCycle.remove(0).visited = false;
            }
            return nodesInCycle;
        }
        
        //INNER WALL CYCLE EDGE CASE BEGIN ******************************************************************
        boolean innerShoreCycle = true;
        for (Node node : nodesInCycle) {
            if (node.featureType != FeatureTypeEnum.InnerShore) {
                innerShoreCycle = false;
                break;
            }
        }

        if(innerShoreCycle){
            //System.out.println("INNER WALL CYCLE DETECTED");
            while(!nodesInCycle.isEmpty()){
                nodesInCycle.remove(0).visited = false;
            }
        }

        for(Node nodeToUnvisit : nodesInCycle){
            nodeToUnvisit.visited = false;
        }

        
        return nodesInCycle;		//actual cycle;
        
    }
}