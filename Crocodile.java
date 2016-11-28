
public class Crocodile {
	int owner;
	int ID;
	Location location;
	// if being used or not - not sure if necessary as you can just tell through looking at location
	boolean status; 

	public Crocodile(){
		this.owner = -1;
		this.location = new Location (-1,-1);
		this.ID = 0;
		this.status = false;
	}
	public Crocodile(int owner, int ID){
		this.owner = owner;
		this.location = new Location (-1,-1);
		this.ID = ID;
		this.status = false;
	}
	void updateLocation(Location location){
		this.location = location;
	}
	int getOwner(){
		return owner;
	}
	
	Location getLocation() { return location; }
	
	boolean getStatus(){
		Location checkLocation = new Location (-1, -1);
		if(this.location == checkLocation){
			return false;
		}
		else {
			return true;
		}
	}
}