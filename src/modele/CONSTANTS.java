package modele;

public class CONSTANTS {

    // Concernant mario et ses déplacement
    public static final int VITESSE_MAX_MARIO = 6;
    public static final int DECELERATION_MARIO = 1;
    public static final int VIE_MAX_MARIO = 3;
    public static final int POSITION_X_ORIGINE_MARIO = 50;

    // Pour les champignons :
    public static final int VITESSE_MUSHROOM = 3;


    // pour la taille des cellules du jeu (les carreaux ...):
    public static final int TAILLE_CELLULE = 32;

    // pour les collisions :
    public static final int slidAreaDefaultX = 8;
    public static final int slidAreaDefaultY = 16;
    
    // Pour la gravité :
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

    // La position du sol :
    public static final int LE_SOL = TAILLE_CELLULE * 11;

    // Intervale d'anticipation de la collision :
    public static final int INTERVALE_COLLISION = 10;

    // La force max des gamecharacters et des coins :
    public static final int FORCE_MAX = 20;
    public static final int FORCE_MAX_COIN = 3;

    // les implusions de mario et coins :
    public static final int IMPULSION_MARIO = 15;
    public static final int IMPULSION_COIN = 2;

    // Les facteurs d'incrémentation des scores: 
    public static final int INCREMENT_SCORE_COIN = 10;
    public static final int INCREMENT_SCORE_MUSHROOM = 50;

    // les types de tuiles :
    public static final int CIEL = 0;
    public static final int SOL = 1;
    public static final int PRIZE_BRICK = 2;
    public static final int COIN = 30;
    public static final int MUSHROOW_BRICK = 31;


    // Les différents DELAY pour les threads
    public static final int DELAY_COLLISION_ENTITE = 17;
    public static final int DELAY_COLLISION_MARIO = 5;
    public static final int DELAY_DEPLACEMENT_ENTITE = 17;
    public static final int DELAY_DESCENTE = 17;
    public static final int DELAY_DESCENTE_COINS = 55;
    public static final int DELAY_REDESSINE = 5;
    public static final int DELAY_DEPLACEMENT_LISTENER = 30;


    // Constantes pour l'affichage du score
    public static final int SCORE_X = 10;
    public static final int SCORE_Y = 30;
  
    // Constante pour l'affichage du nombre de coins
    public static final int COINS_X = 200;
    public static final int COINS_Y = 30;

}
