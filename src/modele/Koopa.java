package modele;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import modele.Tile.TileManager;

public class Koopa extends Ennemi {
    // defines 2 states of Koopa
    public enum State {
        WALKING,
        SHELL
    }

    private State state;

    public Koopa(int x, int speed, boolean movingRight, TileManager tm) {
        super(x, 20, 20, speed, movingRight, "koopa", tm);
        this.state = State.WALKING;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
        if (state == State.SHELL) {
            try {
                BufferedImage shellImage = ImageIO.read(getClass().getResourceAsStream("/resources/koopa_sprites/koopa3.png"));
                this.image = new BufferedImage[1];
                this.image[0] = shellImage;

                this.solidArea.width = shellImage.getWidth();
                this.solidArea.height = shellImage.getHeight();

                this.movingRight = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void moveEnnemi() {
        super.moveEnnemi();
    }
}
