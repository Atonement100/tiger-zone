class PlayerController {
    public GuiAdapter guiAdapter;
    protected Meeple[] localMeeples;
    protected GameBoard localGameBoard;
    protected int playerID;

    protected static int defaultID = 0;
    PlayerController(){
        this.localMeeples = new Meeple[7];
        this.localGameBoard = new GameBoard(77,77);
    }

    PlayerController(GameBoard board){
        this.localMeeples = new Meeple[7];
        this.localGameBoard = board;
        this.playerID = defaultID++;
    }

    PlayerController(GameBoard board, int playerID){
        this.localMeeples = new Meeple[7];
        this.localGameBoard = board;
        this.playerID = playerID;
    }

    void processConfirmedMove(Tile confirmedTile, MoveInformation moveInfo, int playerConfirmed){
        //need to locally update meeple
        //localGameBoard.placeTile(confirmedTile, moveInfo.tileLocation, moveInfo.tileRotation);
        //localGameBoard.placeMeeple(confirmedTile, moveInfo.tileLocation, moveInfo.meepleLocation, playerConfirmed);
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

    public void processFreedMeeple(int ownerID, int meepleID) {
        localGameBoard.freeMeeple(ownerID, meepleID);
    }
}
