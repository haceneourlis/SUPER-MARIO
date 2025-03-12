package model;

public class Ground {
    private int yPosition;
    private int tileSize = 50; 

    public Ground(int screenHeight) {
        this.yPosition = screenHeight - tileSize; // Position the ground at the bottom
    }

    public int getYPosition() {
        return yPosition;
    }

    public int getTileSize() {
        return tileSize;
    }
}
