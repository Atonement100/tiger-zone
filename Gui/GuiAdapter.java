package Gui;

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
    private int meeple;
    public LabelDemo Gui;
    String[] Id = {"JJJJ-", "JJJJX", "JJTJX", "TTTT-", "TJTJ-", "TJTT-", "LLLL-",
            "JLLL-", "LLJJ-", "JLJL-", "LJLJ-", "LJJJ-", "JLLJ-", "TLJT-",
            "TLJTP", "JLTT-", "JLTTB", "TLTJ-", "TLTJD", "TLLL-", "TLTT-",
            "TLTTP", "TLLT-", "TLLTB", "LJTJ-", "LJTJD"};

    public GuiAdapter() {
    }

    public GuiAdapter(int x, int y, int rotation, int meeple) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.meeple = meeple;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getMeeple() {
        return meeple;
    }

    public void setMeeple(int meeple) {
        this.meeple = meeple;
    }

    public String getTileID() {
        return TileID;
    }

    public void setTileID(String tileID) {
        TileID = tileID;
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
        if (gameTile=="A"){GuiID= Id[0];}
        if (gameTile=="B"){GuiID= Id[1];}
        if (gameTile=="C"){GuiID= Id[2];}
        if (gameTile=="D"){GuiID= Id[3];}
        if (gameTile=="E"){GuiID= Id[4];}
        if (gameTile=="F"){GuiID= Id[5];}
        if (gameTile=="G"){GuiID= Id[6];}
        if (gameTile=="H"){GuiID= Id[7];}
        if (gameTile=="I"){GuiID= Id[8];}
        if (gameTile=="J"){GuiID= Id[9];}
        if (gameTile=="K"){GuiID= Id[10];}
        if (gameTile=="L"){GuiID= Id[11];}
        if (gameTile=="M"){GuiID= Id[12];}
        if (gameTile=="N"){GuiID= Id[13];}
        if (gameTile=="O"){GuiID= Id[14];}
        if (gameTile=="P"){GuiID= Id[15];}
        if (gameTile=="Q"){GuiID= Id[16];}
        if (gameTile=="R"){GuiID= Id[17];}
        if (gameTile=="S"){GuiID= Id[18];}
        if (gameTile=="T"){GuiID= Id[19];}
        if (gameTile=="U"){GuiID= Id[20];}
        if (gameTile=="V"){GuiID= Id[21];}
        if (gameTile=="W"){GuiID= Id[22];}
        if (gameTile=="X"){GuiID= Id[23];}
        if (gameTile=="Y"){GuiID= Id[24];}
        if (gameTile=="Z"){GuiID= Id[25];}
        if (gameTile=="a"){GuiID= Id[26];}
       // if (gameTile=="b"){GuiID= Id[27];}


    }
    public void unmapTileid(String gameTile){
      if (GuiID==Id[0]){
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

}
