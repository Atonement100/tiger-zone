#include <vector>

#include "Tile.h"

#ifndef TILESET_RETRIEVER_H
#define TILESET_RETRIEVER_H

class TileSetRetriever {

public:
	TileSetRetriever(); //Default constructor. May be reasonable to take in number of players..
	~TileSetRetriever();
	std::vector<Tile> ImportTileSet();

private:
	std::vector<Tile> ImportTileSetFromFile();
};

#endif //TILESET_RETRIEVER_H