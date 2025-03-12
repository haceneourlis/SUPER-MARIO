package view;

import model.CONSTANTS;
import view.Tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public static boolean PAUSE = false;
    public static int[][] matrice;

    public TileManager tileManager ;
    public static int maxScreenRow = 16;
    public static int maxScreenCol = 32;

    public Ennemi[] a3dawen = new Ennemi[10];
    public Mario mario;


    Thread gameThread;
    public GamePanel() {

        this.setPreferredSize(new Dimension(CONSTANTS.ScreenWidth, CONSTANTS.screenHeight));
        this.setFocusable(true);
        this.setDoubleBuffered(true);

        tileManager = new TileManager(this);
    }

    public void startGameThread(){
        gameThread = new Thread(String.valueOf(this)) ;
        gameThread.start();

    }
    public void run(){

        while(gameThread != null){
            try {
                Thread.sleep(16);

                // on bouge les ennemis ; on bouge mario ;on check les collisions
                update();
                // on repaint
                repaint();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void update()
    {


        // detecter les mouvement du mario


        // detecter les collisions ...


    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // dessiner la map
        tileManager.draw(g2);
        // dessiner mario
        mario.draw(g2);

    }


}
