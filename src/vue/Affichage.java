package vue;

import modele.*;
import modele.Tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class Affichage extends JPanel {
    private Mario JoueurPrincipal;
    private Ennemi ennemi;
    private int x_de_Mario_de_vue;
    private int y_de_Mario_de_vue;
    private final int RATIO_X = 2;
    private final int RATIO_Y = 3;

    public TileManager tm;

    public Affichage() {
        setPreferredSize(new Dimension(CONSTANTS.ScreenWidth, CONSTANTS.screenHeight)); // Set window size
        this.JoueurPrincipal = Mario.getInstance(); // Get the player instance : classe singleton .

        tm = new TileManager(this);

        // Initialiser l'ennemi (au-dessus du sol)
        ennemi = new Ennemi(0, 20, 30, 5, 0, 500, true);
        ennemi.thread.start(); // Lancer le thread de l'ennemi

        // Mettre à jour l'affichage toutes les 50ms
        Timer timer = new Timer(50, e -> repaint());
        timer.start();
    }

    public void transformFromModelToView() {
        this.y_de_Mario_de_vue = (JoueurPrincipal.HMAX + JoueurPrincipal.getPosition().y) * RATIO_Y;
        this.x_de_Mario_de_vue = -JoueurPrincipal.BEFORE * RATIO_X + JoueurPrincipal.getPosition().x * RATIO_X;
    }

    public Ennemi getEnnemi() {
        return ennemi;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        this.tm.draw(g2); // ligne ajoutée pour dessiner les tiles
        this.ennemi.draw(g2);

        transformFromModelToView();
        this.JoueurPrincipal.draw(g2);
        // g.drawRect(this.x_de_Mario_de_vue, this.y_de_Mario_de_vue, 60, 60); -- ligne
        // à supprimer car
        // remplacer avec la derniere ligne

        // plus besoin des 11 lignes suivantes :
        // // Draw the ground using the image
        // int groundY = ground.getYPosition();
        // int tileSize = ground.getTileSize();
        // for (int i = 0; i < getWidth(); i += tileSize) {
        // if (groundImage != null) {
        // g.drawImage(groundImage, i, groundY, tileSize, tileSize, null);
        // } else {
        // g.setColor(new Color(139, 69, 19)); // Fallback color if image fails
        // g.fillRect(i, groundY, tileSize, tileSize);
        // }
        // }

    }
}
