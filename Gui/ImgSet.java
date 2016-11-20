package Gui;

import javax.swing.*;

/**
 * Created by Darshil on 11/19/2016.
 */
public class ImgSet {
    private String file;
    private ImageIcon imageIcon;
    private String imgID;

    public ImgSet() {
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void setImage(String file) {
        imageIcon =new ImageIcon(file);


    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }
}
