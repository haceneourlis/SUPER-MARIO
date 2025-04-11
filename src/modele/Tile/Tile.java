package modele.Tile;

import java.awt.image.BufferedImage;


/**
 * Cette classe représente un objet tuile, qui représente une tuile utilisée pour dessiner la "map" de jeu.
 */
public class Tile {
    // l'Image de la tuile
    public BufferedImage image;
    // Si la tuile est un obstacle, nécessite donc une collision
    public boolean collision = false;
}
