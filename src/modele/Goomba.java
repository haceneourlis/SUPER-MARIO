package modele;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import modele.Tile.TileManager;

public class Goomba extends Ennemi {

    private BufferedImage[] images = new BufferedImage[2];

    public Goomba(int x, int speed, TileManager tileManager, boolean movingRight) {

        super(x, 20, 20, speed, movingRight, tileManager, "goomba");
        try {
            images[0] = ImageIO.read(getClass().getResourceAsStream("/resources/goomba_sprites/goomba1.png"));
            images[1] = ImageIO.read(getClass().getResourceAsStream("/resources/goomba_sprites/goomba2.png"));

            if (images[0] == null || images[1] == null) {
                System.out.println("Goomba images NOT LOADED");
            } else {
                System.out.println("Goomba images loaded");
                this.solidArea.x = 0;
                this.solidArea.y = 0;
                this.solidArea.width = images[0].getWidth();
                this.solidArea.height = images[0].getHeight();
            }

            System.out.println("images[0] = " + images[0]);
            System.out.println("images[1] = " + images[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.position.y = CONSTANTS.LE_SOL;

        this.thread = new Thread(this);

        //
        System.out.println("Goomba y = " + this.position.y + ", height = " + this.solidArea.height);
    }

    @Override
    public BufferedImage getImage(int index) {
        return images[index % 2];
    }
}
