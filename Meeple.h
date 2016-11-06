#ifndef MEEPLE_H
#define MEEPLE_H

typedef struct BoardLocation {
	int x;
	int y;
} BoardLocation;

class Meeple {

public:
	Meeple(int ownerId); //Default constructor. 
	void updateStatus(int status);
	void updateLocation(int x, int y);
	int getOwnerId();
	int getLocationX();
	int getLocationY();
	int getStatus();
	~Meeple();

private:
	int status,
		ownerId;
	BoardLocation location;	
};

#endif //MEEPLE_H
