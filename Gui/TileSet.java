package Gui;

import java.awt.Dimension;
import javax.swing.*;

/**
 * Created by Darshil on 11/19/2016.
 */
public class TileSet {
    private  String filePath = new String();
    private  String  tileID = new String();
    private  JButton tileButton = new JButton();
    private int X,Y;
    private  ImageIcon tileIcon = new ImageIcon();

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
    public void setTileButtonBorder() {
        tileButton.setBorder(BorderFactory.createEtchedBorder(1));
    }

    public void setTileIcon(ImageIcon tileIcon) {
        this.tileIcon = tileIcon;
        tileButton.setIcon(tileIcon);
    }



    public String getFilePath() {
        return filePath;
    }

    public String getTileID() {
        return tileID;
    }

    public JButton getTileButton() {
        return tileButton;
    }

    public ImageIcon getTileIcon() {

        return tileIcon;
    }

    public TileSet() {
        tileButton.setPreferredSize(new Dimension(50, 50));
    }

}



