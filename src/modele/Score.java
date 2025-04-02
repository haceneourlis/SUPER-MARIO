package modele;

public class Score {
    private int current_score;

    public Score() {
        this.current_score = 0;
    }

    public int getCurrentScore() {
        return this.current_score;
    }

    public void incrementCurrentScore(int increment_factor) {
        this.current_score = this.current_score + increment_factor;
    }

    public void reset() {
        this.current_score= 0;
    }
    
}
