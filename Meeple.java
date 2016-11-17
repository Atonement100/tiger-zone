
public class Meeple {
	int owner;
	int status;
	Location location;
	
	//status = 0 : meeple is not on the board
	//status = 1 : meeple is on a field
	//status = 2 : meeple is on a city
	//status = 3 : meeple is on a road
	//status = 4 : meeple in on a monestary
	
	public Meeple(){
		this.owner = -1;
		this.status = -1;
		this.location = new Location(-1,-1);
	}
	
	public Meeple(int owner){
		this.owner = owner;
		this.status = 0;
		this.location = new Location(-1, -1);
	}
	
	void updateLocation(Location location){
		this.location = location;
	}
	
	void updateStatus(int status){
		this.status = status;
	}
	
	int getOwner(){
		return owner;
	}
	
	Location getLocation() { return location; }
	
	int getStatus(){
		return status;
	}
}