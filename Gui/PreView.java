package Gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Darshil on 11/19/2016.
 */
public class PreView extends JFrame{
   public JFrame preview = new JFrame("next tile");
   public JLabel previewLabel = new JLabel();
   public JPanel previewPanel = new JPanel();
    public JButton rotateLeft = new JButton();
    public JButton rotateRight = new JButton();
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

    public PreView() throws HeadlessException {
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        previewLabel.setVerticalAlignment(SwingConstants.TOP);
        rotateLeft.setText("Rotate Left");
        rotateRight.setText("Rotate Right");

      // preview.add(previewPanel.add(rotateRight));

        preview.setSize(300, 300);

        preview.add(previewPanel.add(previewLabel));
        preview.setVisible(true);
    }
}
