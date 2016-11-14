import java.util.ArrayList;
public class Node {
	
	FeatureTypeEnum featureType;
	Meeple meeple;
	boolean meeplePlacedInFeature;
	ArrayList<Node> neighbors;

	boolean visited;
	int owningTileId;
	
	public Node(char featureType, int owningTileId){
		switch (featureType){
			case 'f':
			case 'F': this.featureType = FeatureTypeEnum.Field; break;
			case 'r':
			case 'R': this.featureType = FeatureTypeEnum.Road; break;
			case 'e':
			case 'E': this.featureType = FeatureTypeEnum.RoadEnd; break;
			case 'c':
			case 'C': this.featureType = FeatureTypeEnum.City; break;
			case 'w':
			case 'W': this.featureType = FeatureTypeEnum.Wall; break;
			case 'i':
			case 'I': this.featureType = FeatureTypeEnum.InnerWall; break;
			case 'm':
			case 'M': this.featureType = FeatureTypeEnum.Monastery; break;
			case 'n':
			case 'N':
			default: this.featureType = FeatureTypeEnum.None; break;
		}
		this.neighbors = new ArrayList<Node>();
		this.visited = false;
		this.owningTileId = owningTileId;
		this.meeplePlacedInFeature = false;
	}
	
	
	
}
