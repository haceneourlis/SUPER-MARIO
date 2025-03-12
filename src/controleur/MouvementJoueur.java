package controleur;

import modele.Joueur;
import modele.Jumping;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MouvementJoueur implements KeyListener {
    private boolean left_pressed;
    private boolean right_pressed;
    private boolean space_pressed;

    public MouvementJoueur() {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_RIGHT) { // Flèche droite
            right_pressed = true;
        } else if (keyCode == KeyEvent.VK_LEFT) { // Flèche gauche
            left_pressed = true;
        } else if (keyCode == KeyEvent.VK_SPACE) { // Touche espace
            space_pressed = true;
        }



    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            left_pressed = false;
        } else if (keyCode == KeyEvent.VK_SPACE) {
            space_pressed = false;
        } else if (keyCode == KeyEvent.VK_RIGHT){
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