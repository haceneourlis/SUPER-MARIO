package modele.Tile;

import modele.CONSTANTS;
import vue.Affichage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    Affichage gp;
    public Tile[] tiles;
    public int[][] tilesMatrice;

    public TileManager(Affichage gp) {
        this.gp = gp;

        tiles = new Tile[10];
        tilesMatrice = new int[modele.CONSTANTS.maxScreenRow][modele.CONSTANTS.maxScreenCol];

        getTileImage();
        loadMatrice("/resources/matrice.txt");

    }

    public void getTileImage() {
        try {

            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/obstacle1.jpg"));

            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/brick.png"));

            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(getClass()
                    .getResourceAsStream("/resources/obstacle2.jpg"));
            tiles[2].collision = true;
            // TODO : la suite
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;
        Point point_dans_vue;
        Point point_dans_modele = new Point(x, y);
        while (col < modele.CONSTANTS.maxScreenCol && row < modele.CONSTANTS.maxScreenRow) {

            int TileType = tilesMatrice[row][col];

            point_dans_modele.x = x;
            point_dans_modele.y = y;
            point_dans_vue = gp.transformFromModelToView(point_dans_modele);
            g2.drawImage(tiles[TileType].image, point_dans_vue.x, point_dans_vue.y, null);
            col++;
            x += CONSTANTS.TAILLE_CELLULE;
            if (col == CONSTANTS.maxScreenCol) {
                col = 0;
                row++;
                y += CONSTANTS.TAILLE_CELLULE;
                x = 0;
            }
        }
    }

    public void loadMatrice(String pathToMatrice) {
        try {
            InputStream is = getClass().getResourceAsStream(pathToMatrice);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < CONSTANTS.maxScreenCol && row < CONSTANTS.maxScreenRow) {
                String line = br.readLine();
                while (col < CONSTANTS.maxScreenCol) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    tilesMatrice[row][col] = num;
                    col++;

                }
                if (col == CONSTANTS.maxScreenCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
