package modele;
import java.util.logging.*;
import modele.Tile.TileManager;

/**
 * Cette classe va gérer les collisions avec les entités du jeu (les champignons par exemple).
 * Elle va aussi gérer l'intéraction entre les entités et le joueur (mario).
 * Elle implémente la classe Thread.
 * Il faudra créer un thread par entité, donc un objet de cette classe par entité.
 */
public class Collision_entite extends Thread{

    // L'entité avec laquelle on va gérer les collisions.
    private GameCharacter entity;

    // Le tile manager.
    private TileManager tm;

    // La descente de l'entité (pour la gravité).
    private Descente descente;


    // Les positions des côtés du rectangle representant la hitbox de l'entité.
    private int posLeftenX;
    private int posRightenX;
    private int posTopenY;
    private int posBottomenY;

    // Les positions des côtés du rectangle representant la hitbox de l'entité dans la matrice de jeu.
    private int ligneTopdanslaMatrice;
    private int ligneBottomdanslaMatrice;
    private int colonneLeftdanslaMatrice;
    private int colonneRightdanslaMatrice;

    // Booléen pour savoir si l'entité est sur une brique ou pas.
    private boolean sur_brick = false;

    // Booléen pour savoir si le thread continue sa boucle infinie ou pas.
    private boolean ok_thread = true;

    // Le deplacement de l'entité.
    Deplacement_entite dp_entite;

    // L'instance de mario, pour gérer l'intéraction entre mario et l'entité.
    Mario mario;

    // Le score, pour pouvoir l'incrémenter si nécessaire
    ScoreManager score;

    // Logger pour debug
    private static final Logger logger = Logger.getLogger(Collision.class.getName());


    /**
     * Le constructeur de la classe Collision_entite.
     * @param gc L'entité avec laquelle on va gérer les collisions.
     * @param descente La descente de l'entité (pour la gravité).
     * @param dp_entite Le deplacement de l'entité.
     */
    public Collision_entite(GameCharacter gc, Descente descente, Deplacement_entite dp_entite){
        // L'entité en question.
        this.entity = gc;
        // La descente de l'entité.
        this.descente = descente;
        // Les déplacements de l'entité.
        this.dp_entite = dp_entite;

        // On charge l'instance de mario.
        this.mario = Mario.getInstance();
        // On charge l'instance du score.
        this.score = ScoreManager.getInstance();
        // On charge l'instance du tilemanager.
        this.tm = TileManager.getInstance();
    }
    
    /**
     * Methode qui calcule les position en X et Y des 4 points de la hitbox de l'entité.
     * Elle va aussi calculer les positions dans la matrice de jeu de ces points.
     * @param rien
     * @return rien
     */
    public void calculate_position_indexes(){
        // Pour des explications de comment et pourquoi on calcule de cette manière
        // Se référer à la documentation sur les collisions.


        // On va calculer les positions en X et Y des 4 points de la hitbox de l'entité.
        this.posLeftenX = this.entity.getPosition().x + this.entity.getSolidArea().x;
        this.posRightenX = this.entity.getPosition().x + this.entity.getSolidArea().x + this.entity.getSolidArea().width;
        this.posTopenY = this.entity.getPosition().y;
        this.posBottomenY = this.entity.getPosition().y + this.entity.getSolidArea().y + this.entity.getSolidArea().height * 2;

        // On va calculer les positions dans la matrice de jeu de ces points.
        this.ligneTopdanslaMatrice = (posTopenY) / CONSTANTS.TAILLE_CELLULE;
        this.ligneBottomdanslaMatrice = posBottomenY / CONSTANTS.TAILLE_CELLULE;
        this.colonneLeftdanslaMatrice = posLeftenX / CONSTANTS.TAILLE_CELLULE;
        this.colonneRightdanslaMatrice = posRightenX / CONSTANTS.TAILLE_CELLULE;

    }

    /**
     * Méthode run de la classe Thread.
     */
    @Override
    public void run() {

        // 2 points, pour gérer les collisions, permet de savoir sur quel type de tuile on fait face.
        int point1, point2;

        while (ok_thread) {
            try {
                Thread.sleep(CONSTANTS.DELAY_COLLISION_ENTITE);
                
                // Calcul des points de hitbox.
                calculate_position_indexes();

                /*
                On va check s'il y'a une collision avec mario, pour ce faire on calcule
                Les points de hitbox de mario également.
                */
                int mario_border_left = this.mario.getPosition().x + this.mario.getSolidArea().x;
                int mario_border_right = this.mario.getPosition().x + this.mario.getSolidArea().x + this.mario.getSolidArea().width;
                int mario_border_up = this.mario.getPositionY();
                int mario_border_down = this.mario.getPosition().y + this.mario.getSolidArea().y + this.mario.getSolidArea().height * 2;

                // On regarde si la hitbox de mario et la hitbox de l'entité se chevauchent.
                // On vérifie si le point gauche de la hitbox de l'entité est dans la hitbox de mario
                // ou si le point droit de la hitbox de l'entité est dans la hitbox de mario.
                if ((this.posLeftenX >= mario_border_left && this.posLeftenX <= mario_border_right) || (this.posRightenX >= mario_border_left && this.posRightenX <= mario_border_right)){
                    // Puis on vérifie si le point haut de la hitbox de l'entité est dans la hitbox de mario
                    // ou si le point bas de la hitbox de l'entité est dans la hitbox de mario.
                    if ((this.posTopenY >= mario_border_up && this.posTopenY <= mario_border_down) || (this.posBottomenY >= mario_border_up && this.posBottomenY <= mario_border_down)){
                        // On a une collision entre mario et l'entité.
                        // On vérifie le type de l'entité.
                        if (this.entity instanceof Champignon) {
                            // Si c'est un champignon, on va lui donner une vie.
                            this.mario.augmenterVie();
                            // On va incrémenter le score.
                            this.score.incrementCurrentScore("mushroom");
                            // On va enlever l'entité de la liste des entités.
                            this.tm.removeEntityFromList(this.entity);
                            // et on break à ce moment-là en arrêtant le thread
                            this.ok_thread = false;
                            break;
                        }  
                    }
                }

                // On va vérifier si l'entité est sur une brique ou pas.
                // On regarde les deux types de tuiles qui sont en dessous l'entité (en bas à gauche et en bas à droite).
                point1 = this.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];
                point2 = this.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];
                
                // On va vérifier si ces deux tuiles sont solides ou pas.
                if (this.tm.tiles[point1].collision || this.tm.tiles[point2].collision) {
                    
                    // Si c'est le cas, on va vérifier si l'entité est sur une brique ou pas.
                    if (!sur_brick) {

                        // check if mario is standing on a solid object , if so he does not stop moving
                        // he just continues walking as if he is walking on grass .

                        // On replace l'entité sur la brique.)
                        this.entity.position.y = (ligneBottomdanslaMatrice - 1) * CONSTANTS.TAILLE_CELLULE;

                        // On dit que l'entité est sur une brique
                        sur_brick = true;

                        // bloquer la descente, en mettant la force à 0 (on la reset)
                        this.descente.force = 0;

                        // Et on ne lui permet pas de tomber.
                        this.entity.allowedToFallDown = false;

                        // Et on dit qu'il va aller à gauche ou à droite.
                        if (dp_entite.go_right) {
                            this.entity.setDirection("right");
                        } else {
                            this.entity.setDirection("left");
                        }
                        
                    } 

                // Si les tuiles ne sont pas solides, alors l'entité tombe
                } else {
                    // On permet à l'entité de tomber.
                    this.entity.allowedToFallDown = true;

                    // Et on dit qu'elle n'est pas sur une brique.
                    sur_brick = false;
                }

              

                // On recalcule la position en Y du bas de la hitbox de l'entité.
                posBottomenY = this.entity.getPosition().y + this.entity.getSolidArea().y + this.entity.getSolidArea().height;
                ligneBottomdanslaMatrice = posBottomenY / CONSTANTS.TAILLE_CELLULE;

                // On regarde en fonction de la direction de l'entité.
                switch (this.entity.getDirection()) {

                    // Comment l'entité ne saute pas, on regarde juste ses collisions à gauche et à droite
                    case "left":
                        // On recalcule la colonne de la hitbox gauche de l'entité avec un intervale de collision (d'anticipation on va dire).
                        colonneLeftdanslaMatrice = (posLeftenX - CONSTANTS.INTERVALE_COLLISION) /
                                CONSTANTS.TAILLE_CELLULE;

                        // On va regarder les deux points de la hitbox gauche de l'entité.
                        point1 = this.tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                        point2 = this.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];

                      
                        // On va vérifier si ces deux tuiles sont solides ou pas.
                        if (this.tm.tiles[point1].collision == true
                                || this.tm.tiles[point2].collision == true) {
                            // Si c'est le cas, on demande à l'entité d'aller à droite.
                            this.dp_entite.go_right = true;
                            this.entity.setDirection("right");
                        }
                        // Et dans tous les cas, on autorise l'entité de bouger
                        this.entity.yesMoving();
                        break;


                    case "right":
                        // Exactement similaire à case left, mais pour la droite.

                        // On recalcule la colonne de la hitbox droite de l'entité.
                        colonneRightdanslaMatrice = (posRightenX + CONSTANTS.INTERVALE_COLLISION) /
                                CONSTANTS.TAILLE_CELLULE;


                        // On va regarder les deux points de la hitbox droite de l'entité.
                        point1 = this.tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];
                        point2 = this.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];
                       
                        // On va vérifier si ces deux tuiles sont solides ou pas.
                        if (this.tm.tiles[point1].collision == true || this.tm.tiles[point2].collision == true) {
                            // Si c'est le cas, on demande à l'entité d'aller à gauche.
                            this.dp_entite.go_right = false;
                            this.entity.setDirection("left");
                        }
                        // Et dans tous les cas, on autorise l'entité de bouger
                        this.entity.yesMoving();
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
