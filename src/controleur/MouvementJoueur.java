package controleur;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * Cette classe implémente l'interface KeyListener qui va surveiller quelles touches sont pressées
 * Il utilise 3 booléens :
 * - left_pressed : true si la touche gauche est pressée
 * - right_pressed : true si la touche droite est pressée
 * - space_pressed : true si la touche espace est pressée
 * Ces informations (booléens) seront utilisées par la classe DeplacementListener pour déplacer le joueur en conséquence
 */
public class MouvementJoueur implements KeyListener {

    // Les 3 booléens qui vont être utilisés par DeplacementListener
    private boolean left_pressed;
    private boolean right_pressed;
    private boolean space_pressed;

    /* la méthode reset remet à faux tous les booléens de déplacement */
    public void reset() {
        this.left_pressed = false;
        this.right_pressed = false;
        this.space_pressed = false;
    }

    // Constructeur par défaut
    public MouvementJoueur() {}

    // Méthode qui va être appelée quand une touche est pressée
    @Override
    public void keyPressed(KeyEvent e) {
        // On récupère le code de la touche pressée
        int keyCode = e.getKeyCode();

        // On vérifie quelle touche a été pressée et on met le booléen correspondant à true
        if (keyCode == KeyEvent.VK_RIGHT) { // Flèche droite
            right_pressed = true;
        } else if (keyCode == KeyEvent.VK_LEFT) { // Flèche gauche
            left_pressed = true;
        } else if (keyCode == KeyEvent.VK_SPACE) { // Touche espace
            space_pressed = true;
        }



    }

    // Méthode qui va être appelée quand une touche est relâchée
    @Override
    public void keyReleased(KeyEvent e) {
        // On récupère le code de la touche relâchée
        int keyCode = e.getKeyCode();

        // On vérifie quelle touche a été relâchée et on met le booléen correspondant à false
        if (keyCode == KeyEvent.VK_LEFT) { // Flèche gauche
            left_pressed = false;
        } else if (keyCode == KeyEvent.VK_SPACE) { // Touche espace
            space_pressed = false;
        } else if (keyCode == KeyEvent.VK_RIGHT){ // Flèche droite
            right_pressed = false;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Rien à faire ici pour le moment
    }
    // getter de left_pressed
    public boolean isLeft_pressed() {
        return left_pressed;
    }

    // getter de right_pressed
    public boolean isRight_pressed() {
        return right_pressed;
    }


    // getter de space_pressed
    public boolean isSpace_pressed() {
        return space_pressed;
    }
}