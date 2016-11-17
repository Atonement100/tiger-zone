import java.util.*;

public class HumanPlayerController extends PlayerController {

    @Override
    MoveInformation processPlayerMove(Tile currentTile){
        return getPlayerMove(currentTile);
    }

    @Override
    protected MoveInformation getPlayerMove(Tile currentTile){
        System.out.println("human processing move");
        Scanner scanner = new Scanner(System.in);
        scanner.next();

        return new MoveInformation(new Location(1, 1), 0, -1);
    }


}
