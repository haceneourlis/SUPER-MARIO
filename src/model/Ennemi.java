package model;

// package caractere;

// -------------------------------------------------------
// un ennemi qui bouge de gauche à droite (position initiale se trouve à droite de la fenêtre)
// si l'ennemi atteint la frontière, il change de direction

// definir les coordonnées et la taille de l'ennemi
// definir la vitesse de l'ennemi
// definir les frontières de la fenêtre
// definir une methode pour faire bouger l'ennemi
// definir un thread pour gérer le mouvement de l'ennemi

// -------------------------------------------------------



// une classe qui hérite de la classe Thread pour gérer les ennemis
// l'enemi bouge de droite à gauche et ne peut pas sortir de la fenêtre
public class Ennemi extends Thread{
    // les coordonnees de l'ennemi
    private int x;
    private int y;
    // la taille de l'ennemi
    private int width;
    private int height;
    // la vitesse de l'ennemi
    private int speed;
    // les frontières
    private int leftBorder;
    private int rightBorder;

    private boolean running = true;
    private boolean movingRight; // Indique si l'ennemi va à droite

    public Ennemi(int x, int y, int width, int height, int speed, int leftBorder, int rightBorder, boolean movingRight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;

        this.movingRight = movingRight; // l'ennemi commence par aller à droite
    }

    // Ajouter ces méthodes dans Ennemi.java
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isMovingRight() {
        return movingRight;
    } // Indique si l'ennemi va à droite


    // faire bouger l'ennemi
    public void moveEnnemi() {
        if (movingRight) {
            x += speed;
            if (x + width >= rightBorder) { // si l'ennemi atteint la frontière droite
                x = rightBorder - width; // le mettre à la frontière droite
                movingRight = false; // changer la direction
            }
        }   else {
            x -= speed;
            if (x <= leftBorder) {
                x = leftBorder;
                movingRight = true;
            }
        }
    }
    

    // demarrer le thread
    @Override
    public void run() {
        while (running) {
            moveEnnemi();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // arreter le thread
    public void stopMoving() {
        running = false;
    }
}
