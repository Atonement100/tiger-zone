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
	std::cout << "Retrieving tileset. . ." << std::endl;

	RetrieveTileStack();

	return;
}

void GameController::RetrieveTileStack() {
	TileSetRetriever tileSetRetriver;
	tileStack = tileSetRetriver.ImportTileSet();

	std::cout << "TileSet Retrieved. . ." << std::endl;
}