package modele.Tile;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import modele.CONSTANTS;
import modele.Ennemi;
import modele.Goomba;
import modele.Koopa;
import modele.Mario;

/**
 * Classe qui gère les tuiles du jeu
 * Elle charge les images des tuiles, et offre les "outils" nécessaires pour
 * qu'elles
 * soient affichées dans la vue correctement.
 * Elle charge complétement la matrice du jeu, et fait correspondre chaque case
 * de la matrice
 * avec une tuile.
 */

public class TileManager {

        // instance de Mario pour acceder à sa position dans le modele.
        public Mario mario;

        // un tableau de tuiles (chaque tuile est un objet de la classe Tile)
        public Tile[] tiles;

        // matrice du jeu qui sera importée depuis un fichier texte
        public int[][] tilesMatrice;

        // instance unique de la classe TileManager (singleton)
        private static TileManager instance = null;

        // décalage de mario (en nombre de colonnes) par rapport à une certaine colonne
        // appelée "colonne de scrolling"
        private int decalage = 0;

        // nombre de colonnes de la matrice du jeu, sera initialisé dans la méthode
        // loadMatrice
        private int maxColLevel = 0;

        private ArrayList<Ennemi> listeEnnemis;
        private Ennemi koopa, goomba;

        private TileManager() {
                // On récupère l'instance du Joueur
                this.mario = Mario.getInstance();

                // J'ai mis 64 tuiles ici, mais on peut adapter plus tard
                tiles = new Tile[64];

                listeEnnemis = new ArrayList<>();
                // Ajouter plusieurs ennemis
                // Ajouter plusieurs ennemis
                koopa = new Koopa(600, 5, true, this);
                goomba = new Goomba(300, 4, true, this);
                listeEnnemis.add(koopa);
                listeEnnemis.add(goomba);
                // méthode qui va juste charger les images et les mettres dans le tableau de
                // tuiles
                getTileImage();

                // méthode qui va charger la matrice du jeu dans la matrice tilesMatrice
                loadMatrice("/resources/matrice.txt");
        }

        public static TileManager getInstance() {
                if (instance == null) {
                        // Si l'instance n'existe pas, on la crée
                        instance = new TileManager();

                }
                return instance;
        }

        public ArrayList<Ennemi> getListeEnnemis() {
                return listeEnnemis;
        }

        public void addEnnemi(Ennemi ennemi) {
                this.listeEnnemis.add(ennemi);
        }

        public Ennemi getKoopa() {
                return koopa;
        }

        public Ennemi getGoomba() {
                return goomba;
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
                        tiles[30].collision = false;
                        // TODO : la suite
                } catch (Exception e) {
                        System.err.println("Erreur");
                }
        }

        /**
         * Cette méthode dessine les tuiles du jeu
         * Elle prend en paramètre un objet Graphics2D
         * Elle dessine les tuiles en fonction du décalage de mario
         * Elle sera appelée par la Vue, demander au prof si c'est correct en terme de
         * MVC
         */
        public void draw(Graphics2D g2) {
                // récupère la case actuelle où se trouve mario
                int case_actuelle = (this.mario.getPositionX() / CONSTANTS.TAILLE_CELLULE);

                // On vérifie si sa case actuelle ne dépasse pas une certaine limite de
                // scrolling
                // On rajoute le décalage à cette limite car elle est relative à la position de
                // mario (elle se déplace aussi)
                if (case_actuelle > (CONSTANTS.CELLULE_SCROLLING + this.decalage)) {
                        // si c'est le cas, on incrémente le décalage
                        this.decalage = this.decalage + 1;
                }

                // la col ici représente la colonne de la matrice du jeu à partir de laquelle on
                // va afficher les tuiles
                // elle commence du nombre de décalage qu'on a, car 1 décalage = 1 colonne qui
                // sort de l'écran
                // donc 1 colonne qu'on ne veut plus afficher
                int col = decalage;

                // la ligne ici représente la ligne de la matrice du jeu à partir de laquelle on
                // va afficher les tuiles
                int row = 0;

                // la position en x où on va commencer à dessiner la tuile sur la fenetre
                // Comme le plan ne se déplace pas, on doit incrémenter à chaque fois la
                // position en x de début de dessin, en fonction du décalage de mario
                int x = decalage * CONSTANTS.TAILLE_CELLULE;
                int y = 0;
                Point point_dans_modele = new Point(x, y);

                // On vérifie que la col n'est pas soit en dehors du champs visible de la
                // fenetre (le maxCol_gameMatrix)
                // ou alors que le col est en dehors ou pas de la matrice du jeu chargée du
                // fichier texte.
                while (col < (modele.CONSTANTS.maxCol_gameMatrix + decalage) && col < this.maxColLevel
                                && row < modele.CONSTANTS.maxRow_gameMatrix) {

                        // On récupère le type de la tuile à afficher qui correspond à un indice dans le
                        // tableau de tuiles
                        int TileType = tilesMatrice[row][col];

                        // Ici, le point dans le modele est le meme que dans la vue donc ça ne change
                        // rien
                        point_dans_modele.x = x;
                        point_dans_modele.y = y;

                        // On dessine la tuile
                        g2.drawImage(tiles[TileType].image, point_dans_modele.x, point_dans_modele.y, null);

                        // on incrémente la colonne et la position en x
                        col++;
                        x += CONSTANTS.TAILLE_CELLULE;

                        // si on arrive à la fin de la ligne qu'on peut afficher ou si on arrive à la
                        // fin de la matrice du jeu
                        // on incrémente la ligne et on réinitialise la colonne et la position en x

                        if (col == (CONSTANTS.maxCol_gameMatrix + decalage) || col >= this.maxColLevel) {
                                col = decalage;
                                row++;
                                y += CONSTANTS.TAILLE_CELLULE;
                                x = decalage * CONSTANTS.TAILLE_CELLULE;
                        }

                }

        }

        /**
         * Cette méthode charge la matrice du jeu depuis un fichier texte
         * Elle prend en paramètre le chemin du fichier texte
         * Elle initialise la matrice tilesMatrice
         * 
         * @param pathToMatrice
         */
        public void loadMatrice(String pathToMatrice) {
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
                        tilesMatrice = new int[modele.CONSTANTS.maxRow_gameMatrix][numbers.length];
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

        public void modifyMatrice(int row, int col, int value) {
                tilesMatrice[row][col] = value;
        }
}
