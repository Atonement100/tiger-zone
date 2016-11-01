class GameController {

public:
	GameController(); //Default constructor. May be reasonable to take in number of players here.
	initializeGame();

private:
	//gameBoard array of tiles
	//array of player states
	//tileStack - stack of all undrawn tiles
	int currentPlayerID; //ID of player whose turn it currently is
	int currentTile; //Most recently drawn tile, that is the tile to be played next

	notifyTurn();
	verifyMove();
	drawTile();
}
