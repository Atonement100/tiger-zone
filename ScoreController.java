import java.util.Stack;
import java.util.ArrayList;
public class ScoreController {
	int player1Score;
	int player2Score;
	
	ScoreController(){
		this.player1Score = 0;
		this.player2Score = 0;
	}
	
	public void scoreTile(Tile toCheck){
		
		ArrayList<Node> toUnvisit = new ArrayList<Node>();
		
		Node start = toCheck.edges[1].nodes[1];;
		start.visited= true;
		toUnvisit.add(start);
		
		
		
		Stack<Node> stack = new Stack<Node>();
		start.neighbors.get(1).visited = true;
		
		Node nextTo = start.neighbors.get(1);
		toUnvisit.add(start.neighbors.get(1));
		
		for(int i = 0; i < nextTo.neighbors.size(); i++){
			if(!nextTo.neighbors.get(i).visited && nextTo.neighbors.get(i).featureType.toChar() == 'W')
			stack.add(nextTo.neighbors.get(i));
			toUnvisit.add(nextTo.neighbors.get(i));
		}

		boolean cycle = false;
		while(!stack.isEmpty()){
			Node buffer = stack.pop();
			System.out.println(buffer.featureType.toChar());
			buffer.visited = true;
			toUnvisit.add(buffer);
			for(int i = 0; i < buffer.neighbors.size(); i++){
				if(buffer.neighbors.get(i).visited && buffer.neighbors.get(i) == start){
					cycle = true;
				}
				else if(!buffer.neighbors.get(i).visited && buffer.neighbors.get(i).featureType.toChar() == 'W'){
					stack.add(buffer.neighbors.get(i));
				}
			}
		}
		
		for(int i = 0; i < toUnvisit.size(); i++){
			toUnvisit.get(i).visited = false;
		}
		
		System.out.println(cycle ? "cycle":"no cycle");
		
	}
}