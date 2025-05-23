package modele;

import java.awt.image.BufferedImage;
import modele.Tile.TileManager;

// package caractere;

// -------------------------------------------------------
// un ennemi qui bouge de gauche à droite (position initiale se trouve à droite de la fenêtre)
// si l'ennemi atteint la frontière, il change de direction

// definir les coordonnées et la taille de l'ennemi
// definir la vitesse de l'ennemi
// definir les frontières de la fenêtre
// definir une methode pour faire bouger l'ennemi
// definir un thread pour gérer le mouvement de l'ennemi

// -------------------------------------------------------

// une classe qui hérite de la classe Thread pour gérer les ennemis
// l'enemi bouge de droite à gauche et ne peut pas sortir de la fenêtre ()
// TODO: Adapter la classe pour supporter plusieurs types d'ennemis, pas uniquement Koopa.
public class Ennemi extends GameCharacter implements Runnable {

    // les frontières
    protected int leftBorder;
    protected int rightBorder;

    public Thread thread;
    protected boolean running = true;
    protected boolean movingRight; // Indique si l'ennemi va à droite

    // change to PROTECTED to be accessed by subclasses
    // images de l'ennemi
    protected BufferedImage[] image;

    protected TileManager tileManager;

    // vitesse de descente
    protected int fallSpeed = 0;
    protected final int maxFallSpeed = 10;

    public Ennemi(int x, int width, int height, int speed, boolean movingRight, TileManager tm) {
        super();
        this.tileManager = tm;

        this.position.x = x;

        this.position.y = CONSTANTS.LE_SOL;

        this.speed = speed;
        this.leftBorder = 0;
        this.rightBorder = CONSTANTS.LARGEUR_VUE - this.solidArea.width;

        this.movingRight = movingRight; // l'ennemi commence par aller à droite

        thread = new Thread(this);
    }

    @Override
    // getter de image en fonction de l'index
    public BufferedImage getImage(int index) {
        return this.image[index];
    }

    public boolean isMovingRight() {
        return movingRight;
    } // Indique si l'ennemi va à droite

    // TODO: in the real game, ennemies disappear when they reach the left of the
    // screen

    // faire bouger l'ennemi en tenant compte des collisions
    public void moveEnnemi() {
        int nextX = this.position.x + (movingRight ? speed : -speed); // Déterminer la prochaine position en X

        // calculer la largeur du monde
        int worldWidth = this.tileManager.tilesMatrice[0].length * CONSTANTS.TAILLE_CELLULE;

        // Si l'ennemi touche les bords, il tourne
        if (nextX <= 0) {
            this.position.x = 0;
            movingRight = true;
            return;
        } else if (nextX + this.solidArea.width >= worldWidth) {
            this.position.x = worldWidth - this.solidArea.width;
            movingRight = false;
            return;
        }

        // Pour une détection cohérente, nous utilisons l'avant de l'ennemi en fonction
        // de son sens de déplacement.
        int footX;
        if (movingRight) {
            // Quand il va à droite, la "frontière" est le bord droit
            footX = nextX + this.solidArea.width;
        } else {
            // Quand il va à gauche, la "frontière" est le bord gauche
            footX = nextX;
        }
        int footY = this.position.y + this.solidArea.height + 1; // 1 pixel en dessous du pied
        int colBelow = footX / CONSTANTS.TAILLE_CELLULE;
        int rowBelow = footY / CONSTANTS.TAILLE_CELLULE;
        if (rowBelow >= 0 && rowBelow < this.tileManager.tilesMatrice.length &&
                colBelow >= 0 && colBelow < this.tileManager.tilesMatrice[0].length) {
            int tileBelowType = this.tileManager.tilesMatrice[rowBelow][colBelow];
            // Si le tile en dessous n'est pas solide, c'est de l'air (puit)
            if (!this.tileManager.tiles[tileBelowType].collision) {
                movingRight = !movingRight;
                return;
            }
        }

        // check the next x position, if moving right, check the right edge, if moving
        // left, check the left edge
        int checkX = movingRight ? nextX + this.solidArea.width : nextX;
        int colAhead = checkX / CONSTANTS.TAILLE_CELLULE;

        // check the row of the enemy's feet
        int rowFeet = (this.position.y + this.solidArea.height - 1) / CONSTANTS.TAILLE_CELLULE;

        // Safety check: ensure the calculated row and column are within the matrix
        // bounds
        if (rowFeet >= 0 && rowFeet < CONSTANTS.maxRow_gameMatrix &&
                colAhead >= 0 && colAhead < this.tileManager.tilesMatrice[0].length) {

            int tileTypeAhead = this.tileManager.tilesMatrice[rowFeet][colAhead];
            if (this.tileManager.tiles[tileTypeAhead].collision) {
                // If an obstacle is encountered, compare the ground height at the current and
                // next positions
                int currentGroundY = findGroundY(this.position.x, this.position.y);
                int nextGroundY = findGroundY(nextX, this.position.y);
                // If the ground at the next position is higher than the current position,
                // reverse direction
                if (currentGroundY > nextGroundY) {
                    movingRight = !movingRight;
                    return;
                }
            }
        }

        // Continuer à avancer normalement
        this.position.x = nextX;
    }

    // demarrer le thread
    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(CONSTANTS.DELAY_ENNEMI);
                moveEnnemi();
                applyGravity(); // appliquer gravité à l'ennemi
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void applyGravity() {
        int groundY = findGroundY(this.position.x, this.position.y); // Trouver le sol en fonction de x, y

        if (groundY >= (CONSTANTS.maxScreenRow + 1) * CONSTANTS.TAILLE_CELLULE) {
            this.stopMoving(); // Arrête le thread de l'ennemi
            tileManager.removeEnnemi(this); // Enlève l'ennemi de la matrice
            return;
        }
        if (this.position.y >= groundY) {
            // L'ennemi est au sol, il ne tombe plus
            this.position.y = groundY;
            fallSpeed = 0;
        } else {
            // Accélération progressive de la chute, mais avec une vitesse maximale
            fallSpeed = Math.min(fallSpeed + CONSTANTS.GRAVITY, maxFallSpeed);
            this.position.y += fallSpeed;
        }

    }

    /**
     * Trouve la hauteur du sol pour une position donnée.
     * 
     * @param startX Position x de départ
     * @param startY Position y de départ
     * @return La position y où l'ennemi doit se poser
     */
    protected int findGroundY(int startX, int startY) {

        int col = startX / CONSTANTS.TAILLE_CELLULE; // Colonne actuelle
        int row = startY / CONSTANTS.TAILLE_CELLULE; // Ligne actuelle

        // Cherche le sol en dessous
        while (row < CONSTANTS.maxRow_gameMatrix) {
            int tileType = this.tileManager.tilesMatrice[row][col];
            if (tileManager.tiles[tileType].collision) {
                return row * CONSTANTS.TAILLE_CELLULE - this.solidArea.height;
            }
            row++;
        }

        // Si aucun sol trouvé, retourne la position par défaut
        return this.position.y + CONSTANTS.TAILLE_CELLULE;
    }

    // arreter le thread
    public void stopMoving() {
        running = false;
    }
}