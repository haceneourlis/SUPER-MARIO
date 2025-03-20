package modele.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import modele.CONSTANTS;
import modele.Mario;

/**
 * Classe qui gère les tuiles du jeu
 * Elle charge les images des tuiles, et offre les "outils" nécessaires pour qu'elles
 * soient affichées dans la vue correctement.
 * Elle charge complétement la matrice du jeu, et fait correspondre chaque case de la matrice
 * avec une tuile.
 */

public class TileManager extends Thread{

    // constante de DELAY pour le thread
    private static final int DELAY = 16;

    // instance de Mario pour acceder à sa position dans le modele.
    public Mario mario;

    // un tableau de tuiles (chaque tuile est un objet de la classe Tile)
    public Tile[] tiles;

    // matrice du jeu qui sera importée depuis un fichier texte
    // elle aura pour obligation d'avoir le meme nombre de lignes 
    // que la matrice affichée
    // mais elle peut varier en nombre de colonnes
    public int[][] tilesMatrice;

    // décalage de mario (en nombre de colonnes) par rapport à une certaine colonne appelée "colonne de scrolling" 
    private int decalage = 0;

    // nombre de colonnes de la matrice du jeu, sera initialisé dans la méthode loadMatrice
    private int maxColLevel = 0;

    // Variable qui définit le "start" pour que l'affichage puisse dessiner
    // les tuiles à partir de cette colonne
    private int start = 0;

    // Variable qui définit le "end" pour que l'affichage puisse dessiner
    // les tuiles jusqu'à cette colonne
    private int end = CONSTANTS.maxCol_affichage_gameMatrix;

    
    public TileManager() {
        // On récupère l'instance du Joueur
        this.mario = Mario.getInstance();

        // J'ai mis 64 tuiles ici, mais on peut adapter plus tard
        tiles = new Tile[64];

        // méthode qui va juste charger les images et les mettres dans le tableau de tuiles
        getTileImage();

        // méthode qui va charger la matrice du jeu dans la matrice tilesMatrice
        loadMatrice("/resources/matrice.txt");

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
            tiles[0].collision = false;

            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/brick.png"));
            tiles[1].collision = true;

            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/obstacle1.png"));
            tiles[2].collision = true;

            tiles[3] = new Tile();
            tiles[3].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/gros_buisson/gros_buisson1.png"));
            tiles[3].collision = false;
            tiles[4] = new Tile();
            tiles[4].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/gros_buisson/gros_buisson2.png"));
            tiles[4].collision = false;
            tiles[5] = new Tile();
            tiles[5].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/gros_buisson/gros_buisson3.png"));
            tiles[5].collision = false;
            tiles[6] = new Tile();
            tiles[6].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/gros_buisson/gros_buisson4.png"));
            tiles[6].collision = false;
            tiles[7] = new Tile();
            tiles[7].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/gros_buisson/gros_buisson5.png"));
            tiles[7].collision = false;
            tiles[8] = new Tile();
            tiles[8].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/gros_buisson/gros_buisson6.png"));
            tiles[8].collision = false;
            tiles[9] = new Tile();
            tiles[9].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/gros_buisson/gros_buisson7.png"));
            tiles[9].collision = false;
            tiles[10] = new Tile();
            tiles[10].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/gros_buisson/gros_buisson8.png"));
            tiles[10].collision = false;
            tiles[11] = new Tile();
            tiles[11].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/gros_buisson/gros_buisson9.png"));
            tiles[11].collision = false;
            tiles[12] = new Tile();
            tiles[12].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/petit_buisson/petit_buisson1.png"));
            tiles[12].collision = false;
            tiles[13] = new Tile();
            tiles[13].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/petit_buisson/petit_buisson2.png"));
            tiles[13].collision = false;
            tiles[14] = new Tile();
            tiles[14].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/petit_buisson/petit_buisson3.png"));
            tiles[14].collision = false;
            tiles[15] = new Tile();
            tiles[15].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/petit_buisson/petit_buisson4.png"));
            tiles[15].collision = false;
            tiles[16] = new Tile();
            tiles[16].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/tuyau/tuyau_1.png"));
            tiles[16].collision = false;

            tiles[17] = new Tile();
            tiles[17].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/tuyau/tuyau_2.png"));
            tiles[17].collision = false;

            tiles[18] = new Tile();
            tiles[18].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/tuyau/tuyau_3.png"));
            tiles[18].collision = false;

            tiles[19] = new Tile();
            tiles[19].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/tuyau/tuyau_4.png"));
            tiles[19].collision = false;

            tiles[20] = new Tile();
            tiles[20].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/nuage/cloud1.png"));
            tiles[20].collision = false;

            tiles[21] = new Tile();
            tiles[21].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/nuage/cloud2.png"));
            tiles[21].collision = false;

            tiles[22] = new Tile();
            tiles[22].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/nuage/cloud3.png"));
            tiles[22].collision = false;

            tiles[23] = new Tile();
            tiles[23].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/nuage/cloud4.png"));
            tiles[23].collision = false;

            tiles[24] = new Tile();
            tiles[24].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/nuage/cloud5.png"));
            tiles[24].collision = false;

            tiles[25] = new Tile();
            tiles[25].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/nuage/cloud6.png"));
            tiles[25].collision = false;

            tiles[26] = new Tile();
            tiles[26].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/nuage/cloud7.png"));
            tiles[26].collision = false;

            tiles[27] = new Tile();
            tiles[27].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/nuage/cloud8.png"));
            tiles[27].collision = false;
            
            tiles[28] = new Tile();
            tiles[28].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/nuage/cloud9.png"));
            tiles[28].collision = false;

            tiles[29] = new Tile();
            tiles[29].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/nuage/cloud10.png"));
            tiles[29].collision = false;
            
           
            // TODO : la suite
        } catch (Exception e) {
            System.err.println("Erreur");
        }
    }


        

        // méthode de lancement du thread
        @Override
        public void run() {
                while (true) {
                try {
                        Thread.sleep(DELAY);
                        // récupère la case actuelle où se trouve mario
                        int case_actuelle = (this.mario.getPositionX() / CONSTANTS.TAILLE_CELLULE);

                        // On vérifie si sa case actuelle ne dépasse pas une certaine limite de scrolling
                        // On rajoute le décalage à cette limite car elle est relative à la position de mario (elle se déplace aussi)
                        if (case_actuelle > (CONSTANTS.CELLULE_SCROLLING + this.decalage)) {
                                // si c'est le cas, on incrémente le décalage
                                this.decalage = this.decalage + 1;
                        }
                        // On met à jour le start et end
                        this.start = this.decalage;
                        this.end = this.decalage + CONSTANTS.maxCol_affichage_gameMatrix;

                        if (start >= this.maxColLevel || end >= this.maxColLevel) {
                                this.decalage = 0;
                                this.start = this.decalage;
                                this.end = this.decalage + this.maxColLevel;
                        }

                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                }
        }


    

    /**
     * Cette méthode charge la matrice du jeu depuis un fichier texte
     * Elle prend en paramètre le chemin du fichier texte
     * Elle initialise la matrice tilesMatrice
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
            tilesMatrice = new int[modele.CONSTANTS.maxRow_affichage_gameMatrix][numbers.length];
            // et on sauvegarde la taille en colonne
            this.maxColLevel = numbers.length;

            // On lit le fichier ligne par ligne et on rempli la matrice
            while (col < numbers.length && row < CONSTANTS.maxRow_affichage_gameMatrix) {
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
    // getter de start
        public int getStart() {
                return this.start;
        }
        // getter de end
        public int getEnd() {
                return this.end;
        }
    
}
