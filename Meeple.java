
public class Meeple {
	int owner;
	int status;
	int coordX;
	int coordY;
	
	public Meeple(){
		owner = -1;
		status = -1;
		coordX = -1;
		coordY = -1;
	}
	
	public Meeple(int owner){
		this.owner = owner;
		status = 0;
		coordX = -1;
		coordY = -1;
	}
	
	void updateLocation(int x, int y){
		coordX = x;
		coordY = y;
	}
	
	void updateStatus(int status){
		this.status = status;
	}
	
	int getOwner(){
		return owner;
	}
	
	int getX(){
		return coordX;
	}
	
	int getY(){
		return coordY;
	}
	
	int getStatus(){
		return status;
	}
}