
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

import java.util.Iterator;
import java.util.List;
import java.awt.Rectangle;

public class Collision extends Thread {

    private Mario mario;
    private List<Ennemi> ennemis;

    private Affichage gp;
    private Jumping jumpingThread;
    private Descente threadDescente;

    private int previousMarioFeetY = Mario.getInstance().getPosition().y
            + Mario.getInstance().getSolidArea().y
            + Mario.getInstance().getSolidArea().height;

    private Coin coin;

    protected boolean sur_brick = false;

    private static final Logger logger = Logger.getLogger(Collision.class.getName());

    public Collision(Affichage affichage, Jumping jumpingThread, Descente threadDescente, Coin coin) {
        gp = affichage;
        this.mario = Mario.getInstance();
        this.ennemis = gp.getEnnemis();
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

                mario.updateInvincibility();

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

                    // Enemy's collision area
                    Rectangle ennemiHitbox = new Rectangle(
                            ennemi.getPosition().x + ennemi.getSolidArea().x,
                            ennemi.getPosition().y + ennemi.getSolidArea().y,
                            ennemi.getSolidArea().width,
                            ennemi.getSolidArea().height
                    );

//                    // collision avec les ennemis
//                    if(marioHitbox.intersects(ennemiHitbox)) {
//                        // mario's feet position
//                        int marioFeetY = mario.getPosition().y + mario.getSolidArea().y + mario.getSolidArea().height;
//                        // ennemi's head position
//                        int ennemiHeadY = ennemi.getPosition().y + ennemi.getSolidArea().y;
//
//                        // check if mario jumps exactement on the ennemi
//                        boolean fromAbove = marioFeetY <= ennemiHeadY + 10 && marioFeetY >= ennemiHeadY;
//                        // check if mario is falling
//                        boolean falling = marioFeetY > previousMarioFeetY;
//
//                        // i've added 3 conditions to check if mario is above the ennemi and if he is falling
//                        // whether mario is invincible or not, he has to jump FROM ABOVE and his direction has to be DOWN to kill the ennemi
//                        if(fromAbove && falling && mario.getDirection().equals("down")) {
//                            //mario peut tuer que si il n'est pas invincible
//                            if (!mario.isInvincible()) {
//
//                            System.out.println("Mario kills enemy");
//                            iterator.remove();
//                            threadDescente.force = -jumpingThread.IMPULSION / 2;
//                            }
//                        } else {
//                            // if mario is not jumping on the ennemi, only when mario is not invincible, he will lose a life
//                            if(!mario.isInvincible()){
//                                mario.perdreVie();
//                                if(mario.getVies() == 0) {
//                                    // TODO : restart the game or show game over screen
//                                    // show a message in the screen that mario died
//                                    // and stop the game
//                                    logger.log(Level.INFO, "mario died");
//                                    threadDescente.setSol(CONSTANTS.LE_SOL * 2);
//                                    System.out.println("Game over");
//                                }
//                            } else {
//                                // if mario is invincible, no damage is taken
//                                System.out.println("Mario loses a life, invincible time");
//                            }
//                        }
//                    }



                    // 如果发生碰撞
                    // FIXME: 踩到koopa时会掉命

//                    if(marioHitbox.intersects(ennemiHitbox)) {
//                        int marioFeetY = mario.getPosition().y + mario.getSolidArea().y + mario.getSolidArea().height;
//                        int ennemiHeadY = ennemi.getPosition().y + ennemi.getSolidArea().y;
//
//                        boolean fromAbove = marioFeetY <= ennemiHeadY+15 && marioFeetY >= ennemiHeadY;
//                        // boolean falling = marioFeetY > previousMarioFeetY;
//                        boolean falling = (threadDescente.force > 0);
//
//                        if(fromAbove && falling && mario.getDirection().equals("down")) {
//                            if (!mario.isInvincible()){
//                                // 如果敌人是 Koopa，则根据当前状态进行处理
//                                if(ennemi instanceof Koopa) {
//                                    Koopa koopa = (Koopa) ennemi;
//                                    if(koopa.getState() == Koopa.State.WALKING) {
//                                        // 第一次踩：将 Koopa 状态设为 SHELL
//                                        koopa.setState(Koopa.State.SHELL);
//                                        // 给马里奥一个向上的力，模拟踩敌人后的反弹
//                                        threadDescente.force = -jumpingThread.IMPULSION / 2;
//                                    } else if(koopa.getState() == Koopa.State.SHELL) {
//                                        // 第二次踩：直接移除敌人
//                                        iterator.remove();
//                                        threadDescente.force = -jumpingThread.IMPULSION / 2;
//                                    }
//                                } else {
//                                    // 对于其他敌人（例如 Goomba），直接移除
//                                    iterator.remove();
//                                    threadDescente.force = -jumpingThread.IMPULSION / 2;
//                                }
//                            }
//                        } else {
//                            if(!mario.isInvincible()){
//                                mario.perdreVie();
//                                if(mario.getVies() == 0) {
//                                    // 处理 Game Over 状态
//                                    threadDescente.setSol(CONSTANTS.LE_SOL * 2);
//                                    System.out.println("Game over");
//                                }
//                            }
//                        }
//                    }


                        // FIXME: 这个版本踩不死敌人
//                    if(marioHitbox.intersects(ennemiHitbox)) {
//                        // 计算 Mario 脚底和敌人头部的 Y 坐标
//                        int marioFeetY = mario.getPosition().y + mario.getSolidArea().y + mario.getSolidArea().height;
//                        int ennemiHeadY = ennemi.getPosition().y + ennemi.getSolidArea().y;
//
//                        // 设置一个阈值，例如允许15像素内认为是踩到了敌人
//                        int stompThreshold = 15;
//                        // 判断 Mario 是否从上方踩到敌人（Mario 的脚部距离敌人头部在 0~stompThreshold 之间）
//                        boolean fromAbove = (marioFeetY - ennemiHeadY >= 0) && (marioFeetY - ennemiHeadY <= stompThreshold);
//                        // 利用 Descente 对象判断 Mario 是否在下落（force > 0 表示正在下落）
//                        // boolean falling = (threadDescente.force > 0);
//                        boolean falling = (mario.getPosition().y > previousMarioFeetY);
//
//                        // FIXME:
//                        System.out.println("marioFeetY - ennemiHeadY = " + (marioFeetY - ennemiHeadY));
//                        System.out.println("force = " + threadDescente.force);
//
//                        // 如果满足从上方踩并且处于下落状态，则执行踩敌逻辑
//                        boolean stomped = false;
//                        if(fromAbove && falling) {
//                            stomped = true;
//                            if (!mario.isInvincible()){
//                                if(ennemi instanceof Koopa) {
//                                    Koopa koopa = (Koopa) ennemi;
//                                    if(koopa.getState() == Koopa.State.WALKING) {
//                                        // 第一次踩：将 Koopa 状态设为 SHELL，不扣命
//                                        koopa.setState(Koopa.State.SHELL);
//                                        threadDescente.force = -jumpingThread.IMPULSION / 2;  // 给 Mario 弹跳效果
//                                    } else if(koopa.getState() == Koopa.State.SHELL) {
//                                        // 第二次踩：直接移除敌人
//                                        iterator.remove();
//                                        threadDescente.force = -jumpingThread.IMPULSION / 2;
//                                    }
//                                } else {
//                                    // 对其他敌人（如 Goomba）直接移除
//                                    iterator.remove();
//                                    threadDescente.force = -jumpingThread.IMPULSION / 2;
//                                }
//                            }
//                        }
//
//                        // 如果未满足有效踩敌条件，则视为碰撞，扣除一条命
//                        if(!stomped) {
//                            if(!mario.isInvincible()){
//                                mario.perdreVie();
//                                if(mario.getVies() == 0) {
//                                    threadDescente.setSol(CONSTANTS.LE_SOL * 2);
//                                    System.out.println("Game over");
//                                }
//                            }
//                        }
//                    }


                    // FIXME: 最新版本
                    // 如果发生碰撞
                    if(marioHitbox.intersects(ennemiHitbox)) {
                        int marioFeetY = mario.getPosition().y + mario.getSolidArea().y + mario.getSolidArea().height;
                        int ennemiHeadY = ennemi.getPosition().y + ennemi.getSolidArea().y;

                        boolean fromAbove = marioFeetY <= ennemiHeadY + 15 && marioFeetY >= ennemiHeadY;
                        boolean falling = (threadDescente.force > 0);
                        // (或 boolean falling = (mario.getPosition().y > previousMarioFeetY); )

                        // 标记本次碰撞是否已处理完毕
                        boolean collisionHandled = false;

                        if(fromAbove && falling && !mario.isInvincible()) {
                            // 只要满足踩敌条件，就执行踩敌逻辑
                            if(ennemi instanceof Koopa) {
                                Koopa koopa = (Koopa) ennemi;
                                if(koopa.getState() == Koopa.State.WALKING) {
                                    // 变龟壳
                                    koopa.setState(Koopa.State.SHELL);
                                    // 把 Koopa 稍微上移或左右移，让它不再跟 Mario 重叠
                                    koopa.position.y += 10;

                                    // **新增：将 Mario 立即上移，防止重叠触发扣命**
                                    mario.setPositionY(mario.getPosition().y - 15);

                                    // 重新更新 solidArea(如果壳子尺寸不同):
                                    // koopa.solidArea.x = 0; koopa.solidArea.y = 0; ...
                                    // 给Mario反弹
                                    threadDescente.force = -jumpingThread.IMPULSION / 2;
                                } else if(koopa.getState() == Koopa.State.SHELL) {
                                    // 二踩，直接移除
                                    iterator.remove();
                                    threadDescente.force = -jumpingThread.IMPULSION / 2;
                                }
                            } else {
                                // Goomba等其它敌人
                                iterator.remove();
                                threadDescente.force = -jumpingThread.IMPULSION / 2;
                            }
                            collisionHandled = true;
                        }

                        // 若本次碰撞未被踩敌逻辑处理，则扣命
                        if(!collisionHandled) {
                            if(!mario.isInvincible()){
                                mario.perdreVie();
                                if(mario.getVies() == 0) {
                                    threadDescente.setSol(CONSTANTS.LE_SOL * 2);
                                    System.out.println("Game over");
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