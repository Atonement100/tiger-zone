typedef struct BoardLocation {
	int x;
	int y;
} BoardLocation;

class Meeple {

public:
	Meeple(); //Default constructor. 

private:
	int status,
		ownerId;
	BoardLocation location;	
};
