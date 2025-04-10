package modele;

public class Score {
    private static int score = 0;

    public static int getScore() {
        return score;
    }

    public void incrementCurrentScore() {
        score += CONSTANTS.FACTEUR_SCORE;
    }
}
