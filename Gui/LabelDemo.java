package Gui;


        import java.awt.*;
        import java.awt.event.*;
        import java.io.File;
        import java.io.IOException;
        import javax.imageio.ImageIO;
        import javax.swing.*;
        import javax.swing.border.LineBorder;

/*
 * LabelDemo.java needs one other file:
 *   images/middle.gif
 *
 */
public class LabelDemo extends JFrame  {
    public  final int SIZE = 20;
    public static Image ICON;
    public static int i ;
    public LabelDemo() {
       // addMouseListener(this);
        JFrame frame = new JFrame("Tiger Zone");
        ImageIcon [] img = new ImageIcon [24];
        ImageIcon img2 = new ImageIcon("Gui/asset50x50/1.jpg");
        JPanel panel = new JPanel(new GridLayout(20,20));
        MouseAdapter ma =  new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JLabel reference =(JLabel)e.getSource();

                reference.setIcon(img2);
            }
        };
        try {
           ICON=ImageIO.read(new File("Gui/assets/icon.png"));

        } catch (IOException e) {
        }
        JLabel [][] tiles= new JLabel[SIZE][SIZE];

      // MouseAdapter adapter= new MouseAdapter() { };
    for (int i=0; i<24;i++) {
        img[i]= new ImageIcon("Gui/asset50x50/"+i+".jpg");

    }

        for (i=0;i<SIZE;i++){
            for ( int j=0;j<SIZE;j++){
                tiles[i][j] = new JLabel();
                //tiles[i][j].tilelabel = new JLabel();
               tiles[i][j].setBorder(BorderFactory.createEtchedBorder(1));

                tiles[i][j].addMouseListener(ma);
                // tiles[i][j].setIcon(img);
//                tiles[i][j].setIcon(img[i]);
                panel.add(tiles[i][j]);
            }
        }
        frame.setIconImage(ICON);
        frame.add(panel);
       frame.setSize(1080,1080);

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

//    public static void main (String[] args){
//        LabelDemo l= new LabelDemo();
//        placeStartTile();
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
}