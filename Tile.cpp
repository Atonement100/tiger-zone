#include "Tile.h"

Tile::Tile(){


}

Tile::Tile(bool bHasMonastery, bool bRoadsEnd, bool bCitiesAreIndependent, bool bHasShield, int Edges[])
{
	this->hasMonastery = bHasMonastery;
	this->roadsEnd = bRoadsEnd;
	this->citiesAreIndependent = bCitiesAreIndependent;
	this->hasShield = bHasShield;
	this->edges = Edges;
}

Tile::~Tile() {

}
