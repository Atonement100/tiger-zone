package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Darshil on 11/19/2016.
 */
public class PreView extends JFrame{
   public JFrame preview = new JFrame("next tile");
   public JLabel previewLabel = new JLabel();
   public JPanel previewPanel = new JPanel();

    public ImageIcon img;
    public String imgID;

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }

    public ImageIcon getImg() {
        return img;
    }

    public void setImg(ImageIcon img) {
        this.img = img;
        previewLabel.setIcon(img);
    }

    public PreView()  {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(((JButton)e.getSource()).getText());
            }
        };
         JButton rotateLeft = new JButton("Rotate left");
            rotateLeft.setBackground(Color.WHITE);
        rotateLeft.setBounds(20,125,100,25);
        rotateLeft.addMouseListener(ma);
        JButton rotateRight = new JButton("Rotate right");
        rotateRight.setBackground(Color.WHITE);
        rotateRight.addMouseListener(ma);
        rotateRight.setBounds(150,125,100,25);
        previewLabel.setBounds(110,25,50,50);
//        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        previewLabel.setVerticalAlignment(SwingConstants.TOP);

     //  rotateLeft.setVerticalAlignment(SwingConstants.BOTTOM);
//        rotateRight.setHorizontalAlignment(SwingConstants.BOTTOM);
//        rotateRight.setVerticalAlignment(SwingConstants.RIGHT);

       // rotateRight.setText("Rotate Right");
            preview.add(rotateLeft);
           preview.add(rotateRight);
        preview.add(previewLabel);
      // preview.add(previewPanel.add(rotateRight));

        preview.setSize(300, 300);

        preview.add(previewPanel);
        preview.setVisible(true);
    }
}
