package vue;

import java.awt.image.BufferedImage;

import modele.Ennemi;
import modele.Goomba;

public class AnimationGoomba extends Thread {

    private Ennemi goomba;

    private int currentIndex = 0;

    public final int DELAY = 60;

    private boolean running = true;

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

    public void stopAnimation() {
        this.running = false;
    }

    @Override
    public void run() {
        running = true;

        while (running) {
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
