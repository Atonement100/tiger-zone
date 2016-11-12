import java.util.ArrayList;
public class Node {
	
	char territory;
	Meeple meeple;
	boolean meeplePlacedInFeature;
	ArrayList<Node> neighbors;
	
	public  Node(char territory){
		this.territory = territory;
		this.neighbors = new ArrayList<Node>();
	}
	
	
	
}
