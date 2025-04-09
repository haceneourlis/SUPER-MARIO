package modele;
import java.util.logging.*;

public class Deplacement_entite extends Thread {

    GameCharacter the_entity_to_move;

    private static final int DELAY = 17;


    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    public Deplacement_entite(GameCharacter gc){
        this.the_entity_to_move = gc;

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(DELAY);
                if (this.the_entity_to_move.getDirection() == "left"){
                    this.the_entity_to_move.position.x -= this.the_entity_to_move.getSpeed();
                } else {
                    this.the_entity_to_move.position.x += this.the_entity_to_move.getSpeed();
                }
                

            } catch (Exception e){
                logger.log(Level.SEVERE, "Erreur du thread deplacement_entite");   
            }

        }
    }
}
