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
    public ImageIcon img;

    public ImageIcon getImg() {
        return img;
    }

    public void setImg(ImageIcon img) {
        this.img = img;
        previewLabel.setIcon(img);
    }

    public PreView() throws HeadlessException {
        preview.setSize(200, 200);
        preview.add(previewPanel.add(previewLabel));
        preview.setVisible(true);
    }
}
