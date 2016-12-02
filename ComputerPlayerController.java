import java.util.Arrays;
import java.util.HashSet;

class ComputerPlayerController extends PlayerController {
    private HashSet<Location> possibleTargets = new HashSet<>();
    private int numTigers;
    private int numCrocodiles;
    
    public ComputerPlayerController(GameBoard board){
    	this.localTigers = new Tiger[7];
        this.localGameBoard = board;
        this.numTigers = 7;
        this.numCrocodiles = 2;
        this.playerID = defaultID++ % 2;
    }
    
    public ComputerPlayerController(GuiAdapter g, GameBoard board) {
        this.guiAdapter = g;
        this.localTigers = new Tiger[7];
        this.localGameBoard = board;
        this.numTigers = 7;
        this.numCrocodiles = 2;
        this.playerID = defaultID++ % 2;
    }

    @Override
    MoveInformation processPlayerMove(Tile currentTile){
        return getPlayerMove(currentTile);
    }

    
    @Override
    protected MoveInformation getPlayerMove(Tile currentTile){
        MoveInformation noTigerMoveInfo = new MoveInformation(),
                tigerMoveInfo = new MoveInformation();
        boolean tigerMoveFound = false;

        noTigerMoveInfo.tileLocation = new Location(-1, -1);
        noTigerMoveInfo.tigerLocation = -1;
        tigerMoveInfo.tigerLocation = -1;

        int maxConnections = 0;
        int highestScore = 0;

        for (Location possibleLoc : possibleTargets){
            Location[] neighborLocs = localGameBoard.getEmptyNeighboringLocations(possibleLoc);
            int connections = 0;
            for (Location loc : neighborLocs){
                if (loc == null) connections++;
            }

            if (connections < maxConnections) continue;

            for (int possibleRot = 0; possibleRot < 4; possibleRot++){
                if (this.localGameBoard.verifyTilePlacement(currentTile, possibleLoc, possibleRot)){
                    noTigerMoveInfo.tileLocation = possibleLoc;
                    noTigerMoveInfo.tileRotation = possibleRot;
                    maxConnections = connections;

                    if (numTigers == 0) continue;
                    
                    localGameBoard.placeTemporaryTile(currentTile, possibleLoc, possibleRot);
                    
                    /* Analyze tiger placements */
                    if (numTigers > 0){
                        //These must be considered in this order because of the zoning requirements.
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
                        zoneValues[7] = getValueOfSide(currentTile, 3);         //8
                        zoneValues[8] = getValueOfCorner(currentTile, 2, 3);    //9

                        for (int zoneIndex = 0; zoneIndex < zoneValues.length; zoneIndex++){
                            if (zoneValues[zoneIndex].left > highestScore){
                                highestScore = zoneValues[zoneIndex].left;
                                tigerMoveInfo.tigerZone = zoneIndex + 1;
                                tigerMoveInfo.tigerLocation = zoneValues[zoneIndex].right;
                                tigerMoveInfo.tileRotation = possibleRot;
                                tigerMoveInfo.tileLocation = possibleLoc;
                                tigerMoveFound = true;
                                
                            }
                        }

                        if (tigerMoveFound) {
                            localGameBoard.removeTemporaryTile(possibleLoc);
                            break;
                        }
                        //moveInfo.tigerLocation = best node (for our local system)
                        //moveInfo.tigerZone = best zone (for networking)
                    }

                    /* End tiger placement analysis */

                    localGameBoard.removeTemporaryTile(possibleLoc);

                }
            }
        }


        if (tigerMoveFound){
            numTigers--;
            return tigerMoveInfo;
        }
        else{
            return noTigerMoveInfo;
        }
    }

    //To determine the center zone feature value
    IntegerTuple getValueOfCenter(Tile currentTile){
        //Middle zone
        if (currentTile.hasDen){
            //Value monastery
            return new IntegerTuple(9, 12);
        }
        else{
            int numTrails = 0;
            for (Edge edge : currentTile.edges){
                if (edge.nodes[1].featureType.isSameFeature(FeatureTypeEnum.Trail)) {
                    numTrails++;
                }
            }

            if (!currentTile.trailsEnd && numTrails > 0) { //If trails end no valid placement here.
                for (int edgeIndex = 0; edgeIndex < currentTile.edges.length; edgeIndex++){
                    if (currentTile.edges[edgeIndex].nodes[1].featureType.isSameFeature(FeatureTypeEnum.Trail)){
                        return getValueOfSide(currentTile, edgeIndex);
                    }
                }
            }
            else if (numTrails == 0){
                for (int edgeIndex = 0; edgeIndex < currentTile.edges.length; edgeIndex++){
                    if (currentTile.edges[edgeIndex].nodes[1].featureType.isSameFeature(FeatureTypeEnum.Jungle)){
                        return getValueOfSide(currentTile, edgeIndex);
                    }
                }
            }
        }

        return new IntegerTuple(-1, -1);
    }

    //Gets the feature type of a corner of a tile in order to go between zone and local representation of features
    IntegerTuple getValueOfCorner(Tile currentTile, int leadingEdge, int followingEdge){
        if (currentTile.edges[leadingEdge].nodes[2].featureType == currentTile.edges[followingEdge].nodes[0].featureType &&
                currentTile.edges[leadingEdge].nodes[2].featureType.isSameFeature(FeatureTypeEnum.Lake) &&
                !currentTile.lakesAreIndependent){
            //Guaranteed to be a city zone
            //Use edges[1].nodes[0]
            if(localGameBoard.aiVerifyTigerPlacement(currentTile, followingEdge * 3, this.playerID)){
                return new IntegerTuple(3, followingEdge * 3);
            }
        }
        else{
            //Guaranteed to be a field zone
            if(currentTile.edges[leadingEdge].nodes[2].featureType == FeatureTypeEnum.Jungle){
                //If both nodes considered are fields, doesn't matter which we use. Otherwise pick the one that is a field.
                if (localGameBoard.aiVerifyTigerPlacement(currentTile, leadingEdge * 3 + 2, this.playerID)){
                    return new IntegerTuple(1, leadingEdge * 3 + 2);
                }
            }
            else if (currentTile.edges[followingEdge].nodes[0].featureType == FeatureTypeEnum.Jungle){
                if (localGameBoard.aiVerifyTigerPlacement(currentTile, followingEdge * 3, this.playerID)){
                    return new IntegerTuple(1, followingEdge * 3);
                }
            }
            else{
                //If neither is a field node then we have the JLLJ- case.
                //Have to use edges[2].nodes[0] and apply it here
                if (localGameBoard.aiVerifyTigerPlacement(currentTile, ((followingEdge + 1) % 4) * 3, this.playerID)){
                    return new IntegerTuple(1, ((followingEdge + 1) % 4) * 3);
                }
            }
        }
        return new IntegerTuple(-1, -1);
    }

    //Gets the feature type of a side of a tile in order to go between zone and local representation of features
    IntegerTuple getValueOfSide(Tile currentTile, int edgeNum){
        IntegerTuple move = new IntegerTuple(-1, -1);

        switch (currentTile.edges[edgeNum].nodes[1].featureType){
            case Jungle:
                if (localGameBoard.aiVerifyTigerPlacement(currentTile, edgeNum * 3 + 1, this.playerID)){
                    move = new IntegerTuple(1, edgeNum * 3 + 1);
                }
                break;
            case Trail:
                if (localGameBoard.aiVerifyTigerPlacement(currentTile, edgeNum * 3 + 1, this.playerID)){
                    move = new IntegerTuple(2, edgeNum * 3 + 1);
                }
                break;
            case TrailEnd:
                if (localGameBoard.aiVerifyTigerPlacement(currentTile, edgeNum * 3 + 1, this.playerID)){
                    move = new IntegerTuple(3, edgeNum * 3 + 1);
                }
                break;
            case Lake:
            case Shore:
                if (localGameBoard.aiVerifyTigerPlacement(currentTile, edgeNum * 3 + 1, this.playerID)){
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
        Location moveLocation = moveInfo.tileLocation;

        for (Location loc : localGameBoard.getEmptyNeighboringLocations(moveLocation)){
            if (loc != null){
                possibleTargets.add(loc);
            }
        }
        possibleTargets.remove(moveLocation);
    }

    @Override
    void processConfirmedMove(MoveInformation moveInfo) {
        Location moveLocation = moveInfo.tileLocation;

        for (Location loc : localGameBoard.getEmptyNeighboringLocations(moveLocation)){
            if (loc != null){
                possibleTargets.add(loc);
            }
        }
        possibleTargets.remove(moveLocation);
    }

    @Override
    public void processFreedTiger(int ownerID, int tigerID){
        super.processFreedTiger(ownerID, tigerID);
        if (ownerID == this.playerID) numTigers++;
    }
}
