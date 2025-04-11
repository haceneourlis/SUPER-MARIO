package modele;
import java.util.logging.*;

public class Deplacement_entite extends Thread {

    GameCharacter the_entity_to_move;

    private static final int DELAY = 17;

    protected boolean go_right;


    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    public Deplacement_entite(GameCharacter gc, boolean go_right){
        this.the_entity_to_move = gc;
        this.go_right = go_right;

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(DELAY);
                if (this.go_right){
                    this.the_entity_to_move.deplacer_droite();
                } else {
         
                    this.the_entity_to_move.deplacer_gauche();
                }
                

            } catch (Exception e){
                logger.log(Level.SEVERE, "Erreur du thread deplacement_entite");   
            }

        }
    }
}
