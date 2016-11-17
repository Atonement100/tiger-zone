public class PlayerController {
    protected Meeple[] localMeeples;
    protected Tile[][] localGameBoard;

    PlayerController(){
        this.localMeeples = new Meeple[7];
        this.localGameBoard = new Tile[77][77];
    }

    MoveInformation processPlayerMove(Tile currentTile){
        System.out.println("PlayerController processMove() not overridden");
        return new MoveInformation(new Location(-1, -1), -1, -1);
    }

    protected MoveInformation getPlayerMove(Tile currentTile){

        return new MoveInformation();
    }

}
