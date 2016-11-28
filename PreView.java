

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
    public JLabel score = new JLabel();
    private JLabel upLabel, rightLabel, downLabel, leftLabel;
    private JPanel buttonPanel;
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
                previewButton.setText("");
                Rotation = (Rotation + 1) % 4;
                 if (Rotation == 1) {
                    previewButton.setIcon(img_1);
                    setReturnImg(img_1);
                    upLabel.setText("W");
                    rightLabel.setText("N");
                    downLabel.setText("E");
                    leftLabel.setText("S");
                }
                else if (Rotation == 2) {
                     setReturnImg(img_2);
                    previewButton.setIcon(img_2);
                    upLabel.setText("S");
                    rightLabel.setText("W");
                    downLabel.setText("N");
                    leftLabel.setText("E");
                }
              else  if (Rotation == 3) {
                     setReturnImg(img_3);
                    previewButton.setIcon(img_3);
                    upLabel.setText("E");
                    rightLabel.setText("S");
                    downLabel.setText("W");
                    leftLabel.setText("N");
                }
               else if (Rotation==0){
                     setReturnImg(img);
                    previewButton.setIcon(img);
                    upLabel.setText("N");
                    rightLabel.setText("E");
                    downLabel.setText("S");
                    leftLabel.setText("W");
                }
            }
        };
        upLabel = new JLabel("N");
        JPanel upPanel = new JPanel();
        upPanel.add(upLabel, BorderLayout.CENTER);
        rightLabel = new JLabel("E");
        downLabel = new JLabel("S");
        JPanel downPanel = new JPanel();
        downPanel.add(downLabel, BorderLayout.CENTER);
        leftLabel = new JLabel("W");

        preview.add(score);
        previewButton.addMouseListener(ma);
        previewButton.setBounds(110, 25, 50, 50);
        preview.add(previewButton);
        //preview.setSize(300, 300);
        previewButton.setPreferredSize(new Dimension(50, 50));
        buttonPanel = new JPanel();
        buttonPanel.add(previewButton, BorderLayout.CENTER);
        preview.add(buttonPanel, BorderLayout.CENTER);
        preview.add(upPanel, BorderLayout.NORTH);
        preview.add(rightLabel, BorderLayout.EAST);
        preview.add(downPanel, BorderLayout.SOUTH);
        preview.add(leftLabel, BorderLayout.WEST);
        preview.pack();
        preview.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        preview.setVisible(true);
    }


}
