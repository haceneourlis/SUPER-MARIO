package vue;

import java.awt.image.BufferedImage;
import modele.Ennemi;
import modele.Koopa;

public class AnimationKoopa extends Thread{
    // Instance de Animation (sorte d'héritage)
    private Animation animation;


    // Constante de délai entre chaque vérification du déplacement du joueur
    public final int DELAY = 60;


    // Booléen qui indique si le Thread doit continuer à tourner
    private boolean ok = true;

    // Variable qui va stocker la position en x du joueur à chaque itération.
    private int old_x;

    // Variable qui va stocker l'index maximum de l'animation (le nombre d'images dans l'animation)
    private int maxIndex;

    // Le joueur (personnage) à afficher, ici Koopa
    private Ennemi koopa;

    // Constructeur qui initialise l'instance de Koopa ainsi que le old_x.
    public AnimationKoopa(Ennemi koopa){
        this.koopa = koopa;
        this.old_x = this.koopa.getPosition().x;
        this.maxIndex = 2;
        // On initialise l'animation avec l'index maximum et le joueur principal.
        this.animation = new Animation(this.maxIndex, this.koopa);
    }

    // getter de l'image to draw
    public BufferedImage getCurrentToDraw() {
        if(koopa instanceof Koopa) {
            Koopa kp = (Koopa) koopa;
            if(kp.getState()==Koopa.State.SHELL) {
                return kp.getImage(0);
            }
        }
        return this.animation.getCurrentToDraw();
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
            * et ce en précisant que le joueur se déplace à droite, avec le booléen mis en paramètre d'incrementAnimationIndex (true).
            * Si cette différence est inférieure à 0, cela veut dire que le joueur se déplace à gauche, on incrémente ainsi l'index de l'animation, avec un paramètre false (pour dire qu'il va à gauche).
            * Si la différence est égale à 0, cela veut dire que le joueur est immobile, on remet ainsi l'index de l'animation à 0. (reset_to_idl)
            * */
            old_x = this.koopa.getPosition().x - old_x;
            if (old_x > 0){
                this.animation.incrementAnimationIndex(true);
            } else if (old_x < 0) {
                this.animation.incrementAnimationIndex(false);
            } else {
                this.animation.setCurrentToDraw(koopa.getImage(0));
            }
            // Avant de relancer une itération, on sauvegarde la valeur de la position en x du joueur pour la prochaine itération.
            old_x = this.koopa.getPosition().x;
        }
    }



}
