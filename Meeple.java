
public class Meeple {
	int owner;
	int status;
	Location location;
	
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