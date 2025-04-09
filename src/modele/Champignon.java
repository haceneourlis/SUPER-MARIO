package modele;

import java.awt.Point;
import java.util.logging.*;
import javax.imageio.ImageIO;


public class Champignon extends GameCharacter {
    
    // Le score qui sera relié.
    private Score score;


    private static final Logger logger = Logger.getLogger(Champignon.class.getName());

    private int increment_factor;
    public Champignon(Score score, Point position){
        super();
        this.score = score;
        this.increment_factor = 50;
        this.setSpeed(3);
        try {
            this.image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/champignon.png"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Image champi pas chargée");
        }
        this.setDirection("right");
        this.setPositionX(position.x);
        this.setPositionY(position.y);
        this.getSolidArea().width = 16;

        (new Deplacement_entite(this)).start();
        Descente descente = new Descente(this);
        descente.start();
        (new Collision_entite(this, descente)).start();
    }
}
