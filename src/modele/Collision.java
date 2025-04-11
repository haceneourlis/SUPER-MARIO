
/*
 * cette classe est un thread qui va vérifier les collisions entre les différents objets du jeu
 * elle n'est pas encore complète
 * elle interagit avec la classe Mario et Ennemi
 * elle interagit avec la classe Affichage
 * elle interagit que AVEC LES VALEURS DU MODELE (pas de la vue) - C'EST IMPORTANT à SAVOIR car c'est que le prof nous a demandé la derniere fois-
 */

package modele;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.logging.*;
import modele.Tile.TileManager;

public class Collision extends Thread {

    private Mario mario;
    private List<Ennemi> ennemis;

    private Jumping jumpingThread;
    private Descente threadDescente;

    // Initialiser le gestionnaire de tuiles
    TileManager tm;

    // le score de mario
    private ScoreManager scoreManager = ScoreManager.getInstance();

    private int previousMarioFeetY = Mario.getInstance().getPosition().y
            + Mario.getInstance().getSolidArea().y
            + Mario.getInstance().getSolidArea().height;

    public static Coin coinToCatch = null;

    protected boolean sur_brick = false;

    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    public Collision(Jumping jumpingThread, Descente threadDescente) {
        this.mario = Mario.getInstance();

        this.jumpingThread = jumpingThread;
        this.threadDescente = threadDescente;
        this.tm = TileManager.getInstance();

        this.ennemis = tm.getListeEnnemis();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5);

                mario.updateInvincibility();

                // dés le début de la boucle , on va check si mario est tombé ou pas
                if (mario.getPosition().y >= CONSTANTS.maxScreenRow * CONSTANTS.TAILLE_CELLULE) {
                    // il faut arreter le thread de collision et descente .
                    mario.allowedToFallDown = false;
                    threadDescente.force = 0;

                    System.out.println("mario est tombé ! il est mort !");
                    Mario.killMario();
                    sleep(1000);
                } else {

                    // collision avec la map ou matrice de jeu

                    // on trouve les 4 points du rectangle qui vont check la collision
                    int posLeftenX = mario.getPosition().x + mario.getSolidArea().x;
                    int posRightenX = mario.getPosition().x + mario.getSolidArea().x + mario.getSolidArea().width;
                    int posTopenY = mario.getPosition().y;
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
                    point1 = tm.tilesMatrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];
                    point2 = tm.tilesMatrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];

                    if (tm.tiles[point1].collision || tm.tiles[point2].collision) {
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
                            threadDescente.force = 0;

                            this.mario.allowedToFallDown = false;
                        } else {
                            // System.out.println("je suis sur une brick ---------------#####");
                        }
                    } else {
                        // ICI MARIO RIP . 11/04/2025 THE SUPER MARIO DIED .
                        mario.allowedToFallDown = true;
                        sur_brick = false;
                    }

                    // cat j'ai modifier les bordures sud :
                    posBottomenY = mario.getPosition().y + mario.getSolidArea().y + mario.getSolidArea().height;
                    ligneBottomdanslaMatrice = posBottomenY / CONSTANTS.TAILLE_CELLULE;

                    switch (mario.getDirection()) {

                        // ici le "up" c'est en vrai le saut de mario , donc on check si mario est
                        // rentrer en collision avec un objet en sautant
                        case "up":
                            // on prédit ou sera notre mario aprés avoir bougé
                            ligneTopdanslaMatrice = (posTopenY + threadDescente.force) / CONSTANTS.TAILLE_CELLULE;
                            point1 = tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                            point2 = tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];

                            if (tm.tiles[point1].collision == true
                                    || tm.tiles[point2].collision == true) {
                                // On remet le joueur à l'emplacement juste en dessous de la brique. (pour
                                // éviter tout petit bug visuel)
                                mario.setPositionY((ligneTopdanslaMatrice + 1) * CONSTANTS.TAILLE_CELLULE);
                                // mario , logiquement n'est pas sur brick , innit mate ?
                                sur_brick = false;
                                // Je remets sa force à 0.
                                threadDescente.force = 0;

                                // Je lui permet de descendre.
                                this.mario.allowedToFallDown = true;
                            }
                            // Verification si point1 ou point2 est une pièce, si oui on incrémente le
                            // nombre de pièces
                            // Et on demande à la matrice (locale) d'être modifiée.
                            if (point1 == 30 || point2 == 30) {
                                scoreManager.incrementCurrentCoins();
                                scoreManager.incrementCurrentScore("coin");
                                tm.modifyMatrice(ligneTopdanslaMatrice, colonneLeftdanslaMatrice, 0);
                            }

                            // si mario rentre en collision avec une : brickPrize, cest-à-dire une brique
                            // qui donne une récompense : a coin or a mushroom .
                            // on va faire sortir un coin ou un champignon de la brique
                            // et on va la supprimer de la matrice
                            // TODO : faire sortir un champignon ou une pièce de la brique

                            if (point1 == CONSTANTS.PRIZE_BRICK || point2 == CONSTANTS.PRIZE_BRICK) {

                                // créer un objet (coin) et le faire sauter et redescendre sur la brique et
                                // +prize
                                // au score de mario
                                // et modifier la brique de la matrice pour qu'elle ne soit plus une brique de
                                // récompense.

                                coinToCatch = new Coin(
                                        new Point(colonneLeftdanslaMatrice, (ligneTopdanslaMatrice - 1)));
                                DescenteCoins coinThread = new DescenteCoins(coinToCatch);
                                coinThread.coinAllowedToFallDown = false;
                                jumpingThread.setThreadDecenteCoins(coinThread);
                                jumpingThread.jumpLaCoin();
                                coinThread.start();
                                scoreManager.incrementCurrentCoins();
                                scoreManager.incrementCurrentScore("coin");

                                tm.modifyMatrice(ligneTopdanslaMatrice, colonneLeftdanslaMatrice, 1);
                            }

                            if (point1 == CONSTANTS.MUSHROOW_BRICK || point2 == CONSTANTS.MUSHROOW_BRICK) {
                                Champignon champignon = new Champignon(null,
                                        new Point(colonneLeftdanslaMatrice * CONSTANTS.TAILLE_CELLULE,
                                                (ligneTopdanslaMatrice - 1) * CONSTANTS.TAILLE_CELLULE),
                                        tm.listGameCharacters_nextindex());
                                this.tm.addGameCharacter(champignon);
                                tm.modifyMatrice(ligneTopdanslaMatrice, colonneLeftdanslaMatrice, 1);
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
                            point1 = tm.tilesMatrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                            point2 = tm.tilesMatrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];

                            // verification si point1 ou point2 est une pièce
                            if ((point1 == 30)) {
                                logger.log(Level.INFO, "Coin collected from left with point1 !");
                                tm.modifyMatrice(ligneTopdanslaMatrice, colonneLeftdanslaMatrice, 0);
                                scoreManager.incrementCurrentCoins();
                                scoreManager.incrementCurrentScore("coin");
                            } else if (point2 == 30) {
                                logger.log(Level.INFO, "Coin collected from left with point2 !");
                                tm.modifyMatrice(ligneBottomdanslaMatrice, colonneLeftdanslaMatrice, 0);
                                scoreManager.incrementCurrentCoins();
                                scoreManager.incrementCurrentScore("coin");
                            }

                            if (tm.tiles[point1].collision == true
                                    || tm.tiles[point2].collision == true) {
                                mario.noMoving();
                            } else {
                                mario.yesMoving();
                            }
                            break;
                        case "right":

                            colonneRightdanslaMatrice = (posRightenX + CONSTANTS.INTERVALE_COLLISION) /
                                    CONSTANTS.TAILLE_CELLULE;

                            point1 = tm.tilesMatrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];
                            point2 = tm.tilesMatrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];

                            // verification si point1 ou point2 est une pièce
                            if ((point1 == 30)) {
                                logger.log(Level.INFO, "Coin collected from right with point1 !");
                                tm.modifyMatrice(ligneTopdanslaMatrice, colonneRightdanslaMatrice, 0);
                                scoreManager.incrementCurrentCoins();
                                scoreManager.incrementCurrentScore("coin");
                            } else if (point2 == 30) {
                                logger.log(Level.INFO, "Coin collected from right with point2 !");
                                tm.modifyMatrice(ligneBottomdanslaMatrice, colonneRightdanslaMatrice, 0);
                                scoreManager.incrementCurrentCoins();
                                scoreManager.incrementCurrentScore("coin");
                            }

                            if (tm.tiles[point1].collision == true || tm.tiles[point2].collision == true) {

                                mario.noMoving();
                            } else {
                                mario.yesMoving();
                            }
                            break;

                        default:
                            // System.out.println("chill");
                            break;

                    }

                    Iterator<Ennemi> iterator = this.tm.getListeEnnemis().iterator();
                    while (iterator.hasNext()) {
                        Ennemi ennemi = iterator.next();

                        // Mario's collision area
                        Rectangle marioHitbox = new Rectangle(
                                mario.getPosition().x + mario.getSolidArea().x,
                                mario.getPosition().y + mario.getSolidArea().y,
                                mario.getSolidArea().width,
                                mario.getSolidArea().height);

                        // Enemy's collision area
                        Rectangle ennemiHitbox = new Rectangle(
                                ennemi.getPosition().x + ennemi.getSolidArea().x,
                                ennemi.getPosition().y + ennemi.getSolidArea().y,
                                ennemi.getSolidArea().width,
                                ennemi.getSolidArea().height);

                        // collision avec les ennemis
                        if (marioHitbox.intersects(ennemiHitbox)) {
                            int marioFeetY = mario.getPosition().y + mario.getSolidArea().y
                                    + mario.getSolidArea().height;
                            int ennemiHeadY = ennemi.getPosition().y + ennemi.getSolidArea().y;

                            boolean fromAbove = marioFeetY <= ennemiHeadY + 15 && marioFeetY >= ennemiHeadY;
                            boolean falling = (threadDescente.force > 0);

                            boolean collisionHandled = false;

                            if (fromAbove && falling && !mario.isInvincible()) {
                                if (ennemi instanceof Koopa) {
                                    Koopa koopa = (Koopa) ennemi;
                                    if (koopa.getState() == Koopa.State.WALKING) {
                                        // koopa becomes a shell
                                        koopa.setState(Koopa.State.SHELL);

                                        koopa.position.y += 10;
                                        mario.setPositionY(mario.getPosition().y - 15);

                                        threadDescente.force = -CONSTANTS.IMPULSION_MARIO / 2;
                                    } else if (koopa.getState() == Koopa.State.SHELL) {
                                        // koopa is already a shell, once mario jumps on it, it will be removed
                                        iterator.remove();
                                        threadDescente.force = -CONSTANTS.IMPULSION_MARIO / 2;
                                    }
                                } else {
                                    // Goomba
                                    iterator.remove();
                                    threadDescente.force = -CONSTANTS.IMPULSION_MARIO / 2;
                                }
                                collisionHandled = true;
                            }

                            if (!collisionHandled) {
                                if (!mario.isInvincible()) {
                                    mario.perdreVie();
                                    if (mario.getViesMario() == 0) {
                                        System.out.println("Game over");
                                    }
                                }
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