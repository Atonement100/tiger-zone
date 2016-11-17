public class PlayerController {
    protected Meeple[] localMeeples;
    protected Tile[][] localGameBoard;

    PlayerController(){
        this.localMeeples = new Meeple[7];
        this.localGameBoard = new Tile[77][77];
    }

    void processConfirmedMove(Tile confirmedTile, MoveInformation moveInfo){
        confirmedTile.rotateClockwise(moveInfo.tileRotation);

        //need to locally update meeple

        localGameBoard[moveInfo.tileLocation.Row][moveInfo.tileLocation.Col] = confirmedTile;
    }

    MoveInformation processPlayerMove(Tile currentTile){
        System.out.println("PlayerController processMove() not overridden");
        return new MoveInformation(new Location(-1, -1), -1, -1);
    }

    protected MoveInformation getPlayerMove(Tile currentTile){

        return new MoveInformation();
    }

}