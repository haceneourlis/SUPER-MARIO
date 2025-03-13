package modele;


/**
 * Cette classe définit le thread qui gère le saut du joueur.
 * Elle contient une méthode pour faire sauter le joueur, elle utilise donc une instance de la classe Joueur pour modifier sa position en Y.
 * Nous avons également des constantes GRAVITY et DELAY.
 */
public class Jumping extends Thread {
    // Instance de Joueur pour pouvoir le déplacer en Y.
    private Joueur j;

    // Booléen qui indique si le joueur est en train de sauter.
    private boolean is_jumping;

    // Variable qui indique la force exercée sur le joueur, elle prendra la valeur de l'impulsion lors du saut.
    private int force;

    // Constante de la puissance de l'impulsion à chaque saut
    public final int IMPULSION = 10;

    // Constante de la gravité
    public final int GRAVITY = 1;

    // Constante du délai entre chaque vérification du saut
    public final int DELAY = 25;

    // Constructeur qui initialise l'instance de Joueur, la force et le booléen de saut.
    public Jumping(Joueur j) {
        this.j = j;
        this.force = 0;
        this.is_jumping = false;
    }

    /**
     * Méthode pour faire sauter le joueur.
     * Si le joueur n'est pas déjà en train de sauter, on met le booléen de saut à true et on initialise la force à 10 (valeur de la constante IMPULSION).
     */
    public void jump() {
        // Si le joueur n'est pas déjà en train de sauter
        if (!this.is_jumping) {
            this.is_jumping = true;
            this.force = IMPULSION;
        }
    }

    // Méthode run qui va faire sauter le joueur
    @Override
    public void run() {
        while (true) {
            try {Thread.sleep(DELAY);}
            catch (Exception e) {e.printStackTrace();}
            // Si le joueur est en train de sauter
            if (this.is_jumping) {
                /* On ne s'autorise pas à aller dans des valeurs négatives en Y, je considère que le Y = 0 est le sol.
                * Je vérifie donc que le fait d'ajouter la force ne fasse pas sortir le joueur de la fenêtre.
                * Si c'est le cas, on arrête le saut, on remet la force à 0 et on remet le joueur au sol.
                * (Cette force peut être négative si le joueur est en train de tomber).
                * */
                if (this.j.getPositionY() + this.force <= this.j.Y_ORIGINE) {
                    this.is_jumping = false;
                    this.force = 0;
                    this.j.setPositionY(this.j.Y_ORIGINE);
                }
                // Sinon, on ajoute la force à la position en Y du joueur et on décrémente la force de la gravité.
                else {
                    this.j.setPositionY(this.j.getPositionY() + this.force);
                    this.force -= this.GRAVITY;
                }

            }

        }


    }
}
