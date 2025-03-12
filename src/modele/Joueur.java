package modele;

// classe Joueur qui va définir le joueur (mario) avec sa position en x et en y
public class Joueur{
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
    // variable de position en y
    private int positionY;

    // variable de position en x
    private int positionX;
    // constructeur qui ne fait rien pour le moment
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

    public void deplacer_droite(){
        if (this.vitesse < this.VITESSE_MAX){
            this.vitesse += 1;
        }
        this.positionX += this.vitesse;
    }

    public void deplacer_gauche(){
        if (this.vitesse < this.VITESSE_MAX){
            this.vitesse += 1;
        }
        this.positionX -= this.vitesse;
    }

    public void reinitialiserVitesse(){
        this.vitesse = 0;
    }



}
