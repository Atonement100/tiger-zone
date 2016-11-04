#include <algorithm> //std::reverse
#include <iostream>
#include <string>

#include "Debug.h"
#include "Tile.h"

Tile::Tile(){
	this->hasMonastery = false;
	this->roadsEnd = false;
	this->citiesAreIndependent = false;
	this->hasShield = false;
	this->edges = std::vector<int>(NUM_TILE_EDGES);
	this->tileNodes = std::vector<GraphNode>(NUM_TILE_NODES);
	this->tileType = UNKNOWN_TILE_TYPE;
	return;
}

Tile::Tile(bool bHasMonastery, bool bRoadsEnd, bool bCitiesAreIndependent, bool bHasShield, std::vector<int> Edges)
{
	this->hasMonastery = bHasMonastery;
	this->roadsEnd = bRoadsEnd;
	this->citiesAreIndependent = bCitiesAreIndependent;
	this->hasShield = bHasShield;
	this->edges = Edges;
	this->tileType = UNKNOWN_TILE_TYPE;
	return;
}

Tile::Tile(bool bHasMonastery, bool bRoadsEnd, bool bCitiesAreIndependent, bool bHasShield, std::vector<int> Edges, char cTileType)
{
	this->hasMonastery = bHasMonastery;
	this->roadsEnd = bRoadsEnd;
	this->citiesAreIndependent = bCitiesAreIndependent;
	this->hasShield = bHasShield;
	this->edges = Edges;

	this->tileNodes.resize(NUM_TILE_NODES);
	for (unsigned int edgeIndex = 0; edgeIndex < this->edges.size(); edgeIndex++) {
		//First and third nodes are always plains if the edge is not a city, and a city if the edge is a city. Second node is always equal to edge type.
		tileNodes[edgeIndex * (NODES_PER_EDGE)] = (edges[edgeIndex] == TerrainType::City) ? GraphNode(TerrainType::City) : GraphNode(TerrainType::Plains);
		tileNodes[edgeIndex * (NODES_PER_EDGE) + 1] = GraphNode(edges[edgeIndex]);
		tileNodes[edgeIndex * (NODES_PER_EDGE) + 2] = (edges[edgeIndex] == TerrainType::City) ? GraphNode(TerrainType::City) : GraphNode(TerrainType::Plains);
	}
	tileNodes[NUM_TILE_NODES-1] = GraphNode(TerrainType::Monastery);
	
	for (unsigned int nodeIndex = 0; nodeIndex < this->tileNodes.size() - 1; nodeIndex++) { //-1 because this part should handle only outer nodes
		auto currNode = &this->tileNodes[nodeIndex];
		auto nextNode = &this->tileNodes[(nodeIndex + 1) % (NUM_TILE_NODES - 1)];
		if (currNode->nodeType == nextNode->nodeType) {
			currNode->connectedNodes.push_back(nextNode);
			nextNode->connectedNodes.push_back(currNode);
		}
	}

	this->tileType = cTileType;
	return;
}

Tile::~Tile() {

	return;
}

void Tile::RotateClockwise(int rotations) {
	rotations = rotations % NUM_TILE_EDGES; //4 rotations is equivalent to zero, 5 to 1, etc.
	
	std::reverse(edges.begin(), edges.end());	//Simple and quick circular shift method
	std::reverse(edges.begin(), edges.begin() + rotations - 1);
	std::reverse(edges.begin() + rotations, edges.end());

	std::reverse(tileNodes.begin(), tileNodes.end() - 1); //Don't include the last vector item (tile-center), it should never move!
	std::reverse(tileNodes.begin(), tileNodes.begin() + (rotations * NODES_PER_EDGE) - 1);
	std::reverse(tileNodes.begin() + (rotations * NODES_PER_EDGE), edges.end()-1)

	return;
}


std::vector<int> Tile::GetEdges()
{
	return this->edges;
}

bool Tile::GetHasMonastery()
{
	return this->hasMonastery;
}

bool Tile::GetRoadsEnd()
{
	return this->roadsEnd;
}

bool Tile::GetCitiesAreIndependent()
{
	return this->citiesAreIndependent;
}

bool Tile::GetHasShield()
{
	return this->hasShield;
}

std::string ConvertEdgeEnumToString(int edgeValue) {
	switch (edgeValue) {
	case TerrainType::Plains: return "Plains";
	case TerrainType::Road: return "Road";
	case TerrainType::City: return "City";
	default: return "";
	}
}

void Tile::PrintTileInformation(bool printVerbose) {
	if (printVerbose) {
		std::cout << "Tile Type: " << tileType << std::endl
			<< "Has Monastery: " << ((hasMonastery) ? "Yes" : "No") << std::endl
			<< "Roads End Here: " << ((roadsEnd) ? "Yes" : "No") << std::endl
			<< "Independent Cities: " << ((citiesAreIndependent) ? "Yes" : "No") << std::endl
			<< "Has Shield: " << ((hasShield) ? "Yes" : "No") << std::endl
			<< "North: " << ConvertEdgeEnumToString(edges[0]) << std::endl
			<< "East: " << ConvertEdgeEnumToString(edges[1]) << std::endl
			<< "South: " << ConvertEdgeEnumToString(edges[2]) << std::endl
			<< "West: " << ConvertEdgeEnumToString(edges[3]) << std::endl;
	}
	else {
		std::cout << tileType << " " << hasMonastery << " " << roadsEnd << " " << citiesAreIndependent << " " << hasShield
			<< " " << edges[0] << " " << edges[1] << " " << edges[2] << " " << edges[3] << std::endl;
	}

	return;
}

void Tile::PrintTileNodeInformation(bool printVerbose) {
	if (printVerbose) {

	}
	else {
		std::cout << "NW ";
		for (unsigned int Index = 0; Index < tileNodes.size(); Index++) {
			if (Index % NODES_PER_EDGE == 0) std::cout << " ";
			std::cout << tileNodes[Index].nodeType;
		}
		std::cout << std::endl;
	}
}

bool Tile::operator==(const Tile& tile) {
	if (this->tileType != UNKNOWN_TILE_TYPE) {
		return (this->tileType == tile.tileType);
	}
	
	//If we do not control the input tiles, then we will need to /always/ use the following code
	//which more thoroughly checks if two tiles are equal. As long as we know the tile types are
	//valid then we can simply compare those, as above.

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
