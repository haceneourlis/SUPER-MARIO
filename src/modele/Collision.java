
/* 
 * cette classe est un thread qui va vérifier les collisions entre les différents objets du jeu
 * elle n'est pas encore complète
 * elle interagit avec la classe Mario et Ennemi
 * elle interagit avec la classe Affichage
 * elle interagit que AVEC LES VALEURS DU MODELE (pas de la vue) - C'EST IMPORTANT à SAVOIR car c'est que le prof nous a demandé la derniere fois- 
 */

package modele;

import vue.Affichage;

public class Collision extends Thread {

    private Mario mario;
    private Ennemi ennemi;

    private Affichage gp;
    private Jumping jumpingThread;
    private Descente threadDescente;

    protected boolean sur_brick = false;

    public Collision(Affichage affichage, Jumping jumpingThread, Descente threadDescente) {
        gp = affichage;
        this.mario = Mario.getInstance();
        this.ennemi = gp.getEnnemi();
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

                // collision avec la map ou matrice de jeu

                // on trouve les 4 points du rectangle qui vont check la collision
                int posLeftenX = mario.getPosition().x + mario.getSolidArea().x;
                int posRightenX = mario.getPosition().x + mario.getSolidArea().x + mario.getSolidArea().width;
                int posTopenY = mario.getPosition().y + mario.getSolidArea().y;
                int posBottomenY = mario.getPosition().y + mario.getSolidArea().y + mario.getSolidArea().height;

                // // on trouve maintenant les lignes & colonnes ou se trouve les derniers
                // coordonnées dans la MAtrice du jeu
                // // car c'est avec ça qu'on saura que l'objet (arbre , terre , ... ) est
                // solide ou pas
                int ligneTopdanslaMatrice = posTopenY / CONSTANTS.TAILLE_CELLULE;
                int ligneBottomdanslaMatrice = posBottomenY / CONSTANTS.TAILLE_CELLULE;
                int colonneLeftdanslaMatrice = posLeftenX / CONSTANTS.TAILLE_CELLULE;
                int colonneRightdanslaMatrice = posRightenX / CONSTANTS.TAILLE_CELLULE;

                // on get maintenant en fonction de la direction de mario , le 2 points
                // susceptibles à entrer en collision avec le monde externe ( matrice )
                int point1, point2;

                // check all the time if mario is on the ground .
                // on va anticiper la collision avec la brique :
                point1 = gp.tm.tilesMatrice[ligneBottomdanslaMatrice + 1][colonneLeftdanslaMatrice];
                point2 = gp.tm.tilesMatrice[ligneBottomdanslaMatrice + 1][colonneRightdanslaMatrice];

                if (gp.tm.tiles[point1].collision || gp.tm.tiles[point2].collision) {
                    System.out.println("point1 = " + point1 + " point2 = " + point2);
                    // si c'est une brique de type 3 ( puit , riviere , ... ) alors mario meurt
                    if (point1 == 3 || point2 == 3) {
                        // TODO : mario meurt
                        threadDescente.setSol(CONSTANTS.LE_SOL * 2);
                        jumpingThread.notjumping();
                        mario.noMoving();

                        System.out.println("waaaaaaaaaaaa333333");

                    } else if (!sur_brick) {
                        // TODO : mario dies , game stops if he falls down a hole or a river

                        // let's assume it is just a brick that is solid , so mario walks on it .
                        // check if mario is standing on a solid object , if so he does not stop moving
                        // , he
                        // just continues walking as if he is walking on grass .
                        // System.out.println("STOPPPPPPPPP");

                        mario.position.y = (ligneBottomdanslaMatrice) * CONSTANTS.TAILLE_CELLULE;
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

                switch (mario.getDirection()) {
                    // ici le "up" c'est en vrai le saut de mario , donc on check si mario est
                    // rentrer en collision avec un objet en sautant
                    case "up":
                        // on prédit ou sera notre mario aprés avoir bougé
                        ligneTopdanslaMatrice = (posTopenY - jumpingThread.force) /
                                CONSTANTS.TAILLE_CELLULE;
                        point1 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                        point2 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];

                        if (gp.tm.tiles[point1].collision == true
                                || gp.tm.tiles[point2].collision == true) {
                            // TODO : si le mur donne des rcompenses , mario les récupérera , sinon il
                            // s'arrête et avec l'effet de grivité redescend !
                            System.out.println(
                                    "collision going up ---> because of tile of type = " + point1 +
                                            " or maybe "
                                            + point2);
                            mario.position.y = (ligneTopdanslaMatrice) * CONSTANTS.TAILLE_CELLULE;

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
                            // TODO : mario stops moving
                            System.out.println(
                                    "collision going left <--- because of tile of type = " + point1 +
                                            " or maybe "
                                            + point2);
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
                            // TODO : mario stops moving
                            mario.noMoving();
                        } else {
                            mario.yesMoving();
                        }
                        break;

                    default:
                        // System.out.println("chill");
                        break;

                }

                mario.solidArea.x = CONSTANTS.slidAreaDefaultX;
                mario.solidArea.y = CONSTANTS.slidAreaDefaultY;

                // TODO : + collision avec les ennemeies ,
                // for (int i = 0; i < GamePanel.monsters.length; i++) {
                // if (GamePanel.monsters[i] != null) {
                // // prendre le solid area de cet ennemi
                // GamePanel.monsters[i].solidArea.x = GamePanel.monsters[i].getPosition().x
                // + GamePanel.monsters[i].solidArea.x;
                // GamePanel.monsters[i].solidArea.y = GamePanel.monsters[i].getPosition().y
                // + GamePanel.monsters[i].solidArea.y;

                // // prendre le solid area de mario
                // mario.solidArea.x = mario.getPosition().x + mario.solidArea.x;
                // mario.solidArea.y = mario.getPosition().y + mario.solidArea.y;

                // // check si les 2 solid areas se touchent
                // switch (mario.getDirection()) {
                // // il ne peut rentre en collison que en : down , left et right

                // case "down":
                // mario.solidArea.y += CONSTANTS.GRAVITEEE;
                // if (mario.solidArea.intersects(GamePanel.monsters[i].solidArea)) {
                // // TODO : l'ennemi meurt : creer une methode qui tue l'ennemi
                // }
                // break;
                // case "left":
                // mario.solidArea.x -= mario.getSpeed();
                // if (mario.solidArea.intersects(GamePanel.monsters[i].solidArea)) {
                // // TODO : mario meurt : creer une méthode qui tue mario et arrete le jeu
                // }
                // break;
                // case "right":
                // mario.solidArea.x += mario.getSpeed();
                // if (mario.solidArea.intersects(GamePanel.monsters[i].solidArea)) {
                // // TODO : mario meurt
                // }
                // break;
                // }
                // // reset the solid area of mario to its original value and the solid area of
                // // ennemi to its original value

                // // GamePanel.monsters[i].solidArea.x = CONSTANTS.slidAreaDefaultX;
                // // GamePanel.monsters[i].solidArea.y = CONSTANTS.slidAreaDefaultY;

                // // }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
