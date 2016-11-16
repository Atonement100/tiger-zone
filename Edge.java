
public class Edge {
    Node[] nodes;
    
    public Edge(Node[] nodes){
        this.nodes = nodes;
    }
    
    public boolean isCompatible(Edge other){
        
        boolean edgeIsCompatible = true;
        for(int i = 0; i < nodes.length; i++){
            
            //handle false positives first
            if(this.nodes[i].featureType == FeatureTypeEnum.Wall && other.nodes[2-i].featureType == FeatureTypeEnum.City) continue;
            else if(this.nodes[i].featureType == FeatureTypeEnum.City && other.nodes[2-i].featureType == FeatureTypeEnum.Wall) continue;
            else if(this.nodes[i].featureType == FeatureTypeEnum.InnerWall && other.nodes[2-i].featureType == FeatureTypeEnum.Wall) continue;
            else if(this.nodes[i].featureType == FeatureTypeEnum.Wall && other.nodes[2-i].featureType == FeatureTypeEnum.InnerWall) continue;
            else if(this.nodes[i].featureType != other.nodes[2-i].featureType){
                edgeIsCompatible = false;
            }
        }
        return edgeIsCompatible;
    }
}