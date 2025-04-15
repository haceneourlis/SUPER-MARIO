package modele;

/**
 * Cette classe gère le score du joueur.
 * Elle est implémentée en singleton pour garantir qu'il n'y a qu'une seule instance de ScoreManager.
 * Elle permet de gérer le score et le nombre de pièces collectées.
 */
public class ScoreManager {
    // Instance unique de ScoreManager
    private static ScoreManager instance = null;

    // Score actuel du joueur
    private static int score = 0;

    // Nombre de pièces collectées
    private static int nb_coins = 0;

    // Nombre de champignon collectés
    private static int nb_mushrooms = 0;

    // Nombre de goomba tués
    private static int nb_goomba = 0;

    // Nombre de koopa tués
    private static int nb_koopa = 0;

    // Nombre de koopa transformées en tortues
    private static int shells = 0;

    private ScoreManager() {
        // Constructeur privé pour empêcher l'instanciation de la classe
        // car y a un seul score ...
    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    /**
     * Méthode pour réinitialiser le score et le nombre de pièces.
     */
    public static void resetScore() {
        score = 0;
        nb_coins = 0;
        nb_mushrooms = 0;
        nb_goomba = 0;
        nb_koopa = 0;
        shells = 0;
    }
    /**
     * Méthode pour obtenir le score actuel.
     * @return score actuel du joueur, un entier.
     */
    public static int getScore() {
        return score;
    }
    /**
     * Méthode pour obtenir le nombre de pièces collectées.
     * @return nombre de pièces collectées, un entier.
     */
    public static int getCoins() {
        return nb_coins;
    }
    /**
     * Méthode pour incrémenter le nombre de coins.
     */
    public static void incrementCurrentCoins() {
        nb_coins++;
    }

    /**
     * Méthode pour obtenir le nombre de champignons collectés.
     * @return
     */
    public static int getMushrooms() {
        return nb_mushrooms;
    }



    /**
     * Méthode pour incrémenter le nombre de champignons.
     */
    public static void incrementCurrentMushrooms() {
        nb_mushrooms++;
    }

    /**
     * Méthode pour obtenir le nombre de goomba tués.
     * @return nombre de goomba tués, un entier.
     */
    public static int getGoomba() {
        return nb_goomba;
    }

    /**
     * Méthode pour obtenir le nombre de koopa tués.
     * @return nombre de koopa tués, un entier.
     */
    public static int getKoopa() {
        return nb_koopa;
    }

    /**
     * Méthode pour incrémenter le nombre de goomba tués.
     */
    public static void incrementCurrentGoomba() {
        nb_goomba++;
    }

    /**
     * Méthode pour obtenir le nombre de koopa tués.
     */
    public static void incrementCurrentKoopa() {
        nb_koopa++;
    }

    /**
     * Méthode qui retourne le nombre de koopa transformées en tortues.
     * @return nombre de shells retoruné, un entier.
     */
    public static int getShells() {
        return shells;
    }

    /**
     * Méthode pour incrémenter le nombre de koopa transformées en tortues.
     */
    public static void incrementShells() {
        shells++;
    }


    /**
     * Méthode pour incrémenter le score actuel.
     * Elle incrémente le score en fonction du type de bonus collecté.
     * @param bonus_type string : "coin", "mushroom" 
     * @return no return
     */
    public static void incrementCurrentScore(String bonus_type) {
        switch (bonus_type){
            case "coin" -> score += CONSTANTS.INCREMENT_SCORE_COIN;
            case "mushroom" -> score += CONSTANTS.INCREMENT_SCORE_MUSHROOM;
            case "koopa" -> score += CONSTANTS.INCREMENT_SCORE_KOOPA;
            case "goomba" -> score += CONSTANTS.INCREMENT_SCORE_GOOMBA;
            case "shell" -> score += CONSTANTS.INCREMENT_SCORE_SHELL;
        }
    }

    
}
