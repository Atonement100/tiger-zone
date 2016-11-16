import java.util.*;

public class HumanPlayerController extends PlayerController {

    @Override
    Location processMove(){
        System.out.println("human processing move");
        Scanner scanner = new Scanner(System.in);
        scanner.next();

        return new Location(1,1);
    }

}
