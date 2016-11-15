
public class Edge {
	Node[] nodes;
	
	public Edge(Node[] nodes){
		this.nodes = nodes;
	}
	
	public boolean isCompatible(Edge other){
		
		boolean edgeIsCompatible = true;
		for(int i = 0; i < nodes.length; i++){
			if(this.nodes[i].featureType == FeatureTypeEnum.Wall && other.nodes[2-i].featureType == FeatureTypeEnum.City) continue;				//false signal, valid placement
			else if(this.nodes[i].featureType == FeatureTypeEnum.City && other.nodes[2-i].featureType == FeatureTypeEnum.Wall) continue;		//false signal, valid placement
			else if(this.nodes[i].featureType == FeatureTypeEnum.Wall && other.nodes[2-i].featureType == FeatureTypeEnum.Field) continue;
			else if(this.nodes[i].featureType == FeatureTypeEnum.Field && other.nodes[2-i].featureType == FeatureTypeEnum.Wall) continue;
			else if(this.nodes[i].featureType != other.nodes[2-i].featureType){
				edgeIsCompatible = false;
			}
		}
		
		return edgeIsCompatible;
		
	}
}