#ifndef PLAYERSTATE_H
#define PLAYERSTATE_H

class PlayerState {

public:
	PlayerState(); //Default constructor. 
	~PlayerState();

private:
	int playerId; //Assigned by gameHandler
	int score;
	//reference to current tile?
	bool isWaiting; //Is waiting for turn?
};

#endif //PLAYERSTATE_H