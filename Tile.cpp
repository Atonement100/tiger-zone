#include "Tile.h"

Tile::Tile(){

	return;
}

Tile::Tile(bool bHasMonastery, bool bRoadsEnd, bool bCitiesAreIndependent, bool bHasShield, std::vector<int> Edges)
{
	this->hasMonastery = bHasMonastery;
	this->roadsEnd = bRoadsEnd;
	this->citiesAreIndependent = bCitiesAreIndependent;
	this->hasShield = bHasShield;
	this->edges = Edges;
	return;
}

Tile::~Tile() {

	return;
}

std::vector<int> Tile::GetEdges()
{
	return edges;
}
