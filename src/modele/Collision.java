
/* 
 * cette classe est un thread qui va vérifier les collisions entre les différents objets du jeu
 * elle n'est pas encore complète
 * elle interagit avec la classe Mario et Ennemi
 * elle interagit avec la classe Affichage
 * elle interagit que AVEC LES VALEURS DU MODELE (pas de la vue) - C'EST IMPORTANT à SAVOIR car c'est que le prof nous a demandé la derniere fois- 
 */

package modele;

import java.util.logging.*;
import vue.Affichage;

public class Collision extends Thread {

    private Mario mario;
    private Ennemi ennemi;

    private Affichage gp;
    private Jumping jumpingThread;
    private Descente threadDescente;

    private Coin coin;

    protected boolean sur_brick = false;

    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    public Collision(Affichage affichage, Jumping jumpingThread, Descente threadDescente, Coin coin) {
        gp = affichage;
        this.mario = Mario.getInstance();
        this.ennemi = gp.getEnnemi();
        this.jumpingThread = jumpingThread;
        this.threadDescente = threadDescente;
        this.coin = coin;
    }

    @Override
    public void run() {
        while (true) {
            // synchronized (gp) {
            // while (gp.PAUSE) {
            // try {
            // gp.wait();
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }
            // }
            // }

            try {
                Thread.sleep(5);

                // collision avec la map ou matrice de jeu

                // on trouve les 4 points du rectangle qui vont check la collision
                int posLeftenX = mario.getPosition().x + mario.getSolidArea().x;
                int posRightenX = mario.getPosition().x + mario.getSolidArea().x + mario.getSolidArea().width;

                // Ici j'ai modifié la variable y, il y'avait un - quelque chose.. et il ne y'en a pas besoin
                int posTopenY = mario.getPosition().y;

                // Je modifie cette ligne : CAR effectivement y avait un probleme lors de
                // l'atterissage de mario
                // sur une brique , il se teleportait d'un niveau i à un niveau i + 1
                // donc pour regler ça j'ai multiplié par 2 la hauteur de la solid area de mario
                // pour que mario aie jusqu'au niveau i+1 ensuite s'arrette , je corige
                // l'affichage en l'affichant au niveau i . GG .
                int posBottomenY = mario.getPosition().y + mario.getSolidArea().y + mario.getSolidArea().height * 2;

                // // on trouve maintenant les lignes & colonnes ou se trouve les derniers
                // coordonnées dans la MAtrice du jeu
                // // car c'est avec ça qu'on saura que l'objet (arbre , terre , ... ) est
                // solide ou pas
                int ligneTopdanslaMatrice = (posTopenY) / CONSTANTS.TAILLE_CELLULE;
                int ligneBottomdanslaMatrice = posBottomenY / CONSTANTS.TAILLE_CELLULE;
                int colonneLeftdanslaMatrice = posLeftenX / CONSTANTS.TAILLE_CELLULE;
                int colonneRightdanslaMatrice = posRightenX / CONSTANTS.TAILLE_CELLULE;

                // on get maintenant en fonction de la direction de mario , le 2 points
                // susceptibles à entrer en collision avec le monde externe ( matrice )
                int point1, point2;

                // check all the time if mario is on the ground .
                // on va anticiper la collision avec la brique :
                point1 = gp.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];
                point2 = gp.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];

                if (gp.tm.tiles[point1].collision || gp.tm.tiles[point2].collision) {
                    // System.out.println("point1 = " + point1 + " point2 = " + point2);
                    // si c'est une brique de type 3 ( puit , riviere , ... ) alors mario meurt
                    // if (point1 == 3 || point2 == 3) {
                    // // TODO : mario meurt
                    // threadDescente.setSol(CONSTANTS.LE_SOL * 2);
                    // jumpingThread.notjumping();
                    // mario.noMoving();

                    // System.out.println("waaaaaaaaaaaa333333");
                    
                    // } else



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

                        threadDescente.allowedToFallDown = false;

                        // threadDescente.setSol((ligneBottomdanslaMatrice - 1) *
                        // CONSTANTS.TAILLE_CELLULE);
                        jumpingThread.notjumping();
                    } else {
                        // System.out.println("je suis sur une brick ---------------#####");
                    }
                } else {
                    // // debloquer la descente
                    // threadDescente.setSol(CONSTANTS.LE_SOL);

                    threadDescente.allowedToFallDown = true;
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
                        ligneTopdanslaMatrice = (posTopenY + threadDescente.force) / CONSTANTS.TAILLE_CELLULE;
                        point1 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                        point2 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];
                        
                        if (gp.tm.tiles[point1].collision == true
                                || gp.tm.tiles[point2].collision == true) {
                            // On remet le joueur à l'emplacement juste en dessous de la brique. (pour éviter tout petit bug visuel)
                            mario.setPositionY((ligneTopdanslaMatrice+1)*CONSTANTS.TAILLE_CELLULE);
                            
                            logger.log(Level.INFO ,"collision up");

                            // Je remets sa force à 0.
                            threadDescente.force = 0;

                            // Je lui permet de descendre.
                            threadDescente.allowedToFallDown = true;
                        }
                        // Verification si point1 ou point2 est une pièce, si oui on incrémente le nombre de pièces
                        // Et on demande à la matrice (locale) d'être modifiée.
                        if ((point1 == 30)){
                            logger.log(Level.INFO, "Coin collected from up with point1 !");
                            this.coin.IncrementNombreDePieces();
                            gp.tm.modifyMatrice(ligneTopdanslaMatrice, colonneLeftdanslaMatrice, 0);
                        } else if (point2 == 30){
                            logger.log(Level.INFO, "Coin collected from up with point2 !");
                            this.coin.IncrementNombreDePieces();
                            gp.tm.modifyMatrice(ligneTopdanslaMatrice, colonneRightdanslaMatrice, 0);
                        }
                        break;

                    case "left":
                        // same as for up , but we check the left of mario , not the right
                        // because mario can hit a wall or a tree , in which case he stops moving
                        // or even get in collision with an ennemy , in which case he dies !

                        colonneLeftdanslaMatrice = (posLeftenX - mario.getSpeed()) /
                                CONSTANTS.TAILLE_CELLULE;
                        point1 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                        point2 = gp.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];
                        
                        // verification si point1 ou point2 est une pièce
                        if ((point1 == 30)){
                            logger.log(Level.INFO, "Coin collected from left with point1 !");
                            this.coin.IncrementNombreDePieces();
                            gp.tm.modifyMatrice(ligneTopdanslaMatrice, colonneLeftdanslaMatrice, 0);
                        } else if (point2 == 30){
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

                        colonneRightdanslaMatrice = (posRightenX + mario.getSpeed()) /
                                CONSTANTS.TAILLE_CELLULE;
                        point1 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];
                        point2 = gp.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];

                        // verification si point1 ou point2 est une pièce
                        if ((point1 == 30)){
                            logger.log(Level.INFO, "Coin collected from right with point1 !");
                            this.coin.IncrementNombreDePieces();
                            gp.tm.modifyMatrice(ligneTopdanslaMatrice, colonneRightdanslaMatrice, 0);
                        } else if (point2 == 30){
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

                // TODO : + collision avec les ennemeies ,

                // prendre le solid area de cet ennemi
                ennemi.solidArea.x = ennemi.getPosition().x + ennemi.solidArea.x;
                ennemi.solidArea.y = ennemi.getPosition().y + ennemi.solidArea.y;

                // prendre le solid area de mario
                mario.solidArea.x = mario.getPosition().x + mario.solidArea.x;
                mario.solidArea.y = mario.getPosition().y + mario.solidArea.y;

                if (mario.solidArea.intersects(ennemi.solidArea)) {
                    // mario meurt :

                    // show a message in the screen that mario died
                    // and stop the game
                    logger.log(Level.INFO, "mario died");
                    threadDescente.setSol(CONSTANTS.LE_SOL * 2);
                }
                // reset the solid area of mario to its original value and the solid area of
                // ennemi to its original value

                ennemi.solidArea.x = CONSTANTS.slidAreaDefaultX;
                ennemi.solidArea.y = CONSTANTS.slidAreaDefaultY;
                mario.solidArea.x = CONSTANTS.slidAreaDefaultX;
                mario.solidArea.y = CONSTANTS.slidAreaDefaultY;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
