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
    public GuiInterface Gui;
    
    String[] Id = {"JJJJ-", "JJJJX", "JJTJX", "TTTT-", "TJTJ-", "TJJT-", "TJTT-",
            "LLLL-", "JLLL-", "LLJJ-", "JLJL-", "LJLJ-", "LJJJ-", "JLLJ-",
            "TLJT-", "TLJTP", "JLTT-", "JLTTB", "TLTJ-", "TLTJD", "TLLL-",
            "TLTT-", "TLTTP", "TLLT-", "TLLTB", "LJTJ-", "LJTJD", "TLLLC"};
            // Not sure if C is the correct animal ID for crocodile on last tile.

    public GuiAdapter(int boardSize) {
        Gui = new GuiInterface(boardSize);

    }

    public GuiAdapter(int x, int y, int rotation, int tiger) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.tiger = tiger;
    }
    int getX() {
        x= Gui.getX();
        return x;
    }

    private void setX(int x) {
        this.x = x;
    }

    int getY() {
        y= Gui.getY();
        return y;
    }

    private void setY(int y) {
        this.y = y;
    }

    int getRotation() {
        rotation =Gui.getRotation();
        return rotation;
    }

    private void setRotation(int rotation) {
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

    void setTileID(String tileID) {
        Gui.setDone(false);
        TileID = tileID;
        mapTileid(tileID);
        Gui.setupPreview(GuiID);
        while (!Gui.isDone()){

            Gui.refreshPreview(GuiID,Gui.getRotation());
        }
    }

    public String getGuiID() {
        return GuiID;
    }

    public void setGuiID(String guiID) {
        GuiID = guiID;
    }

    public GuiInterface getGui() {
        return Gui;
    }

    public void setGui(GuiInterface gui) {
        Gui = gui;
    }

    private void mapTileid(String gameTile){
        switch (gameTile) {
            case "A":
                GuiID = Id[0];
                break;
            case "B":
                GuiID = Id[1];
                break;
            case "C":
                GuiID = Id[2];
                break;
            case "D":
                GuiID = Id[3];
                break;
            case "E":
                GuiID = Id[4];
                break;
            case "F":
                GuiID = Id[5];
                break;
            case "G":
                GuiID = Id[6];
                break;
            case "H":
                GuiID = Id[7];
                break;
            case "I":
                GuiID = Id[8];
                break;
            case "J":
                GuiID = Id[9];
                break;
            case "K":
                GuiID = Id[10];
                break;
            case "L":
                GuiID = Id[11];
                break;
            case "M":
                GuiID = Id[12];
                break;
            case "N":
                GuiID = Id[13];
                break;
            case "O":
                GuiID = Id[14];
                break;
            case "P":
                GuiID = Id[15];
                break;
            case "Q":
                GuiID = Id[16];
                break;
            case "R":
                GuiID = Id[17];
                break;
            case "S":
                GuiID = Id[18];
                break;
            case "T":
                GuiID = Id[19];
                break;
            case "U":
                GuiID = Id[20];
                break;
            case "V":
                GuiID = Id[21];
                break;
            case "W":
                GuiID = Id[22];
                break;
            case "X":
                GuiID = Id[23];
                break;
            case "Y":
                GuiID = Id[24];
                break;
            case "Z":
                GuiID = Id[25];
                break;
            case "a":
                GuiID = Id[26];
                break;
            case "b":
                GuiID = Id[27];
                break;
        }


    }

    void placeFirstTile(int x,int y,String id){
        mapTileid(id);
        Gui.placeFirstTile(x,y,GuiID);
    }

    private void addTile(int row, int col, int tileRotation, String s) {
        mapTileid(s);
        Gui.placeComputertile(row,col,tileRotation,GuiID);
    }

    void proccessConfirmedMove(Tile confirmedTile, MoveInformation moveInfo, int playerConfirmed){
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

    void updateScores(int player1Score, int player2Score){
        Gui.updateScores(player1Score, player2Score);
    }
}
