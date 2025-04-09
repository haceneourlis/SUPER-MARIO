package modele;
import java.util.logging.*;
import modele.Tile.TileManager;

public class Collision_entite extends Thread{

    private GameCharacter entity;

    private TileManager tm;
    private static final int DELAY = 17;


    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    private int posLeftenX;
    private int posRightenX;
    private int posTopenY;
    private int posBottomenY;
    private int ligneTopdanslaMatrice;
    private int ligneBottomdanslaMatrice;
    private int colonneLeftdanslaMatrice;
    private int colonneRightdanslaMatrice;

    private boolean sur_brick = false;


    public Collision_entite(GameCharacter gc){
        this.entity = gc;
        this.tm = TileManager.getInstance();
    }
    
    /**
     * Methode qui calcule les position en X et Y des 4 points de la hitbox du gameCharacter
     */
    public void calculate_position_indexes(){
        this.posLeftenX = this.entity.getPosition().x + this.entity.getSolidArea().x;
        this.posRightenX = this.entity.getPosition().x + this.entity.getSolidArea().x + this.entity.getSolidArea().width;

        this.posTopenY = this.entity.getPosition().y;
        this.posBottomenY = this.entity.getPosition().y + this.entity.getSolidArea().y + this.entity.getSolidArea().height * 2;

        this.ligneTopdanslaMatrice = (posTopenY) / CONSTANTS.TAILLE_CELLULE;
        this.ligneBottomdanslaMatrice = posBottomenY / CONSTANTS.TAILLE_CELLULE;
        this.colonneLeftdanslaMatrice = posLeftenX / CONSTANTS.TAILLE_CELLULE;
        this.colonneRightdanslaMatrice = posRightenX / CONSTANTS.TAILLE_CELLULE;

    }


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(DELAY);
                
                // Calcul des points de hitbox
                calculate_position_indexes();

                int point1, point2;

                // check all the time if mario is on the ground .
                // on va anticiper la collision avec la brique :
                point1 = this.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];
                point2 = this.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];
                
                if (this.tm.tiles[point1].collision || this.tm.tiles[point2].collision) {
                    

                    if (!sur_brick) {
                        // TODO : mario dies , game stops if he falls down a hole or a river

                        // let's assume it is just a brick that is solid , so mario walks on it .
                        // check if mario is standing on a solid object , if so he does not stop moving
                        // , he
                        // just continues walking as if he is walking on grass .
                        // System.out.println("STOPPPPPPPPP");

                        mario.position.y = (ligneBottomdanslaMatrice - 1) * CONSTANTS.TAILLE_CELLULE;
                        sur_brick = true;
                        // bloquer la descente !
                        threadDescente.force_mario = 0;

                        threadDescente.marioAllowedToFallDown = false;
                    } else {
                        // System.out.println("je suis sur une brick ---------------#####");
                    }
                } else {
                    // // debloquer la descente
                    // threadDescente.setSol(CONSTANTS.LE_SOL * );

                    threadDescente.marioAllowedToFallDown = true;
                    // System.out.println("je ne suis plus sur un brick ---------------#####");
                    sur_brick = false;
                }

                // ligneBottomdanslaMatrice = posBottomenY / CONSTANTS.TAILLE_CELLULE;
                // point1 = 0;
                // point2 = 0;

                // cat j'ai modifier les bordures sud :
                posBottomenY = mario.getPosition().y + mario.getSolidArea().y + mario.getSolidArea().height;
                ligneBottomdanslaMatrice = posBottomenY / CONSTANTS.TAILLE_CELLULE;

                switch (mario.getDirection()) {

                    // ici le "up" c'est en vrai le saut de mario , donc on check si mario est
                    // rentrer en collision avec un objet en sautant
                    case "up":
                        logger.log(Level.INFO, "at least we are in this case <up>");
                        // on prédit ou sera notre mario aprés avoir bougé
                        ligneTopdanslaMatrice = (posTopenY + threadDescente.force_mario) / CONSTANTS.TAILLE_CELLULE;
                        point1 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                        point2 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];

                        if (gp.tm.tiles[point1].collision == true
                                || gp.tm.tiles[point2].collision == true) {
                            // On remet le joueur à l'emplacement juste en dessous de la brique. (pour
                            // éviter tout petit bug visuel)
                            mario.setPositionY((ligneTopdanslaMatrice + 1) * CONSTANTS.TAILLE_CELLULE);

                            logger.log(Level.INFO, "collision up");

                            // mario , logiquement n'est pas sur brick , innit mate ?
                            sur_brick = false;

                            // Je remets sa force à 0.
                            threadDescente.force_mario = 0;

                            // Je lui permet de descendre.
                            threadDescente.marioAllowedToFallDown = true;
                        }
                        // Verification si point1 ou point2 est une pièce, si oui on incrémente le
                        // nombre de pièces
                        // Et on demande à la matrice (locale) d'être modifiée.
                        if (point1 == 30 || point2 == 30) {
                            this.coin.IncrementNombreDePieces();
                            gp.tm.modifyMatrice(ligneTopdanslaMatrice, colonneLeftdanslaMatrice, 0);
                        }

                        // si mario rentre en collision avec une : brickPrize, cest-à-dire une brique
                        // qui donne une récompense : a coin or a mushroom .
                        // on va faire sortir un coin ou un champignon de la brique
                        // et on va la supprimer de la matrice
                        // TODO : faire sortir un champignon ou une pièce de la brique

                        if (point1 == 2 || point2 == 2) {

                            // créer un objet (coin) et le faire sauter et redescendre sur la brique et +1
                            // au score ed mario
                            // et modifier la brique de la matrice pour qu'elle ne soit plus une brique de
                            // récompense.

                            coinToCatch = new Coin(
                                    new Point(colonneLeftdanslaMatrice, (ligneTopdanslaMatrice - 1)));
                            jumpingThread.jumpLaCoin();

                            this.coin.IncrementNombreDePieces();
                            logger.log(Level.WARNING, "la coin a été crée en position x = {0} et y = {1}",
                                    new Object[] { coinToCatch.position.x, coinToCatch.position.y });

                            logger.log(Level.INFO, "brickPrize collected !");
                            gp.tm.modifyMatrice(ligneTopdanslaMatrice, colonneLeftdanslaMatrice, 1);
                        }

                        point1 = 0;
                        point2 = 0;
                        break;

                    case "left":
                        // same as for up , but we check the left of mario , not the right
                        // because mario can hit a wall or a tree , in which case he stops moving
                        // or even get in collision with an ennemy , in which case he dies !

                        colonneLeftdanslaMatrice = (posLeftenX - CONSTANTS.INTERVALE_COLLISION) /
                                CONSTANTS.TAILLE_CELLULE;
                        point1 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                        point2 = gp.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];

                        // verification si point1 ou point2 est une pièce
                        if ((point1 == 30)) {
                            logger.log(Level.INFO, "Coin collected from left with point1 !");
                            this.coin.IncrementNombreDePieces();
                            gp.tm.modifyMatrice(ligneTopdanslaMatrice, colonneLeftdanslaMatrice, 0);
                        } else if (point2 == 30) {
                            logger.log(Level.INFO, "Coin collected from left with point2 !");
                            this.coin.IncrementNombreDePieces();
                            gp.tm.modifyMatrice(ligneBottomdanslaMatrice, colonneLeftdanslaMatrice, 0);
                        }

                        if (gp.tm.tiles[point1].collision == true
                                || gp.tm.tiles[point2].collision == true) {
                            mario.noMoving();
                        } else {
                            mario.yesMoving();
                        }
                        break;
                    case "right":

                        colonneRightdanslaMatrice = (posRightenX + CONSTANTS.INTERVALE_COLLISION) /
                                CONSTANTS.TAILLE_CELLULE;

                        point1 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];
                        point2 = gp.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];

                        // verification si point1 ou point2 est une pièce
                        if ((point1 == 30)) {
                            logger.log(Level.INFO, "Coin collected from right with point1 !");
                            this.coin.IncrementNombreDePieces();
                            gp.tm.modifyMatrice(ligneTopdanslaMatrice, colonneRightdanslaMatrice, 0);
                        } else if (point2 == 30) {
                            logger.log(Level.INFO, "Coin collected from right with point2 !");
                            this.coin.IncrementNombreDePieces();
                            gp.tm.modifyMatrice(ligneBottomdanslaMatrice, colonneRightdanslaMatrice, 0);
                        }

                        if (gp.tm.tiles[point1].collision == true || gp.tm.tiles[point2].collision == true) {

                            mario.noMoving();
                        } else {
                            mario.yesMoving();
                        }
                        break;

                    default:
                        // System.out.println("chill");
                        break;

                }

            } catch (Exception e){
                logger.log(Level.SEVERE, "Erreur du thread deplacement_entite");   
            }

        }
    }
}
