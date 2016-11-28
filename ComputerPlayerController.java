import java.util.Arrays;
import java.util.HashSet;

class ComputerPlayerController extends PlayerController {
    public GuiAdapter guiAdapter;
    private HashSet<Location> possibleTargets = new HashSet<>();
    private int numMeeples;
    private int numCrocodiles;
    
    public ComputerPlayerController(GuiAdapter g, GameBoard board) {
        this.guiAdapter = g;
        this.localMeeples = new Meeple[7];
        this.localGameBoard = board;
        this.numMeeples = 7;
        this.numCrocodiles = 2;
        this.playerID = defaultID++ % 2;
    }

    @Override
    MoveInformation processPlayerMove(Tile currentTile){
        return getPlayerMove(currentTile);
    }

    @Override
    protected MoveInformation getPlayerMove(Tile currentTile){
        //AI Logic / fx calls may come from here
        // System.out.println("computer processing move");

        MoveInformation noMeepleMoveInfo = new MoveInformation(),
        meepleMoveInfo = new MoveInformation();
        boolean meepleMoveFound = false;

        noMeepleMoveInfo.tileLocation = new Location(-1, -1);
        noMeepleMoveInfo.meepleLocation = -1;
        meepleMoveInfo.meepleLocation = -1;
        int maxConnections = 0;
        int highestScore = 0;

        for (Location possibleLoc : possibleTargets){
            Location[] neighborLocs = localGameBoard.getEmptyNeighboringLocations(possibleLoc);
            int connections = 0;
            for (Location loc : neighborLocs){
                if (loc == null){ 
                	connections++;                	
                }
            }

            if (connections <= maxConnections) continue;
            for (int possibleRot = 0; possibleRot < 4; possibleRot++){
                if (this.localGameBoard.verifyTilePlacement(currentTile, possibleLoc, possibleRot)){
                    noMeepleMoveInfo.tileLocation = possibleLoc;
                    noMeepleMoveInfo.tileRotation = possibleRot;
                    maxConnections = connections;

                    localGameBoard.placeTemporaryTile(currentTile, possibleLoc, possibleRot);

                    /* Analyze meeple placements */
                    if (numMeeples > 0){
                        //These must be considered in this order because of the... interesting zoning requirements.
                        //The idea here is to keep a running total of the highest scoring node and only replace the intended target if we /exceed/
                        //The current highest. If we only replace on higher values, we'll never pick a later zone is the same as a previous zone.
                        
                        //int[] zoneScores = new int[9], nodeIndices = new int[9];
                        IntegerTuple[] zoneValues = new IntegerTuple[9];

                        zoneValues[0] = getValueOfCorner(currentTile, 0, 1);    //Zone 1
                        zoneValues[1] = getValueOfSide(currentTile, 1);         //Zone 2
                        zoneValues[2] = getValueOfCorner(currentTile, 1, 2);    //3
                        zoneValues[3] = getValueOfSide(currentTile, 0);         //4
                        zoneValues[4] = getValueOfCenter(currentTile);          //5
                        zoneValues[5] = getValueOfSide(currentTile, 2);         //6
                        zoneValues[6] = getValueOfCorner(currentTile, 3, 0);    //7
                        zoneValues[7] = getValueOfSide(currentTile, 3);         //12 haha just kidding its 8
                        zoneValues[8] = getValueOfCorner(currentTile, 2, 3);    //9

                        for (int zoneIndex = 0; zoneIndex < zoneValues.length; zoneIndex++){
                            if (zoneValues[zoneIndex].left > highestScore){
                                highestScore = zoneValues[zoneIndex].left;
                                meepleMoveInfo.meepleZone = zoneIndex + 1;
                                meepleMoveInfo.meepleLocation = zoneValues[zoneIndex].right;
                                meepleMoveInfo.tileRotation = possibleRot;
                                meepleMoveInfo.tileLocation = possibleLoc;
                                meepleMoveFound = true;
                            }
                        }

                        if (meepleMoveFound) {
                            localGameBoard.removeTemporaryTile(possibleLoc);
                            break;
                        }
                        //moveInfo.meepleLocation = best node (for our local system)
                        //moveInfo.meepleZone = best zone (for networking)
                    }


                    /* End meeple placement analysis */

                    localGameBoard.removeTemporaryTile(possibleLoc);

                }


            }
        }

        if (meepleMoveFound){
            numMeeples--;
            return meepleMoveInfo;
        }
        else{
            return noMeepleMoveInfo;
        }
        // System.out.println("No viable location to place :(");
        //return new MoveInformation(new Location(-1, -1), -1, -1);
    }

    IntegerTuple getValueOfCenter(Tile currentTile){
        //Middle zone
        if (currentTile.hasMonastery){
            //Value monastery
            return new IntegerTuple(9, 12);
        }
        else{
            int numRoads = 0;
            for (Edge edge : currentTile.edges){
                if (edge.nodes[1].featureType.isSameFeature(FeatureTypeEnum.Road)) {
                    numRoads++;
                }
            }

            if (!currentTile.roadsEnd && numRoads > 0) { //If roads end no valid placement here.
                for (int edgeIndex = 0; edgeIndex < currentTile.edges.length; edgeIndex++){
                    if (currentTile.edges[edgeIndex].nodes[1].featureType.isSameFeature(FeatureTypeEnum.Road)){
                        return getValueOfSide(currentTile, edgeIndex);
                    }
                }
            }
            else if (numRoads == 0){
                for (int edgeIndex = 0; edgeIndex < currentTile.edges.length; edgeIndex++){
                    if (currentTile.edges[edgeIndex].nodes[1].featureType.isSameFeature(FeatureTypeEnum.Field)){
                        return getValueOfSide(currentTile, edgeIndex);
                    }
                }
            }
        }

        return new IntegerTuple(-1, -1);
    }

    IntegerTuple getValueOfCorner(Tile currentTile, int leadingEdge, int followingEdge){
        if (currentTile.edges[leadingEdge].nodes[2].featureType == currentTile.edges[followingEdge].nodes[0].featureType &&
                currentTile.edges[leadingEdge].nodes[2].featureType.isSameFeature(FeatureTypeEnum.City) &&
                !currentTile.citiesAreIndependent){
            //Guaranteed to be a city zone
            //Use edges[1].nodes[0]
            if(localGameBoard.aiVerifyMeeplePlacement(currentTile, followingEdge * 3, this.playerID)){
                return new IntegerTuple(1, followingEdge * 3);
            }
        }
        else{
            //Guaranteed to be a field zone
            if(currentTile.edges[leadingEdge].nodes[2].featureType == FeatureTypeEnum.Field){
                //If both nodes considered are fields, doesn't matter which we use. Otherwise pick the one that is a field.
                if (localGameBoard.aiVerifyMeeplePlacement(currentTile, leadingEdge * 3 + 2, this.playerID)){
                    return new IntegerTuple(1, leadingEdge * 3 + 2);
                }
            }
            else if (currentTile.edges[followingEdge].nodes[0].featureType == FeatureTypeEnum.Field){
                if (localGameBoard.aiVerifyMeeplePlacement(currentTile, followingEdge * 3, this.playerID)){
                    return new IntegerTuple(1, followingEdge * 3);
                }
            }
            else{
                //If neither is a field node then we have the JLLJ- case.
                //Have to use edges[2].nodes[0] and apply it here
                if (localGameBoard.aiVerifyMeeplePlacement(currentTile, ((followingEdge + 1) % 4) * 3, this.playerID)){
                    return new IntegerTuple(1, ((followingEdge + 1) % 4) * 3);
                }
            }
        }
        return new IntegerTuple(-1, -1);
    }

    IntegerTuple getValueOfSide(Tile currentTile, int edgeNum){
        IntegerTuple move = new IntegerTuple(-1, -1);
    	
    	switch (currentTile.edges[edgeNum].nodes[1].featureType){
            case Field:
            	  if (localGameBoard.aiVerifyMeeplePlacement(currentTile, edgeNum * 3 + 1, this.playerID)){
                      move = new IntegerTuple(1, edgeNum * 3 + 1);
                  }
                  break;
            case Road:
            	  if (localGameBoard.aiVerifyMeeplePlacement(currentTile, edgeNum * 3 + 1, this.playerID)){
                      move = new IntegerTuple(2, edgeNum * 3 + 1);
                  }
                  break;
            case RoadEnd:
            	  if (localGameBoard.aiVerifyMeeplePlacement(currentTile, edgeNum * 3 + 1, this.playerID)){
                      move = new IntegerTuple(3, edgeNum * 3 + 1);
                  }
                  break;
            case City:   
            case Wall:
                if (localGameBoard.aiVerifyMeeplePlacement(currentTile, edgeNum * 3 + 1, this.playerID)){
                    move = new IntegerTuple(3, edgeNum * 3 + 1);
                }
                break;
            default: //Realistically should throw exception but let's just pretend like the node doesn't exist and move on with our lives
                break;
        }
        return move;
    }

    @Override
    void processConfirmedMove(Tile confirmedTile, MoveInformation moveInfo, int playerConfirmed) {
        //super.processConfirmedMove(confirmedTile, moveInfo, playerConfirmed);
        //System.out.println("Computer player has confirmed the recent move Row: " + moveInfo.tileLocation.Row + " Col: " + moveInfo.tileLocation.Col + " Rotation: " + moveInfo.tileRotation);

        Location moveLocation = moveInfo.tileLocation;

        for (Location loc : localGameBoard.getEmptyNeighboringLocations(moveLocation)){
            if (loc != null){
                possibleTargets.add(loc);
            }
        }
        possibleTargets.remove(moveLocation);

        /*for (Location loc : possibleTargets){
            System.out.println("target: " + loc.Row + ", " + loc.Col); //Commented out to free console space.
        }*/
    }

    @Override
    public void processFreedMeeple(int ownerID, int meepleID){
        super.processFreedMeeple(ownerID, meepleID);
        if (ownerID == this.playerID) numMeeples++;
    }
}
