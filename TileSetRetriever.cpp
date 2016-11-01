#include "TileSetRetriever.h"


TileSetRetriever::TileSetRetriever(){

}

TileSetRetriever::~TileSetRetriever() {

}

std::vector<Tile> TileSetRetriever::FetchTileSet()
{
	std::vector<Tile> Deck = std::vector<Tile>();

	Deck.push_back(Tile(false, false, false, true, [EdgeType::Plains, EdgeType::Plains, EdgeType::City, EdgeType::City]));

	return Deck;
}
