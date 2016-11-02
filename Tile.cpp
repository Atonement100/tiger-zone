#include <iostream>

#include "Tile.h"

Tile::Tile(){
	this->hasMonastery = false;
	this->roadsEnd = false;
	this->citiesAreIndependent = false;
	this->hasShield = false;
	this->edges = std::vector<int>(NUM_TILE_EDGES);
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
