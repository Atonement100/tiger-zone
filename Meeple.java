
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
				return MeepleStatusEnum.onField;
			case 1:
			case 4:
				return MeepleStatusEnum.onRoad;
			case 2:
			case 3:
			case 5:
				return MeepleStatusEnum.onCity;
			case 6:
				return MeepleStatusEnum.onMonastery;
			case 9:
				return MeepleStatusEnum.onNone;
			default:
				throw new IllegalStateException();

		}
	}
}