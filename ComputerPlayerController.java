public class ComputerPlayerController extends PlayerController {

    @Override
    Location processMove(){
        System.out.println("computer processing move");
        return new Location(1,1);
    }
}
