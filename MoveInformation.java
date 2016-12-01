public class MoveInformation {
    Location tileLocation;
    int tileRotation;
    int tigerLocation;
    int tigerZone;

    MoveInformation(){
        this.tileLocation = new Location(-1,-1);
        this.tileRotation = -1;
        this.tigerLocation = -1;
        this.tigerZone = -1;

    }

    MoveInformation(Location tileLocation, int tileRotation, int tigerLocation){
        this.tileLocation = tileLocation;
        this.tileRotation = tileRotation;
        this.tigerLocation = tigerLocation;
        this.tigerZone = -1;
    }

    MoveInformation(Location tileLocation, int tileRotation, int tigerLocation, int tigerZone){
        this.tileLocation = tileLocation;
        this.tileRotation = tileRotation;
        this.tigerLocation = tigerLocation;
        this.tigerZone = tigerZone;
    }

    @Override
    public String toString(){
        String str = "(" + tileLocation.Row + ", " + tileLocation.Col + "), " + tileRotation + ", " + tigerLocation;
        return str;
    }
}
