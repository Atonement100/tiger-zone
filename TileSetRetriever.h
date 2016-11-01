#include <vector>

#include "Tile.h"

#ifndef TILESET_RETRIEVER_H
#define TILESET_RETRIEVER_H

#define NUM_PLAYERS 2

class TileSetRetriever {

public:
	TileSetRetriever(); //Default constructor. May be reasonable to take in number of players..
	~TileSetRetriever();

	std::vector<Tile> FetchTileSet();

private:

};

#endif //TILESET_RETRIEVER_H