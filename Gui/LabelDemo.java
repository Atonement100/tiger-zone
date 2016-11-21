package Gui;


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
 * LabelDemo.java needs one other file:
 *   images/middle.gif
 *
 */
public class LabelDemo extends JFrame {
    public final int SIZE = 21;
    public static Image ICON;

    public boolean nextmove;
    public String buttonText;
    public int rotation;
    private int x, y;
    private String[] imgId = {"JJJJ-", "JJJJX", "JJTJX", "TTTT-", "TJTJ-", "TJTT-", "LLLL-",
            "JLLL-", "LLJJ-", "JLJL-", "LJLJ-", "LJJJ-", "JLLJ-", "TLJT-",
            "TLJTP", "JLTT-", "JLTTB", "TLTJ-", "TLTJD", "TLLL-", "TLTT-",
            "TLTTP", "TLLT-", "TLLTB", "LJTJ-", "LJTJD"};
    public TileSet[][] tiles = new TileSet[SIZE][SIZE];
    public PreView preView = new PreView();
    public ImgSet[] img = new ImgSet[26];

    public void getImgID(String s){
        nextmove= false;
        setupPreview(preView,s);

    }
    public boolean getnextMove(){
        return nextmove;
    }
    public LabelDemo() {
        // addMouseListener(this);
        JFrame frame = new JFrame("Tiger Zone");
        JPanel panel = new JPanel(new GridLayout(21, 21));
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //  System.out.println(e.getButton());
                JButton reference = (JButton) e.getSource();
                //System.out.print("clicked button: " + reference.getText());
                buttonText = reference.getText();
                String[] rowcol = buttonText.split("[,]");
                x = Integer.parseInt(rowcol[0]);
                y = Integer.parseInt(rowcol[1]);
               // System.out.println("x: " + x + " y: " + y);
                //reference.setText("+");
                reference.setText("");
                reference.setIcon(preView.getImg());
            }
        };
        try {
            ICON = ImageIO.read(new File("Gui/assets/icon.png"));

        } catch (IOException e) {
        }


        // MouseAdapter adapter= new MouseAdapter() { };
        for (int i = 0; i < 26; i++) {
            img[i] = new ImgSet();
            img[i].setImageIcon("Gui/asset50x50/" + i + ".jpg");
            img[i].setImageIcon_1("Gui/assets50x50_1/" + i + ".jpg");
            img[i].setImageIcon_2("Gui/asset50x50_2/" + i + ".jpg");
            img[i].setImageIcon_3("Gui/asset50x50_3/" + i + ".jpg");
            img[i].setImgID(imgId[i]);

        }


        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tiles[i][j] = new TileSet();
                tiles[i][j].setTileButtonBorder();
                tiles[i][j].getTileButton().setBackground(Color.WHITE);
                tiles[i][j].getTileButton().addMouseListener(ma);
                panel.add(tiles[i][j].getTileButton());
            }
        }

        /*this forloop are basicly naming the button on the screen to match what server is taking*/
        for (int i = SIZE / 2; i < SIZE; i++) {
            for (int j = SIZE / 2; j < SIZE; j++) {
                tiles[i][j].getTileButton().setText(((SIZE / 2) - j) * -1 + "," + (i - (SIZE / 2)) * -1);
            }
            for (int j = 0; j < SIZE / 2; j++) {
                tiles[i][j].getTileButton().setText(((SIZE / 2) - j) * -1 + "," + (i - (SIZE / 2)) * -1);
            }

        }

        for (int i = 0; i < SIZE / 2; i++) {
            for (int j = SIZE / 2; j < SIZE; j++) {
                tiles[i][j].getTileButton().setText(((SIZE / 2) - j) * -1 + "," + (i - (SIZE / 2)) * -1);
            }
            for (int j = 0; j < SIZE / 2; j++) {
                tiles[i][j].getTileButton().setText(((SIZE / 2) - j) * -1 + "," + (i - (SIZE / 2)) * -1);
            }

        }
        /*******************************************************************************************/

        frame.setIconImage(ICON);
        frame.add(panel);
        frame.setSize(1080, 1080);

        frame.setResizable(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel panel = new JPanel(new GridLayout(0,3));
//        JFrame f = new JFrame("Jlable");
//        panel.setBorder(BorderFactory.createLineBorder(Color.black));
//        f.setLayout(new GridLayout(9,9,3,3));
//        JLabel j = new JLabel(img);
//        f.setLayout(new GridLayout(7,7));
//        j.setBorder(BorderFactory.createLineBorder(Color.black));
//        //addMouseListener(this);
//        f.setSize(new Dimension(1080,1080));
//       // j.setBounds(70,70,img.getIconWidth(),img.getIconHeight());
//        //f.add(j);
//      //
//        //
//         f.add(panel);
////      //  /f.pack();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setVisible(true);

    }

   public void setupPreview(PreView p ,String ID){
       preView.setImgID(ID);
       preView.setImage(findImg(ID).getImageIcon());
       preView.setImg_1(findImg(ID).getImageIcon_1());
       preView.setImg_2(findImg(ID).getImageIcon_2());
       preView.setImg_3(findImg(ID).getImageIcon_3());
   };

    public String getTileIndexs(int x, int y) {
        String s = "";

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
               // System.out.println(tiles[i][j].getTileButton().getText());
                if ((x + "," + y) .equals(tiles[i][j].getTileButton().getText())) {
                  //  System.out.println(tiles[i][j].getTileButton().getText());
                    s = s+i + "," + j;
                    return s;
                }
            }
        }
        return s;
    }

//    public static void main (String[] args){
//        LabelDemo l= new LabelDemo();
//        //placeStartTile();
//
//        System.out.println("hello form main");
//
//
//    }
//
//    private static void placeStartTile() {
//
//    }
//
//
//
//    int geti(){
//        return i;
//    }



    public void possibleMove(int[] x, int[] y) {


    }


    public ImgSet findImg(String tileId) {
        for (int i = 0; i < 26; i++) {
            if (img[i].getImgID() == tileId) {
                return img[i];
            }
        }
        System.out.println("Imge is not found in imgset, tile id:" + tileId);
        return null;
    }
    public void placeFirstTile(int x,int y,String id){
        String  s =getTileIndexs(x,y);
        System.out.println(s);
        String[] rowcol = s.split("[,]");
        tiles[Integer.parseInt(rowcol[0])][Integer.parseInt(rowcol[1])].setTileIcon(findImg(id).getImageIcon());


    }
}