import java.util.ArrayList;
public class Node {
	
	FeatureTypeEnum featureType;
	Meeple meeple;
	boolean meeplePlacedInFeature;
	ArrayList<Node> neighbors;
	
	public Node(char featureType){
		switch (featureType){
			case 'F': this.featureType = FeatureTypeEnum.Field; break;
			case 'R': this.featureType = FeatureTypeEnum.Road; break;
			case 'C': this.featureType = FeatureTypeEnum.City; break;
			case 'W': this.featureType = FeatureTypeEnum.Wall; break;
			case 'M': this.featureType = FeatureTypeEnum.Monastery; break;
			default: this.featureType = FeatureTypeEnum.None; break;
		}
		this.neighbors = new ArrayList<Node>();
	}
	
	
	
}
