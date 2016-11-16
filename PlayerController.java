public class PlayerController {
    private Meeple[] meeples;

    Location processMove(){
        System.out.println("PlayerController processMove() not overridden");
        return new Location(-1, -1);
    }

}
