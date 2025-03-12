package model;

import view.Ennemi;
import view.Mario;

import java.util.ArrayList;

public class Collision extends Thread {

    private Mario mario;
    private ArrayList<Ennemi> ennemi;

    Collision(){
        this.mario = Mario.getInstance();
        this.ennemi = CreateurObjets.getEnnemies();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (GameAffichage) {
                while (GamePanel.PAUSE) {
                    try {
                        GamePanel.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                    Thread.sleep(16);


                    // collision avec la map ou matrice de jeu

                    // on trouve les 4 points du rectangle qui vont check la collision
                    int posLeftenX = mario.getPosition().x + mario.getSolidArea().x;
                    int posRightenX = mario.getPosition().x + mario.getSolidArea().x + mario.getSolidArea().width;
                    int posTopenY = mario.getPosition().y + mario.getSolidArea().y;
                    int posBottomenY = mario.getPosition().y + mario.getSolidArea().y + mario.getSolidArea().height;

//                    // on trouve maintenant les lignes & colonnes ou se trouve les derniers coordonnées dans la MAtrice du jeu
//                    // car c'est avec ça qu'on saura que l'objet (arbre , terre , ... ) est solide ou pas
                    int ligneTopdanslaMatrice = posTopenY / CONSTANTS.TAILLE_CELLULE;
                    int ligneBottomdanslaMatrice = posBottomenY / CONSTANTS.TAILLE_CELLULE;
                    int colonneLeftdanslaMatrice = posLeftenX / CONSTANTS.TAILLE_CELLULE;
                    int colonneRightdanslaMatrice = posRightenX / CONSTANTS.TAILLE_CELLULE;

                    // on get maintenant en fonction de la direction de mario , le 2 points susceptibles à entrer en collision avec le monde externe ( matrice )
                    int point1 , point2 ;

                    switch (mario.getDirection()){
                        // ici le "up" c'est en vrai le saut de mario , donc on check si mario est rentrer en collision avec un objet en sautant
                        case "up":
                            // on prédit ou sera notre mario aprés avoir bougé
                            ligneTopdanslaMatrice = (posTopenY - LA_VALEUR_DU_SAUT) / CONSTANTS.TAILLE_CELLULE;
                            point1 = GamePanel.matrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                            point2 = GamePanel.matrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];

                            if(GamePanel.matrice[point1].collision == true || GamePanel.matrice[point2].collision == true){
                                // TODO : si le mur donne des rcompenses , mario les récupérera , sinon il s'arrête et avec l'effet de grivité redescend !
                            }
                            break;
                        case "down":
                            // same as for up , but we check the bottom of mario , not the top
                            // because mario can fall down a river or a hole , in which case he dies

                            ligneBottomdanslaMatrice = (posBottomenY + CONSTANTS.GRAVITEEE) / CONSTANTS.TAILLE_CELLULE;
                            point1 = GamePanel.matrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];
                            point2 = GamePanel.matrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];

                            if(GamePanel.matrice[point1].collision == true || GamePanel.matrice[point2].collision == true){
                                // TODO : mario dies , game stops if he falls down a hole or a river

                            }

                            break;
                        case "left":
                            // same as for up , but we check the left of mario , not the right
                            // because mario can hit a wall or a tree , in which case he stops moving
                            // or even get in collision with an ennemy , in which case he dies !

                            colonneLeftdanslaMatrice = (posLeftenX - mario.getSpeed()) / CONSTANTS.TAILLE_CELLULE;
                            point1 = GamePanel.matrice[ligneTopdanslaMatrice][colonneLeftdanslaMatrice];
                            point2 = GamePanel.matrice[ligneBottomdanslaMatrice][colonneLeftdanslaMatrice];

                            if(GamePanel.matrice[point1].collision == true || GamePanel.matrice[point2].collision == true){
                                // TODO : mario stops moving
                            }
                            break;
                        case "right":

                            colonneRightdanslaMatrice = (posRightenX + mario.getSpeed()) / CONSTANTS.TAILLE_CELLULE;
                            point1 = GamePanel.matrice[ligneTopdanslaMatrice][colonneRightdanslaMatrice];
                            point2 = GamePanel.matrice[ligneBottomdanslaMatrice][colonneRightdanslaMatrice];

                            if(GamePanel.matrice[point1].collision == true || GamePanel.matrice[point2].collision == true){
                                // TODO : mario stops moving
                            }
                            break;
                    }
                // TODO : + collision avec les ennemeies ,
                    for (int i = 0 ; i < GamePanel.monsters.length;i++){
                        if(GamePanel.monsters[i] != null){
                            // prendre le solid area de cet ennemi
                            GamePanel.monsters[i].solidArea.x = GamePanel.monsters[i].getPosition().x + GamePanel.monsters[i].solidArea.x;
                            GamePanel.monsters[i].solidArea.y = GamePanel.monsters[i].getPosition().y + GamePanel.monsters[i].solidArea.y;

                            // prendre le solid area de mario
                            mario.solidArea.x = mario.getPosition().x + mario.solidArea.x;
                            mario.solidArea.y = mario.getPosition().y + mario.solidArea.y;

                            // check si les 2 solid areas se touchent
                            switch (mario.getDirection()){
                               // il ne peut rentre en collison que en : down , left et right

                                case "down":
                                    mario.solidArea.y += CONSTANTS.GRAVITEEE;
                                    if(mario.solidArea.intersects(GamePanel.monsters[i].solidArea)){
                                        // TODO : l'ennemi meurt : creer une methode qui tue l'ennemi
                                    }
                                    break;
                                case "left":
                                    mario.solidArea.x -= mario.getSpeed();
                                    if(mario.solidArea.intersects(GamePanel.monsters[i].solidArea)){
                                        // TODO : mario meurt : creer une méthode qui tue mario et arrete le jeu
                                    }
                                    break;
                                case "right":
                                    mario.solidArea.x += mario.getSpeed();
                                    if(mario.solidArea.intersects(GamePanel.monsters[i].solidArea)){
                                        // TODO : mario meurt
                                    }
                                    break;
                            }
                            // reset the solid area of mario to its original value and the solid area of the ennemi to its original value
                            mario.solidArea.x = CONSTANTS.slidAreaDefaultX;
                            mario.solidArea.y = CONSTANTS.slidAreaDefaultY;
                            GamePanel.monsters[i].solidArea.x = CONSTANTS.slidAreaDefaultX;
                            GamePanel.monsters[i].solidArea.y = CONSTANTS.slidAreaDefaultY;


                        }


                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
