import javax.swing.*;

class ImgSet {
    private String file;
    private ImageIcon imageIcon;
    private ImageIcon imageIcon_1;
    private ImageIcon imageIcon_2;
    private ImageIcon imageIcon_3;
    private String imgID;

    ImgSet() {
    }
    ImageIcon getImageIcon_1() {
        return imageIcon_1;
    }

    void setImageIcon_1(String file) {
        imageIcon_1 =new ImageIcon(file);
    }

    ImageIcon getImageIcon_2() {
        return imageIcon_2;
    }

    void setImageIcon_2(String file) {
        imageIcon_2 =new ImageIcon(file);
    }

    ImageIcon getImageIcon_3() {
        return imageIcon_3;
    }

    void setImageIcon_3(String file) {
        imageIcon_3 =new ImageIcon(file);
    }

    ImageIcon getImageIcon() {
        return imageIcon;
    }

    void setImageIcon(String file) {
        imageIcon =new ImageIcon(file);


    }

    String getImgID() {
        return imgID;
    }

    void setImgID(String imgID) {
        this.imgID = imgID;
    }
}
