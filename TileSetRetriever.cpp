#include <fstream>
#include <iostream>

#include "TileSetRetriever.h"


TileSetRetriever::TileSetRetriever(){

	return;
}

TileSetRetriever::~TileSetRetriever() {

	return;
}

std::vector<Tile> TileSetRetriever::ImportTileSet()
{
	/* This should act as an interfact function for the Game Controller, redirecting to a function here for easy interchangability */
	return ImportTileSetFromFile();
}

std::vector<Tile> TileSetRetriever::ImportTileSetFromFile() {
	/* This processes a file for the tileset, expecting the file to be presented in the format:								*
	 * Quantity of tile | Has Monastery | Roads End | Cities Independent | Has Shield | Edge 1 | Edge 2 | Edge 3 | Edge 4	*
	 * Note the pipes are just for clarity of reading, the file should only have integers representing each value			*/
	std::ifstream tileSetFile;
	tileSetFile.open("tileset.txt");

	if (!tileSetFile.is_open()) {
		std::cout << "Error with importing tileset from file" << std::endl;
		return std::vector<Tile>();
	}

	std::vector<Tile> tileSet = std::vector<Tile>();
	std::vector<int> fileInput(9);
	while (tileSetFile >> fileInput[0]) { 
		for (unsigned int Index = 1; Index < fileInput.size(); Index++) {
			tileSetFile >> fileInput[Index];
		}

		Tile currentInputTile(fileInput[1], fileInput[2], fileInput[3], fileInput[4], { fileInput[5], fileInput[6], fileInput[7], fileInput[8] });
		for (int Quantity = fileInput[0]; Quantity > 0; Quantity--) {
			tileSet.push_back(currentInputTile);
		}
	}

	return tileSet;
}