
class Edge {
    Node[] nodes;
    
    Edge(Node[] nodes){
        this.nodes = nodes;
    }
    
    //Checks to see if edges are compatible between tiles
    boolean isCompatible(Edge other){
        boolean edgeIsCompatible = true;
        for(int i = 0; i < nodes.length; i++){
            
            //handle false positives first
            
            //WALL AND CITY
            if(this.nodes[i].featureType == FeatureTypeEnum.Shore && other.nodes[2-i].featureType == FeatureTypeEnum.Lake) continue;
            else if(this.nodes[i].featureType == FeatureTypeEnum.Lake && other.nodes[2-i].featureType == FeatureTypeEnum.Shore) continue;
            
            //WALL AND INNER WALL
            else if(this.nodes[i].featureType == FeatureTypeEnum.InnerShore && other.nodes[2-i].featureType == FeatureTypeEnum.Shore) continue;
            else if(this.nodes[i].featureType == FeatureTypeEnum.Shore && other.nodes[2-i].featureType == FeatureTypeEnum.InnerShore) continue;
            
            //INNER WALL AND CITY
            else if(this.nodes[i].featureType == FeatureTypeEnum.Lake && other.nodes[2-i].featureType == FeatureTypeEnum.InnerShore) continue;
            else if(this.nodes[i].featureType == FeatureTypeEnum.InnerShore && other.nodes[2-i].featureType == FeatureTypeEnum.Lake) continue;
            
            //ROAD AND INTERSECTION
            else if(this.nodes[i].featureType == FeatureTypeEnum.Trail && other.nodes[2-i].featureType == FeatureTypeEnum.TrailEnd) continue;
            else if(this.nodes[i].featureType == FeatureTypeEnum.TrailEnd && other.nodes[2-i].featureType == FeatureTypeEnum.Trail) continue;
            
            else if(this.nodes[i].featureType != other.nodes[2-i].featureType){
                edgeIsCompatible = false;
            }
        }
        return edgeIsCompatible;
    }
}