package vue;

import modele.CONSTANTS;

public class Redessine extends Thread {

    
    private boolean ok = true;
    private Affichage monAffichage;

    //constructeur de redessine
    public Redessine(Affichage a){
        this.monAffichage = a;
    }

    //getter de l'affichage
    public Affichage get_affichage(){
        return this.monAffichage;
    }

    //stopper le thread
    public void stop_thread(){
        this.ok = false;
    }

    //lancer le thread
    @Override
    public void run(){
        this.ok = true;
        while (this.ok){
            try{Thread.sleep(CONSTANTS.DELAY_REDESSINE);}
            catch (Exception e) { e.printStackTrace(); }
            this.monAffichage.revalidate();
            this.monAffichage.repaint();
        }
    }



}
