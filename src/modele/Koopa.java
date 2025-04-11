package modele;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import vue.AnimationKoopa;
import modele.Tile.TileManager;


public class Koopa extends Ennemi {
    // defines 2 states of Koopa
    public enum State {
        WALKING,
        SHELL
    }

    private State state;
    // add: every koopa has its own animation
    private AnimationKoopa animationKoopa;

    public Koopa(int x, int speed, boolean movingRight, TileManager tm) {
        super(x, 20, 20, speed, movingRight, "koopa", tm);
        this.state = State.WALKING;

        this.animationKoopa = new AnimationKoopa(this);
        this.animationKoopa.start();
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

    // getter de l'animation
    public AnimationKoopa getAnimationKoopa() {
        return this.animationKoopa;
    }

    @Override
    public void moveEnnemi() {
        super.moveEnnemi();
    }
}
