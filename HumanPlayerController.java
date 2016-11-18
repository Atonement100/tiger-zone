import java.util.*;

public class HumanPlayerController extends PlayerController {

    @Override
    MoveInformation processPlayerMove(Tile currentTile){
        return getPlayerMove(currentTile);
    }

    @Override
    void processConfirmedMove(Tile confirmedTile, MoveInformation moveInfo, int playerConfirmed) {
        super.processConfirmedMove(confirmedTile, moveInfo, playerConfirmed);

        System.out.println("Human player has confirmed the recent move Row: " + moveInfo.tileLocation.Row + " Col: " + moveInfo.tileLocation.Col + " Rotation: " + moveInfo.tileRotation);
    }

    @Override
    protected MoveInformation getPlayerMove(Tile currentTile){
        System.out.println("human processing move");

        return getPlayerMoveFromConsole();
    }

    private MoveInformation getPlayerMoveFromConsole(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input row, column, clockwise rotations, and meeple location [-1,12]");
        int[] inputs = new int[4];
        for (int index = 0; index < inputs.length; index++){
            inputs[index] = scanner.nextInt();
        }

        return new MoveInformation(new Location(inputs[0], inputs[1]), inputs[2], inputs[3]);
    }

}
