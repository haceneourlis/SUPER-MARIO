
// classe qui va juste lancer la fenetre de jeu depuis l'affichage
import controleur.*;
import javax.swing.JFrame;
import modele.*;
import modele.Tile.TileManager;
import vue.*;

public class Main {
    // on lance ça dans la méthode main
    public static void main(String[] args) {

        // on crée une fenetre
        JFrame fenetre = new JFrame();

        // Get the player instance : classe singleton .
        Mario j = Mario.getInstance();
        // Get the tile manager instance : classe singleton .;

        TileManager tilemanager = TileManager.getInstance(); // Get the tile manager instance : classe singleton .;

        // on ajoute un panel à la fenetre
        Affichage GamePanel = new Affichage();
        System.out.println("test4");

        fenetre.add(GamePanel);

        // on ajoute un thread pour la gravité
        Descente des = new Descente(j);
        des.start();

        // on crée un thread pour le saut
        Jumping jumpin = new Jumping(des);

        // on crée un thread pour le mouvement : qui detecte les touches (<- et -> et
        // ESPACE)
        MouvementJoueur mv = new MouvementJoueur();
        DeplacementListener dl = new DeplacementListener(mv, j, jumpin);
        dl.start();

        // on ajoute un thread pour la collision
        Collision col = new Collision(jumpin, des);
        col.start();

        // on ajoute un thread pour la mort
        Death death = Death.getInstance(des, col);
        death.start();

        fenetre.addKeyListener(mv);
        fenetre.pack();
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fenetre.setVisible(true);
    }

}