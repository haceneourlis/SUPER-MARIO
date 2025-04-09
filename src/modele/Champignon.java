package modele;

import java.awt.image.BufferedImage;
import java.util.logging.*;


public class Champignon extends GameCharacter {
    
    // Le score qui sera relié.
    private Score score;

    // l'image du champignon
    private BufferedImage image;

    private static final Logger logger = Logger.getLogger(Champignon.class.getName());

    private int increment_factor;
    public Champignon(Score score){
        super();
        this.score = score;
        this.image = null;
        this.increment_factor = 50;
        this.setSpeed(10);
        this.setImage("/resources/champignon.png");
        this.setDirection("left");

        (new Deplacement_entite(this)).start();
    }
}
