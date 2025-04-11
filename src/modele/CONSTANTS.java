package modele;

public class CONSTANTS {

    // pour affichage de mario et autres ... DU modele à la vue :
    public static final int BEFORE = -30;
    public static final int AFTER = 240;
    public static final int HMAX = 60;
    public static final int HMIN = -30;
    public static final int RATIO_X = 1;
    public static final int RATIO_Y = 1;
    public static final int DECELERATION = 1;

    // pour la taille des cellules du jeu (les carreaux ...):
    public static final int TAILLE_CELLULE = 32;

    // pour les collisions :
    public static final int slidAreaDefaultX = 8;
    public static final int slidAreaDefaultY = 16;

    public static final int GRAVITY = 1;

    // pour la taille de la fenetre & matrice de jeu :
    public static final int maxScreenRow = 16;
    public static final int maxScreenCol = 25;

    // pour la taille de la matrice de jeu :
    public static final int maxRow_gameMatrix = 16;
    public static final int maxCol_gameMatrix = 32;

    // Pour gérer le scrolling de la caméra:
    // ça represente la cellule à partir de laquelle on commence à scroller
    public static final int CELLULE_SCROLLING = 10;

    // pour l'affichage de la fenetre :
    public static int LARGEUR_MODELE = maxScreenCol * TAILLE_CELLULE;
    public static int HAUTEUR_MODELE = maxScreenRow * TAILLE_CELLULE;
    public static final int LARGEUR_VUE = (LARGEUR_MODELE);
    public static final int HAUTEUR_VUE = (HAUTEUR_MODELE);

    public static final int LE_SOL = TAILLE_CELLULE * 11;

    public static final int INTERVALE_COLLISION = 10;

    public static final int FORCE_MAX_MARIO = 20;
    public static final int FORCE_MAX_COIN = 3;

    // Les facteurs d'incrémentation : 
    public static final int INCREMENT_SCORE_COIN = 10;
    public static final int INCREMENT_SCORE_MUSHROOM = 50;


    // les types de tuiles :
    
    public static final int CIEL = 0;
    public static final int SOL = 1;
    public static final int PRIZE_BRICK = 2;

    // facteur d'incrementation du score:
    public static final int FACTEUR_SCORE = 100;

    public static final int FORCE_MAX = 20;

    // les implusions de mario et coins :
    public static final int IMPULSION_MARIO = 15;
    public static final int IMPULSION_COIN = 2;
}
