package modele;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class GameCharacter extends Rectangle {
    // solid area of the character
    public Rectangle solidArea;
    // position of the character
    public Point position;
    // speed of the character
    protected int speed;
    // direction of the character
    private String direction;
    // image of the character
    public BufferedImage image;

    public boolean allowedToFallDown;

    public boolean canMove;

    // constructor
    protected GameCharacter() {
        // rectangle representing the solid area of the character (collision area) :
        // tout le charcter est une zone solide
        this.solidArea = new Rectangle();
        solidArea.x = CONSTANTS.slidAreaDefaultX;
        solidArea.y = CONSTANTS.slidAreaDefaultY;
        solidArea.height = 8;
        solidArea.width = 8;

        this.position = new Point();
        this.speed = 0;
        this.direction = "left"; // default direction
        this.image = null;
    }

    // set the solid area of the character
    public void setSolidArea() {
        // TODO : ??
    }

    // get the solid area of the character
    public Rectangle getSolidArea() {
        return this.solidArea;
    }

    // set the x position of the character
    public void setPositionX(int x) {
        this.position.x = x;
    }

    // set the y position of the character
    public void setPositionY(int y) {
        this.position.y = y;
    }

    // get the position of the character
    public Point getPosition() {
        return this.position;
    }

    // set the speed of the character
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // get the speed of the character
    public int getSpeed() {
        return this.speed;
    }

    // get the direction of the character
    public String getDirection() {
        return this.direction;
    }

    // set the direction of the character
    public void setDirection(String direction) {
        this.direction = direction;
    }

    // set the image of the character
    public void setImage(String path_to_image) {
        try {
            this.image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path_to_image)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // get the image of the character (par défaut l'appeler avec 0)
    public BufferedImage getImage(int index) {
        return this.image;
    }

    // draw method
    public void draw(Graphics2D g2, Point position_dans_la_vue) {
        g2.drawImage(this.image, position_dans_la_vue.x, position_dans_la_vue.y, null);
    }

    /**
     * Méthode pour déplacer le joueur à droite.
     * Si la vitesse est inférieure à la vitesse maximale, on l'incrémente de 1.
     * On incrémente la position en x de la vitesse.
     */
    public void deplacer_droite() {
        this.setDirection("right");
        if (canMove)
            this.position.x += this.speed;
    }

    /**
     * Méthode pour déplacer le joueur à gauche.
     * Si la vitesse est inférieure à la vitesse maximale, on l'incrémente de 1.
     * On décrémente la position en x de la vitesse.
     */
    public void deplacer_gauche() {
        this.setDirection("left");
        if (canMove)
            this.position.x -= this.speed;
    }


    public void noMoving() {
        this.canMove = false;
    }

    public void yesMoving() {
        this.canMove = true;
    }

}
