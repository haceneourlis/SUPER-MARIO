package vue;

import modele.*;
import modele.Tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class Affichage extends JPanel {
    private Mario JoueurPrincipal;
    private Ennemi ennemi;

    public TileManager tm;

    public Affichage() {
        setPreferredSize(new Dimension(CONSTANTS.LARGEUR_VUE, CONSTANTS.HAUTEUR_VUE)); // Set window size

        this.JoueurPrincipal = Mario.getInstance(); // Get the player instance : classe singleton .

        tm = new TileManager(this);

        // Initialiser l'ennemi (au-dessus du sol)
        ennemi = new Ennemi(0, 20, 30, 5, 0, 500, true);
        ennemi.thread.start(); // Lancer le thread de l'ennemi

        // Mettre à jour l'affichage toutes les 50ms
        Timer timer = new Timer(50, e -> repaint());
        timer.start();
    }

    // modification : j'ai supprimé la transformation , càd : point dans vue = point
    // dans modele ou presque
    public Point transformFromModelToView(Point point_dans_modele) {

        // Point point_dans_vue = new Point();

        // point_dans_vue.x = -CONSTANTS.BEFORE + point_dans_modele.x;
        // point_dans_vue.y = (CONSTANTS.HAUTEUR_MODELE - CONSTANTS.TAILLE_CELLULE -
        // point_dans_modele.y);

        // return point_dans_vue;
        return point_dans_modele;
    }

    public Ennemi getEnnemi() {
        return ennemi;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        // affichons la matrice du jeu :
        this.tm.draw(g2);
        // le seul ennemi du jeu :
        this.ennemi.draw(g2, transformFromModelToView(ennemi.getPosition()));
        // affichons mario en dernier ( pour qu'il soit au-dessus de touttt ) :
        this.JoueurPrincipal.draw(g2, transformFromModelToView(JoueurPrincipal.getPosition()));

        // Dessiner un rectangle pour tester :
        g2.setColor(Color.RED);
        g2.drawRect(60, 632, 30, 30);

        g2.dispose();
    }
}
