
// classe qui va juste lancer la fenetre de jeu depuis l'affichage
import javax.swing.JFrame;
// import Affichage
import controleur.*;
import modele.*;
import vue.*;

public class Main {
    // on lance ça dans la méthode main
    public static void main(String[] args) {
        // on crée une fenetre
        JFrame fenetre = new JFrame();

        // Get the player instance : classe singleton .
        Mario j = Mario.getInstance();

        // on crée un thread pour le saut
        Jumping jumpin = new Jumping(j);
        jumpin.start();

        // on crée un thread pour le mouvement : qui detecte les touches (<- et -> et
        // ESPACE)
        MouvementJoueur mv = new MouvementJoueur();
        DeplacementListener dl = new DeplacementListener(mv, j, jumpin);
        dl.start();

        // on ajoute un panel à la fenetre
        Affichage GamePanel = new Affichage();
        fenetre.add(GamePanel);

        // on ajoute un thread pour la collision
        Collision col = new Collision(GamePanel, jumpin);
        col.start();
        fenetre.addKeyListener(mv);
        fenetre.pack();
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fenetre.setVisible(true);
    }

}