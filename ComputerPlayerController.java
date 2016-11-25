import Gui.GuiAdapter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.*;

public class ComputerPlayerController extends PlayerController {
    public GuiAdapter guiAdapter;
    private HashSet<Location> possibleTargets = new HashSet<>();
    
    public ComputerPlayerController(GuiAdapter g, GameBoard board) {
        this.guiAdapter = g;
        this.localMeeples = new Meeple[7];
        this.localGameBoard = board;
    }

    @Override
    MoveInformation processPlayerMove(Tile currentTile){
        return getPlayerMove(currentTile);
    }

    @Override
    protected MoveInformation getPlayerMove(Tile currentTile){
        //AI Logic / fx calls may come from here
        System.out.println("computer processing move");
        MoveInformation moveInfo = new MoveInformation();
        moveInfo.meepleLocation = -1;


        for (Location possibleLoc : possibleTargets){
            for (int possibleRot = 0; possibleRot < 4; possibleRot++){
                if (this.localGameBoard.verifyTilePlacement(currentTile, possibleLoc, possibleRot)){
                    moveInfo.tileLocation = possibleLoc;
                    moveInfo.tileRotation = possibleRot;
                    return moveInfo;
                }
            }
        }

        System.out.println("No viable location to place :(");
        return new MoveInformation(new Location(-1, -1), -1, -1);
    }

    @Override
    void processConfirmedMove(Tile confirmedTile, MoveInformation moveInfo, int playerConfirmed) {
        //super.processConfirmedMove(confirmedTile, moveInfo, playerConfirmed);
        System.out.println("Computer player has confirmed the recent move Row: " + moveInfo.tileLocation.Row + " Col: " + moveInfo.tileLocation.Col + " Rotation: " + moveInfo.tileRotation);

        Location moveLocation = moveInfo.tileLocation;

        for (Location loc : localGameBoard.getEmptyNeighboringLocations(moveLocation)){
            if (loc != null){
                possibleTargets.add(loc);
            }
        }
        possibleTargets.remove(moveLocation);

        for (Location loc : possibleTargets){
            System.out.println("target: " + loc.Row + ", " + loc.Col);
        }
    }

}
