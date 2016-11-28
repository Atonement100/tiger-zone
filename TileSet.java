

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
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
    public void setTileButtonBorder() {
        tileButton.setBorder(BorderFactory.createEtchedBorder(1));
    }

    public void setTileIcon(ImageIcon tileIcon) {
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

    public JButton getTileButton() {
        return tileButton;
    }

    public ImageIcon getTileIcon() {

        return tileIcon;
    }

    public TileSet() {
        tileButton.setPreferredSize(new Dimension(50, 50));
        tileButton.setLayout(new GridLayout(3, 3));
    }
    
    // Loads the corresponding image for the specified absolute tiger position and combines it with
    // the current tile image.
    public void setTiger(int position, int playerNum){
        ImageIcon untigeredTileIcon = getTileIcon();
        BufferedImage combinedImg = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImg.createGraphics();
        g.drawImage(untigeredTileIcon.getImage(), 0, 0, null);
        //g.drawImage(tigerIcon.getImage(), 0, 0, null);
        Point tigerPixelPos = getPositionCoordinate(position);
        if(playerNum == 0){
            g.setColor(Color.ORANGE);
        }else{
        	g.setColor(Color.BLUE);
        }
        g.fillOval(tigerPixelPos.x - 4, tigerPixelPos.y - 4, 8, 8);
        g.setColor(Color.BLACK);
        g.drawOval(tigerPixelPos.x - 4, tigerPixelPos.y - 4, 8, 8);
        g.dispose();
        setTileIcon(new ImageIcon(combinedImg));
    }
    
    private Point getPositionCoordinate(int position){
    	switch(position){
    	case 0:
    		return new Point(10, 35);
    	case 1:
    		return new Point(10, 25);
    	case 2:
    		return new Point(10, 35);
    	case 3:
    		return new Point(15, 10);
    	case 4:
    		return new Point(25, 10);
    	case 5:
    		return new Point(35, 10);
    	case 6:
    		return new Point(40, 15);
    	case 7:
    		return new Point(40, 25);
    	case 8:
    		return new Point(40, 35);
    	case 9:
    		return new Point(35, 40);
    	case 10:
    		return new Point(25, 40);
    	case 11:
    		return new Point(15, 40);
    	case 12:
    		return new Point(25, 25);
		default:
			return new Point(-1, -1);
    	}
    }
}



