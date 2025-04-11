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
    private Mario player;

    // Instance de Jumping pour pouvoir sauter
    private Jumping jumpin;

    // Booléen qui permet d'indiquer dans quelle direction le personnage se déplace
    // Ce sera utile pour pouvoir continuer à faire avancer le personnage dans la dernière
    // direction où il se déplaçait, tant que la vitesse != 0 (en decelerant evidemment).
    public boolean last = false; // false = left, right = true;

    // Constructeur qui initialise les instances de MouvementJoueur et Joueur et Jumping
    public DeplacementListener(MouvementJoueur mj, Mario j, Jumping jumpin){
        this.mj = mj;
        this.player = j;
        this.jumpin = jumpin;
    }

    // Méthode run qui va vérifier les booléens de mj et déplacer le joueur en conséquence
    @Override
    public void run(){
        while(true){
            if (player.getVies() <= 0) {
                return; // Stoppe le thread proprement
            }
            try{Thread.sleep(CONSTANTS.DELAY_DEPLACEMENT_LISTENER);}
            catch (Exception e) { e.printStackTrace(); }
            if (mj.isLeft_pressed()){
                // Touche gauche préssée, on déplace le joueur à gauche en incrémentant sa vitesse.
                player.increment_speed();
                player.deplacer_gauche();
                last = false;
            }
            if (mj.isRight_pressed()){
                // Touche droite préssée, on déplace le joueur à droite en incrémentant sa vitesse.
                player.increment_speed();
                player.deplacer_droite();
                last = true;
            }
            if (mj.isSpace_pressed()){
                // Touche espace préssée, on fait sauter le joueur
                jumpin.jump();
            }
            if (!mj.isRight_pressed() && !mj.isLeft_pressed()){
                // Si aucune touche n'est pressée, on décrémente la vitesse du joueur (jusqu'à qu'elle atteigne 0)
                player.decelerer();
                // Et on vérifie la dernière direction où il se déplaçait et on continue à le déplacer dans cette direction.
                if (last) {
                    player.deplacer_droite();
                } else {
                    player.deplacer_gauche();
                }
            }
        }
    }

}