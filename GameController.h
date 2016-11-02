#include <vector>

#include "Tile.h"
#include "PlayerState.h"
#include "TileSetRetriever.h"

#ifndef GAMECONTROLLER_H
#define GAMECONTROLLER_H

#define NUM_PLAYERS 2

class GameController {

public:
	GameController(); //Default constructor. May be reasonable to take in number of players..
	~GameController();

	void InitializeGame();

private:
	std::vector<Tile> gameBoard;
	PlayerState playerStates[NUM_PLAYERS];
	std::vector<Tile> tileStack; //Stack of all undrawn tiles
	int currentPlayerID; //ID of player whose turn it currently is
	int currentTile; //Most recently drawn tile, that is the tile to be played next

	void RetrieveTileStack();
	void PrepareTileStack();
	void NotifyTurn();
	void VerifyMove();
	void DrawTile();

	void PrintTileStack();

	const Tile startingTile = Tile(false, false, false, false, {0, 1, 2, 1});
};

#endif //GAMECONTROLLER_H