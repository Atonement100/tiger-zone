import java.util.ArrayList;
public class Node {
    FeatureTypeEnum featureType;
    Tiger tiger;
    boolean tigerPlacedInFeature;
    ArrayList<Node> neighbors;

    boolean visited;
    int owningTileId;
    int featureID;
    
    public Node(char featureType, int owningTileId){
        
        this.featureID = -1;				//not complete, gets set in scoreController
        
        switch (featureType){
            case 'f':
            case 'F': this.featureType = FeatureTypeEnum.Jungle; break;
            case 'r':
            case 'R': this.featureType = FeatureTypeEnum.Trail; break;
            case 'e':
            case 'E': this.featureType = FeatureTypeEnum.TrailEnd; break;
            case 'c':
            case 'C': this.featureType = FeatureTypeEnum.Lake; break;
            case 'w':
            case 'W': this.featureType = FeatureTypeEnum.Shore; break;
            case 'i':
            case 'I': this.featureType = FeatureTypeEnum.InnerShore; break;
            case 'm':
            case 'M': this.featureType = FeatureTypeEnum.Den; break;
            case 'n':
            case 'N':
            default: this.featureType = FeatureTypeEnum.None; break;
        }
        this.neighbors = new ArrayList<Node>();
        this.visited = false;
        this.owningTileId = owningTileId;
        this.tigerPlacedInFeature = false;
    }
}