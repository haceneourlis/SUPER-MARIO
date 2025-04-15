package vue;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import modele.GameCharacter;
import modele.Koopa;

/**
 * Classe "abstraite" qui gère les animations du jeu avec les différentes
 * méthodes
 * nécessaires pour l'animation du joueur.
 * Bien lire la doc, c'est un peu compliqué parfois.
 */
public class Animation {

    // Current image to draw
    private BufferedImage currentToDraw;

    // taille de l'animation (nombre d'images dans l'animation)
    private int size;

    // current_animation index (où on est dans l'animation)
    private int currentIndex;

    // game character (Le personnage qu'on va vouloir dessiner)
    private GameCharacter gc;

    // Constructeur qui va initialiser l'image courante à null et la taille de
    // l'animation à 0
    // et l'index de l'animation à 0.
    public Animation(int size, GameCharacter gc) {
        this.gc = gc;
        this.currentToDraw = null;
        this.size = size;
        this.currentIndex = 0;
    }

    // getter de la currentImageToDraw
    public BufferedImage getCurrentToDraw() {
        return this.currentToDraw;
    }

    // setter de la currentImageToDraw
    public void setCurrentToDraw(BufferedImage image) {
        this.currentToDraw = image;
    }

    // méthode qui s'occupe simplement de retourner l'image à l'horizontale à 180
    // degrés
    // NE PAS LA LIRE, ça sert à rien. (c'est du chat gpt de toute façon)
    public BufferedImage flipImage(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); // Miroir horizontal
        tx.translate(-image.getWidth(), 0); // Déplacer l'image pour qu'elle reste visible

        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    // Méthode pour incrémenter l'index de l'animation
    // c'est à dire, passer d'une image de l'animation à la suivante.
    // Et créer un effet de 'loop' pour l'animation.
    public void incrementAnimationIndex(boolean right) {
        // Je rajoute + 1, car je considère que la PREMIERE image de l'animation est
        // l'image idl
        // cad, l'image où le personnage ne bouge pas.

        if (gc instanceof Koopa) {

            Koopa kp = (Koopa) gc;
            if (kp.getState() == Koopa.State.SHELL) {
                this.setCurrentToDraw(kp.getImage(0)); // car thread exception index out of bound ;
                                                       // AAAAAAAAAAAAAAAAAAAAA !
                return;
            }
        }
        this.currentIndex = (currentIndex) % size + 1;
        // si il va à droite je le retourne pas, sinon je le retourne
        if (!right) {
            this.setCurrentToDraw(this.flipImage(gc.getImage(currentIndex)));
        } else {
            this.setCurrentToDraw(gc.getImage(currentIndex));
        }
    }

}
