package modele;


/**
 * Cette classe définit le joueur (Mario) avec sa position en x et en y (en 0,0 à l'origine).
 * Elle contient des méthodes pour déplacer le joueur à droite et à gauche avec une certaine vitesse.
 * Nous avons également des constantes AFTER BEFORE HMAX HMIN, qui délimitent les frontières dans le modèle qui sera projetée dans la vue.
 */
public class Joueur{
    // Constante des coordonnées d'origine (0,0)
    public final int X_ORIGINE = 0;
    public final int Y_ORIGINE = 0;

    // Constantes des frontières
    public final int BEFORE = -50;
    public final int AFTER = 240;
    public final int HMAX = 60;
    public final int HMIN = -30;

    // vitesse du joueur
    public int vitesse = 1;

    // Constante de la vitesse maximale atteignable
    public final int VITESSE_MAX = 4;
    // Constante du facteur de décélération
    public final int DECELERATION = 3;


    // variable de position en y
    private int positionY;

    // variable de position en x
    private int positionX;


    // constructeur qui ne fait que mettre le joueur à l'origine (0,0)
    public Joueur(){
        this.positionY = Y_ORIGINE;
        this.positionX = X_ORIGINE;
    }

    // getter de la position en Y
    public int getPositionY(){
        return this.positionY;
    }

    // getter de la position en X
    public int getPositionX(){
        return this.positionX;
    }

    // setter de la position en Y
    public void setPositionY(int y){
        this.positionY = y;
    }


    /**
     * Méthode pour déplacer le joueur à droite.
     * Si la vitesse est inférieure à la vitesse maximale, on l'incrémente de 1.
     * On incrémente la position en x de la vitesse.
     */
    public void deplacer_droite(){
        if (this.vitesse < this.VITESSE_MAX){
            this.vitesse += 1;
        }
        this.positionX += this.vitesse;
    }


    /**
     * Méthode pour déplacer le joueur à gauche.
     * Si la vitesse est inférieure à la vitesse maximale, on l'incrémente de 1.
     * On décrémente la position en x de la vitesse.
     */
    public void deplacer_gauche(){
        if (this.vitesse < this.VITESSE_MAX){
            this.vitesse += 1;
        }
        this.positionX -= this.vitesse;
    }

    /**
     * Méthode pour décrémenter la vitesse du joueur.
     * Si la vitesse est supérieure à 0, on la décrémente du facteur de décélaration.
     */
    public void decelerer() {
        if (this.vitesse - DECELERATION > 0) {
            this.vitesse -= DECELERATION;
        } else {
            this.vitesse = 0;
        }
    }

}
