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
    private Mario mario;
    private List<Ennemi> listeEnnemis;

    // Variable pour l'animation du joueur (Mario)
    private AnimationJoueur animationJoueur;

    // Variable pour l'animation du (des) koopa
    private List<AnimationKoopa> animationKoopa;
    private List<AnimationGoomba> animationGoomba;

    // Variable pour le gestionnaire de tuiles
    public TileManager tilemanager;

    // score manager
    private ScoreManager scoreManager;

    private Font marioFont;

    private int decalage = 0;

    private BufferedImage coeurImage;

    // Constantes pour l'affichage du score
    private final int SCORE_X = 10;
    private final int SCORE_Y = 30;

    // Constante pour l'affichage du nombre de coins
    private final int COINS_X = 200;
    private final int COINS_Y = 30;

    int largeurTotale = 0; // Largeur totale des cœurs à dessiner
    int startX = 0; // Position de départ pour centrer les cœurs
    int vies = 0; // Nombre de vies restantes
    // attribut static pour savoir si le jeu est terminé ou pas
    public static boolean GAME_OVER = false;

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
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        // Initialiser le joueur (classe singleton)
        this.mario = Mario.getInstance(); // Get the player instance : classe singleton .

        // Initialiser le gestionnaire de tuiles
        this.tilemanager = TileManager.getInstance(); // Get the tile manager instance : classe singleton .;

        this.scoreManager = ScoreManager.getInstance(); // Get the score manager instance : classe singleton .

        this.listeEnnemis = new ArrayList<>(); // Liste des ennemis
        this.listeEnnemis = tilemanager.getListeEnnemis(); // Récupérer la liste des ennemis depuis le TileManager

        // Initialiser l'ennemi (au-dessus du sol)
        // ennemi = new Ennemi(630, 20, 20, 5, true, tilemanager);
        // ennemi.thread.start(); // Lancer le thread de l'ennemi

        animationKoopa = new ArrayList<>();
        animationGoomba = new ArrayList<>();

        // Mettre à jour l'affichage toutes les 50ms
        (new Redessine(this)).start();

        // Lancer l'animation du joueur (Mario).
        animationJoueur = new AnimationJoueur(mario);
        animationJoueur.start();

        // télecharger l'image du coeur
        try {
            coeurImage = ImageIO.read(getClass().getResourceAsStream("/resources/coeur.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < listeEnnemis.size(); i++) {
            if (listeEnnemis.get(i) instanceof Koopa) {
                animationKoopa.add(new AnimationKoopa(listeEnnemis.get(i)));
            }
            if (listeEnnemis.get(i) instanceof Goomba) {
                animationGoomba.add(new AnimationGoomba(listeEnnemis.get(i)));
            }
        }

        for (AnimationKoopa Koopa : animationKoopa) {
            Koopa.start();
        }
        for (AnimationGoomba Goomba : animationGoomba) {
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

        if (Mario.VIVANT) {
            // je récupère la case de mario actuelle, relative au décalage
            int case_actuelle = ((this.mario.getPositionX() - decalage) / CONSTANTS.TAILLE_CELLULE);
            // Si la case de mario dépasse la case de scrolling, on décale la fenêtre
            if (case_actuelle >= CONSTANTS.CELLULE_SCROLLING) {
                // Le décalage correspond à la distance entre mario et la case de scrolling
                this.decalage = mario.getPositionX() - CONSTANTS.CELLULE_SCROLLING * CONSTANTS.TAILLE_CELLULE;
            }

            // On applique le décalage du plan de jeu
            // (note que comme l'objet Graphics2D est rechargé à chaque appel, les
            // transformations ne s'aditionnent pas)
            g2.translate(-this.decalage, 0);

            // affichons la matrice du jeu : (le terrain)
            this.drawTiles(g2);

            int goombaIndex = 0;
            for (Ennemi ennemi : listeEnnemis) {
                if (ennemi != null) {
                    BufferedImage imageEnnemi = null;
                    if (ennemi instanceof Koopa) {
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
            }

            for (int i = 0; i < this.tilemanager.sizeGameCharacterList(); i++) {

                GameCharacter gc = this.tilemanager.getListeGameCharacters(i);
                if (gc == null) {
                    continue; // Skip if the GameCharacter is null
                }
                g2.drawImage(gc.getImage(0), gc.getPosition().x, gc.getPosition().y, null);
            }
            this.drawCoin(g2);

            // Mario clignote uniquement s'il est invincible, sans affecter le reste du
            // dessin
            if (!mario.isInvincible() || (System.currentTimeMillis() / 200) % 2 == 0) {
                g2.drawImage(this.animationJoueur.getCurrentToDraw(), mario.getPositionX(), mario.getPositionY(), null);
            }

            // Dessiner les vies (cœurs) CENTRÉS en haut
            vies = Mario.getViesMario();

            // Calcul de la largeur totale des cœurs à dessiner
            largeurTotale = vies * CONSTANTS.HEART_WIDTH + (vies - 1) * CONSTANTS.SPACE_BETWEEN_HEARTS;

            // Calcul du point de départ X pour centrer
            startX = (getWidth() - largeurTotale) / 2;

            // 2. ANNULER le décalage AVANT de dessiner les cœurs
            g2.translate(this.decalage, 0); // Remet le contexte à 0 (sans décalage)
            // Dessiner les cœurs
            for (int i = 0; i < vies; i++) {
                int x = startX + i * (CONSTANTS.HEART_WIDTH + CONSTANTS.SPACE_BETWEEN_HEARTS);
                g.drawImage(coeurImage, x, 10, CONSTANTS.HEART_WIDTH, CONSTANTS.HEART_HEIGHT, null);
            }
            g2.setFont(marioFont);
            g2.setColor(Color.WHITE);
            g2.drawString("Score : " + ScoreManager.getScore(), SCORE_X, SCORE_Y);
            g2.drawString("Coins : " + ScoreManager.getCoins(), COINS_X, COINS_Y);
        } else {
            // mario meurt, on affiche le message de game over
            g2.setFont(marioFont);
            g2.setColor(Color.RED);
            g2.drawString("GAME OVER", (getWidth() - g2.getFontMetrics().stringWidth("GAME OVER")) / 2,
                    getHeight() / 2);

            this.decalage = 0; // Reset the offset to 0 when the game is over

        }

        // mario est mort, on affiche le message de game over
    }

    // draw un coin sautant
    public void drawCoin(Graphics2D g2) {
        if (Collision.coinToCatch != null) {
            g2.drawImage(Collision.coinToCatch.image,
                    Collision.coinToCatch.position.x * CONSTANTS.TAILLE_CELLULE,
                    Collision.coinToCatch.position.y * CONSTANTS.TAILLE_CELLULE, null);
        }
    }

    /*
     * parce que M.V.C
     */
    public void drawTiles(Graphics2D g2) {
        // récupère la case actuelle où se trouve mario
        int case_actuelle = (this.mario.getPositionX() / CONSTANTS.TAILLE_CELLULE);

        // On vérifie si sa case actuelle ne dépasse pas une certaine limite de
        // scrolling
        // On rajoute le décalage à cette limite car elle est relative à la position de
        // mario (elle se déplace aussi)
        if (case_actuelle > (CONSTANTS.CELLULE_SCROLLING + this.decalage)) {
            // si c'est le cas, on incrémente le décalage
            tilemanager.decalage = tilemanager.decalage + 1;
        }

        // la col ici représente la colonne de la matrice du jeu à partir de laquelle on
        // va afficher les tuiles
        // elle commence du nombre de décalage qu'on a, car 1 décalage = 1 colonne qui
        // sort de l'écran
        // donc 1 colonne qu'on ne veut plus afficher
        int col = tilemanager.decalage;

        // la ligne ici représente la ligne de la matrice du jeu à partir de laquelle on
        // va afficher les tuiles
        int row = 0;

        // la position en x où on va commencer à dessiner la tuile sur la fenetre
        // Comme le plan ne se déplace pas, on doit incrémenter à chaque fois la
        // position en x de début de dessin, en fonction du décalage de mario
        int x = tilemanager.decalage * CONSTANTS.TAILLE_CELLULE;
        int y = 0;
        Point point_dans_modele = new Point(x, y);

        // On vérifie que la col n'est pas soit en dehors du champs visible de la
        // fenetre (le maxCol_gameMatrix)
        // ou alors que le col est en dehors ou pas de la matrice du jeu chargée du
        // fichier texte.
        while (col < (CONSTANTS.maxCol_gameMatrix + decalage) && col < tilemanager.maxColLevel
                && row < CONSTANTS.maxRow_gameMatrix) {

            // On récupère le type de la tuile à afficher qui correspond à un indice dans le
            // tableau de tuiles
            int TileType = tilemanager.tilesMatrice[row][col];

            // Ici, le point dans le modele est le meme que dans la vue donc ça ne change
            // rien
            point_dans_modele.x = x;
            point_dans_modele.y = y;

            // On dessine la tuile
            g2.drawImage(tilemanager.tiles[TileType].image, point_dans_modele.x, point_dans_modele.y, null);

            // on incrémente la colonne et la position en x
            col++;
            x += CONSTANTS.TAILLE_CELLULE;

            // si on arrive à la fin de la ligne qu'on peut afficher ou si on arrive à la
            // fin de la matrice du jeu
            // on incrémente la ligne et on réinitialise la colonne et la position en x

            if (col == (CONSTANTS.maxCol_gameMatrix + decalage) || col >= tilemanager.maxColLevel) {
                col = tilemanager.decalage;
                row++;
                y += CONSTANTS.TAILLE_CELLULE;
                x = tilemanager.decalage * CONSTANTS.TAILLE_CELLULE;
            }

        }

    }
}