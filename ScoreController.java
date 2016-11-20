import java.util.*;

public class ScoreController {
    
    // W,N,E,S,NW,NE,SW,SE
    
    
    GameBoard board;
    int player1Score;
    int player2Score;
    private static int lakeIdentifier = 0;
    private static int denIdentifier = 0;
    ArrayList<Tile> gameTileReference;
    
    ScoreController(ArrayList<Tile> gameTileReference, GameBoard board){
        this.player1Score = 0;
        this.player2Score = 0;
        this.gameTileReference = gameTileReference;
        this.board = board;
    }
    
    public void attemptScoring(Tile toCheck){
        
        ArrayList<Node> cycleBuffer;
        
        //WALL CYCLES
        for(int edgeIndex = 0; edgeIndex < toCheck.edges.length; edgeIndex++){
            for(int cornerNodeIndex = 0; cornerNodeIndex < toCheck.edges[edgeIndex].nodes.length; cornerNodeIndex += 2){
                cycleBuffer = getWallCycleNodes(toCheck.edges[edgeIndex].nodes[cornerNodeIndex]);
                System.out.println("NODES IN WALL CYCLE " + cycleBuffer.size());
                
                //FOR TESTING *******************************
                while(!cycleBuffer.isEmpty()){
                    cycleBuffer.remove(0).visited = false;
                }
                //*******************************************
                System.out.println("");
            }
        }
        
        //ROAD CYCLES
        for(int edgeIndex = 0; edgeIndex < toCheck.edges.length; edgeIndex++){
            cycleBuffer = getRoadCycleNodes(toCheck.edges[edgeIndex].nodes[1]);
            System.out.println("NODES IN ROAD CYCLE " + cycleBuffer.size());
            
            //FOR TESTING *******************************
            while(!cycleBuffer.isEmpty()){
                cycleBuffer.remove(0).visited = false;
            }
            //*******************************************
            System.out.println("");
        }
    }
    
    void scoreField(Node start){
        ArrayDeque<Node> nodeQueue = new ArrayDeque<>();
        ArrayDeque<Node> visitedNodes = new ArrayDeque<>();
        
        HashSet<Integer> uniqueCities = new HashSet<Integer>();
        HashSet<Integer> uniqueDens = new HashSet<Integer>();
        
        int[] meeplesReturned = new int[2];
        
        nodeQueue.add(start);
        
        while (!nodeQueue.isEmpty()){
            Node currNode = nodeQueue.removeFirst();
            visitedNodes.add(currNode);
            currNode.visited = true;
            if (currNode.meeple != null) meeplesReturned[currNode.meeple.owner]++;
            
            for (Node neighbor : currNode.neighbors){
                if (!neighbor.visited){
                    if (neighbor.featureType == FeatureTypeEnum.Field){
                        nodeQueue.add(neighbor);
                    }
                    else if (neighbor.featureType == FeatureTypeEnum.Monastery){
                        if (!uniqueDens.contains(neighbor.featureID)){
                            uniqueDens.add(neighbor.featureID);
                        }
                    }
                    else if (neighbor.featureType == FeatureTypeEnum.Wall){
                        if (!uniqueCities.contains(neighbor.featureID)){
                            uniqueCities.add(neighbor.featureID);
                        }
                    }
                }
            }
        }
        
        if(meeplesReturned[0] != 0 || meeplesReturned[1] != 0){
            int featureValue = 3*uniqueCities.size() + 5*uniqueDens.size();
            if(meeplesReturned[0] == meeplesReturned[1]){
                this.player1Score += featureValue;
                this.player2Score += featureValue;
            }
            else if(meeplesReturned[0] > meeplesReturned[1]){
                this.player1Score += featureValue;
            }
            else{
                this.player2Score += featureValue;
            }
        }
    }
    
    void scoreCompleteDen(Node den){
        
        
        denIdentifier++;
        den.featureID = denIdentifier;
        
        if(den.meeplePlacedInFeature && den.meeple != null){
            if(den.meeple.owner == 0){
                player1Score += 9;
                //player1Meeples++;
            }
            else if(den.meeple.owner == 1){
                player2Score += 9;
                //player2Meeples++;
            }
        }
        
    }
    
    void scoreIncompleteDen(Node den){
        
    }
    
    
    public int[] scoreRoad(Node start){
        
        int[] meeplesReturned = new int[2];
        
        HashSet<Integer> uniqueTiles = new HashSet<Integer>();
        HashSet<Integer> uniqueAnimals = new HashSet<Integer>();
        
        Queue<Node> bfsQueue = new LinkedList<Node>();
        bfsQueue.add(start);
        if(start.meeplePlacedInFeature){
            uniqueTiles.add(start.owningTileId);
            if(start.meeple.owner == 0){
                meeplesReturned[0]++;
            }
            else if(start.meeple.owner == 1){
                meeplesReturned[1]++;
            }
        }
        
        while(!bfsQueue.isEmpty()){
            Node buffer = bfsQueue.poll();
            buffer.visited = true;
            for(int i = 0; i < buffer.neighbors.size(); i++){
                if(!buffer.neighbors.get(i).visited &&
                   (buffer.neighbors.get(i).featureType.toChar() == 'R' || buffer.neighbors.get(i).featureType.toChar() == 'E'))
                {
                    if(buffer.meeplePlacedInFeature && !uniqueTiles.contains(buffer.owningTileId)){
                        
                        uniqueTiles.add(buffer.owningTileId);
                        if(this.gameTileReference.get(buffer.owningTileId).animalType != 0){
                            uniqueAnimals.add(this.gameTileReference.get(buffer.owningTileId).animalType);
                        }
                        
                        if(buffer.meeple.owner == 0){
                            meeplesReturned[0]++;
                        }
                        else if(buffer.meeple.owner == 1){
                            meeplesReturned[1]++;
                        }
                        bfsQueue.add(buffer.neighbors.get(i));
                    }
                    
                }
            }
        }
        
        if(meeplesReturned[0] != 0 || meeplesReturned[1] != 0){
            if(meeplesReturned[0] == meeplesReturned[1]){
                this.player1Score += uniqueTiles.size() + uniqueAnimals.size();
                this.player2Score += uniqueTiles.size() + uniqueAnimals.size();
            }
            else if(meeplesReturned[0] > meeplesReturned[1]){
                this.player1Score += uniqueTiles.size() + uniqueAnimals.size();
            }
            else{
                this.player2Score += uniqueTiles.size() + uniqueAnimals.size();
            }
        }
        
        
        return meeplesReturned;
    }
    
    boolean isRoadScorable(Node start){
        ArrayDeque<Node> nodeQueue = new ArrayDeque<>();
        ArrayDeque<Node> nodeCameFrom = new ArrayDeque<>(); //This goes hand in hand with nodequeue and indicates the previous node for each node in the nodequeue.
        ArrayDeque<Node> visitedNodes = new ArrayDeque<>();
        ArrayList<Integer> visitedTiles = new ArrayList<>();
        int endpointsReached = 0;
        boolean cycleDetected = false;
        
        nodeQueue.add(start);
        nodeCameFrom.add(start);
        if (start.featureType == FeatureTypeEnum.RoadEnd) endpointsReached++;
        
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
                        System.out.println ("road cycle");
                        cycleDetected = true;
                    }
                    continue;
                }
                
                if (neighbor.featureType == FeatureTypeEnum.Road){
                    nodeQueue.add(neighbor);
                    nodeCameFrom.add(currNode);
                }
                else if (neighbor.featureType == FeatureTypeEnum.RoadEnd){
                    System.out.println("reached endpt");
                    endpointsReached++;
                    if (!visitedTiles.contains(neighbor.owningTileId)){ //this should never be false, since endpoints are always only one node in
                        visitedTiles.add(neighbor.owningTileId);
                    }
                }
            }
        }
        
        System.out.println("endpoints: " + endpointsReached);
        
        return ((endpointsReached == 0 && cycleDetected) || endpointsReached == 2); //Either 2 endpoints need to have been reached or a cycle was detected.
        /* Old code used to return tile ids instead of boolean
         switch (endpointsReached){
         case 0:
         if (cycleDetected) return visitedTiles;
         else return new ArrayList<>();
         case 1: return new ArrayList<>(); // if only one endpoint was found, cant be a cycle and isnt complete
         case 2: return visitedTiles;
         default: throw new IllegalStateException();
         }
         */
    }
    
    public int[] scoreIncompleteCity(Node start){
        int[] meeplesReturned = new int[2];
        
        HashSet<Integer> uniqueTiles = new HashSet<Integer>();
        HashSet<Integer> uniqueAnimals = new HashSet<Integer>();
        
        Queue<Node> bfsQueue = new LinkedList<Node>();
        bfsQueue.add(start);
        if(start.meeplePlacedInFeature){
            uniqueTiles.add(start.owningTileId);
            if(start.meeple.owner == 0){
                meeplesReturned[0]++;
            }
            else if(start.meeple.owner == 1){
                meeplesReturned[1]++;
            }
        }
        
        while(!bfsQueue.isEmpty()){
            Node buffer = bfsQueue.poll();
            buffer.visited = true;
            for(int i = 0; i < buffer.neighbors.size(); i++){
                if(!buffer.neighbors.get(i).visited &&
                   (buffer.neighbors.get(i).featureType.toChar() == 'W' || buffer.neighbors.get(i).featureType.toChar() == 'I'
                    ||buffer.neighbors.get(i).featureType.toChar() == 'C'))
                {
                    if(buffer.meeplePlacedInFeature && !uniqueTiles.contains(buffer.owningTileId)){
                        
                        uniqueTiles.add(buffer.owningTileId);
                        if(this.gameTileReference.get(buffer.owningTileId).animalType != 0){
                            uniqueAnimals.add(this.gameTileReference.get(buffer.owningTileId).animalType);
                        }
                        
                        if(buffer.meeple.owner == 0){
                            meeplesReturned[0]++;
                        }
                        else if(buffer.meeple.owner == 1){
                            meeplesReturned[1]++;
                        }
                        bfsQueue.add(buffer.neighbors.get(i));
                    }
                    
                }
            }
        }
        
        if(meeplesReturned[0] != 0 || meeplesReturned[1] != 0){
            if(meeplesReturned[0] == meeplesReturned[1]){
                this.player1Score += uniqueTiles.size() *(1+uniqueAnimals.size());
                this.player2Score += uniqueTiles.size() *(1+uniqueAnimals.size());
            }
            else if(meeplesReturned[0] > meeplesReturned[1]){
                this.player1Score += uniqueTiles.size() *(1+uniqueAnimals.size());
            }
            else{
                this.player2Score += uniqueTiles.size() *(1+uniqueAnimals.size());
            }
        }
        
        
        return meeplesReturned;
    }
    
    
    public int[] scoreCompleteCity(Node start){
        
        lakeIdentifier++;
        int[] meeplesReturned = new int[2];
        
        
        HashSet<Integer> uniqueTiles = new HashSet<Integer>();
        HashSet<Integer> uniqueAnimals = new HashSet<Integer>();
        
        Queue<Node> bfsQueue = new LinkedList<Node>();
        bfsQueue.add(start);
        if(start.meeplePlacedInFeature){
            uniqueTiles.add(start.owningTileId);
            if(start.meeple.owner == 0){
                meeplesReturned[0]++;
            }
            else if(start.meeple.owner == 1){
                meeplesReturned[1]++;
            }
        }
        
        while(!bfsQueue.isEmpty()){
            Node buffer = bfsQueue.poll();
            buffer.visited = true;
            buffer.featureID = lakeIdentifier;
            for(int i = 0; i < buffer.neighbors.size(); i++){
                if(!buffer.neighbors.get(i).visited &&
                   (buffer.neighbors.get(i).featureType.toChar() == 'W' || buffer.neighbors.get(i).featureType.toChar() == 'I'
                    ||buffer.neighbors.get(i).featureType.toChar() == 'C'))
                {
                    if(buffer.meeplePlacedInFeature && !uniqueTiles.contains(buffer.owningTileId)){
                        
                        uniqueTiles.add(buffer.owningTileId);
                        if(this.gameTileReference.get(buffer.owningTileId).animalType != 0){
                            uniqueAnimals.add(this.gameTileReference.get(buffer.owningTileId).animalType);
                        }
                        
                        if(buffer.meeple.owner == 0){
                            meeplesReturned[0]++;
                        }
                        else if(buffer.meeple.owner == 1){
                            meeplesReturned[1]++;
                        }
                        bfsQueue.add(buffer.neighbors.get(i));
                    }
                    
                }
            }
        }
        
        if(meeplesReturned[0] != 0 || meeplesReturned[1] != 0){
            if(meeplesReturned[0] == meeplesReturned[1]){
                this.player1Score += 2*uniqueTiles.size() *(1+uniqueAnimals.size());
                this.player2Score += 2*uniqueTiles.size() *(1+uniqueAnimals.size());
            }
            else if(meeplesReturned[0] > meeplesReturned[1]){
                this.player1Score += 2*uniqueTiles.size() *(1+uniqueAnimals.size());
            }
            else{
                this.player2Score += 2*uniqueTiles.size() *(1+uniqueAnimals.size());
            }
        }
        
        
        return meeplesReturned;
    }
    
    public ArrayList<Node> getWallCycleNodes(Node start){
        
        //tentative list of nodesInCycle
        ArrayList<Node> nodesInCycle = new ArrayList<Node>();
        
        //first mark start as visited, and add it in the cycle list
        start.visited = true;
        nodesInCycle.add(start);
        System.out.println("START\n" + start.featureType.toChar() + " " + start.owningTileId + " " + start.hashCode());
        
        //get one of start node's neighbors of the same feature type and add it in the cycle list
        for(int neighborIndex = 0; neighborIndex < start.neighbors.size(); neighborIndex++)
        {
            if(start.neighbors.get(neighborIndex).featureType.toChar() == 'W' || start.neighbors.get(neighborIndex).featureType.toChar() == 'I')
            {
                nodesInCycle.add(start.neighbors.get(neighborIndex));
                System.out.println(start.neighbors.get(neighborIndex).featureType.toChar() + " " + start.neighbors.get(neighborIndex).owningTileId + " " + start.neighbors.get(neighborIndex).hashCode());
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
            System.out.println(buffer.featureType.toChar() + " "+ buffer.owningTileId + " " + buffer.hashCode());
            for(int i = 0; i < buffer.neighbors.size(); i++){
                if(buffer.neighbors.get(i).visited && buffer.neighbors.get(i) == start)
                {
                    cycle = true;
                }
                else if(!buffer.neighbors.get(i).visited &&
                        (buffer.neighbors.get(i).featureType.toChar() == 'W' || buffer.neighbors.get(i).featureType.toChar() == 'I'))
                {
                    queue.add(buffer.neighbors.get(i));
                }
            }
        }
        
        if(!cycle){
            while(!nodesInCycle.isEmpty()){
                nodesInCycle.remove(0).visited = false;
            }
        }
        
        //INNER WALL CYCLE EDGE CASE BEGIN ******************************************************************
        boolean innerWallCycle = true;
        if(cycle){
            for(int nodeIndex = 0; nodeIndex < nodesInCycle.size(); nodeIndex++){
                if(nodesInCycle.get(nodeIndex).featureType.toChar() !='I'){
                    innerWallCycle = false;
                    break;
                }
            }
        }
        if(innerWallCycle){
            System.out.println("INNER WALL CYCLE DETECTED");
            while(!nodesInCycle.isEmpty()){
                nodesInCycle.remove(0).visited = false;
            }
        }
        //INNER WALL CYCLE EDGE CASE END ******************************************************************
        
        return nodesInCycle;		//actual cycle;
        
    }
    
    public ArrayList<Node> getRoadCycleNodes(Node start){
        
        //tentative list of nodesInCycle
        ArrayList<Node> nodesInCycle = new ArrayList<Node>();
        
        //first mark start as visited, and add it in the cycle list
        start.visited = true;
        nodesInCycle.add(start);
        
        //get one of start node's neighbors of the same feature type and add it in the cycle list
        for(int neighborIndex = 0; neighborIndex < start.neighbors.size(); neighborIndex++)
        {
            if(start.neighbors.get(neighborIndex).featureType.toChar() == 'R')
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
            if(!oneLevelDepthNode.neighbors.get(neighborIndex).visited && oneLevelDepthNode.neighbors.get(neighborIndex).featureType.toChar() == 'R')
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
                if(buffer.neighbors.get(i).visited && buffer.neighbors.get(i) == start){
                    cycle = true;
                }
                else if(!buffer.neighbors.get(i).visited && buffer.neighbors.get(i).featureType.toChar() == 'R'){
                    queue.add(buffer.neighbors.get(i));
                }
            }
        }
        
        if(!cycle){
            while(!nodesInCycle.isEmpty()){
                nodesInCycle.remove(0).visited = false;
            }
        }
        return nodesInCycle;
    }
}