public class MoveInformation {
    Location tileLocation;
    int tileRotation;
    int meepleLocation;

    MoveInformation(){
        this.tileLocation = new Location(-1,-1);
        this.tileRotation = -1;
        this.meepleLocation = -1;
    }

    MoveInformation(Location tileLocation, int tileRotation, int meepleLocation){
        this.tileLocation = tileLocation;
        this.tileRotation = tileRotation;
        this.meepleLocation = meepleLocation;
    }

}
