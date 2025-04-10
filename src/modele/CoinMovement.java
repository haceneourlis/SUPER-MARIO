package modele;

import java.awt.Point;

public class CoinMovement extends Thread {
    
    private Coin c;

    private boolean thread_continue;

    // La position de départ de la pièce.
    public Point positionDepart;
    
    public static final int DELAY = 15;
    public CoinMovement(Coin c, Point position_depart){
        this.c = c;   
        this.thread_continue = true;
        this.positionDepart = position_depart;
        (new Descente(c)).start();
    }

    public void stop_thread(){
        this.thread_continue = false;
    }


    @Override
    public void run(){
        while (thread_continue) { 
            try {
                Thread.sleep(DELAY);

                if (this.c.position.y >= positionDepart.y) {
                    this.c.allowedToFallDown = false;
                    this.stop_thread();
                }
            } catch (Exception e) {
            }
        }}
}




