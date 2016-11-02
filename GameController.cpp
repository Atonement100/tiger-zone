#include <algorithm> // random_shuffle, swap
#include <iostream>

#include "GameController.h"


GameController::GameController(){
	InitializeGame();

	return;
}

GameController::~GameController() {

	return;
}

void GameController::InitializeGame() {
	RetrieveTileStack();
	PrepareTileStack();
	PrintTileStack();

	return;
}

void GameController::RetrieveTileStack() {
	TileSetRetriever tileSetRetriver;
	tileStack = tileSetRetriver.ImportTileSet();
	tileSetRetriver.~TileSetRetriever();
}

void GameController::PrepareTileStack() {
	for (unsigned int Index = 0; Index < tileStack.size(); Index++) {	//Remove one instance of the starting tile from the tileStack
		if (tileStack[Index] == startingTile) {
			std::swap(tileStack[Index], tileStack.back());		//Swap item to be removed to the back, since order doesn't matter 
			tileStack.pop_back();										//we can save time by removing only the final element
			break;
		}
	}

	std::srand(time(NULL));	 //Seed random_shuffle
	std::random_shuffle(tileStack.begin(), tileStack.end());
}

void GameController::PrintTileStack()
{
	std::cout << "M R C S 1 2 3 4" << std::endl;
	for (unsigned int tileIndex = 0; tileIndex < tileStack.size(); tileIndex++) {
		Tile tempTileRef = tileStack[tileIndex];
		std::vector<int> tempTileEdges = tempTileRef.GetEdges();

		std::cout << tempTileRef.GetHasMonastery() << " " << tempTileRef.GetRoadsEnd() << " " << tempTileRef.GetCitiesAreIndependent() << " " << tempTileRef.GetHasShield()
			<< " " << tempTileEdges[0] << " " << tempTileEdges[1] << " " << tempTileEdges[2] << " " << tempTileEdges[3] << std::endl;
	}

}
