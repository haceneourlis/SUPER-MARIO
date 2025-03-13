package modele;

public class Jumping extends Thread {
    private Mario j;
    private boolean is_jumping;
    public int impulsion;
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
        }
    }

    // methode de lancement du thread
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.is_jumping) {
                if (this.j.getPosition().y - impulsion >= CONSTANTS.LE_SOL) {
                    this.is_jumping = false;
                    impulsion = 0;
                    this.j.setPositionY(CONSTANTS.LE_SOL);
                } else {
                    this.j.setPositionY(this.j.getPosition().y - impulsion);
                    impulsion -= this.GRAVITY;
                }
            }

        }

    }

}
