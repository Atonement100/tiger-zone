class PlayerState {

public:
	PlayerState(); //Default constructor. 

private:
	int playerId; //Assigned by gameHandler
	int score;
	//reference to current tile?
	bool isWaiting; //Is waiting for turn?
}
