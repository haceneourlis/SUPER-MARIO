package modele;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.*;

// classe Joueur qui va définir le joueur (mario) avec sa position en x et en y
public class Mario extends GameCharacter {

    // instance unique de la classe Mario
    private static Mario instance = null;

    // constante de coordonnées d'origine du joueur
    public final int X_ORIGINE = 0;

    public int vitesse = 1;
    // vitesse max constante
    public final int VITESSE_MAX = 10;

    // constructeur privé
    private Mario() {
        super();
        this.position = new Point(X_ORIGINE, CONSTANTS.LE_SOL);
    }

    public static Mario getInstance() {
        if (instance == null) {
            instance = new Mario();
            try {
                instance.image = ImageIO
                        .read(new File("C:\\Users\\ourlis\\IdeaProjects\\super_mario_PCII\\src\\resources\\mario.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void deplacer_droite() {

        this.setDirection("right");
        if (this.vitesse < this.VITESSE_MAX) {
            this.vitesse += 1;
        }

        this.position.x += this.vitesse;
    }

    public void deplacer_gauche() {
        this.setDirection("left");
        if (this.vitesse < this.VITESSE_MAX) {
            this.vitesse += 1;
        }
        this.position.x -= this.vitesse;
    }

    public void reinitialiserVitesse() {

        this.vitesse = 0;
    }

}
