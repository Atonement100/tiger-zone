public class MoveInformation {
	/*
	 * MoveInformation stores the information for a move
	 * A move consists of a location, rotation, tiger location for local representation, and tiger zone for server representation
	 * 
	 */
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
        return "(" + tileLocation.Row + ", " + tileLocation.Col + "), " + tileRotation + ", " + tigerLocation;
    }
}
