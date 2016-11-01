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
	Tile(bool bHasMonastery, bool bRoadsEnd, bool bCitiesAreIndependent, bool bHasShield, int Edges[]);
	~Tile();

	RotateTileClockwise(int Rotations);	//Rotates 90 degrees times the number of rotations. 

private:
	bool hasMonastery,			//Tile contains a monastery, which may have a meeple placed on it, and is scored differently.
		 roadsEnd,				//All roads connected to this tile may view this tile as an endpoint.
		 citiesAreIndependent,  //There are cases when a tile may have 2 city edges, but these cities are independent of one another.
		 hasShield;				//Increases value of tile by 1.
	int edges[NUM_TILE_EDGES];	//Array of edge types. 0 is North, 1 is East, 2 South, 3 West.
								
};

#endif //TILE_H