package modele;
import java.util.logging.*;
import modele.Tile.TileManager;

public class Collision_entite extends Thread{

    private GameCharacter entity;

    private TileManager tm;
    Descente descente;

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


    public Collision_entite(GameCharacter gc, Descente descente){
        this.entity = gc;
        this.tm = TileManager.getInstance();
        this.descente = descente;
        
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
                        // he just continues walking as if he is walking on grass .

                        this.entity.position.y = (ligneBottomdanslaMatrice - 1) * CONSTANTS.TAILLE_CELLULE;
                        sur_brick = true;
                        // bloquer la descente !
                        this.descente.force = 0;

                        this.entity.allowedToFallDown = false;
                        this.entity.setDirection("right");
                        
                    } 

                } else {
                    // // debloquer la descente

                    this.entity.allowedToFallDown = true;
                    sur_brick = false;
                }

              

                // cat j'ai modifier les bordures sud :
                posBottomenY = this.entity.getPosition().y + this.entity.getSolidArea().y + this.entity.getSolidArea().height;
                ligneBottomdanslaMatrice = posBottomenY / CONSTANTS.TAILLE_CELLULE;

                switch (this.entity.getDirection()) {

                    // ici le "up" c'est en vrai le saut de mario , donc on check si mario est
                    // rentrer en collision avec un objet en sautant
                    case "up":

                        // on prédit ou sera notre mario aprés avoir bougé
                        ligneTopdanslaMatrice = (posTopenY + this.descente.force) / CONSTANTS.TAILLE_CELLULE;
                        point1 = this.tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                        point2 = this.tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];

                        if (this.tm.tiles[point1].collision == true
                                || this.tm.tiles[point2].collision == true) {

                            // On remet le joueur à l'emplacement juste en dessous de la brique. (pour
                            // éviter tout petit bug visuel)
                            this.entity.setPositionY((ligneTopdanslaMatrice + 1) * CONSTANTS.TAILLE_CELLULE);


                            // mario , logiquement n'est pas sur brick , innit mate ?
                            sur_brick = false;

                            // Je remets sa force à 0.
                            this.descente.force = 0;

                            // Je lui permet de descendre.
                            this.entity.allowedToFallDown = true;
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
                        point1 = this.tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                        point2 = this.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];

                      

                        if (this.tm.tiles[point1].collision == true
                                || this.tm.tiles[point2].collision == true) {
                            this.entity.yesMoving();
                            this.entity.setDirection("right");
                        } else {
                            this.entity.yesMoving();
                        }
                        break;
                    case "right":

                        colonneRightdanslaMatrice = (posRightenX + CONSTANTS.INTERVALE_COLLISION) /
                                CONSTANTS.TAILLE_CELLULE;

                        point1 = this.tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];
                        point2 = this.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];
                       
                        if (this.tm.tiles[point1].collision == true || this.tm.tiles[point2].collision == true) {
                            this.entity.yesMoving();
                            this.entity.setDirection("left");
                        } else {
                            this.entity.yesMoving();
                        }
                        break;

                    default:
                        break;

                }

            } catch (Exception e){
                logger.log(Level.SEVERE, "Erreur du thread deplacement_entite");   
            }

        }
    }
}
