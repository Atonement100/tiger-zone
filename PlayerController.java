public class PlayerController {
    private Meeple[] meeples;

    MoveInformation processMove(){
        System.out.println("PlayerController processMove() not overridden");
        return new MoveInformation(new Location(-1, -1), -1, -1);
    }

}
