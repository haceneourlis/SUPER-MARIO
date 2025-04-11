package modele;

import modele.Tile.TileManager;

// this class handles mario's death

public class Death extends Thread {
    // instance unique de la classe Death
    private static Death instance = null;

    Thread descente;
    Thread collision;

    TileManager tileManager = TileManager.getInstance();

    private Death(Thread descente, Thread collision) {
        this.descente = descente;
        this.collision = collision;
    }

    // méthode pour créer une instance unique de la classe Death (singleton)
    public static Death getInstance(Thread descente, Thread collision) {
        if (instance == null) {
            instance = new Death(descente, collision);
        }
        return instance;
    }

    public void run() {
        while (true) {
            try {
                sleep(5);

                if (Mario.getViesMario() <= 0) {
                    Mario.VIVANT = false; // Mario est mort
                }

                if (!Mario.VIVANT) {
                    System.out.println("GAME OVER !");
                    sleep(3000);

                    tileManager.decalage = 0; // Réinitialiser le décalage
                    // suppression de toutes les entités
                    tileManager.eraseALLEntities();
                    // rechargement de la carte
                    tileManager.loadMatrice_1();

                    sleep(1000);
                    // reload les ennemis aussi
                    tileManager.loadEnnemis_1();

                    Mario.reborn();
                }
            } catch (Exception e) {
                System.out.println("Erreur dans le thread de la mort : " + e.getMessage());
            }
        }
    }
}