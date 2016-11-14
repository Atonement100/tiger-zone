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
			citiesAreIndependent,
			hasShield;


	/*public Tile(Edge[] edges, Node middle){
		this.edges = edges;
		this.middle = middle;
		this.connectNodes();
		this.ID = ++identify;
	}*/

	public Tile(boolean hasMonastery, boolean roadsEnd, boolean citiesAreIndependent, boolean hasShield, int[] edgeValues, char tileType){
		this.hasMonastery = hasMonastery;
		this.roadsEnd = roadsEnd;
		this.citiesAreIndependent = citiesAreIndependent;
		this.hasShield = hasShield;
		this.tileType = tileType;
		this.rotations = 0;
		this.edgeValues = edgeValues;

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