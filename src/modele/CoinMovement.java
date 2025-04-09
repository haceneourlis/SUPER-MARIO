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
    }

    public void stop_thread(){
        this.thread_continue = false;
    }


    @Override
    public void run(){
        while (thread_continue) { 
            try {
                Thread.sleep(DELAY);
                
                positionDepart = Collision.coinToCatch.position;
                if (force_coin >= 0)
                    logger.log(Level.WARNING, "coin is going down");
                else
                    logger.log(Level.WARNING, "coin is going up");

                this.force_coin += CONSTANTS.GRAVITY;
                if (this.force_coin >= CONSTANTS.FORCE_MAX)
                    this.force_coin = CONSTANTS.FORCE_MAX;

                Collision.coinToCatch.position.y += this.force_coin;

                if (Collision.coinToCatch.position.y >= positionDepart.y) {
                    coinAllowedToFallDown = false;
                    Collision.coinToCatch = null;
                    force_coin = 0;
                }
            } catch (Exception e) {
            }
        }}
    }



}
