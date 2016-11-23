package Gui;

import javax.swing.*;

/**
 * Created by Darshil on 11/19/2016.
 */
public class ImgSet {
    private String file;
    private ImageIcon imageIcon;
    private ImageIcon imageIcon_1;
    private ImageIcon imageIcon_2;
    private ImageIcon imageIcon_3;
    private String imgID;

    public ImgSet() {
    }
    public ImageIcon getImageIcon_1() {
        return imageIcon_1;
    }

    public void setImageIcon_1(String file) {
        imageIcon_1 =new ImageIcon(file);
    }

    public ImageIcon getImageIcon_2() {
        return imageIcon_2;
    }

    public void setImageIcon_2(String file) {
        imageIcon_2 =new ImageIcon(file);
    }

    public ImageIcon getImageIcon_3() {
        return imageIcon_3;
    }

    public void setImageIcon_3(String file) {
        imageIcon_3 =new ImageIcon(file);
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(String file) {
        imageIcon =new ImageIcon(file);


    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }
}
