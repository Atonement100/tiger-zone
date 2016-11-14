import java.util.ArrayList;

public class Tile {
	
	private static int identify = 0;
	
	int ID,
		rotations;
	int[] edgeValues;
	Edge[] edges;
	Node middle;
	char tileType;
	boolean hasMonastery,
		 	roadsEnd,
			citiesAreIndependent,
			hasShield;


	public Tile(Edge[] edges, Node middle){
		this.edges = edges;
		this.middle = middle;
		this.connectNodes();
		this.ID = ++identify;
	}

	public Tile(boolean hasMonastery, boolean roadsEnd, boolean citiesAreIndependent, boolean hasShield, int[] edges, char tileType){
		this.hasMonastery = hasMonastery;
		this.roadsEnd = roadsEnd;
		this.citiesAreIndependent = citiesAreIndependent;
		this.hasShield = hasShield;
		this.tileType = tileType;
		this.rotations = 0;
		this.edgeValues = edges;

		if (this.hasMonastery) this.middle = new Node('M');
		else this.middle = new Node('n'); //none

		//Count number of road + city edges
		int numRoads = 0, numCities = 0;
		ArrayList<Integer> cityLocations = new ArrayList<Integer>();
		for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++){
			if (edges[edgeIndex] == FeatureTypeEnum.City.toInt()){
				numCities++;
				cityLocations.add(edgeIndex);
			}
			else if (edges[edgeIndex] == FeatureTypeEnum.Road.toInt()){
				numRoads++;
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