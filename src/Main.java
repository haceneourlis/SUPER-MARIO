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

        Joueur j = new Joueur();
        Jumping jumpin = new Jumping(j);
        jumpin.start();
        MouvementJoueur mv = new MouvementJoueur();
        DeplacementListener dl = new DeplacementListener(mv, j, jumpin);
        dl.start();
        // on ajoute un affichage
        fenetre.add(new Affichage(j));
        fenetre.addKeyListener(mv);
        // et on l'affiche
        fenetre.pack();
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        fenetre.setVisible(true);

        // fermer quand on appuie sur la croix
    }

}