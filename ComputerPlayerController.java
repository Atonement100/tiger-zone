import Gui.GuiAdapter;

import java.util.*;
public class ComputerPlayerController extends PlayerController {
    public GuiAdapter guiAdapter;
    public ComputerPlayerController(GuiAdapter g) {
        this.guiAdapter = g;
    }

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
    void processConfirmedMove(Tile confirmedTile, MoveInformation moveInfo, int playerConfirmed) {
        super.processConfirmedMove(confirmedTile, moveInfo, playerConfirmed);

        System.out.println("Computer player has confirmed the recent move Row: " + moveInfo.tileLocation.Row + " Col: " + moveInfo.tileLocation.Col + " Rotation: " + moveInfo.tileRotation);
    }

}
