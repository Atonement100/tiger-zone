#include <vector>

#ifndef TILE_H
#define TILE_H

#define NUM_TILE_EDGES 4
#define NODES_PER_EDGE 3
#define NUM_TILE_NODES (NUM_TILE_EDGES*NODES_PER_EDGE + 1) //i.e. + 1 center node
#define UNKNOWN_TILE_TYPE '?'

enum TerrainType {
	Plains,
	Road,
	City,
	Monastery,
	None
};

typedef struct GraphNode {
	int nodeType;
	bool featureEnd;
	std::vector<GraphNode*> connectedNodes;

	GraphNode() {
		return;
	}

	GraphNode(int iNodeType) {
		this->nodeType = iNodeType;
		this->featureEnd = false;
		this->connectedNodes = std::vector<GraphNode*>();
		return;
	}
} GraphNode;

class Tile {

public:
	Tile(); //Default constructor. 
	Tile(bool bHasMonastery, bool bRoadsEnd, bool bCitiesAreIndependent, bool bHasShield, std::vector<int> Edges);
	Tile(bool bHasMonastery, bool bRoadsEnd, bool bCitiesAreIndependent, bool bHasShield, std::vector<int> Edges, char cTileType);
	~Tile();

	/*Mutators*/
	void RotateClockwise(int rotations);	//Rotates 90 degrees times the number of rotations. 
	
	/*Accessors*/
	std::vector<int> GetEdges();	
	bool GetHasMonastery();
	bool GetRoadsEnd();
	bool GetCitiesAreIndependent();
	bool GetHasShield();

	void PrintTileInformation(bool printVerbose = false);
	void PrintTileNodeInformation(bool printVerbose = false);

	/*Operators*/
	bool operator==(const Tile& tile);

private:
	char tileType;
	bool hasMonastery,			//Tile contains a monastery, which may have a meeple placed on it, and is scored differently.
		 roadsEnd,				//All roads connected to this tile may view this tile as an endpoint.
		 citiesAreIndependent,  //There are cases when a tile may have 2 city edges, but these cities are independent of one another.
		 hasShield;				//Increases value of tile by 1.
	std::vector<int> edges;		//Array of edge types. 0 is North, 1 is East, 2 South, 3 West.
	std::vector<GraphNode> tileNodes;
};

#endif //TILE_H