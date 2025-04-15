package vue;

import java.awt.image.BufferedImage;

import modele.Ennemi;
import modele.Goomba;

public class AnimationGoomba extends Thread {

    private Ennemi goomba;

    private int currentIndex = 0;

    public final int DELAY = 60;

    private boolean ok = true;

    private int old_x;

    public AnimationGoomba(Ennemi goomba) {
        this.goomba = goomba;
        this.old_x = goomba.getPosition().x;
    }

    public Goomba getGoomba() {
        return (Goomba) goomba;
    }

    public BufferedImage getCurrentToDraw() {
        return goomba.getImage(currentIndex);
    }

    public void stopThread() {
        this.ok = false;
    }

    @Override
    public void run() {
        this.ok = true;

        while (this.ok) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int delta = goomba.getPosition().x - old_x;

            if (delta != 0) {
                // if goomba is moving, switch the frame
                currentIndex = (currentIndex + 1) % 2;
            } else {
                currentIndex = 0;
            }

            old_x = goomba.getPosition().x;
        }
    }

}
