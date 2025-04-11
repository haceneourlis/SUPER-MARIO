package modele;

import java.awt.Point;
import java.util.logging.*;
import javax.imageio.ImageIO;


public class Champignon extends GameCharacter {
    
    // Le score qui sera relié.
    private ScoreManager score;


    private static final Logger logger = Logger.getLogger(Champignon.class.getName());

    


    public Champignon(ScoreManager score, Point position, int index_in_list){
        super();
        this.score = score;

        this.index_current_objet = index_in_list;

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
        Deplacement_entite dp_entite = new Deplacement_entite(this, true);
        dp_entite.start();
        Descente descente = new Descente(this);
        descente.start();
        (new Collision_entite(this, descente, dp_entite)).start();
    }
}
