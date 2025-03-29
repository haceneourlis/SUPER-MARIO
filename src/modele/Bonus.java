package modele;

import java.awt.image.BufferedImage;


/*
 * Superclass Bonus.
 * Cette class modélise des objets bonus avec leurs attributs généraux.
 */
public class Bonus {
    // facteur d'incrémentation du score 
    protected int increment_factor;


    public Bonus() {
        // On initialise ce facteur à 0 au début pour éviter des bugs.
        this.increment_factor = 0;
    
    }
    
}
