package modele;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
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

    protected String type; // Type de l'ennemi (koopa, goomba, etc.)

    public Ennemi(int x, int width, int height, int speed, boolean movingRight, TileManager tileManager, String type) {
        super();
        this.type = type;
        if (type.equals("koopa")) {
            image = new BufferedImage[3];
            try {
                image[0] = ImageIO.read(getClass().getResourceAsStream("/resources/koopa_sprites/koopa2.png"));
                image[1] = ImageIO.read(getClass().getResourceAsStream("/resources/koopa_sprites/koopa1.png"));
                image[2] = ImageIO.read(getClass().getResourceAsStream("/resources/koopa_sprites/koopa2.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // the other types (like goomba) will be implemented in the subclass

        this.position.x = x;

        this.position.y = CONSTANTS.LE_SOL;
        this.tileManager = tileManager;

        if (image != null && image.length > 0) {
            this.solidArea.x = 0;
            this.solidArea.y = 0;
            this.solidArea.height = image[0].getHeight();
            this.solidArea.width = image[0].getWidth();
        }

        this.speed = speed;
        this.leftBorder = 0;
        this.rightBorder = CONSTANTS.LARGEUR_VUE - this.solidArea.width;

        this.movingRight = movingRight; // l'ennemi commence par aller à droite

        thread = new Thread(this);
    }

    public String getType() {
        return type;
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
        int col = (nextX + (movingRight ? this.solidArea.width - 1 : 0)) / CONSTANTS.TAILLE_CELLULE;
        int row = (this.position.y + this.solidArea.height - 1) / CONSTANTS.TAILLE_CELLULE;

        // Si Koopa touche les bords, il tourne
        if (nextX <= 0) {
            this.position.x = 0;
            movingRight = true;
            return;
        } else if (nextX + this.solidArea.width >= CONSTANTS.LARGEUR_VUE) {
            this.position.x = CONSTANTS.LARGEUR_VUE - this.solidArea.width;
            movingRight = false;
            return;
        }

        // Vérifier les obstacles normaux
        if (col < CONSTANTS.maxScreenCol) {
            int tileType = this.tileManager.tilesMatrice[row][col];
            if (this.tileManager.tiles[tileType].collision) {
                movingRight = !movingRight;
                return;
            }
        }

        // Continuer à avancer
        this.position.x = nextX;
    }

    // demarrer le thread
    @Override
    public void run() {
        while (running) {
            applyGravity(); // appliquer gravité à l'ennemi
            moveEnnemi();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void applyGravity() {
        int groundY = findGroundY(this.position.x, this.position.y); // Trouver le sol en fonction de x, y

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
        while (row < CONSTANTS.maxScreenRow) {
            int tileType = this.tileManager.tilesMatrice[row][col];
            if (this.tileManager.tiles[tileType].collision) {
                return row * CONSTANTS.TAILLE_CELLULE - this.solidArea.height;
            }
            row++;
        }

        // Si aucun sol trouvé, retourne la position par défaut
        return CONSTANTS.LE_SOL;
    }

    // arreter le thread
    public void stopMoving() {
        running = false;
    }
}