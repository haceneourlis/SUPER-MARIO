package modele;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import modele.Tile.TileManager;
import vue.AnimationGoomba;

public class Goomba extends Ennemi {

    private BufferedImage[] images = new BufferedImage[2];
    private AnimationGoomba animationGoomba;

    public Goomba(int x, int speed, boolean movingRight, TileManager tm) {
        super(x, 20, 20, speed, movingRight, tm);

        try {
            images[0] = ImageIO.read(getClass().getResourceAsStream("/resources/goomba_sprites/goomba1.png"));
            images[1] = ImageIO.read(getClass().getResourceAsStream("/resources/goomba_sprites/goomba2.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.solidArea.x = 0;
        this.solidArea.y = 0;
        this.solidArea.width = images[0].getWidth();
        this.solidArea.height = images[0].getHeight();

        this.animationGoomba = new AnimationGoomba(this);
        this.animationGoomba.start();

        this.position.y = CONSTANTS.LE_SOL;
    }

    // getter de l'animation
    public AnimationGoomba getAnimationGoomba() {
        return this.animationGoomba;
    }

    @Override
    public BufferedImage getImage(int index) {
        return images[index % 2];
    }
}
