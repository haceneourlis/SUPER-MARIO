
import controleur.*;
import javax.swing.JFrame;
import modele.*;
import modele.Tile.TileManager;
import vue.*;

// classe qui va juste lancer la fenetre de jeu depuis l'affichage
// Et initialiser les dfferents threads
public class Main {
    // on lance ça dans la méthode main
    public static void main(String[] args) {

        // on crée une fenetre
        JFrame fenetre = new JFrame();


        // Get the player instance : classe singleton .
        Mario mario = Mario.getInstance();
        


        // Get the tile manager instance : classe singleton .
        TileManager tilemanager = TileManager.getInstance();
   
        // on ajoute un panel à la fenetre
        Affichage GamePanel = new Affichage();

        // on ajoute le gamepanel à la fenetre
        fenetre.add(GamePanel);

        // on ajoute un thread pour la gravité
        Descente descente_thread = new Descente(mario);
        descente_thread.start();

        // on crée un thread pour le saut
        Jumping jumping_thread = new Jumping(descente_thread);

        // lancer le thread de tous les ennemis
        for (Ennemi ennemi : tilemanager.getListeEnnemis()) {
            ennemi.thread.start();
        }

        // on crée un thread pour le mouvement : qui detecte les touches (<- et -> et
        // ESPACE)
        MouvementJoueur mouvement_key_listener = new MouvementJoueur();
        DeplacementListener deplacement_listener = new DeplacementListener(mouvement_key_listener, mario, jumping_thread);
        deplacement_listener.start();

        // on ajoute un thread pour la collision
        Collision collision_thread = new Collision(jumping_thread, descente_thread);
        collision_thread.start();
        fenetre.addKeyListener(mouvement_key_listener);
        fenetre.pack();
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fenetre.setVisible(true);
    }

}