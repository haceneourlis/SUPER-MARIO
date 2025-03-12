package view;

import model.CONSTANTS;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GameCharacter extends Rectangle {
    // solid area of the character
    public  Rectangle solidArea;
    // position of the character
    private Point position;
    // speed of the character
    private int speed;
    // direction of the character
    private String direction;
    // image of the character
    BufferedImage image;



    // constructor
    GameCharacter() {
        // rectangle representing the solid area of the character (collision area) : tout le charcter est une zone solide
        this.solidArea = new Rectangle();
        solidArea.x = 8 ;
        solidArea.y = 16 ;
        solidArea.height = 32 ;
        solidArea.width = 32 ;

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

    // set the image of the character
    public void setImage(String path_to_image) {
        try {
            this.image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path_to_image)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // get the image of the character
    public BufferedImage getImage() {
        return this.image;
    }
}
