package modele;

import java.awt.Rectangle;
import java.util.Iterator;

import modele.Tile.TileManager;

public class Collision_Ennemi extends Thread {

    TileManager tm;
    Descente threadDescente;
    Mario mario;

    Rectangle marioHitbox;
    Rectangle ennemiHitbox;

    public Collision_Ennemi(Descente threadDescente) {
        this.threadDescente = threadDescente;
        this.mario = Mario.getInstance();
        this.tm = TileManager.getInstance();
    }

    @Override
    public void run() {
        // collision avec les ennemis
        while (true) {

            try {

                sleep(CONSTANTS.DELAY_COLLISION_ENNEMI);
                synchronized (tm.getListeEnnemis()) {
                    Iterator<Ennemi> iterator = tm.getListeEnnemis().iterator();
                    while (iterator.hasNext()) {
                        Ennemi ennemi = iterator.next();

                        // Mario's collision area
                        marioHitbox = new Rectangle(
                                mario.getPosition().x + mario.getSolidArea().x,
                                mario.getPosition().y + mario.getSolidArea().y,
                                mario.getSolidArea().width,
                                mario.getSolidArea().height);

                        // Enemy's collision area
                        ennemiHitbox = new Rectangle(
                                ennemi.getPosition().x + ennemi.getSolidArea().x,
                                ennemi.getPosition().y + ennemi.getSolidArea().y,
                                ennemi.getSolidArea().width,
                                ennemi.getSolidArea().height);

                        // collision avec les ennemis
                        if (marioHitbox.intersects(ennemiHitbox)) {
                            int marioFeetY = mario.getPosition().y + mario.getSolidArea().y
                                    + mario.getSolidArea().height;
                            int ennemiHeadY = ennemi.getPosition().y + ennemi.getSolidArea().y;

                            boolean fromAbove = marioFeetY <= ennemiHeadY + 15 && marioFeetY >= ennemiHeadY;
                            boolean falling = (threadDescente.force > 0);

                            boolean collisionHandled = false;

                            if (fromAbove && falling && !mario.isInvincible()) {
                                if (ennemi instanceof Koopa) {
                                    Koopa koopa = (Koopa) ennemi;
                                    if (koopa.getState() == Koopa.State.WALKING) {
                                        // koopa becomes a shell
                                        koopa.setState(Koopa.State.SHELL);
                                        threadDescente.force = -CONSTANTS.IMPULSION_MARIO / 2;
                                        ScoreManager.incrementShells();
                                        // ScoreManager.incrementCurrentScore("shell");
                                    } else if (koopa.getState() == Koopa.State.SHELL) {
                                        // koopa is already a shell, once mario jumps on it, it will be removed
                                        iterator.remove();
                                        threadDescente.force = -CONSTANTS.IMPULSION_MARIO / 2;
                                        ScoreManager.incrementCurrentKoopa();
                                        ScoreManager.incrementCurrentScore("koopa");
                                    }
                                } else {
                                    // Goomba
                                    iterator.remove();
                                    threadDescente.force = -CONSTANTS.IMPULSION_MARIO / 2;
                                    ScoreManager.incrementCurrentGoomba();
                                    ScoreManager.incrementCurrentScore("goomba");
                                }
                                collisionHandled = true;
                            }

                            if (!collisionHandled) {
                                if (!mario.isInvincible()) {
                                    mario.perdreVie();
                                }
                            }
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
