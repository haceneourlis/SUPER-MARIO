package vue;

import java.awt.*;
import java.io.File;
import java.io.IOException;
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
    private Ennemi ennemi;

    // Variable pour l'animation du joueur (Mario)
    private AnimationJoueur animationJoueur;

    // Variable pour l'animation du (des) koopa 
    private AnimationKoopa animationKoopa;

    // Variable pour le gestionnaire de tuiles
    public TileManager tm;

    private Font marioFont;

    // Un score et les coins
    private Score score;
    private Coin coin;

    private int decalage = 0;

    // Constantes pour l'affichage du score 
    private final int SCORE_X = 10;
    private final int SCORE_Y = 30;

    // Constante pour l'affichage du nombre de coins
    private final int COINS_X = 200;
    private final int COINS_Y = 30;

    /**
     * Constructeur de la classe Affichage.
     * On initialise la taille de la fenêtre et on crée les instances de Mario et de l'ennemi.
     * On lance également les threads de l'ennemi, de l'animation du joueur et de l'actualisation de la fenetre (redessine).
     * @throws IOException 
     * @throws FontFormatException 
     */
    public Affichage(Score score, Coin coin){
        // Initialiser la fenêtre avec les dimensions prévues.
        setPreferredSize(new Dimension(CONSTANTS.LARGEUR_VUE, CONSTANTS.HAUTEUR_VUE)); // Set window size
        this.score = score;

        this.coin = coin;

        try{
            this.marioFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/PressStart2P-Regular.ttf")).deriveFont(16f);
            System.out.println("Police Mario chargée !");
        }
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
            System.out.println("Police Mario pas chargée !");
            
        }
        // Initialiser le joueur (classe singleton)
        this.JoueurPrincipal = Mario.getInstance(); // Get the player instance : classe singleton .
        
        // Initialiser le gestionnaire de tuiles
        tm = new TileManager();

        // Initialiser l'ennemi (au-dessus du sol)
        ennemi = new Ennemi(300, 20, 20, 5, true, tm);
        ennemi.thread.start(); // Lancer le thread de l'ennemi

        // Mettre à jour l'affichage toutes les 50ms
        (new Redessine(this)).start();

        // Lancer l'animation du joueur (Mario).
        animationJoueur = new AnimationJoueur(JoueurPrincipal);
        animationJoueur.start();

        // lnacer l'animation du koopa
        animationKoopa = new AnimationKoopa(ennemi);
        animationKoopa.start();

        
    }
    
    // getter de tileManager
    public TileManager getTileManager() {
        return this.tm;
    }

    /**
     * Getter de l'objet Ennemi.
     * @return l'ennemi du jeu.
     */
    public Ennemi getEnnemi() {
        return ennemi;
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

        // le seul ennemi du jeu : (pour le moment)
        g2.drawImage(this.animationKoopa.getCurrentToDraw(), ennemi.getPosition().x, ennemi.getPosition().y, null);

        // affichons mario en dernier (pour qu'il soit au-dessus de tout) :
        g2.drawImage(this.animationJoueur.getCurrentToDraw(), JoueurPrincipal.getPositionX() ,JoueurPrincipal.getPositionY(), null);

                        
        g2.setFont(marioFont);
        g2.setColor(Color.WHITE);
        g2.drawString("Score : " + score.getCurrentScore(), SCORE_X + decalage, SCORE_Y);
        g2.drawString("Coins : " + coin.getNombreDePieces(), COINS_X + decalage, COINS_Y);
    }

    
}
