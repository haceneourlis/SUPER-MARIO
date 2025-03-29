package vue;

import java.awt.*;
import javax.swing.*;
import modele.*;
import modele.Tile.TileManager;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;



/**
 * Classe qui affiche le jeu
 * Elle utilise la classe Redessine pour actualiser l'affichage
 * Elle utilisera également divers classes d'animations.
 */

public class Affichage extends JPanel {

    // Variables pour les instances de Mario et de l'ennemi
    private Mario JoueurPrincipal;
    private List<Ennemi> listeEnnemis;

    // Variable pour l'animation du joueur (Mario)
    private AnimationJoueur animationJoueur;

    // Variable pour l'animation du (des) koopa 
    private List<AnimationKoopa> animationKoopa;
    private List<AnimationGoomba> animationGoomba;

    // Variable pour le gestionnaire de tuiles
    public TileManager tm;

    private int decalage = 0;

    private BufferedImage coeurImage;

    /**
     * Constructeur de la classe Affichage.
     * On initialise la taille de la fenêtre et on crée les instances de Mario et de l'ennemi.
     * On lance également les threads de l'ennemi, de l'animation du joueur et de l'actualisation de la fenetre (redessine).
     */
    public Affichage() {
        // Initialiser la fenêtre avec les dimensions prévues.
        setPreferredSize(new Dimension(CONSTANTS.LARGEUR_VUE, CONSTANTS.HAUTEUR_VUE)); // Set window size

        // Initialiser le joueur (classe singleton)
        this.JoueurPrincipal = Mario.getInstance(); // Get the player instance : classe singleton .
        
        // Initialiser le gestionnaire de tuiles
        tm = new TileManager();

        // Initialiser l'ennemi (au-dessus du sol)
//        ennemi = new Ennemi(630, 20, 20, 5, true, tm);
//        ennemi.thread.start(); // Lancer le thread de l'ennemi


        listeEnnemis = new ArrayList<>();
        animationKoopa = new ArrayList<>();
        animationGoomba = new ArrayList<>();

        // Ajouter plusieurs ennemis
        Ennemi koopa = new Ennemi(600, 20, 20, 5, true, tm, "koopa");
        // Ennemi goomba = new Ennemi(500, 20, 20, 5, false, tm);
        listeEnnemis.add(koopa);
        // listeEnnemis.add(goomba);

        // Mettre à jour l'affichage toutes les 50ms
        (new Redessine(this)).start();

        // Lancer l'animation du joueur (Mario).
        animationJoueur = new AnimationJoueur(JoueurPrincipal);
        animationJoueur.start();

        //télecharger l'image du coeur
        try {
            coeurImage = ImageIO.read(getClass().getResourceAsStream("/resources/coeur.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Démarrer les threads des ennemis et de leurs animations
        animationKoopa.add(new AnimationKoopa(koopa));
        // animationGoomba.add(new AnimationGoomba(goomba));

        for (Ennemi ennemi : listeEnnemis) {
            ennemi.thread.start();
        }
        for (AnimationKoopa Koopa : animationKoopa) {
            Koopa.start();
        }
//        for (AnimationGoomba Goomba : animationGoomba) {
//            Goomba.start();
//        }
    }
    
    // getter de tileManager
    public TileManager getTileManager() {
        return this.tm;
    }

    /**
     * Getter de l'objet Ennemi.
     * @return l'ennemi du jeu.
     */
//    public Ennemi getEnnemi() {
//        return ennemi;
//    }
    public List<Ennemi> getEnnemis() {
        return listeEnnemis;
    }

    public void removeEnnemi(Ennemi ennemi) {
        this.listeEnnemis.remove(ennemi);
    }
    /**
     * Méthode qui dessiner les différents éléments sur la fenetre.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // On crée un objet Graphics2D pour dessiner les éléments
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
     

        // je récupère la case de mario actuelle, relative au décalage
        int case_actuelle = ((this.JoueurPrincipal.getPositionX() - decalage)/ CONSTANTS.TAILLE_CELLULE);
        
        // Si la case de mario dépasse la case de scrolling, on décale la fenêtre
        if (case_actuelle >= CONSTANTS.CELLULE_SCROLLING){
            // Le décalage correspond à la distance entre mario et la case de scrolling
            this.decalage = JoueurPrincipal.getPositionX() - CONSTANTS.CELLULE_SCROLLING*CONSTANTS.TAILLE_CELLULE;
        }
        
        // On applique le décalage du plan de jeu 
        // (note que comme l'objet Graphics2D est rechargé à chaque appel, les transformations ne s'aditionnent pas)
        g2.translate(-this.decalage, 0);

        // affichons la matrice du jeu : (le terrain)
        this.tm.draw(g2);

        // Dessiner tous les ennemis avec leur animation respective
        for (int i = 0; i < listeEnnemis.size(); i++) {
            Ennemi ennemi = listeEnnemis.get(i);
            BufferedImage imageEnnemi = null;

            // Sélectionner l'animation correcte en fonction du type d'ennemi
            if (ennemi.getType().equals("koopa") && i < animationKoopa.size()) {
                imageEnnemi = animationKoopa.get(i).getCurrentToDraw();
            }
            // TODO: GOOMBA


            // Dessiner l'ennemi
            if (imageEnnemi != null) {
                g2.drawImage(imageEnnemi, ennemi.getPosition().x, ennemi.getPosition().y, null);
            }
        }

        // Si Mario est invincible, il clignote à l'écran : on saute une frame sur deux
        if (JoueurPrincipal.isInvincible()) {
            if ((System.currentTimeMillis() / 100) % 2 == 0) return; // skip draw every other frame
        }

        // affichons mario en dernier (pour qu'il soit au-dessus de tout) :
        g2.drawImage(this.animationJoueur.getCurrentToDraw(), JoueurPrincipal.getPositionX() ,JoueurPrincipal.getPositionY(), null);

        // Dessiner les vies (cœurs) CENTRÉS en haut
        int vies = JoueurPrincipal.getVies();
        int coeurWidth = 30;
        int coeurHeight = 30;
        int espaceEntreCoeurs = 10;

        // Calcul de la largeur totale des cœurs à dessiner
        int largeurTotale = vies * coeurWidth + (vies - 1) * espaceEntreCoeurs;

        // Calcul du point de départ X pour centrer
        int startX = (getWidth() - largeurTotale) / 2;

        // Dessiner les cœurs
        for (int i = 0; i < vies; i++) {
            int x = startX + i * (coeurWidth + espaceEntreCoeurs);
            g.drawImage(coeurImage, x, 10, coeurWidth, coeurHeight, null);

        }

        // Optionnel : Afficher "Game Over" au centre si plus de vies
        if (vies <= 0) {
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", getWidth() / 2 - 150, getHeight() / 2);
        }
        
        g2.dispose();
    }

    
}
