package modele;

public class Coin extends Bonus{

    private int nombre_de_pieces;

    // un score qui est relié
    private Score score;


    
    public Coin(Score score) {
        
        super();
        // nombre de pièces récupérées par le joueur
        this.nombre_de_pieces = 0;

        // on récupère le score
        this.score = score;
        
        // désigne le facteur d'incrémentation du score (choix arbitraire de valeur)
        this.increment_factor = 20;
       
    }



    public int getNombreDePieces() {
        return this.nombre_de_pieces;
    }

    public void IncrementNombreDePieces() {
        this.nombre_de_pieces++;
        this.score.incrementCurrentScore(this.increment_factor);
    }


}
