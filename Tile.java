import javax.smartcardio.TerminalFactory;
import java.util.ArrayList;

public class Tile {

	private static final int EDGES_PER_TILE = 4;
	private static final int NODES_PER_EDGE = 3;
	private static int identify = 0;
	
	int ID,
		rotations;
	int[] edgeValues;
	Edge[] edges = new Edge[EDGES_PER_TILE];
	Node middle;
	char tileType;
	boolean hasMonastery,
		 	roadsEnd,
			citiesAreIndependent;
	int animalType;


	/*public Tile(Edge[] edges, Node middle){
		this.edges = edges;
		this.middle = middle;
		this.connectNodes();
		this.ID = ++identify;
	}*/

	public Tile(boolean hasMonastery, boolean roadsEnd, boolean citiesAreIndependent, int animalType, int[] edgeValues, char tileType){
		this.hasMonastery = hasMonastery;
		this.roadsEnd = roadsEnd;
		this.citiesAreIndependent = citiesAreIndependent;
		this.tileType = tileType;
		this.rotations = 0;
		this.edgeValues = edgeValues;
		this.animalType = animalType;

		if (this.hasMonastery) this.middle = new Node(FeatureTypeEnum.Monastery.toChar());
		else this.middle = new Node('n'); //none

		//Count number of road + city edges
		int numRoads = 0, numCities = 0;
		ArrayList<Integer> cityLocations = new ArrayList<Integer>();
		for (int edgeIndex = 0; edgeIndex < edgeValues.length; edgeIndex++){
			if (edgeValues[edgeIndex] == FeatureTypeEnum.City.toInt()){
				numCities++;
				cityLocations.add(edgeIndex);
			}
			else if (edgeValues[edgeIndex] == FeatureTypeEnum.Road.toInt()){
				numRoads++;
			}
		}

		//Can't combine these two loops, this depends on numCities and numRoads.
		//This loop handles the creation of all the edges (and their composing nodes) for the tile.
		for (int edgeIndex = 0; edgeIndex < edgeValues.length; edgeIndex++){
			Node[] nodeBuffer = new Node[NODES_PER_EDGE];

			for (int nodeIndex = 0; nodeIndex < nodeBuffer.length; nodeIndex++) {
				switch (nodeIndex) {
					case 0:	// For first and third nodes, always field if not a city, otherwise always a city
					case 2:
						nodeBuffer[nodeIndex] = (edgeValues[edgeIndex] == FeatureTypeEnum.City.toInt()) ? new Node(FeatureTypeEnum.City.toChar()) : new Node(FeatureTypeEnum.Field.toChar());
						break;
					case 1: // For second node, always equal to the type of edge it is on.
						if (edgeValues[edgeIndex] == FeatureTypeEnum.City.toInt())
							nodeBuffer[nodeIndex] = new Node(FeatureTypeEnum.City.toChar());
						else if (edgeValues[edgeIndex] == FeatureTypeEnum.Road.toInt())
							nodeBuffer[nodeIndex] = new Node(FeatureTypeEnum.Road.toChar());
						else if (edgeValues[edgeIndex] == FeatureTypeEnum.Field.toInt())
							nodeBuffer[nodeIndex] = new Node(FeatureTypeEnum.Field.toChar());
						else
							System.out.println("Error with tile import: invalid edge value");
						break;
					default:
						System.out.println("Node buffer should be out of bounds");
						break;
				}
			}

			//Handle some of the edge cases down here. Certainly can be incorporated above, but makes it much harder to read
			if (edgeValues[edgeIndex] == FeatureTypeEnum.City.toInt() && (this.citiesAreIndependent || numCities == 1)){
				for (int nodeIndex = 0; nodeIndex < nodeBuffer.length; nodeIndex++){
					nodeBuffer[nodeIndex].featureType = FeatureTypeEnum.Wall;
				}
			}
			else if (edgeValues[edgeIndex] == FeatureTypeEnum.Road.toInt() && (this.roadsEnd || numRoads == 1)){
				nodeBuffer[1].featureType = FeatureTypeEnum.RoadEnd;
			}

			this.edges[edgeIndex] = new Edge(nodeBuffer);
		}

		//Now we need to handle city edge cases
		//numCities == 1 case is already handled above
		if (numCities == 2 && !this.citiesAreIndependent){ //Independent cities are already handled above
			int firstCityLoc = cityLocations.get(0), secondCityLoc = cityLocations.get(1);
			if (secondCityLoc - firstCityLoc == 2){ //In this case, the cities oppose each other on the tile
				for (int nodeIndex = 0; nodeIndex < NODES_PER_EDGE; nodeIndex++){
					int antiNodeIndex = NODES_PER_EDGE - 1 - nodeIndex; //Used to mirror the nodeIndex on edges opposite each other

					if (nodeIndex != 1) { //Prevents the type of the middle node from being changed
						edges[firstCityLoc].nodes[nodeIndex].featureType = FeatureTypeEnum.Wall;
						edges[secondCityLoc].nodes[antiNodeIndex].featureType = FeatureTypeEnum.Wall;
					}
					//Then connect opposing nodes
					edges[firstCityLoc].nodes[nodeIndex].neighbors.add( edges[secondCityLoc].nodes[antiNodeIndex] );
					edges[secondCityLoc].nodes[antiNodeIndex].neighbors.add( edges[firstCityLoc].nodes[nodeIndex] );
				}
			}
			else{ //Otherwise, the two city edges are adjacent on the tile
				if (secondCityLoc - firstCityLoc == 3) { int swap = firstCityLoc; firstCityLoc = secondCityLoc; secondCityLoc = swap; }
				//These [inner nodes] will be automatically linked later by the adjacency linker
				edges[firstCityLoc].nodes[2].featureType = FeatureTypeEnum.InnerWall;
				edges[secondCityLoc].nodes[0].featureType = FeatureTypeEnum.InnerWall;

				//Outer node set up. Need to be linked here.
				edges[firstCityLoc].nodes[0].featureType = FeatureTypeEnum.Wall;
				edges[secondCityLoc].nodes[2].featureType = FeatureTypeEnum.Wall;

				edges[firstCityLoc].nodes[0].neighbors.add( edges[secondCityLoc].nodes[2] );
				edges[secondCityLoc].nodes[2].neighbors.add( edges[secondCityLoc].nodes[2] );
			}
		}
		else if (numCities == 3){
			for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++){
				//This sets up the true wall neighboring the field
				if(edges[edgeIndex].nodes[2].featureType == FeatureTypeEnum.City && edges[(edgeIndex + 1) % edges.length].nodes[2].featureType == FeatureTypeEnum.Field){
					edges[edgeIndex].nodes[2].featureType = FeatureTypeEnum.Wall;
					edges[(edgeIndex + 2) % edges.length].nodes[0].featureType = FeatureTypeEnum.Wall;

					edges[edgeIndex].nodes[2].neighbors.add(edges[(edgeIndex + 2) % edges.length].nodes[0]);
					edges[(edgeIndex + 2) % edges.length].nodes[0].neighbors.add(edges[edgeIndex].nodes[2]);
				}

				//This sets up the inner connections to be linked later
				if((edgeValues[edgeIndex] == FeatureTypeEnum.City.toInt()) && edgeValues[(edgeIndex + 1) % edges.length] == FeatureTypeEnum.City.toInt()){
					edges[edgeIndex].nodes[2].featureType = FeatureTypeEnum.InnerWall;
					edges[(edgeIndex + 1) % edges.length].nodes[0].featureType = FeatureTypeEnum.InnerWall;
				}
			}
		}
		else if (numCities == 4){
			//Will be linked later by adjacency linker
			for (Edge edge : edges) {
				edge.nodes[0].featureType = FeatureTypeEnum.InnerWall;
				edge.nodes[2].featureType = FeatureTypeEnum.InnerWall;
			}
		}

		for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++){
			for (int nodeIndex = 0; nodeIndex < NODES_PER_EDGE - 1; nodeIndex++) { //We can only loop for 1st two nodes on an edge. Third node needs to be handled specially
				if (edges[edgeIndex].nodes[nodeIndex].featureType == edges[edgeIndex].nodes[nodeIndex + 1].featureType) {
					edges[edgeIndex].nodes[nodeIndex].neighbors.add(edges[edgeIndex].nodes[nodeIndex + 1]);
					edges[edgeIndex].nodes[nodeIndex + 1].neighbors.add(edges[edgeIndex].nodes[nodeIndex]);
				}
			}

			//3rd node handler
			if (this.citiesAreIndependent &&
					edges[edgeIndex].nodes[2].featureType == FeatureTypeEnum.City &&
					edges[(edgeIndex + 1) % edges.length].nodes[0].featureType == FeatureTypeEnum.City){
				continue;
			}

			edges[edgeIndex].nodes[2].neighbors.add(edges[(edgeIndex + 1) % edges.length].nodes[0]);
			edges[(edgeIndex + 1) % edges.length].nodes[0].neighbors.add(edges[edgeIndex].nodes[2]);
		}

		//Road handler
		roadLoop:
		if (numRoads == 2){
			for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++){
				if (edges[edgeIndex].nodes[1].featureType == FeatureTypeEnum.Road){
					for (int targetEdgeIndex = edgeIndex; edgeIndex < edges.length; edgeIndex++){
						if (edges[targetEdgeIndex].nodes[1].featureType == FeatureTypeEnum.Road){
							edges[edgeIndex].nodes[1].neighbors.add(edges[targetEdgeIndex].nodes[1]);
							edges[targetEdgeIndex].nodes[1].neighbors.add(edges[edgeIndex].nodes[1]);
							break roadLoop;
						}
					}
				}
			}
		}
	}
	
	public void connectNodes(){
		
		if(this.edges[0].nodes[1].featureType == FeatureTypeEnum.Road && this.edges[2].nodes[1].featureType == FeatureTypeEnum.Road && this.middle.featureType == FeatureTypeEnum.Road){
			
			this.edges[0].nodes[1].neighbors.add(this.middle);
			
			this.middle.neighbors.add(this.edges[0].nodes[1]);
			this.middle.neighbors.add(this.edges[2].nodes[1]);
			
			this.edges[2].nodes[1].neighbors.add(this.middle);
			
			System.out.println("YES");
		}
		
		
		
	}
	
	/*
	public void rotate(int cnt){
		
	}
	*/
}