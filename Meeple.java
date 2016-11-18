
public class Meeple {
	int owner;
	MeepleStatusEnum status;
	Location location;
	
	//status = 0 : meeple is not on the board
	//status = 1 : meeple is on a field
	//status = 2 : meeple is on a city
	//status = 3 : meeple is on a road
	//status = 4 : meeple in on a monastery
	
	public Meeple(){
		this.owner = -1;
		this.status = MeepleStatusEnum.onNone;
		this.location = new Location(-1,-1);
	}
	
	public Meeple(int owner){
		this.owner = owner;
		this.status = MeepleStatusEnum.onNone;
		this.location = new Location(-1, -1);
	}
	
	void updateLocation(Location location){
		this.location = location;
	}
	
	void setStatus(int newStatus){
		this.status = convertIntToMeepleStatus(newStatus);
	}

	void setStatus(MeepleStatusEnum newStatus){
		this.status = newStatus;
	}
	
	int getOwner(){
		return owner;
	}
	
	Location getLocation() { return location; }
	
	MeepleStatusEnum getStatus(){
		return status;
	}

	public MeepleStatusEnum convertIntToMeepleStatus (int status){
		switch (status) {
			case 0:
				return MeepleStatusEnum.onNone;
			case 1:
				return MeepleStatusEnum.onField;
			case 2:
				return MeepleStatusEnum.onCity;
			case 3:
				return MeepleStatusEnum.onRoad;
			case 4:
				return MeepleStatusEnum.onMonastery;
			default:
				throw new IllegalStateException();

		}
	}
}