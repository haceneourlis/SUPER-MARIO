package modele;

import java.util.logging.*;

/**
 * Classe qui gere le deplacement d'une entite.
 * Elle implémente un Thread.
 * Elle vérifie à chaque instant si l'entité doit se déplacer à droite ou à
 * gauche
 * et effectue ce déplacement.
 */
public class Deplacement_entite extends Thread {

    // L'entité à déplacer.
    GameCharacter the_entity_to_move;

    // La direction du déplacement : true = droite, false = gauche.
    protected boolean go_right;

    // Logger pour debug
    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    /**
     * Constructeur de la classe Deplacement_entite.
     * 
     * @param gc       L'entité à déplacer.
     * @param go_right La direction du déplacement : true = droite, false = gauche.
     */
    public Deplacement_entite(GameCharacter gc, boolean go_right) {
        this.the_entity_to_move = gc;
        this.go_right = go_right;

    }

    /**
     * Méthode run qui lance le thread.
     * A chaque "DELAY", elle déplace l'entité à droite ou à gauche selon la
     * direction spécifiée.
     */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(CONSTANTS.DELAY_DEPLACEMENT_ENTITE);
                if (this.go_right) {
                    this.the_entity_to_move.deplacer_droite();
                } else {
                    this.the_entity_to_move.deplacer_gauche();
                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erreur du thread deplacement_entite");
            }

        }
    }
}
