public class ComputerPlayerController extends PlayerController {

    @Override
    MoveInformation processPlayerMove(Tile currentTile){
        return getPlayerMove(currentTile);
    }

    @Override
    protected MoveInformation getPlayerMove(Tile currentTile){
        //AI Logic / fx calls may come from here
        System.out.println("computer processing move");
        return new MoveInformation(new Location(2, 2), 0, -1);

    }

    @Override
    void processConfirmedMove(Tile confirmedTile, MoveInformation moveInfo) {
        super.processConfirmedMove(confirmedTile, moveInfo);

        System.out.println("Computer player has confirmed the recent move Row: " + moveInfo.tileLocation.Row + " Col: " + moveInfo.tileLocation.Col + " Rotation: " + moveInfo.tileRotation);
    }

}