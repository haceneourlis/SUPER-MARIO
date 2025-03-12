package view.Tile;

import model.CONSTANTS;
import view.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    Tile[] tiles;
    int [][] TilesMatrice;
    public TileManager(GamePanel gp) {
        this.gp = gp;

        tiles = new Tile[10];
        TilesMatrice = new int[gp.maxScreenRow][gp.maxScreenCol];


        getTileImage();
        loadMatrice("Tile/matrice.txt");

    }

    public void getTileImage() {
        try{

            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tile1.png"));
            tiles[0].collision = true ;


            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tile2.png"));

            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tile3.png"));
             // TODO : la suite
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int col = 0 ;
        int row = 0 ;
        int x = 0 ;
        int y = 0 ;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow){

            int TileType = gp.matrice[row][col];

            g2.drawImage(tiles[TileType].image,x,y,null);
            col++;
            x += CONSTANTS.TAILLE_CELLULE;
            if(col == gp.maxScreenCol){
                col = 0;
                row++;
                y += CONSTANTS.TAILLE_CELLULE;
                x = 0;
            }
        }
    }

    public void loadMatrice(String pathToMatrice){
        try{
            InputStream is = getClass().getResourceAsStream(pathToMatrice);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxScreenCol && row < gp.maxScreenRow){
               String line = br.readLine();
               while(col < gp.maxScreenCol){
                   String numbers[] = line.split(" ");
                   int num = Integer.parseInt(numbers[col]);
                   gp.matrice[row][col] = num;
                   col++;

               }
               if(col == gp.maxScreenCol){
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
