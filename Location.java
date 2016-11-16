
public class Location {
	int Row, Col;
	
	Location(int Row, int Col){
		this.Row = Row;
		this.Col = Col;
	}

	boolean isEqual(Location other){
		return ((this.Row == other.Row) && (this.Col == other.Col));
	}
}
