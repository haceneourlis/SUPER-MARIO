package modele;

import java.awt.Point;
import java.util.logging.*;

/**
 * Cette classe représente un champignon dans le jeu.
 * Elle hérite de la classe GameCharacter, car cela facilite toute la gestion des collisions.
 * Donc pour avoir un champignon sur la map, il suffit de créer cet objet, donner une position initiale et le rajouter à la liste des entitées du tile manager.
 */
public class Champignon extends GameCharacter {
    

    // Logger pour debug
    private static final Logger logger = Logger.getLogger(Champignon.class.getName());

    

    /**
     * Constructeur de la classe Champignon.
     * @param position La position initiale du champignon.
     */
    public Champignon(Point position){
        // Appel du constructeur de la classe mère
        super();

        // On met la vitesse à 3 pour le champignon
        this.setSpeed(CONSTANTS.VITESSE_MUSHROOM);

        // On charge l'image du champignon
        this.setImage("/resources/champignon.png");
        
        // Par défaut, le champignon aura pour direction right
        this.setDirection("right");

        // On "set" la position initiale du champignon (cette position initiale est récupérée depuis les paramètres du constructeur)
        this.setPositionX(position.x);
        this.setPositionY(position.y);

        // L'épaisseur de la tuile du champignon
        this.getSolidArea().width = 16;

        // On charge et lance un thread pour le déplacement du champignon, voir Deplacement_entite pour plus de détails
        Deplacement_entite dp_entite = new Deplacement_entite(this, true);
        dp_entite.start();

        // Thread pour la descente du champignon (le mettre sous gravité)
        Descente descente = new Descente(this);
        descente.start();

        // On lance un thread pour la gestion des collisions du champignon avec son environnement, voir la classe en question pour + de détails
        (new Collision_entite(this, descente, dp_entite)).start();
    }
}
