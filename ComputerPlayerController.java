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

}
