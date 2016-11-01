#include "TileSetRetriever.h"


TileSetRetriever::TileSetRetriever(){

	return;
}

TileSetRetriever::~TileSetRetriever() {

	return;
}

std::vector<Tile> TileSetRetriever::FetchTileSet()
{
	std::vector<Tile> Deck = std::vector<Tile>();

	std::vector<int> Edges =  { EdgeType::Plains, EdgeType::Road, EdgeType::City, EdgeType::City };
	Deck.push_back(Tile(false, false, false, true, Edges));

	return Deck;
}
