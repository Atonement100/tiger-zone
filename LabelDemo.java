import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
 * LabelDemo.java needs one other file:
 *   images/middle.gif
 *
 */
public class LabelDemo extends JFrame {
    public int size = 2;
    public static Image ICON;
    private boolean done= false;
    public boolean nextmove;



    private int rotation;
    private int meeple;
    public String buttonText;
    private int x, y;
    private String[] imgId = {"JJJJ-", "JJJJX", "JJTJX", "TTTT-", "TJTJ-", "TJJT-", "TJTT-",
            "LLLL-", "JLLL-", "LLJJ-", "JLJL-", "LJLJ-", "LJJJ-", "JLLJ-",
            "TLJT-", "TLJTP", "JLTT-", "JLTTB", "TLTJ-", "TLTJD", "TLLL-",
            "TLTT-", "TLTTP", "TLLT-", "TLLTB", "LJTJ-", "LJTJD", "TLLLC"};
    public TileSet[][] tiles;
    public PreView preView = new PreView();
    public ImgSet[] img = new ImgSet[28];
    private JLabel p1ScoreLabel, p2ScoreLabel;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean getnextMove() {
        return nextmove;
    }


    public LabelDemo(int boardSize) {
        size = boardSize;
        tiles = new TileSet[size][size];
        JFrame frame = new JFrame("Tiger Zone");
        JPanel panel = new JPanel(new GridLayout(size, size));
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
                //reference.setIcon(preView.returnImg);
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter a meeple placement for placed tile range [-1,12]");
                setMeeple(scanner.nextInt());
                setDone(true);
            }
        };
        try {
            ICON = ImageIO.read(new File("tile_assets/assets/icon.png"));
        } catch (IOException e) {

        }
        /*Initialize all the img to imgset class*/
        for (int i = 0; i < 28; i++) {
            img[i] = new ImgSet();
            img[i].setImageIcon("tile_assets/Tiles50x50/" + i + ".jpg");
            img[i].setImageIcon_1("tile_assets/Tiles50x50_1/" + i + ".jpg");
            img[i].setImageIcon_2("tile_assets/Tiles50x50_2/" + i + ".jpg");
            img[i].setImageIcon_3("tile_assets/Tiles50x50_3/" + i + ".jpg");
            img[i].setImgID(imgId[i]);

        }

        /*Initialize all the button on the Game board and Name them according to row and col*/
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tiles[i][j] = new TileSet();
                tiles[i][j].setTileButtonBorder();
                tiles[i][j].getTileButton().setBackground(Color.WHITE);
                tiles[i][j].getTileButton().setText(i + "," + j);

                tiles[i][j].getTileButton().addMouseListener(ma);
                panel.add(tiles[i][j].getTileButton());
            }
        }
        // Setup score panel.
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.LINE_AXIS));
        JPanel player1Panel = new JPanel();
        p1ScoreLabel = new JLabel("Player 1 Score: 0");
        player1Panel.add(p1ScoreLabel);
        player1Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JPanel player2Panel = new JPanel();
        p2ScoreLabel = new JLabel("Player 2 Score: 0");
        player2Panel.add(p2ScoreLabel);
        player2Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scorePanel.add(player1Panel);
        scorePanel.add(player2Panel);

        /*******************************************************************************************/
        /*Display the Jframe for the main Game board*/
        frame.setIconImage(ICON);
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(scorePanel, BorderLayout.NORTH);
        frame.setSize(800, 600);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    /*Set up the preview windo for the next tile*/
    public void setupPreview(String ID) {
        preView.setImgID(ID);
        preView.setImage(findImg(ID).getImageIcon());
        preView.setImg_1(findImg(ID).getImageIcon_1());
        preView.setImg_2(findImg(ID).getImageIcon_2());
        preView.setImg_3(findImg(ID).getImageIcon_3());
        preView.setReturnImg(findImg(ID).getImageIcon());
    }
    public void refreshPreview(String ID, int rotation) {
        if (rotation==0){
            preView.setImage(findImg(ID).getImageIcon());
            preView.setImg(findImg(ID).getImageIcon());
        }else if (rotation==1){
            preView.setImage(findImg(ID).getImageIcon_1());
            preView.setImg(findImg(ID).getImageIcon_1());

        }else if (rotation==2){
            preView.setImage(findImg(ID).getImageIcon_2());
            preView.setImg(findImg(ID).getImageIcon_2());
        }else if (rotation==3){
            preView.setImage(findImg(ID).getImageIcon_3());
            preView.setImg(findImg(ID).getImageIcon_3());
        }
    }



    /*this will get the index of the button on the screen if layout is inplemented as Dave has in documentation*/
    public String getTileIndexs(int x, int y) {
        String s = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((x + "," + y).equals(tiles[i][j].getTileButton().getText())) {
                    s = s + i + "," + j;
                    return s;
                }
            }
        }
        return s;
    }

    public void getImgID(String s) {
        nextmove = false;
        setupPreview(s);

    }

    public int getMeeple() {
        return meeple;
    }

    public void setMeeple(int meeple) {
        this.meeple = meeple;
    }

    public ImgSet findImg(String tileId) {
        for (int i = 0; i < 28; i++) {
            if (img[i].getImgID().equals(tileId)) {
                return img[i];
            }
        }
        System.out.println("Imge is not found in imgset, tile id:" + tileId);
        return null;
    }

    public void placeFirstTile(int x, int y, String id) {
        tiles[x][y].setTileIcon(findImg(id).getImageIcon());
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
    public int getRotation() {
        rotation= preView.getRotation();
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
    public void placeComputertile(int row, int col, int tileRotation, String guiID) {
        setRotation(tileRotation);
        if (rotation==0){
            tiles[row][col].setTileIcon(findImg(guiID).getImageIcon());
        }else if (rotation==1){
            tiles[row][col].setTileIcon(findImg(guiID).getImageIcon_1());

        }else if (rotation==2){
            tiles[row][col].setTileIcon(findImg(guiID).getImageIcon_2());
        }else if (rotation==3){
            tiles[row][col].setTileIcon(findImg(guiID).getImageIcon_3());
        }

    }
    
    // Place a tiger icon (orange dot) on the tile.
    public void placeTiger(int row, int col, int tigerPos){
        tiles[row][col].setTiger(tigerPos);
    }
    
    
    public void updateScores(int p1Score, int p2Score){
        p1ScoreLabel.setText("Player 1 Score: " + p1Score);
        p1ScoreLabel.repaint();
        p2ScoreLabel.setText(("Player 2 score: " + p2Score));
        p2ScoreLabel.repaint();
    }
}