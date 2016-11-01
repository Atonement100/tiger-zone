#define NUM_TILE_EDGES 4

class Tile {

public:
	Tile(); //Default constructor. 

private:
	bool hasMonastery,
		 roadsEnd,
		 hasShield;
	int edges[NUM_TILE_EDGES]; 

	rotateTile();
};
