#ifndef MEEPLE_H
#define MEEPLE_H

typedef struct BoardLocation {
	int x;
	int y;
} BoardLocation;

class Meeple {

public:
	Meeple(); //Default constructor. 
	~Meeple();

private:
	int status,
		ownerId;
	BoardLocation location;	
};

#endif //MEEPLE_H