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
    public void incrementCurrentCoins() {
        nb_coins++;
    }

    /**
     * Méthode pour incrémenter le score actuel.
     * Elle incrémente le score en fonction du type de bonus collecté.
     * @param bonus_type string : "coin", "mushroom" 
     * @return no return
     */
    public void incrementCurrentScore(String bonus_type) {
        switch (bonus_type){
            case "coin" -> score += CONSTANTS.INCREMENT_SCORE_COIN;
            case "mushroom" -> score += CONSTANTS.INCREMENT_SCORE_MUSHROOM;
        }
    }

    
}
