public class ComputerPlayerController extends PlayerController {

    @Override
    MoveInformation processMove(){
        System.out.println("computer processing move");
        return new MoveInformation(new Location(2, 2), 0, -1);
    }
}
