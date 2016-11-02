#include <vector>

#ifndef TILE_H
#define TILE_H

#define NUM_TILE_EDGES 4

enum EdgeType {
	Plains,
	Road,
	City
};

class Tile {

public:
	Tile(); //Default constructor. 
	Tile(bool bHasMonastery, bool bRoadsEnd, bool bCitiesAreIndependent, bool bHasShield, std::vector<int> Edges);
	~Tile();

	/*Mutators*/
	void RotateClockwise(int Rotations);	//Rotates 90 degrees times the number of rotations. 
	
	/*Accessors*/
	std::vector<int> GetEdges();				

	bool operator==(const Tile& tile);

private:
	bool hasMonastery,			//Tile contains a monastery, which may have a meeple placed on it, and is scored differently.
		 roadsEnd,				//All roads connected to this tile may view this tile as an endpoint.
		 citiesAreIndependent,  //There are cases when a tile may have 2 city edges, but these cities are independent of one another.
		 hasShield;				//Increases value of tile by 1.
	std::vector<int> edges;		//Array of edge types. 0 is North, 1 is East, 2 South, 3 West.
								
};

#endif //TILE_H