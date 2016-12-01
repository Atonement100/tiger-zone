/**
 * Created by Darshil on 11/21/2016.
 *
 * map the Game controller input to the gui input
 * match the tile ID and pass it to GUI,
 */
public class GuiAdapter {
    private String TileID;
    private String GuiID;
    private int x;
    private int  y;
    private int rotation;
    private int tiger;
    public LabelDemo Gui;
    
    String[] Id = {"JJJJ-", "JJJJX", "JJTJX", "TTTT-", "TJTJ-", "TJJT-", "TJTT-",
            "LLLL-", "JLLL-", "LLJJ-", "JLJL-", "LJLJ-", "LJJJ-", "JLLJ-",
            "TLJT-", "TLJTP", "JLTT-", "JLTTB", "TLTJ-", "TLTJD", "TLLL-",
            "TLTT-", "TLTTP", "TLLT-", "TLLTB", "LJTJ-", "LJTJD", "TLLLC"};
            // Not sure if C is the correct animal ID for crocodile on last tile.

    public GuiAdapter(int boardSize) {
        Gui = new LabelDemo(boardSize);

    }

    public GuiAdapter(int x, int y, int rotation, int tiger) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.tiger = tiger;
    }
    public int getX() {
        x= Gui.getX();
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        y= Gui.getY();
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRotation() {
        rotation =Gui.getRotation();
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getTiger() {
        tiger = Gui.getTiger();
        return tiger;
    }

    public void setTiger(int tiger) {
        this.tiger = tiger;
    }

    public String getTileID() {
        return TileID;
    }

    public void setTileID(String tileID) {
        Gui.setDone(false);
        TileID = tileID;
        mapTileid(tileID);
        Gui.setupPreview(GuiID);
        while (!Gui.isDone()){

            Gui.refreshPreview(GuiID,Gui.getRotation());
        };



        //  System.out.println(GuiID);

    }

    public String getGuiID() {
        return GuiID;
    }

    public void setGuiID(String guiID) {
        GuiID = guiID;
    }

    public LabelDemo getGui() {
        return Gui;
    }

    public void setGui(LabelDemo gui) {
        Gui = gui;
    }
    public void mapTileid(String gameTile){
        if (gameTile.equals("A")){GuiID= Id[0];}
        else if (gameTile.equals("B")){GuiID= Id[1];}
        else if (gameTile.equals("C")){GuiID= Id[2];}
        else if (gameTile.equals("D")){GuiID= Id[3];}
        else if (gameTile.equals("E")){GuiID= Id[4];}
        else  if (gameTile.equals("F")){GuiID= Id[5];}
        else  if (gameTile.equals("G")){GuiID= Id[6];}
        else if (gameTile.equals("H")){GuiID= Id[7];}
        else if (gameTile.equals("I")){GuiID= Id[8];}
        else if (gameTile.equals("J")){GuiID= Id[9];}
        else if (gameTile.equals("K")){GuiID= Id[10];}
        else  if (gameTile.equals("L")){GuiID= Id[11];}
        else  if (gameTile.equals("M")){GuiID= Id[12];}
        else  if (gameTile.equals("N")){GuiID= Id[13];}
        else if (gameTile.equals("O")){GuiID= Id[14];}
        else if (gameTile.equals("P")){GuiID= Id[15];}
        else if (gameTile.equals("Q")){GuiID= Id[16];}
        else if (gameTile.equals("R")){GuiID= Id[17];}
        else if (gameTile.equals("S")){GuiID= Id[18];}
        else if (gameTile.equals("T")){GuiID= Id[19];}
        else  if (gameTile.equals("U")){GuiID= Id[20];}
        else  if (gameTile.equals("V")){GuiID= Id[21];}
        else  if (gameTile.equals("W")){GuiID= Id[22];}
        else  if (gameTile.equals("X")){GuiID= Id[23];}
        else   if (gameTile.equals("Y")){GuiID= Id[24];}
        else   if (gameTile.equals("Z")){GuiID= Id[25];}
        else   if (gameTile.equals("a")){GuiID= Id[26];}
        else if (gameTile.equals("b")){GuiID= Id[27];}


    }
    public void unmapTileid(String gameTile){
        if (gameTile.equals(Id[0])){
            setTileID("A");
        }
        if (GuiID==Id[1]){
            setTileID("B");
        } if (GuiID==Id[2]){
            setTileID("C");
        } if (GuiID==Id[3]){
            setTileID("D");
        } if (GuiID==Id[4]){
            setTileID("E");
        } if (GuiID==Id[5]){
            setTileID("F");
        } if (GuiID==Id[6]){
            setTileID("G");
        } if (GuiID==Id[7]){
            setTileID("H");
        } if (GuiID==Id[8]){
            setTileID("I");
        } if (GuiID==Id[9]){
            setTileID("J");
        } if (GuiID==Id[10]){
            setTileID("K");
        } if (GuiID==Id[11]){
            setTileID("L");
        } if (GuiID==Id[12]){
            setTileID("M");
        } if (GuiID==Id[13]){
            setTileID("N");
        } if (GuiID==Id[14]){
            setTileID("O");
        } if (GuiID==Id[15]){
            setTileID("P");
        } if (GuiID==Id[16]){
            setTileID("Q");
        } if (GuiID==Id[17]){
            setTileID("R");
        } if (GuiID==Id[18]){
            setTileID("S");
        } if (GuiID==Id[19]){
            setTileID("T");
        } if (GuiID==Id[20]){
            setTileID("U");
        } if (GuiID==Id[21]){
            setTileID("V");
        } if (GuiID==Id[22]){
            setTileID("W");
        } if (GuiID==Id[23]){
            setTileID("X");
        } if (GuiID==Id[24]){
            setTileID("Y");
        } if (GuiID==Id[25]){
            setTileID("Z");
        } if (GuiID==Id[26]){
            setTileID("a");
        } if (GuiID==Id[27]){
            // Not sure if this should be the internal GuiAdapter version ("b") or the tile
            // version ("U").
            setTileID("b");  
        }
    }
    public void placeFirstTile(int x,int y,String id){
        mapTileid(id);
        Gui.placeFirstTile(x,y,GuiID);
    }
    public void placeTile(int x,int y,String id){
        mapTileid(id);
        //Gui.placeFirstTile(x,y,GuiID);
    }
    public void tilePlaced(int x,int y,int rotation,int tiger){
        setX(x);
        setY(y);
        setRotation(rotation);
        setTiger(tiger);
    }

    public void addTile(int row, int col, int tileRotation, String s) {
        mapTileid(s);
        Gui.placeComputertile(row,col,tileRotation,GuiID);
    }

    public void proccessConfirmedMove(Tile confirmedTile, MoveInformation moveInfo, int playerConfirmed){
        // Tile TLLL- and TLLLC both have id character 'U', so a different one must be used for 
        //      TLLLC in GUIAdapter to map the tile id character to the tile id string. 
        char tileIDChar = confirmedTile.tileType;
        if(tileIDChar == 'U' && confirmedTile.animalType == 4){
            tileIDChar = 'b';
        }
        addTile(moveInfo.tileLocation.Row, moveInfo.tileLocation.Col, moveInfo.tileRotation, Character.toString(tileIDChar));
        if(moveInfo.tigerLocation >= 0){
            Gui.placeTiger(moveInfo.tileLocation.Row, moveInfo.tileLocation.Col, moveInfo.tigerLocation);
        }
    }

    public void updateScores(int player1Score, int player2Score){
        Gui.updateScores(player1Score, player2Score);
    }
}
