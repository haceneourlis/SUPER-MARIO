package modele;

import java.awt.Point;
import java.util.logging.*;

import javax.imageio.ImageIO;

/**
 * Classe qui hérite de Bonus, elle désigne les pièces récupérables par le
 * joueur sur son parcours.
 */
public class Coin extends GameCharacter {

    public Point positionDepart = null;

    private static final Logger logger = Logger.getLogger(Coin.class.getName());

    public Coin(Point position_coin) {
        // On appelle le constructeur de la superclass : Bonus.
        super();
        try {
            this.image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/coin.png"));

            // position du coin to catch
            this.position = position_coin;
            this.positionDepart = new Point();
            positionDepart.x = position_coin.x;
            positionDepart.y = position_coin.y + 1;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur dans le constructeur de Coin", e);
        }
    }
}
