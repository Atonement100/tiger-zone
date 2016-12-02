import java.util.*;

public class HumanPlayerController extends PlayerController {

    public GuiAdapter guiAdapter;

    HumanPlayerController(GuiAdapter g){
        this.guiAdapter = g;
    }


    HumanPlayerController(GameBoard board){
        this.localMeeples = new Meeple[7];
        this.localGameBoard = board;
    }

    @Override
    MoveInformation processPlayerMove(Tile currentTile){
        return getPlayerMove(currentTile);
    }

    @Override
    void processConfirmedMove(Tile confirmedTile, MoveInformation moveInfo, int playerConfirmed) {
        super.processConfirmedMove(confirmedTile, moveInfo, playerConfirmed);
    }

    @Override
    protected MoveInformation getPlayerMove(Tile currentTile){
        return getPlayerMoveFromGui(currentTile);
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

    private MoveInformation getPlayerMoveFromGui(Tile currentTile){
        guiAdapter.setTileID(""+currentTile.tileType);
        MoveInformation move = new MoveInformation(new Location(guiAdapter.getX(), guiAdapter.getY()), guiAdapter.getRotation(), guiAdapter.getMeeple());
        return move;
    }

}
