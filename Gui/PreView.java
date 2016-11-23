package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Darshil on 11/19/2016.
 */
public class PreView extends JFrame {
    public JFrame preview = new JFrame("next tile");
    public JButton previewButton = new JButton();
    public JPanel previewPanel = new JPanel();
    public JLabel score = new JLabel();
    public ImageIcon img;
    public ImageIcon img_1;
    public ImageIcon img_2;
    public ImageIcon img_3;

    public ImageIcon getReturnImg() {
        return returnImg;
    }

    public void setReturnImg(ImageIcon returnImg) {
        this.returnImg = returnImg;
    }

    public ImageIcon returnImg;
    public String imgID;

    public int getMeeple() {

        return meeple;
    }

    public int meeple;

    public int getRotation() {
        return Rotation;
    }

    private int Rotation = 0;

    public ImageIcon getImg_1() {
        return img_1;
    }

    public void setImg_1(ImageIcon img_1) {
        this.img_1 = img_1;
    }

    public ImageIcon getImg_2() {
        return img_2;
    }

    public void setImg(ImageIcon img) {
        this.img = img;
    }

    public ImageIcon getImg() {
        return img;
    }

    public void setImg_2(ImageIcon img_2) {
        this.img_2 = img_2;
    }

    public ImageIcon getImg_3() {
        return img_3;
    }

    public void setImg_3(ImageIcon img_3) {
        this.img_3 = img_3;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }

    public void displayScore(int i) {
        score.setText("Score: " + i);
        score.setBounds(25, 250, 25, 45);

    }

    public void setImage(ImageIcon img) {

        previewButton.setIcon(img);
    }

    public PreView()
        {
            setReturnImg(img);
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                previewButton = (JButton) e.getSource();
                //System.out.print("clicked button: " + previewButton.getText());
                previewButton.setText("");
                Rotation = (Rotation + 1) % 4;
                System.out.println(Rotation);
                 if (Rotation == 1) {
                    previewButton.setIcon(img_1);
                    setReturnImg(img_1);
                   System.out.println("ccw once");
                }
                else if (Rotation == 2) {
                     setReturnImg(img_2);
                    previewButton.setIcon(img_2);
                    System.out.println("ccw two");
                }
              else  if (Rotation == 3) {
                     setReturnImg(img_3);
                    previewButton.setIcon(img_3);
                    System.out.println("ccw three");
                }
               else if (Rotation==0){
                     setReturnImg(img);
                    previewButton.setIcon(img);
                    System.out.println("ccw 0");
                }
                preview.add(previewButton);
            }
        };

        preview.add(score);
        previewButton.addMouseListener(ma);
        previewButton.setBounds(110, 25, 50, 50);
        preview.add(previewButton);
        preview.setSize(300, 300);
        preview.add(previewPanel);
        preview.setVisible(true);
    }


}
