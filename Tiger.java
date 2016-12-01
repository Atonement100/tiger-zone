
public class Tiger {
	int owner;
	int ID;
	TigerStatusEnum status;
	Location location;
	
	public Tiger(){
		this.owner = -1;
		this.status = TigerStatusEnum.onNone;
		this.location = new Location(-1,-1);
		this.ID = 0;
	}
	
	public Tiger(int owner, int ID){
		this.owner = owner;
		this.status = TigerStatusEnum.onNone;
		this.location = new Location(-1, -1);
		this.ID = ID;
	}
	
	void updateLocation(Location location){
		this.location = location;
	}
	
	void setStatus(int newStatus){
		this.status = convertIntToTigerStatus(newStatus);
	}

	void setStatus(TigerStatusEnum newStatus){
		this.status = newStatus;
	}
	
	int getOwner(){
		return owner;
	}
	
	Location getLocation() { return location; }

	TigerStatusEnum getStatus(){
		return status;
	}

	public TigerStatusEnum convertIntToTigerStatus (int status){
		switch (status) {
			case 0:
				return TigerStatusEnum.onJungle;
			case 1:
			case 4:
				return TigerStatusEnum.onTrail;
			case 2:
			case 3:
			case 5:
				return TigerStatusEnum.onLake;
			case 6:
				return TigerStatusEnum.onDen;
			case 9:
				return TigerStatusEnum.onNone;
			default:
				throw new IllegalStateException();

		}
	}
}