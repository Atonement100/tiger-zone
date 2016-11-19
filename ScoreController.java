import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashSet;
public class ScoreController {
    int player1Score;
    int player2Score;
    ArrayList<Tile> gameTileReference;
    
    ScoreController(ArrayList<Tile> gameTileReference){
        this.player1Score = 0;
        this.player2Score = 0;
        this.gameTileReference = gameTileReference;
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
    
    
    //scoring complete trail is the same as scoring incomplete trail
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
                        if(this.gameTileReference.get(buffer.owningTileId).animalType != -1){
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
                this.player1Score += uniqueTiles.size() +uniqueAnimals.size();
                this.player2Score += uniqueTiles.size() +uniqueAnimals.size();
            }
            else if(meeplesReturned[0] > meeplesReturned[1]){
                this.player1Score += uniqueTiles.size() +uniqueAnimals.size();
            }
            else{
                this.player2Score += uniqueTiles.size() +uniqueAnimals.size();
            }
        }
        
        
        return meeplesReturned;
    }
    
    public int[] scoreCompleteCity(Node start){
        
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
                        if(this.gameTileReference.get(buffer.owningTileId).animalType != -1){
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