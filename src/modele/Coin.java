package modele;

import java.awt.Point;
import java.util.logging.*;

/**
 * Cette classe représente une pièce dans le jeu.
 * Elle hérite de la classe GameCharacter, car cela facilite toute la gestion des collisions.
 */
public class Coin extends GameCharacter {

    // La position de départ de la pièce (là où elle va apparaitre)
    public Point positionDepart = null;

    // Logger pour debug
    private static final Logger logger = Logger.getLogger(Coin.class.getName());

    /**
     * Constructeur de la classe Coin.
     * Prend en paramètre la position de départ de la pièce
     * @param position_coin
     */
    public Coin(Point position_coin) {
        // On appelle le constructeur de la superclass : GameCharacter.
        super();
        // On charge l'image de coin
        this.setImage("/resources/coin.png");

        // position du coin, correspond à la position donnée de départ
        this.position = position_coin;

        // Et poisition de départ on lui donne cette même position donnée, mais on rajoute un petit facteur en y.
        this.positionDepart = new Point();
        positionDepart.x = position_coin.x;
        positionDepart.y = position_coin.y + 2;
    
    }
}
