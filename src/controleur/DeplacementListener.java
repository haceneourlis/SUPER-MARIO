// Imports...

package controleur;
import modele.*;


/**
 * Cette classe est un thread qui va faire bouger le joueur en fonction des touches pressées
 * Il utilise 3 booléens :
 * - left_pressed : true si la touche gauche est pressée
 * - right_pressed : true si la touche droite est pressée
 * - space_pressed : true si la touche espace est pressée
 * Ainsi, si plusieurs touches sont appuyées en même temps, cela ne pose pas de problème
 */

public class DeplacementListener extends Thread{

    // Instance de MouvementJoueur pour récupérer les booléens et savoir quelle touche a été pressée
    private MouvementJoueur mj;

    // Instance de Joueur pour pouvoir le déplacer
    private Joueur j;

    // Instance de Jumping pour pouvoir sauter
    private Jumping jumpin;

    // Constante de délai entre chaque vérification des booléens de mj
    public final int DELAY = 30;

    // Constructeur qui initialise les instances de MouvementJoueur et Joueur et Jumping
    public DeplacementListener(MouvementJoueur mj, Joueur j, Jumping jumpin){
        this.mj = mj;
        this.j = j;
        this.jumpin = jumpin;
    }

    // Méthode run qui va vérifier les booléens de mj et déplacer le joueur en conséquence
    @Override
    public void run(){
        while(true){
            try{Thread.sleep(DELAY);}
            catch (Exception e) { e.printStackTrace(); }
            if (mj.isLeft_pressed()){
                // Touche gauche préssée, on déplace le joueur à gauche
                j.deplacer_gauche();
            }
            if (mj.isRight_pressed()){
                // Touche droite préssée, on déplace le joueur à droite
                j.deplacer_droite();
            }
            if (mj.isSpace_pressed()){
                // Touche espace préssée, on fait sauter le joueur
                jumpin.jump();
            }
            if (!mj.isRight_pressed() && !mj.isLeft_pressed()){
                // Si aucune touche n'est pressée, on décrémente la vitesse du joueur (jusqu'à qu'elle atteigne 0)
                j.decelerer();
            }
        }
    }

}
