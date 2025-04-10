package vue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import modele.*;
import modele.Tile.TileManager;

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
    public TileManager tilemanager;

    private Font marioFont;

    // Un score et les coins
    private Score score;
    private Coin coin;

    private int decalage = 0;

    private BufferedImage coeurImage;

    // Constantes pour l'affichage du score
    private final int SCORE_X = 10;
    private final int SCORE_Y = 30;

    // Constante pour l'affichage du nombre de coins
    private final int COINS_X = 200;
    private final int COINS_Y = 30;

    /**
     * Constructeur de la classe Affichage.
     * On initialise la taille de la fenêtre et on crée les instances de Mario et de
     * l'ennemi.
     * On lance également les threads de l'ennemi, de l'animation du joueur et de
     * l'actualisation de la fenetre (redessine).
     * 
     * @throws IOException
     * @throws FontFormatException
     */
    public Affichage() {
        // Initialiser la fenêtre avec les dimensions prévues.
        setPreferredSize(new Dimension(CONSTANTS.LARGEUR_VUE, CONSTANTS.HAUTEUR_VUE)); // Set window size

        try {
            this.marioFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/PressStart2P-Regular.ttf"))
                    .deriveFont(16f);
            System.out.println("Police Mario chargée !");
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            System.out.println("Police Mario pas chargée !");

        }
        // Initialiser le joueur (classe singleton)
        this.JoueurPrincipal = Mario.getInstance(); // Get the player instance : classe singleton .

        // Initialiser le gestionnaire de tuiles
        this.tilemanager = TileManager.getInstance(); // Get the tile manager instance : classe singleton .;

        this.listeEnnemis = tilemanager.getListeEnnemis(); // Récupérer la liste des ennemis depuis le TileManager

        // Initialiser l'ennemi (au-dessus du sol)
        // ennemi = new Ennemi(630, 20, 20, 5, true, tilemanager);
        // ennemi.thread.start(); // Lancer le thread de l'ennemi

        animationKoopa = new ArrayList<>();
        animationGoomba = new ArrayList<>();

        // Mettre à jour l'affichage toutes les 50ms
        (new Redessine(this)).start();

        // Lancer l'animation du joueur (Mario).
        animationJoueur = new AnimationJoueur(JoueurPrincipal);
        animationJoueur.start();

        // télecharger l'image du coeur
        try {
            coeurImage = ImageIO.read(getClass().getResourceAsStream("/resources/coin.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < listeEnnemis.size(); i ++){
            if (listeEnnemis.get(i) instanceof Koopa){
                animationKoopa.add(new AnimationKoopa(listeEnnemis.get(i)));
            }
            if (listeEnnemis.get(i) instanceof Goomba){
                animationGoomba.add(new AnimationGoomba(listeEnnemis.get(i)));
            }
        }
        
        for (AnimationKoopa Koopa : animationKoopa) {
            Koopa.start();
        }
        for (AnimationGoomba Goomba : animationGoomba){
            Goomba.start();
        }
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
        int case_actuelle = ((this.JoueurPrincipal.getPositionX() - decalage) / CONSTANTS.TAILLE_CELLULE);

        // Si la case de mario dépasse la case de scrolling, on décale la fenêtre
        if (case_actuelle >= CONSTANTS.CELLULE_SCROLLING) {
            // Le décalage correspond à la distance entre mario et la case de scrolling
            this.decalage = JoueurPrincipal.getPositionX() - CONSTANTS.CELLULE_SCROLLING * CONSTANTS.TAILLE_CELLULE;
        }

        // On applique le décalage du plan de jeu
        // (note que comme l'objet Graphics2D est rechargé à chaque appel, les
        // transformations ne s'aditionnent pas)
        g2.translate(-this.decalage, 0);

        // affichons la matrice du jeu : (le terrain)
        this.tilemanager.draw(g2);


        int goombaIndex = 0;
        for (Ennemi ennemi : listeEnnemis) {
            BufferedImage imageEnnemi = null;
            if (ennemi instanceof Koopa) {
                // 对Koopa使用 AnimationKoopa 绘制（内部已判断状态，SHELL状态下直接返回静态图）
                if (!animationKoopa.isEmpty()) {
                    imageEnnemi = animationKoopa.get(0).getCurrentToDraw();
                }
            } else if (ennemi instanceof Goomba) {
                if (goombaIndex < animationGoomba.size()) {
                    imageEnnemi = animationGoomba.get(goombaIndex).getCurrentToDraw();
                    goombaIndex++;
                }
            }
            if (imageEnnemi != null) {
                g2.drawImage(imageEnnemi, ennemi.getPosition().x, ennemi.getPosition().y, null);
            }
        }

       


        for (int i = 0; i < this.tilemanager.sizeGameCharacterList(); i ++){
            GameCharacter gc = this.tilemanager.getListeGameCharacters(i);
            g2.drawImage(gc.getImage(0), gc.getPosition().x, gc.getPosition().y, null);
        }
        this.drawCoin(g2);
       

        // affichons mario en dernier (pour qu'il soit au-dessus de tout) :
        g2.drawImage(this.animationJoueur.getCurrentToDraw(), JoueurPrincipal.getPositionX(),
                JoueurPrincipal.getPositionY(), null);

        // Mario clignote uniquement s'il est invincible, sans affecter le reste du dessin
        if (!JoueurPrincipal.isInvincible() || (System.currentTimeMillis() / 200) % 2 == 0) {
            g2.drawImage(this.animationJoueur.getCurrentToDraw(), JoueurPrincipal.getPositionX(), JoueurPrincipal.getPositionY(), null);
        }

        // Dessiner les vies (cœurs) CENTRÉS en haut
        int vies = JoueurPrincipal.getVies();
        int coeurWidth = 30;
        int coeurHeight = 30;
        int espaceEntreCoeurs = 10;

        // Calcul de la largeur totale des cœurs à dessiner
        int largeurTotale = vies * coeurWidth + (vies - 1) * espaceEntreCoeurs;

        // Calcul du point de départ X pour centrer
        int startX = (getWidth() - largeurTotale) / 2;
        // 2. ANNULER le décalage AVANT de dessiner les cœurs
        g2.translate(this.decalage, 0);  // Remet le contexte à 0 (sans décalage)

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

    }

    // draw un coin sautant
    public void drawCoin(Graphics2D g2) {
        if (Collision.coinToCatch != null) {
            g2.drawImage(Collision.coinToCatch.image,
                    Collision.coinToCatch.position.x * CONSTANTS.TAILLE_CELLULE,
                    Collision.coinToCatch.position.y * CONSTANTS.TAILLE_CELLULE, null);
            // System.out.println("coinToCatch : " + Collision.coinToCatch.position.x *
            // CONSTANTS.TAILLE_CELLULE + " "
            // + Collision.coinToCatch.position.y * CONSTANTS.TAILLE_CELLULE);
        } else {
            // System.out.println("euuh coinToCatch null ? ");
        }
    }

   
}
