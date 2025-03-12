package view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// class singleton Mario
public class Mario extends GameCharacter {
    private static Mario instance = null;
    // constante de coordonnées d'origine du joueur
    public final int X_ORIGINE = 0;
    public final int Y_ORIGINE = 0;
    public final int BEFORE = -30;
    public final int AFTER = 240;
    public final int HMAX = 60;
    public final int HMIN = -30;
    public int vitesse = 1;
    // vitesse max constante
    public final int VITESSE_MAX = 10;


    // constructeur privé
    private Mario() {
    }

    public static Mario getInstance() {
        if (instance == null) {
            instance = new Mario();
            try {
                instance.image = ImageIO.read(new File("view.ressources/mario.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(image, this.getPosition().x,this.getPosition().y, null);
    }


    public void deplacer_droite(){
        if (this.vitesse < this.VITESSE_MAX){
            this.vitesse += 1;
        }
        this.setPositionX(this.getPosition().x+this.vitesse);
    }

    public void deplacer_gauche(){
        if (this.vitesse < this.VITESSE_MAX){
            this.vitesse += 1;
        }
        this.setPositionX(this.getPosition().x-this.vitesse);
    }

    public void reinitialiserVitesse(){
        this.vitesse = 0;
    }
}
