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
	std::cout << std::endl << std::endl;

	while (tileStack.size() > 0) {
		DrawTile();
		currentTile.PrintTileInformation(0);
		//currentTile.PrintTileNodeInformation(1);
	}
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
			std::swap(tileStack[Index], tileStack.back());				//Swap item to be removed to the back, since order doesn't matter 
			currentTile = tileStack.back();
			tileStack.pop_back();										//We can save time by removing only the final element
			break;
		}
	}

	std::srand(time(NULL));	 //Seed random_shuffle
	std::random_shuffle(tileStack.begin(), tileStack.end());
}

void GameController::PrintTileStack()
{
	std::cout << "T M R C S 1 2 3 4" << std::endl;
	for (unsigned int tileIndex = 0; tileIndex < tileStack.size(); tileIndex++) {
		Tile tempTileRef = tileStack[tileIndex];
		tempTileRef.PrintTileInformation(0);
	}

}

void GameController::DrawTile()
{
	currentTile = tileStack.back();
	tileStack.pop_back();
	return;
}
