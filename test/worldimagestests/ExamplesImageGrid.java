package worldimagestests;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javalib.impworld.*;
import javalib.worldimages.AboveImage;
import javalib.worldimages.BesideImage;
import javalib.worldimages.EmptyImage;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

class Cell {
    static Random r = new Random();
    static int DEFAULT_SIZE = 5;
    int size;
    int x, y;
    Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));

    Cell(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    Cell(int x, int y) {
        this(x, y, DEFAULT_SIZE);
    }

    WorldImage draw() {
        return new RectangleImage(this.size, this.size, "solid", c);
    }

    int getX() {
        return x * this.size + (this.size / 2);
    }

    int getY() {
        return y * this.size + (this.size / 2);
    }
}

class GridWorld extends World {
    ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();
    String worldType;
    int size, pixelSize; // Size in cells, size in pixels (square grid)

    GridWorld(String worldType, int size) {
        this.worldType = worldType;
        this.size = size;
        this.pixelSize = size * Cell.DEFAULT_SIZE;
        this.initCells();
    }

    void initCells() {
        for (int i = 0; i < this.size; i++) {
            ArrayList<Cell> row = new ArrayList<Cell>();
            for (int j = 0; j < this.size; j++) {
                row.add(new Cell(i, j));
            }
            cells.add(row);
        }
    }

    public WorldImage overlay() {
        long start = System.currentTimeMillis();
        WorldImage grid = new EmptyImage();
        for (ArrayList<Cell> row : this.cells) {
            WorldImage r = new EmptyImage();
            for (Cell c : row) {
                r = new BesideImage(r, c.draw());
            }
            grid = new AboveImage(grid, r);
        }
        System.out.println("Overlay time: "
                + (System.currentTimeMillis() - start));
        return grid;
    }

    public WorldScene placeImages(WorldScene scene) {
        long start = System.currentTimeMillis();
        for (ArrayList<Cell> row : cells) {
            for (Cell c : row) {
                scene.placeImageXY(c.draw(), c.getX(), c.getY());
            }
        }
        System.out.println("Place Image time: "
                + (System.currentTimeMillis() - start));
        return scene;
    }

    @Override
    public WorldScene makeScene() {
        if (this.worldType.equals("overlay")) {
            WorldScene scn = new WorldScene(this.pixelSize, this.pixelSize);
            scn.placeImageXY(this.overlay(), this.pixelSize / 2,
                    this.pixelSize / 2);
            return scn;
        } else {
            return this.placeImages(new WorldScene(this.pixelSize,
                    this.pixelSize));
        }
    }
}

public class ExamplesImageGrid {

    public static void main(String[] args) {
        GridWorld w = new GridWorld("overlay", 100);
        w.bigBang(w.pixelSize, w.pixelSize, 3);

        // GridWorld w2 = new GridWorld("placeImage", 100);
        // w2.bigBang(w2.pixelSize, w2.pixelSize, 0.01);
    }
}
