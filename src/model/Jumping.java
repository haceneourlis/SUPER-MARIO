package model;

import view.Mario;

public class Jumping extends Thread {
    private Mario j;
    private boolean is_jumping;
    private int impulsion;
    public final int GRAVITY = 3;
    public final int DELAY = 40;


    public Jumping(Mario j) {
        this.j = j;
        this.impulsion = 0;
        this.is_jumping = false;
    }


    public void jump() {
        if (!this.is_jumping) {
            this.is_jumping = true;
            this.impulsion = 10;
        }
    }

    // methode de lancement du thread
    @Override
    public void run() {
        while (true) {
            try {Thread.sleep(DELAY);}
            catch (Exception e) {e.printStackTrace();}
            if (this.is_jumping) {
                if (this.j.getPosition().y - this.impulsion >= this.j.Y_ORIGINE) {
                    this.is_jumping = false;
                    this.impulsion = 0;
                    this.j.setPositionY(this.j.Y_ORIGINE);
                } else {
                    this.j.setPositionY(this.j.getPosition().y - this.impulsion);
                    this.impulsion -= this.GRAVITY;
                }
            }

        }


    }
}
