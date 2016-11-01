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

	GetTileSet();

	std::vector<int> aa = tileStack[0].GetEdges();
	for (std::vector<int>::iterator it = aa.begin(); it != aa.end(); it++) {
		std::cout << aa[*it];
	}

	//std::cout << tileStack[0].GetEdges() << std::endl;

	return;
}

std::vector<Tile> GameController::GetTileSet() {
	TileSetRetriever tileSetRetriver;
	tileStack = tileSetRetriver.FetchTileSet();

	std::cout << "TileSet Retrieved. . ." << std::endl;

	return tileStack;
}