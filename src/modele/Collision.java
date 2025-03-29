
/*
 * cette classe est un thread qui va vérifier les collisions entre les différents objets du jeu
 * elle n'est pas encore complète
 * elle interagit avec la classe Mario et Ennemi
 * elle interagit avec la classe Affichage
 * elle interagit que AVEC LES VALEURS DU MODELE (pas de la vue) - C'EST IMPORTANT à SAVOIR car c'est que le prof nous a demandé la derniere fois-
 */

package modele;

import vue.Affichage;

import java.util.Iterator;
import java.util.List;
import java.awt.Rectangle;

public class Collision extends Thread {

    private Mario mario;
    private List<Ennemi> ennemis;

    private Affichage gp;
    private Jumping jumpingThread;
    private Descente threadDescente;

    protected boolean sur_brick = false;

    public Collision(Affichage affichage, Jumping jumpingThread, Descente threadDescente) {
        gp = affichage;
        this.mario = Mario.getInstance();
        this.ennemis = gp.getEnnemis();
        this.jumpingThread = jumpingThread;
        this.threadDescente = threadDescente;
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
                Thread.sleep(16);

                mario.updateInvincibility();

                // collision avec la map ou matrice de jeu

                // on trouve les 4 points du rectangle qui vont check la collision
                int posLeftenX = mario.getPosition().x + mario.getSolidArea().x;
                int posRightenX = mario.getPosition().x + mario.getSolidArea().x + mario.getSolidArea().width;
                int posTopenY = mario.getPosition().y - mario.getSolidArea().y * 2;

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
                        System.out.println("at least we are in this case <up>");
                        // on prédit ou sera notre mario aprés avoir bougé
                        ligneTopdanslaMatrice = (posTopenY - jumpingThread.force) / CONSTANTS.TAILLE_CELLULE;
                        point1 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                        point2 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];

                        System.out.println("point1 = " + point1 + " point2 = " + point2);
                        if (gp.tm.tiles[point1].collision == true
                                || gp.tm.tiles[point2].collision == true) {
                            // TODO : si le mur donne des rcompenses , mario les récupérera , sinon il
                            // s'arrête et avec l'effet de grivité redescend !
                            System.out.println("collision up");
                            mario.position.y = 10 * CONSTANTS.TAILLE_CELLULE;
                            jumpingThread.notjumping();
                            threadDescente.allowedToFallDown = true;

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

                Iterator<Ennemi> iterator = gp.getEnnemis().iterator();
                while(iterator.hasNext()) {
                    Ennemi ennemi = iterator.next();

                    // Mario's collision area
                    Rectangle marioHitbox = new Rectangle(
                            mario.getPosition().x + mario.getSolidArea().x,
                            mario.getPosition().y + mario.getSolidArea().y,
                            mario.getSolidArea().width,
                            mario.getSolidArea().height
                    );

                    // Koopa's collision area
                    Rectangle ennemiHitbox = new Rectangle(
                            ennemi.getPosition().x + ennemi.getSolidArea().x,
                            ennemi.getPosition().y + ennemi.getSolidArea().y,
                            ennemi.getSolidArea().width,
                            ennemi.getSolidArea().height
                    );

                    // collision avec les ennemis
                    if(marioHitbox.intersects(ennemiHitbox)) {
                        // mario's feet position
                        int marioFeetY = mario.getPosition().y + mario.getSolidArea().y + mario.getSolidArea().height;
                        // ennemi's head position
                        int ennemiHeadY = ennemi.getPosition().y + ennemi.getSolidArea().y;

                        // check if mario jumps exactement on the ennemi
                        boolean fromAbove = marioFeetY <= ennemiHeadY + 10 && marioFeetY >= ennemiHeadY;
                        boolean isFalling = jumpingThread.force < 0;

//                        if(fromAbove && isFalling) { // add 5 as tolerance
//                            // mario jumps on the ennemi and kills it
//                            System.out.println("Mario kills enemy");
//                            iterator.remove();
//                            jumpingThread.force = jumpingThread.IMPULSION/2; // mario jumps higher
//                        } else {
//                            if(!mario.isInvincible()) {
//                                mario.loseLife();
//
//                                if(mario.getLives() == 0) {
//                                    // TODO : restart the game
//                                    System.out.println("Game over");
//                                }
//                            } else {
//                                System.out.println("Mario loses a life, invincible time");
//                            }
//                        }
                        // 无论是否无敌，都要求从上方并且Mario的方向为"down"才能消灭敌人
                        if(fromAbove && mario.getDirection().equals("down")) {
                            System.out.println("Mario kills enemy");
                            iterator.remove();
                            jumpingThread.force = jumpingThread.IMPULSION / 2;
                        } else {
                            // 如果不是从上方撞击，只有当Mario不是无敌状态时，才会扣血
                            if(!mario.isInvincible()){
                                mario.perdreVie();
                                if(mario.getVies() == 0) {
                                    // TODO : restart the game or show game over screen
                                    System.out.println("Game over");
                                }
                            } else {
                                // 无敌状态下，如果不是从上方，则不做任何处理
                                System.out.println("Mario loses a life, invincible time");
                            }
                        }
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}