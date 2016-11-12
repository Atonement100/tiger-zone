
public class Tile {
	
	private static int identify = 0;
	
	int ID;
	Edge[] edges;
	Node middle;
	
	public Tile(Edge[] edges, Node middle){
		this.edges = edges;
		this.middle = middle;
		this.connectNodes();
		this.ID = ++identify;
	}
	
	public void connectNodes(){
		
		if(this.edges[0].nodes[1].territory == 'R' && this.edges[2].nodes[1].territory == 'R' && this.middle.territory == 'R'){
			
			this.edges[0].nodes[1].neighbors.add(this.middle);
			
			this.middle.neighbors.add(this.edges[0].nodes[1]);
			this.middle.neighbors.add(this.edges[2].nodes[1]);
			
			this.edges[2].nodes[1].neighbors.add(this.middle);
			
			System.out.println("YES");
		}
		
		
		
	}
	
	/*
	public void rotate(int cnt){
		
	}
	*/
}