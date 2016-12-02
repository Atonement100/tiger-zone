public class MoveInformation {
    Location tileLocation;
    int tileRotation;
    int meepleLocation;
    int meepleZone;

    MoveInformation(){
        this.tileLocation = new Location(-1,-1);
        this.tileRotation = -1;
        this.meepleLocation = -1;
        this.meepleZone = -1;

    }

    MoveInformation(Location tileLocation, int tileRotation, int meepleLocation){
        this.tileLocation = tileLocation;
        this.tileRotation = tileRotation;
        this.meepleLocation = meepleLocation;
        this.meepleZone = -1;
    }

    MoveInformation(Location tileLocation, int tileRotation, int meepleLocation, int meepleZone){
        this.tileLocation = tileLocation;
        this.tileRotation = tileRotation;
        this.meepleLocation = meepleLocation;
        this.meepleZone = meepleZone;
    }

    @Override
    public String toString(){
        return "(" + tileLocation.Row + ", " + tileLocation.Col + "), " + tileRotation + ", " + meepleLocation;
    }
}
