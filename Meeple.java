
public class Meeple {
	int owner;
	int ID;
	MeepleStatusEnum status;
	Location location;
	
	public Meeple(){
		this.owner = -1;
		this.status = MeepleStatusEnum.onNone;
		this.location = new Location(-1,-1);
		this.ID = 0;
	}
	
	public Meeple(int owner, int ID){
		this.owner = owner;
		this.status = MeepleStatusEnum.onNone;
		this.location = new Location(-1, -1);
		this.ID = ID;
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