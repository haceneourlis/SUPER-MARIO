package vue;

import java.awt.*;
import java.io.File;
import java.io.IOException;
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
    public Affichage(Score score, Coin coin) {
        // Initialiser la fenêtre avec les dimensions prévues.
        setPreferredSize(new Dimension(CONSTANTS.LARGEUR_VUE, CONSTANTS.HAUTEUR_VUE)); // Set window size
        this.score = score;

        this.coin = coin;

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
        tm = new TileManager();

        listeEnnemis = new ArrayList<>();
        animationKoopa = new ArrayList<>();
        animationGoomba = new ArrayList<>();

        // Ajouter plusieurs ennemis
        Koopa koopa = new Koopa(600, 5, true, tm);
        Goomba goomba = new Goomba(300, 4, true, tm);
        listeEnnemis.add(koopa);
        listeEnnemis.add(goomba);

        // Mettre à jour l'affichage toutes les 50ms
        (new Redessine(this)).start();

        // Lancer l'animation du joueur (Mario).
        animationJoueur = new AnimationJoueur(JoueurPrincipal);
        animationJoueur.start();

        // télecharger l'image du coeur
        try {
            coeurImage = ImageIO.read(getClass().getResourceAsStream("/resources/coeur.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Démarrer les threads des ennemis et de leurs animations
        animationKoopa.add(new AnimationKoopa(koopa));
        animationGoomba.add(new AnimationGoomba(goomba));

        for (Ennemi ennemi : listeEnnemis) {
            ennemi.thread.start();
        }
        for (AnimationKoopa Koopa : animationKoopa) {
            Koopa.start();
        }
        for (AnimationGoomba Goomba : animationGoomba) {
            Goomba.start();
        }
    }

    // getter de tileManager
    public TileManager getTileManager() {
        return this.tm;
    }

    /**
     * Getter de l'objet Ennemi.
     * 
     * @return l'ennemi du jeu.
     */
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
        this.tm.draw(g2);

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

        // Mario clignote uniquement s'il est invincible, sans affecter le reste du
        // dessin
        if (!JoueurPrincipal.isInvincible() || (System.currentTimeMillis() / 200) % 2 == 0) {
            g2.drawImage(this.animationJoueur.getCurrentToDraw(), JoueurPrincipal.getPositionX(),
                    JoueurPrincipal.getPositionY(), null);
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
        g2.translate(this.decalage, 0); // Remet le contexte à 0 (sans décalage)

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

        g2.setFont(marioFont);
        g2.setColor(Color.WHITE);
        g2.drawString("Score : " + score.getCurrentScore(), SCORE_X, SCORE_Y);
        g2.drawString("Coins : " + coin.getNombreDePieces(), COINS_X, COINS_Y);
    }

}
