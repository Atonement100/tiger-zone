#include <iostream>
#include <algorithm> //std::reverse

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

void Tile::RotateClockwise(int Rotations)
{
	Rotations = Rotations % NUM_TILE_EDGES; //4 rotations is equivalent to zero, 5 to 1, etc.
	
	std::reverse(edges.begin(), edges.end());	//Simple and quick circular shift method
	std::reverse(edges.begin(), edges.begin() + Rotations - 1);
	std::reverse(edges.begin() + Rotations, edges.end());

	return;
}


std::vector<int> Tile::GetEdges()
{
	return edges;
}

bool Tile::operator==(const Tile& tile) {
	if (this->hasMonastery == tile.hasMonastery &&
		this->roadsEnd == tile.roadsEnd &&
		this->citiesAreIndependent == tile.citiesAreIndependent &&
		this->hasShield == tile.hasShield) {

		std::vector<int> storedEdges = this->edges;

		for (int RotationAttempt = 0; RotationAttempt < NUM_TILE_EDGES; RotationAttempt++) {
			if (this->edges == tile.edges) {
				this->edges = storedEdges; //Restore edges to their original
				return true;
			}
			else this->RotateClockwise(1);
		}

		this->edges = storedEdges; //In case we fall through
	}

	return false;
}
