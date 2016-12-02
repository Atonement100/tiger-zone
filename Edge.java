
class Edge {
    Node[] nodes;
    
    Edge(Node[] nodes){
        this.nodes = nodes;
    }
    
    boolean isCompatible(Edge other){
        boolean edgeIsCompatible = true;
        for(int i = 0; i < nodes.length; i++){
            
            //handle false positives first
            
            //WALL AND CITY
            if(this.nodes[i].featureType == FeatureTypeEnum.Wall && other.nodes[2-i].featureType == FeatureTypeEnum.City) continue;
            else if(this.nodes[i].featureType == FeatureTypeEnum.City && other.nodes[2-i].featureType == FeatureTypeEnum.Wall) continue;
            
            //WALL AND INNER WALL
            else if(this.nodes[i].featureType == FeatureTypeEnum.InnerWall && other.nodes[2-i].featureType == FeatureTypeEnum.Wall) continue;
            else if(this.nodes[i].featureType == FeatureTypeEnum.Wall && other.nodes[2-i].featureType == FeatureTypeEnum.InnerWall) continue;
            
            //INNER WALL AND CITY
            else if(this.nodes[i].featureType == FeatureTypeEnum.City && other.nodes[2-i].featureType == FeatureTypeEnum.InnerWall) continue;
            else if(this.nodes[i].featureType == FeatureTypeEnum.InnerWall && other.nodes[2-i].featureType == FeatureTypeEnum.City) continue;
            
            //ROAD AND INTERSECTION
            else if(this.nodes[i].featureType == FeatureTypeEnum.Road && other.nodes[2-i].featureType == FeatureTypeEnum.RoadEnd) continue;
            else if(this.nodes[i].featureType == FeatureTypeEnum.RoadEnd && other.nodes[2-i].featureType == FeatureTypeEnum.Road) continue;
            
            else if(this.nodes[i].featureType != other.nodes[2-i].featureType){
                edgeIsCompatible = false;
            }
        }
        return edgeIsCompatible;
    }
}