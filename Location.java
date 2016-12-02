import java.util.Objects;

public class Location {
	int Row, Col;
	
	/*
	 * Holds a location based on row and column values
	 */
	
	Location(int Row, int Col){
		this.Row = Row;
		this.Col = Col;
	}

	boolean isEqual(Location other){
		return ((this.Row == other.Row) && (this.Col == other.Col));
	}

	@Override
	public boolean equals(Object other){
		if (this == other) return true;
		if (! (other instanceof Location)) return false;
		Location otherLoc = (Location)other;
		return ((this.Row == otherLoc.Row) && (this.Col == otherLoc.Col));
	}

	@Override
	public int hashCode(){
		return 163 * Row + Col;
	}
}
