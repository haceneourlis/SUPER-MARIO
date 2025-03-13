package vue;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import modele.*;
import modele.Tile.TileManager;


public class Affichage extends JPanel {
    private Mario JoueurPrincipal;
    private Ennemi ennemi;
    private BufferedImage joueurImageIdl;
    private BufferedImage[] walkSprites;
    private BufferedImage current_to_draw;
    private int walkIndex = 0;



    public TileManager tm;

    public Affichage() {
        setPreferredSize(new Dimension(CONSTANTS.LARGEUR_VUE, CONSTANTS.HAUTEUR_VUE)); // Set window size

        this.JoueurPrincipal = Mario.getInstance(); // Get the player instance : classe singleton .

        try {
            joueurImageIdl = ImageIO.read(new File("src/resources/mario_sprites/mario_idl.png"));
            // faire un tableau pour les sprites de walk
            current_to_draw = joueurImageIdl;
            walkSprites = new BufferedImage[3]; // Exemple : 3 images d’animations
            walkSprites[0] = ImageIO.read(new File("src/resources/mario_sprites/mario_walk1.png"));
            walkSprites[1] = ImageIO.read(new File("src/resources/mario_sprites/mario_walk2.png"));
            walkSprites[2] = ImageIO.read(new File("src/resources/mario_sprites/mario_walk3.png"));

        } catch (IOException e) {
            System.out.println("Erreur : Impossible de charger l'image du joueur.");
            e.printStackTrace();
        }

        tm = new TileManager(this);

        // Initialiser l'ennemi (au-dessus du sol)
        ennemi = new Ennemi(0, 20, 30, 5, 0, 500, true);
        ennemi.thread.start(); // Lancer le thread de l'ennemi

        // Mettre à jour l'affichage toutes les 50ms
        (new Redessine(this)).start();
        (new AnimationJoueur(this)).start();
    }

    // modification : j'ai supprimé la transformation , càd : point dans vue = point
    // dans modele ou presque
    public Point transformFromModelToView(Point point_dans_modele) {

        return point_dans_modele;
    }

    public Ennemi getEnnemi() {
        return ennemi;
    }

    public int get_x_joueur() {
        return JoueurPrincipal.getPosition().x;
    }

    public void incrementWalkIndex(boolean right) {
        this.walkIndex = (walkIndex + 1) % 3;
        if (!right) {
            setCurrentToDraw(flipImage(walkSprites[walkIndex]));
        } else {
            setCurrentToDraw(walkSprites[walkIndex]);
        }
    }

    // setter current to draw
    public void setCurrentToDraw(BufferedImage current_to_draw) {
        this.current_to_draw = current_to_draw;
    }

    // getter de image idl
    public void reset_to_idl() {
        setCurrentToDraw(joueurImageIdl);
        this.walkIndex = 0;
    }

    public static BufferedImage flipImage(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); // Miroir horizontal
        tx.translate(-image.getWidth(), 0); // Déplacer l'image pour qu'elle reste visible

        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
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
        g.drawImage(current_to_draw, transformFromModelToView(JoueurPrincipal.getPosition()).x,transformFromModelToView(JoueurPrincipal.getPosition()).y, null);

        // Dessiner un rectangle pour tester :
     
        g2.dispose();
    }

    
}
