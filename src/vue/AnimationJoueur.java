package vue;

public class AnimationJoueur extends Thread{
    private Affichage affichage;
    public final int DELAY = 60;
    private boolean ok = true;
    int old_dx;


    public AnimationJoueur(Affichage a){
        this.affichage = a;
    }

    public boolean detecterDeplacement(){
        double dx = this.affichage.get_x_joueur();
        dx = dx - this.affichage.get_x_joueur();
        // print de debog
        System.out.println("dx = " + this.affichage.get_x_joueur());
        if (dx != 0){
            return true;
        }
        return false;
    }

    public void stopThread(){
        this.ok = false;
    }

    @Override
    public void run(){
        this.ok = true;
        while (this.ok){
            try{Thread.sleep(DELAY);}
            catch (Exception e) { e.printStackTrace(); }
            old_dx = this.affichage.get_x_joueur() - old_dx;
            if (old_dx > 0){
                this.affichage.incrementWalkIndex(true);
            } else if (old_dx < 0) {
                this.affichage.incrementWalkIndex(false);
            } else {
                this.affichage.reset_to_idl();
            }
            old_dx = this.affichage.get_x_joueur();
        }
    }
}
