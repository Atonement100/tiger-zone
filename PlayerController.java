public class PlayerController {
    public GuiAdapter guiAdapter;
    protected Tiger[] localTigers;
    protected GameBoard localGameBoard;
    protected int playerID;

    protected static int defaultID = 0;
    PlayerController(){
        this.localTigers = new Tiger[7];
        this.localGameBoard = new GameBoard(77,77);
    }

    PlayerController(GameBoard board){
        this.localTigers = new Tiger[7];
        this.localGameBoard = board;
        this.playerID = defaultID++;
    }

    PlayerController(GameBoard board, int playerID){
        this.localTigers = new Tiger[7];
        this.localGameBoard = board;
        this.playerID = playerID;
    }

    void processConfirmedMove(Tile confirmedTile, MoveInformation moveInfo, int playerConfirmed){
        //need to locally update tiger
        //localGameBoard.placeTile(confirmedTile, moveInfo.tileLocation, moveInfo.tileRotation);
        //localGameBoard.placeTiger(confirmedTile, moveInfo.tileLocation, moveInfo.tigerLocation, playerConfirmed);
    }

    void processConfirmedMove(MoveInformation moveInfo) {
    }

    MoveInformation processPlayerMove(Tile currentTile){
        System.out.println("PlayerController processMove() not overridden");
        return new MoveInformation(new Location(-1, -1), -1, -1);
    }

    protected MoveInformation getPlayerMove(Tile currentTile){

        return new MoveInformation();
    }

    public void processFreedTiger(int ownerID, int tigerID) {
        localGameBoard.freeTiger(ownerID, tigerID);
    }
}
