
/*
 * cette classe est un thread qui va vérifier les collisions entre les différents objets du jeu
 * elle n'est pas encore complète
 * elle interagit avec la classe Mario et Ennemi
 * elle interagit avec la classe Affichage
 * elle interagit que AVEC LES VALEURS DU MODELE (pas de la vue) - C'EST IMPORTANT à SAVOIR car c'est que le prof nous a demandé la derniere fois-
 */

package modele;

import vue.Affichage;
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

                switch (mario.getDirection()) {
                    // ici le "up" c'est en vrai le saut de mario , donc on check si mario est
                    // rentrer en collision avec un objet en sautant
                    case "up":
                        // on prédit ou sera notre mario aprés avoir bougé
                        ligneTopdanslaMatrice = (posTopenY - jumpingThread.force) / CONSTANTS.TAILLE_CELLULE;
                        point1 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                        point2 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];

                        if (gp.tm.tiles[point1].collision == true
                                || gp.tm.tiles[point2].collision == true) {
                            // TODO : si le mur donne des rcompenses , mario les récupérera , sinon il
                            // s'arrête et avec l'effet de grivité redescend !
                        }
                        break;
                    case "down":
                        // same as for up , but we check the bottom of mario , not the top
                        // because mario can fall down a river or a hole , in which case he dies

                        ligneBottomdanslaMatrice = (posBottomenY + CONSTANTS.GRAVITY) / CONSTANTS.TAILLE_CELLULE;
                        point1 = gp.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];
                        point2 = gp.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];

                        if (gp.tm.tiles[point1].collision == true
                                || gp.tm.tiles[point2].collision == true) {
                            // TODO : mario dies , game stops if he falls down a hole or a river

                        }

                        break;
                    case "left":
                        // same as for up , but we check the left of mario , not the right
                        // because mario can hit a wall or a tree , in which case he stops moving
                        // or even get in collision with an ennemy , in which case he dies !

                        colonneLeftdanslaMatrice = (posLeftenX - mario.getSpeed()) / CONSTANTS.TAILLE_CELLULE;
                        point1 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                        point2 = gp.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];

                        if (gp.tm.tiles[point1].collision == true
                                || gp.tm.tiles[point2].collision == true) {
                            // TODO : mario stops moving
                            System.out.println(
                                    "collision going left <-  because of tile of type = " + point1 + " or maybe "
                                            + point2);

                        }
                        break;
                    case "right":

                        colonneRightdanslaMatrice = (posRightenX + mario.getSpeed()) / CONSTANTS.TAILLE_CELLULE;
                        point1 = gp.tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];
                        point2 = gp.tm.tilesMatrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];

                        if (gp.tm.tiles[point1].collision == true || gp.tm.tiles[point2].collision == true) {
                            // TODO : mario stops moving
                            System.out.println(
                                    "collision going right -> because of tile of type = " + point1 + " or maybe "
                                            + point2);

                        }
                        break;
                }

                mario.solidArea.x = CONSTANTS.slidAreaDefaultX;
                mario.solidArea.y = CONSTANTS.slidAreaDefaultY;
                // TODO : + collision avec les ennemeies ,

                for (Ennemi ennemi : gp.getEnnemis()) {
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

                    // collision avec les ennemeies
                    if(marioHitbox.intersects(ennemiHitbox)) {
                        System.out.println("Mario est en collision avec un ennemi");
                    }


                    Rectangle r1 = new Rectangle(58, 368, 32, 32);
                    Rectangle r2 = new Rectangle(613, 368, 32, 48);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}