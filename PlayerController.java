public class PlayerController {
    protected Meeple[] localMeeples;
    protected GameBoard localGameBoard;

    PlayerController(){
        this.localMeeples = new Meeple[7];
        this.localGameBoard = new GameBoard(77,77);
    }

    void processConfirmedMove(Tile confirmedTile, MoveInformation moveInfo, int playerConfirmed){
        confirmedTile.rotateClockwise(moveInfo.tileRotation);

        //need to locally update meeple
        localGameBoard.placeTile(confirmedTile, moveInfo.tileLocation, moveInfo.tileRotation);
        localGameBoard.placeMeeple(confirmedTile, moveInfo.tileLocation, moveInfo.meepleLocation, playerConfirmed);
    }

    MoveInformation processPlayerMove(Tile currentTile){
        System.out.println("PlayerController processMove() not overridden");
        return new MoveInformation(new Location(-1, -1), -1, -1);
    }

    protected MoveInformation getPlayerMove(Tile currentTile){

        return new MoveInformation();
    }

}
