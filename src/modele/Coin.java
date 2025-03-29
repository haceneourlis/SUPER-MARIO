package modele;

import java.util.logging.*;

/**
 * Classe qui hérite de Bonus, elle désigne les pièces récupérables par le joueur sur son parcours.
 */
public class Coin extends Bonus{

    // Compteur du nombre de pièces. On l'utilisera principalement pour l'afficher dans la vue.
    private int nombre_de_pieces;

    // Le score qui sera relié.
    private Score score;

    private static final Logger logger = Logger.getLogger(Coin.class.getName());
    
    public Coin(Score score) {
        // On appelle le constructeur de la superclass : Bonus.
        super();

        // On initialise le compteur de pièces à 0.
        this.nombre_de_pieces = 0;

        // On récupère l'objet score.
        this.score = score;
        
        // Cet attribut est hérité de la superclass, désigne le facteur d'incrémentation du score, le choix fait dans l'analyse est de mettre un facteur de 20 pour les pièces.
        this.increment_factor = 20;
       
    }


    /**
     * Getter du compteur du nombre de pièces récoltées.
     * 
     * @return Le nombre de pièces récoltées par le joueur.
     */
    public int getNombreDePieces() {
        return this.nombre_de_pieces;
    }

    /**
     * Méthode qui incrémente le nombre de pièces récoltées par le Joueur.
     * 
     * @return Rien
     */
    public void IncrementNombreDePieces() {
        this.nombre_de_pieces++;
        logger.log(Level.INFO, "On incr\u00e9mente le nombre de pi\u00e8ces, sa nouvelle valeur = {0}.", nombre_de_pieces);
        this.score.incrementCurrentScore(this.increment_factor);
    }

}
