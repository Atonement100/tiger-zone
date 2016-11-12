import java.util.ArrayList;
import java.io.*;
public class TileRetriever {

	ArrayList<Tile> tiles;

	public TileRetriever(String path){
		tiles = new ArrayList<Tile>();

		String line = null;

		try {
			FileReader fileReader = new FileReader(path);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int cnt = 0;
			while((line = bufferedReader.readLine()) != null) {
				
				Edge[] edgesBuffer = new Edge[4];
	            for(int e = 0; e < 4; e++){
	                String edge = line;
	                Node[] nodes = new Node[3];
	                for(int n = 0; n < 3; n++){
	                    Node buffer = new Node(edge.charAt(n));
	                    nodes[n] = buffer;
	                }
	                edgesBuffer[e] = new Edge(nodes);
	                line = bufferedReader.readLine();
	            }
	            Node middleBuffer = new Node(line.charAt(0));
	            tiles.add(new Tile(edgesBuffer, middleBuffer));
	            
			}
			bufferedReader.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println(
					"Unable to open file '" + 
							path + "'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + path + "'");                  
		}   
	}
}