
// classe qui va juste lancer la fenetre de jeu depuis l'affichage
import controleur.*;
import javax.swing.JFrame;
import modele.*;
import vue.*;

public class Main {
    // on lance ça dans la méthode main
    public static void main(String[] args) {

        // On crée l'objet de loggingconfig qui va gérer le fichier de log
        LoggingConfig logs_manager = new LoggingConfig();
        // on crée une fenetre
        JFrame fenetre = new JFrame();

        // Get the player instance : classe singleton .
        Mario j = Mario.getInstance();

  

        Score score = new Score();
        Coin coin = new Coin(score);
        // on ajoute un panel à la fenetre
        Affichage GamePanel = new Affichage(score, coin);
        fenetre.add(GamePanel);

        // on ajoute un thread pour la gravité
        Descente des = new Descente(GamePanel);
        des.start();

         // on crée un thread pour le saut
        Jumping jumpin = new Jumping(des);

      
        // on crée un thread pour le mouvement : qui detecte les touches (<- et -> et
        // ESPACE)
        MouvementJoueur mv = new MouvementJoueur();
        DeplacementListener dl = new DeplacementListener(mv, j, jumpin);
        dl.start();
        

        // on ajoute un thread pour la collision
        Collision col = new Collision(GamePanel, jumpin, des, coin);
        col.start();
        fenetre.addKeyListener(mv);

        j.reset(mv); // lancer la partie

        fenetre.pack();
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fenetre.setVisible(true);
    }

}