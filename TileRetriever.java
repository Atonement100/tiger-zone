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

			while((line = bufferedReader.readLine()) != null) {
				String[] tokens = line.split("[ ]+");
				int[] parameters = new int[tokens.length];
				for (int Index = 0; Index < parameters.length - 1; Index++){
					parameters[Index] = Integer.parseInt(tokens[Index]);
				}

				for (int spawning = 0; spawning < parameters[0]; spawning++){
					tiles.add(new Tile(parameters[1] == 1 ,parameters[2] == 1 ,parameters[3] == 1, parameters[4], //Monastery, Roads End, Independent Cities, Animal type
								new Integer[]{parameters[5],parameters[6],parameters[7],parameters[8]}, //Array of edge values (city, plains, road)
								tokens[tokens.length-1].charAt(0))); //character identifier for easy comparison of tile equality
				}
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