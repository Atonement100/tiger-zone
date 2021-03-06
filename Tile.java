import javafx.scene.transform.Rotate;

import javax.smartcardio.TerminalFactory;
import java.util.*;
import java.util.ArrayList;

public class Tile {
	private static final int EDGES_PER_TILE = 4;
	private static final int NODES_PER_EDGE = 3;
	private static int identify = 0;
	
	int ID,
		rotations = 0;
	Integer[] edgeValues;
	Edge[] edges = new Edge[
			EDGES_PER_TILE];
	Node middle;
	char tileType;
	boolean hasDen,
		 	trailsEnd,
			lakesAreIndependent;
	int animalType;

	static void resetTileIdentify(){
		identify = 0;
	}

	public Tile(Tile tileToCopy){
		Tile newTile = new Tile(tileToCopy.hasDen, tileToCopy.trailsEnd, tileToCopy.lakesAreIndependent, tileToCopy.animalType, tileToCopy.edgeValues, tileToCopy.tileType);
		this.hasDen = newTile.hasDen;
		this.trailsEnd = newTile.trailsEnd;
		this.lakesAreIndependent = newTile.lakesAreIndependent;
		this.tileType = newTile.tileType;
		this.rotations = newTile.rotations;
		this.edgeValues = newTile.edgeValues;
		this.animalType = newTile.animalType;
		this.edges = newTile.edges;
		this.middle = newTile.middle;
		this.ID = tileToCopy.ID % 77;
	}
	
	public Tile(Tile tileToCopy, boolean unrotate){
		Tile newTile = new Tile(tileToCopy.hasDen, tileToCopy.trailsEnd, tileToCopy.lakesAreIndependent, tileToCopy.animalType, tileToCopy.edgeValues, tileToCopy.tileType);
		
		Tile.printTile(newTile);
		if (unrotate) newTile.rotateClockwise(-tileToCopy.rotations);
		Tile.printTile(newTile);
		this.hasDen = newTile.hasDen;
		this.trailsEnd = newTile.trailsEnd;
		this.lakesAreIndependent = newTile.lakesAreIndependent;
		this.tileType = newTile.tileType;
		this.rotations = 0;
		this.edgeValues = newTile.edgeValues;
		this.animalType = newTile.animalType;
		this.edges = newTile.edges;
		this.middle = newTile.middle;
		this.ID = tileToCopy.ID % 77;
		Tile.printTile(this);
	}
	
	

	public Tile(boolean hasDen, boolean trailsEnd, boolean lakesAreIndependent, int animalType, Integer[] edgeValues, char tileType){
		this.hasDen = hasDen;
		this.trailsEnd = trailsEnd;
		this.lakesAreIndependent = lakesAreIndependent;
		this.tileType = tileType;
		this.rotations = 0;
		this.edgeValues = edgeValues;
		this.animalType = animalType;
		this.ID = identify++ % 77;

		//Count number of trail + city edges
		int numTrails = 0, numCities = 0;
		ArrayList<Integer> cityLocations = new ArrayList<Integer>();
		for (int edgeIndex = 0; edgeIndex < edgeValues.length; edgeIndex++){
			if (edgeValues[edgeIndex] == FeatureTypeEnum.Lake.toInt()){
				numCities++;
				cityLocations.add(edgeIndex);
			}
			else if (edgeValues[edgeIndex] == FeatureTypeEnum.Trail.toInt()){
				numTrails++;
			}
		}

		//Can't combine these two loops, this depends on numCities and numTrails.
		//This loop handles the creation of all the edges (and their composing nodes) for the tile.
		for (int edgeIndex = 0; edgeIndex < edgeValues.length; edgeIndex++){
			Node[] nodeBuffer = new Node[NODES_PER_EDGE];

			for (int nodeIndex = 0; nodeIndex < nodeBuffer.length; nodeIndex++) {
				switch (nodeIndex) {
					case 0:	// For first and third nodes, always field if not a city, otherwise always a city
					case 2:
						nodeBuffer[nodeIndex] = (edgeValues[edgeIndex] == FeatureTypeEnum.Lake.toInt()) ? new Node(FeatureTypeEnum.Lake.toChar(), this.ID) : new Node(FeatureTypeEnum.Jungle.toChar(), this.ID);
						break;
					case 1: // For second node, always equal to the type of edge it is on.
						if (edgeValues[edgeIndex] == FeatureTypeEnum.Lake.toInt())
							nodeBuffer[nodeIndex] = new Node(FeatureTypeEnum.Lake.toChar(), this.ID);
						else if (edgeValues[edgeIndex] == FeatureTypeEnum.Trail.toInt())
							nodeBuffer[nodeIndex] = new Node(FeatureTypeEnum.Trail.toChar(), this.ID);
						else if (edgeValues[edgeIndex] == FeatureTypeEnum.Jungle.toInt())
							nodeBuffer[nodeIndex] = new Node(FeatureTypeEnum.Jungle.toChar(), this.ID);
						else
							System.out.println("Error with tile import: invalid edge value");
						break;
					default:
						System.out.println("Node buffer should be out of bounds");
						break;
				}
			}

			//Handle some of the edge cases down here. Certainly can be incorporated above, but makes it much harder to read
			if (edgeValues[edgeIndex] == FeatureTypeEnum.Lake.toInt() && (this.lakesAreIndependent || numCities == 1)){
				for (int nodeIndex = 0; nodeIndex < nodeBuffer.length; nodeIndex++){
					nodeBuffer[nodeIndex].featureType = FeatureTypeEnum.Shore;
				}
			}
			else if (edgeValues[edgeIndex] == FeatureTypeEnum.Trail.toInt() && (this.trailsEnd || numTrails == 1)){
				nodeBuffer[1].featureType = FeatureTypeEnum.TrailEnd;
			}

			this.edges[edgeIndex] = new Edge(nodeBuffer);
		}

		if (this.hasDen) {
			this.middle = new Node(FeatureTypeEnum.Den.toChar(), this.ID);

			for (Edge edge : this.edges){
				if (edge.nodes[1].featureType == FeatureTypeEnum.Jungle){
					addEachNodeAsNeighbor(middle, edge.nodes[1]);
					break;
				}
			}
		}
		else this.middle = new Node(FeatureTypeEnum.None.toChar(), this.ID); //none

		//Now we need to handle city edge cases
		//numCities == 1 case is already handled above
		if (numCities == 2 && !this.lakesAreIndependent){ //Independent lakes are already handled above
			int firstLakeLoc = cityLocations.get(0), secondLakeLoc = cityLocations.get(1);
			if (secondLakeLoc - firstLakeLoc == 2){ //In this case, the lakes oppose each other on the tile
				for (int nodeIndex = 0; nodeIndex < NODES_PER_EDGE; nodeIndex++){
					int antiNodeIndex = NODES_PER_EDGE - 1 - nodeIndex; //Used to mirror the nodeIndex on edges opposite each other

					if (nodeIndex != 1) { //Prevents the type of the middle node from being changed
						edges[firstLakeLoc].nodes[nodeIndex].featureType = FeatureTypeEnum.Shore;
						edges[secondLakeLoc].nodes[antiNodeIndex].featureType = FeatureTypeEnum.Shore;
					}
					//Then connect opposing nodes
					addEachNodeAsNeighbor(edges[firstLakeLoc].nodes[nodeIndex], edges[secondLakeLoc].nodes[antiNodeIndex]);
				}
			}
			else{ //Otherwise, the two city edges are adjacent on the tile
				if (secondLakeLoc - firstLakeLoc == 3) { int swap = firstLakeLoc; firstLakeLoc = secondLakeLoc; secondLakeLoc = swap; }
				//These [inner nodes] will be automatically linked later by the adjacency linker
				edges[firstLakeLoc].nodes[2].featureType = FeatureTypeEnum.InnerShore;
				edges[secondLakeLoc].nodes[0].featureType = FeatureTypeEnum.InnerShore;

				//Outer node set up. Need to be linked here.
				edges[firstLakeLoc].nodes[0].featureType = FeatureTypeEnum.Shore;
				edges[secondLakeLoc].nodes[2].featureType = FeatureTypeEnum.Shore;

				addEachNodeAsNeighbor(edges[firstLakeLoc].nodes[0], edges[secondLakeLoc].nodes[2]);
				addEachNodeAsNeighbor(edges[firstLakeLoc].nodes[1], edges[secondLakeLoc].nodes[1]);
			}
		}
		else if (numCities == 3){
			for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++){
				//This sets up the true wall neighboring the field
				if(edges[edgeIndex].nodes[2].featureType == FeatureTypeEnum.Lake && edges[(edgeIndex + 1) % edges.length].nodes[2].featureType == FeatureTypeEnum.Jungle){
					edges[edgeIndex].nodes[2].featureType = FeatureTypeEnum.Shore;
					edges[(edgeIndex + 2) % edges.length].nodes[0].featureType = FeatureTypeEnum.Shore;

					addEachNodeAsNeighbor(edges[edgeIndex].nodes[2], edges[(edgeIndex + 2) % edges.length].nodes[0]);

					//Connects inner city nodes
					addEachNodeAsNeighbor(edges[edgeIndex].nodes[1], edges[(edgeIndex + 2) % edges.length].nodes[1]);
					addEachNodeAsNeighbor(edges[edgeIndex].nodes[1], edges[(edgeIndex + 3) % edges.length].nodes[1]);
					addEachNodeAsNeighbor(edges[(edgeIndex + 3) % edges.length].nodes[1], edges[(edgeIndex + 2) % edges.length].nodes[1]);
				}

				//This sets up the inner connections to be linked later
				if((edgeValues[edgeIndex] == FeatureTypeEnum.Lake.toInt()) && edgeValues[(edgeIndex + 1) % edges.length] == FeatureTypeEnum.Lake.toInt()){
					edges[edgeIndex].nodes[2].featureType = FeatureTypeEnum.InnerShore;
					edges[(edgeIndex + 1) % edges.length].nodes[0].featureType = FeatureTypeEnum.InnerShore;
				}
			}
		}
		else if (numCities == 4){
			//Will be linked later by adjacency linker
			for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++) {
				edges[edgeIndex].nodes[0].featureType = FeatureTypeEnum.InnerShore;
				edges[edgeIndex].nodes[2].featureType = FeatureTypeEnum.InnerShore;

				addEachNodeAsNeighbor(edges[edgeIndex].nodes[1], (edges[(edgeIndex + 1) % EDGES_PER_TILE]).nodes[1]);
			}
		}

		/*Adjacency linker*/
		for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++){
			for (int nodeIndex = 0; nodeIndex <
					NODES_PER_EDGE - 1; nodeIndex++) { //We can only loop for 1st two nodes on an edge. Third node needs to be handled specially
				//if (edges[edgeIndex].nodes[nodeIndex].featureType == edges[edgeIndex].nodes[nodeIndex + 1].featureType || 					//If same feature type
				//		edges[edgeIndex].nodes[nodeIndex].featureType.isShoreToLake(edges[edgeIndex].nodes[nodeIndex + 1].featureType)) {	//Or a wall to city connection
				//	addEachNodeAsNeighbor(edges[edgeIndex].nodes[nodeIndex], edges[edgeIndex].nodes[nodeIndex + 1]);
				//}

				//Just link all adjacent nodes if they are the same feature.
				if (edges[edgeIndex].nodes[nodeIndex].featureType.isSameFeature(edges[edgeIndex].nodes[nodeIndex+1].featureType)){
					addEachNodeAsNeighbor(edges[edgeIndex].nodes[nodeIndex], edges[edgeIndex].nodes[nodeIndex + 1]);
				}
			}

			//3rd node handler
			if (this.lakesAreIndependent &&
					edges[edgeIndex].nodes[2].featureType == FeatureTypeEnum.Shore &&
					edges[(edgeIndex + 1) % edges.length].nodes[0].featureType == FeatureTypeEnum.Shore){
				continue;
			}

			addEachNodeAsNeighbor(edges[edgeIndex].nodes[2], edges[(edgeIndex + 1) % edges.length].nodes[0]);
		}

		//Trail handler
		trailLoop:
		if (numTrails == 2){
			for (int edgeIndex = 0; edgeIndex < edges.length; edgeIndex++){
				if (edges[edgeIndex].nodes[1].featureType == FeatureTypeEnum.Trail){
					for (int targetEdgeIndex = edgeIndex + 1; targetEdgeIndex < edges.length; targetEdgeIndex++){
						if (edges[targetEdgeIndex].nodes[1].featureType == FeatureTypeEnum.Trail){
							//Connect the trails
							addEachNodeAsNeighbor(edges[edgeIndex].nodes[1], edges[targetEdgeIndex].nodes[1]);
							//Consider fields next to trail for connections
							if (numCities == 1){
								int lowerIndex = cityLocations.get(0) - 1;
								if (lowerIndex < 0) lowerIndex += EDGES_PER_TILE;
								addEachNodeAsNeighbor(edges[lowerIndex].nodes[2], edges[(cityLocations.get(0) + 1) % EDGES_PER_TILE].nodes[0]);
							}
							else if (numCities == 2){
								int lowerIndex = cityLocations.get(0) - 1;
								if (lowerIndex < 0) lowerIndex += EDGES_PER_TILE;
								addEachNodeAsNeighbor(edges[lowerIndex].nodes[2], edges[(cityLocations.get(1) + 1) % EDGES_PER_TILE].nodes[0]);
							}
							break trailLoop;
						}
					}
				}
			}
		}
		else if (numTrails == 3 && numCities > 0){
			int lowerIndex = cityLocations.get(0) - 1;
			if (lowerIndex < 0) lowerIndex += EDGES_PER_TILE;
			addEachNodeAsNeighbor(edges[lowerIndex].nodes[2], edges[(cityLocations.get(0) + 1) % EDGES_PER_TILE].nodes[0]);
		}

		if(this.tileType == 'H'){
			this.printNodeHashes();
		}
	}

	private void addEachNodeAsNeighbor(Node nodeA, Node nodeB){
		nodeA.neighbors.add(nodeB);
		nodeB.neighbors.add(nodeA);
	}

	boolean isEqual(Tile other){
		//More robust checks do not need to be implemented as long as we control tileset input.
		return this.tileType == other.tileType;
	}


	void rotateClockwise(int rotations){
		rotations %= EDGES_PER_TILE;
		if (rotations == 0) return;
		else if (rotations < 0) rotations += EDGES_PER_TILE;

		this.rotations = (this.rotations + rotations) % EDGES_PER_TILE;
		
		Collections.rotate(Arrays.asList(edges), rotations);
		Collections.rotate(Arrays.asList(edgeValues), rotations);
		return;
	}

	void rotateAntiClockwise(int rotations){
		rotations %= EDGES_PER_TILE;
		if (rotations == 0) return;
		else if (rotations < 0) rotations += EDGES_PER_TILE;

		rotateClockwise(Math.abs(rotations - EDGES_PER_TILE));
	}

	public void printNodeHashes(){
		for (Edge edge : this.edges){
			for (Node node : edge.nodes){
				System.out.print("\n" + node.hashCode() + "(" + node.featureType + "): ");
				for (Node neighbor : node.neighbors){
					System.out.print(neighbor.hashCode() + "(" + neighbor.featureType + "), ");
				}
			}
		}
	}
    
    static void printTile(Tile toPrint){
        System.out.print(" ");
        for(int i = 0; i < 3; i++){
            System.out.print(toPrint.edges[1].nodes[i].featureType.toChar());
        }
        System.out.println("");
        System.out.println(toPrint.edges[0].nodes[2].featureType.toChar() + "   " + toPrint.edges[2].nodes[0].featureType.toChar());
        System.out.println(toPrint.edges[0].nodes[1].featureType.toChar() + "   " + toPrint.edges[2].nodes[1].featureType.toChar());
        System.out.println(toPrint.edges[0].nodes[0].featureType.toChar() + "   " + toPrint.edges[2].nodes[2].featureType.toChar());
        System.out.print(" ");
        for(int i = 2; i >= 0; i--){
            System.out.print(toPrint.edges[3].nodes[i].featureType.toChar());
        }
        System.out.println("");
    }
    
}