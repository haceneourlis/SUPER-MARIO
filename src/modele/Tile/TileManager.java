package modele.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import modele.*;

/**
 * Classe qui gère les tuiles du jeu
 * Elle charge les images des tuiles et les mets dans un tableau.
 * Elle charge complétement la matrice du jeu, et fait correspondre chaque case
 * de la matrice avec une tuile.
 * Elle charge également les différents ennemis présents sur la map.
 */

public class TileManager {

        // attribut de Mario pour acceder à sa position dans le modele.
        public Mario mario;

        // un tableau de tuiles (chaque tuile est un objet de la classe Tile).
        public Tile[] tiles;

        // matrice du jeu qui sera importée depuis un fichier texte.
        public int[][] tilesMatrice;

        // instance unique de la classe TileManager (singleton).
        private static TileManager instance = null;

        // décalage de mario (en nombre de colonnes) par rapport à une certaine colonne
        // appelée "colonne de scrolling"
        public int decalage = 0;

        // nombre de colonnes de la matrice du jeu, sera initialisé dans la méthode
        // loadMatrice
        public int maxColLevel = 0;

        // Une liste d'ennemis présents sur la map.
        public ArrayList<Ennemi> listeEnnemis;
        private Ennemi kp1, kp2, gb1, gb2, gb3;

        // La liste des entitées présentes sur la map. (ex : champignons)
        private ArrayList<GameCharacter> listeEntities;

        private TileManager() {
                // On récupère l'instance du Joueur
                this.mario = Mario.getInstance();

                // J'ai mis 64 tuiles ici, mais on peut adapter plus tard
                tiles = new Tile[64];

                // On initialise la liste d'ennemis.
                listeEnnemis = new ArrayList<>();

                // On initialise la liste d'entitées.
                listeEntities = new ArrayList<>();

                // méthode qui va juste charger les images et les mettres dans le tableau de
                // tuiles
                getTileImage();

                // méthode qui va charger la matrice du jeu dans la matrice tilesMatrice
                loadMatrice_1();
                loadEnnemis_1();
        }

        /**
         * Méthode qui retourne l'instance unique de la classe TileManager
         * 
         * @return l'instance unique de la classe TileManager
         */
        public static TileManager getInstance() {
                if (instance == null) {
                        // Si l'instance n'existe pas, on la crée
                        instance = new TileManager();
                }
                return instance;
        }

        /**
         * Cette méthode retourne la liste des ennemis présents sur la map.
         * 
         * @return la liste des ennemis présents sur la map.
         */
        public ArrayList<Ennemi> getListeEnnemis() {
                return listeEnnemis;
        }

        public void eraseALLEntities() {
                for (int i = 0; i < listeEntities.size(); i++) {
                        listeEntities.remove(i);
                        i--;
                }
        }

        public void loadMatrice_1() {
                // On recharge la matrice du jeu
                load("/resources/matrice.txt");
        }

        public void loadEnnemis_1() {
                // On recharge les ennemis

                // supprimer tous les ennemis de la liste
                for (int i = 0; i < listeEnnemis.size(); i++) {
                        listeEnnemis.remove(i);
                        i--;
                }
                // On recharge les ennemis
                kp1 = new Koopa(400, 4, true, this);
                this.addEnnemi(kp1);
                kp1.thread.start();

                kp2 = new Koopa(90 * CONSTANTS.TAILLE_CELLULE, 4, true, this);
                this.addEnnemi(kp2);
                kp2.thread.start();

                gb1 = new Goomba(100 * CONSTANTS.TAILLE_CELLULE, 4, false, this);
                this.addEnnemi(gb1);
                gb1.thread.start();

                gb2 = new Goomba(500, 3, false, this);
                this.addEnnemi(gb2);
                gb2.thread.start();

                gb3 = new Goomba(150, 3, false, this);
                this.addEnnemi(gb3);
                gb3.thread.start();

                System.out.println("Ennemis rechargés !:::::::::::::::::::::::::::!::::::::::::::");
        }

        /**
         * Cette méthode prend un ennemi en paramètre et l'ajoute à la liste des
         * ennemis.
         * 
         * @param ennemi
         * @return no return
         */
        public void addEnnemi(Ennemi ennemi) {
                this.listeEnnemis.add(ennemi);
        }

        /**
         * cette méthode supprime un ennemi de la liste des ennemis
         * 
         * @param ennemi
         * @return no return
         */
        public void removeEnnemi(Ennemi ennemi) {
                this.listeEnnemis.remove(ennemi);
        }

        /**
         * Cette méthode rend la ième entité dans la liste des entités.
         * 
         * @param i
         * @return l'entité ième
         */
        public GameCharacter getListEntities(int i) {
                return this.listeEntities.get(i);
        }

        /**
         * Cette méthode prend une entity de type GameCharacter et la met dans la liste
         * des entitées.
         * 
         * @param gc
         * @return no return
         */
        public void addEntityToList(GameCharacter gc) {
                this.listeEntities.add(gc);

        }

        /**
         * Cette méthode prend une entity de type GameCharacter et la retire de la liste
         * des entitées.
         * 
         * @param gc
         * @return no return
         */
        public void removeEntityFromList(GameCharacter gc) {
                this.listeEntities.remove(gc);
        }

        /**
         * Cette méthode retourne la taille de la liste des entitées.
         * 
         * @return int size de la liste des entitées.
         */
        public int sizeEntitiesList() {
                return this.listeEntities.size();
        }

        /**
         * Cette méthode ne fait que charger les tiles dans le tableau de tuiles
         * Elle est appelée dans le constructeur de la classe
         * Je ne vais pas détailler le code, car c'est juste de la lecture d'images
         */
        public void getTileImage() {
                try {
                        tiles[0] = new Tile();
                        tiles[0].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/sky.png"));

                        tiles[1] = new Tile();
                        tiles[1].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/brick.png"));
                        tiles[1].collision = true;

                        tiles[2] = new Tile();
                        tiles[2].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/brickPrize.png"));
                        tiles[2].collision = true;

                        tiles[3] = new Tile();
                        tiles[3].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/gros_buisson/gros_buisson1.png"));

                        tiles[4] = new Tile();
                        tiles[4].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/gros_buisson/gros_buisson2.png"));

                        tiles[5] = new Tile();
                        tiles[5].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/gros_buisson/gros_buisson3.png"));

                        tiles[6] = new Tile();
                        tiles[6].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/gros_buisson/gros_buisson4.png"));

                        tiles[7] = new Tile();
                        tiles[7].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/gros_buisson/gros_buisson5.png"));

                        tiles[8] = new Tile();
                        tiles[8].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/gros_buisson/gros_buisson6.png"));

                        tiles[9] = new Tile();
                        tiles[9].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/gros_buisson/gros_buisson7.png"));

                        tiles[10] = new Tile();
                        tiles[10].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/gros_buisson/gros_buisson8.png"));

                        tiles[11] = new Tile();
                        tiles[11].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/gros_buisson/gros_buisson9.png"));

                        tiles[12] = new Tile();
                        tiles[12].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/petit_buisson/petit_buisson1.png"));

                        tiles[13] = new Tile();
                        tiles[13].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/petit_buisson/petit_buisson2.png"));

                        tiles[14] = new Tile();
                        tiles[14].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/petit_buisson/petit_buisson3.png"));

                        tiles[15] = new Tile();
                        tiles[15].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/petit_buisson/petit_buisson4.png"));

                        tiles[16] = new Tile();
                        tiles[16].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/tuyau/tuyau_1.png"));
                        tiles[16].collision = true;
                        tiles[17] = new Tile();
                        tiles[17].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/tuyau/tuyau_2.png"));
                        tiles[17].collision = true;
                        tiles[18] = new Tile();
                        tiles[18].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/tuyau/tuyau_3.png"));
                        tiles[18].collision = true;
                        tiles[19] = new Tile();
                        tiles[19].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/tuyau/tuyau_4.png"));
                        tiles[19].collision = true;
                        tiles[20] = new Tile();
                        tiles[20].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/nuage/cloud1.png"));

                        tiles[21] = new Tile();
                        tiles[21].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/nuage/cloud2.png"));

                        tiles[22] = new Tile();
                        tiles[22].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/nuage/cloud3.png"));

                        tiles[23] = new Tile();
                        tiles[23].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/nuage/cloud4.png"));

                        tiles[24] = new Tile();
                        tiles[24].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/nuage/cloud5.png"));

                        tiles[25] = new Tile();
                        tiles[25].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/nuage/cloud6.png"));

                        tiles[26] = new Tile();
                        tiles[26].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/nuage/cloud7.png"));

                        tiles[27] = new Tile();
                        tiles[27].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/nuage/cloud8.png"));

                        tiles[28] = new Tile();
                        tiles[28].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/nuage/cloud9.png"));

                        tiles[29] = new Tile();
                        tiles[29].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/nuage/cloud10.png"));

                        tiles[30] = new Tile();
                        tiles[30].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/coin.png"));
                        tiles[31] = new Tile();
                        tiles[31].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/brickPrize.png"));
                        tiles[31].collision = true;

                        tiles[32] = new Tile();
                        tiles[32].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/drapeau/drapeau1.png"));
                        tiles[32].collision = true;
                        tiles[33] = new Tile();
                        tiles[33].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/drapeau/drapeau2.png"));
                        tiles[33].collision = true;
                        tiles[34] = new Tile();
                        tiles[34].image = ImageIO.read(getClass()
                                        .getResourceAsStream("/resources/drapeau/drapeau3.png"));
                        tiles[34].collision = true;

                } catch (Exception e) {
                        System.err.println("Erreur");
                }
        }

        /**
         * Cette méthode charge la matrice du jeu depuis un fichier texte
         * Elle prend en paramètre le chemin du fichier texte
         * Elle initialise la matrice tilesMatrice
         * 
         * @param pathToMatrice
         */
        public void load(String pathToMatrice) {
                try {
                        // On charge le fichier texte
                        InputStream is = getClass().getResourceAsStream(pathToMatrice);
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));

                        // On initialise la colonne et la ligne à 0
                        int col = 0;
                        int row = 0;

                        // On lit la première ligne pour récupérer la taille en colonne de la matrice
                        String line = br.readLine();
                        String numbers[] = line.split(" ");

                        // et on initialise la matrice avec la taille en colonne
                        tilesMatrice = new int[CONSTANTS.maxRow_gameMatrix][numbers.length];
                        // et on sauvegarde la taille en colonne
                        this.maxColLevel = numbers.length;

                        // On lit le fichier ligne par ligne et on rempli la matrice
                        while (col < numbers.length && row < CONSTANTS.maxRow_gameMatrix) {
                                while (col < numbers.length) {
                                        numbers = line.split(" ");
                                        int num = Integer.parseInt(numbers[col]);
                                        tilesMatrice[row][col] = num;
                                        col++;
                                }
                                line = br.readLine();
                                // Si on arrive en fin de colonne, on passe à la ligne suivante
                                if (col == numbers.length) {
                                        col = 0;
                                        row++;
                                }
                        }
                        br.close();

                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
        }

        /**
         * Cette méthode permet de modifier une case de la matrice avec une valeur
         * donnée.
         * 
         * @param row
         * @param col
         * @param value
         */
        public void modifyMatrice(int row, int col, int value) {
                tilesMatrice[row][col] = value;
        }

}