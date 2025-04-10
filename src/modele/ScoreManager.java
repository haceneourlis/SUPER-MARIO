package modele;

// singleton class.
public class ScoreManager {

    private static ScoreManager instance = null;

    private static int score = 0;
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

    public static void resetScore() {
        score = 0;
        nb_coins = 0;
    }

    public static int getScore() {
        return score;
    }

    public static int getCoins() {
        return nb_coins;
    }

    public void incrementCurrentScore(String bonus_type) {
        switch (bonus_type){
            case "coin" -> score += CONSTANTS.INCREMENT_SCORE_COIN;
            case "mushroom" -> score += CONSTANTS.INCREMENT_SCORE_MUSHROOM;
        }
    }

    public void incrementCurrentCoins() {
        nb_coins++;
    }
}
