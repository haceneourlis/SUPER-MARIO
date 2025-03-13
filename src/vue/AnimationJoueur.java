package vue;


/**
 * Cette classe définit l'animation du joueur (Mario), elle hérite de la classe Thread.
 * Elle vérifie si le joueur se déplace à gauche ou à droite (grâce à un Thread) et incrémente l'index de l'animation en conséquence.
 * L'animation est gérée par la classe Affichage, et ceux avec un tableau contenant les images de l'animation.
 * L'animation est donc gérée avec un défilement rapide de ces images contenues dans le tableau.
 * Cette classe va se concentrer à incrémenter l'index de l'animation en fonction du déplacement du joueur et indiquer s'il se déplace vers la gauche ou la droite.
 * Elle contient une méthode pour lancer l'animation du joueur.
 */
public class AnimationJoueur extends Thread{
    // Instance de Affichage pour récupérer les coordonnées du joueur.
    private Affichage affichage;

    // Constante de délai entre chaque vérification du déplacement du joueur
    public final int DELAY = 60;

    // Booléen qui indique si le Thread doit continuer à tourner
    private boolean ok = true;

    // Variable qui va stocker la position en x du joueur à chaque itération.
    private int old_x;

    // Constructeur qui initialise l'instance de Affichage ainsi que le old_x.
    public AnimationJoueur(Affichage a){
        this.affichage = a;
        this.old_x = this.affichage.get_x_joueur();
    }

    // Méthode pour arrêter le Thread.
    public void stopThread(){
        this.ok = false;
    }

    // Méthode run qui va vérifier si le joueur se déplace à gauche ou à droite et incrémente l'index de l'animation en conséquence.
    @Override
    public void run(){
        this.ok = true;
        while (this.ok){
            try{Thread.sleep(DELAY);}
            catch (Exception e) { e.printStackTrace(); }
            /* On va récupérer à chaque fois la position en x du joueur et la comparer à la position en x du joueur à l'itération précédente.
            * Si cette différence est supérieure à 0, cela veut dire que le joueur se déplace à droite, on incrémente donc l'index de l'animation,
            * et ce en précisant que le joueur se déplace à droite, avec le booléen mis en paramètre d'incrementWalkIndex (true).
            * Si cette différence est inférieure à 0, cela veut dire que le joueur se déplace à gauche, on incrémente ainsi l'index de l'animation, avec un paramètre false (pour dire qu'il va à gauche).
            * Si la différence est égale à 0, cela veut dire que le joueur est immobile, on remet ainsi l'index de l'animation à 0. (reset_to_idl)
            * */
            old_x = this.affichage.get_x_joueur() - old_x;
            if (old_x > 0){
                this.affichage.incrementWalkIndex(true);
            } else if (old_x < 0) {
                this.affichage.incrementWalkIndex(false);
            } else {
                this.affichage.reset_to_idl();
            }
            // Avant de relancer une itération, on sauvegarde la valeur de la position en x du joueur pour la prochaine itération.
            old_x = this.affichage.get_x_joueur();
        }
    }
}
