
public class Edge {
	Node[] nodes;
	
	public Edge(Node[] nodes){
		this.nodes = nodes;
	}
	
	public boolean isCompatible(Edge other){
		
		boolean compatible = true;
		for(int i = 0; i < nodes.length; i++){
			if(this.nodes[i].territory == 'W' && other.nodes[i].territory == 'C') continue;				//false signal, valid placement
			else if(this.nodes[i].territory == 'C' && other.nodes[i].territory == 'W') continue;		//false signal, valid placement
			else if(this.nodes[i].territory == 'W' && other.nodes[i].territory == 'F') continue;
			else if(this.nodes[i].territory == 'F' && other.nodes[i].territory == 'W') continue;
			else if(this.nodes[i].territory != other.nodes[i].territory){
				compatible = false;
			}
		}
		
		return compatible;
		
	}
}