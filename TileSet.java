import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.*;

class TileSet {
    private String filePath;
    private String tileID;
    private JButton tileButton = new JButton();
    private int X,Y;
    private ImageIcon tileIcon = new ImageIcon();
    private int[] tigerToGridPos = new int[]{0, 1, 2, 2, 5, 8, 8, 7, 6, 6, 3, 0, 4};

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setTileID(String tileID) {
        this.tileID = tileID;
    }

    public void setTileButton(JButton tileButton) {
          this.tileButton = tileButton;
    }
    void setTileButtonBorder() {
        tileButton.setBorder(BorderFactory.createEtchedBorder(1));
    }

    void setTileIcon(ImageIcon tileIcon) {
        this.tileIcon = tileIcon;
        tileButton.setIcon(tileIcon);
        tileButton.setText("");
    }

    public String getFilePath() {
        return filePath;
    }

    public String getTileID() {
        return tileID;
    }

    JButton getTileButton() {
        return tileButton;
    }

    private ImageIcon getTileIcon() {

        return tileIcon;
    }

    TileSet() {
        tileButton.setPreferredSize(new Dimension(50, 50));
        tileButton.setLayout(new GridLayout(3, 3));
    }
    
    // Loads the corresponding image for the specified absolute tiger position and combines it with
    // the current tile image.
    void setTiger(int position){
        ImageIcon tigerIcon = new ImageIcon("tile_assets/Tigers/" + position + ".png");
        ImageIcon untigeredTileIcon = getTileIcon();
        BufferedImage combinedImg = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImg.createGraphics();
        g.drawImage(untigeredTileIcon.getImage(), 0, 0, null);
        g.drawImage(tigerIcon.getImage(), 0, 0, null);
        g.dispose();
        setTileIcon(new ImageIcon(combinedImg));
    }
}



