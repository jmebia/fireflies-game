package com.crackerjacks.game.core.dungeonGenerator;

import com.crackerjacks.game.core.interactions.Technique;
import com.crackerjacks.game.core.objects.Enemy;
import com.crackerjacks.game.core.genetic.Algorithm;
import com.crackerjacks.game.core.interactions.Type;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by jm on 7/7/17.
 */
public class Generator implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Point playerPosition = new Point();
    private Point goalPosition = new Point();
    private int[][] dungeon;
    private int[][] designLayer1; // main rooms and corridors

    private int[][] designLayer2; // overlap-able background designs

    // core map elements
    private final int ROOM = 1;
    private final int CORRIDOR = 2;
    private final int VOID = 0;

    // core design elements
    private final int ROOM_TOP_LEFT = 0;
    private final int ROOM_TOP_CENTER = 1;
    private final int ROOM_TOP_RIGHT = 2;
    private final int ROOM_LEFT = 3;
    private final int ROOM_CENTER = 4;
    private final int ROOM_RIGHT = 5;
    private final int ROOM_BOTTOM_LEFT = 6;
    private final int ROOM_BOTTOM_CENTER = 7;
    private final int ROOM_BOTTOM_RIGHT = 8;

    private final int gridRow;
    private final int minRoomSize;
    private final int mapSize;

    private boolean firstGeneration;

    // objective requirements
    private ArrayList<Point> keysCoordinates = new ArrayList<>();

    // items
    private ArrayList<Point> itemLoots = new ArrayList<>();

    /** CONSTRUCTOR **/
    public Generator(int mapSize, int gridCount, int minRoomSize) {
        this.gridRow = gridCount;
        this.mapSize = mapSize;
        this.minRoomSize = minRoomSize;

        firstGeneration = true;
        dungeon = new int[mapSize + 4][mapSize + 4];
        designLayer1 = new int[mapSize + 4][mapSize + 4];
        designLayer2 = new int[mapSize + 4][mapSize + 4];
    }

    /** GENERATION **/

    public void generateDungeon(ArrayList<Enemy> parents) {

        keysCoordinates.clear();

        initializeMap();
        createRooms();
        createCorridors();
        placeEntities(); // player, goal, loots, and enemies
        addEnemyElements(parents);
        plotRooms();

        plotDesign();
        plotDesign2();

        if (firstGeneration) firstGeneration = false;
    }

    private void initializeMap() {
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                dungeon[y][x] = VOID;
                designLayer1[y][x] = VOID;
            }
        }
    }

    private void createRooms() {

        rooms.clear();

        System.out.println("Generating rooms started...");

        // calculate for the size of the grids
        int gridSize = mapSize / gridRow;

        System.out.println("Grid Size = " + gridSize);

        // iterate through every grid and place a room
        for (int x = 0; x < mapSize; x += gridSize) {
            for (int y = 0; y < mapSize; y += gridSize) {
                int roomHere = new Random().nextInt(2);

                if (roomHere == 1) { // generate room

                    int maxRoomSize = gridSize - 1;
                    // randomize room size based on the size of the grid
                    // the maximum size is the grid's size + 1 and the minimum is the grid's size / 2
                    int width = new Random().nextInt(maxRoomSize - minRoomSize) + minRoomSize;
                    int height = new Random().nextInt(maxRoomSize - minRoomSize) + minRoomSize;

                    System.out.println("Room added with size (" + width + ", " + height + ")");

                    // randomize x and y points
                    int xPos = x + (x==0? 1 : ( x==mapSize - 1? -1 : new Random().nextInt((gridSize - width))));
                    int yPos = y + (y==0? 1 : ( y==mapSize - 1? -1 : new Random().nextInt((gridSize - height))));

                    rooms.add(new Room(xPos, yPos, width, height, rooms.size() + 1));

                }
            }
        }

        System.out.println("Generated " + rooms.size() + " rooms");
        System.out.println("Generating rooms finished...");

    }

    private void createCorridors() {

        // shuffle rooms
        Collections.shuffle(rooms);

        // collect all center points
        // trace all centers from the list
        for ( Room room : rooms) {

            int x1 = (int) room.getCenter().getLocation().getX();
            int y1 = (int) room.getCenter().getLocation().getY();

            System.out.println("x1 = " + x1 + ", y1 = " + y1);

            int x2;
            int y2;
            Room r2;
            try {
                r2 = rooms.get(rooms.indexOf(room) + 1);
            } catch (IndexOutOfBoundsException e) {
                r2 = rooms.get(0);
            }
            x2 = (int) r2.getCenter().getLocation().getX();
            y2 = (int) r2.getCenter().getLocation().getY();

            System.out.println("x2 = " + x2 + ", y2 = " + y2);

            // trace corridor first in the X axis
            if (x1 - x2 > 0) { // if not negative
                for (int i = x2; i <= x1; i++) {
                    if (dungeon[y1][i] == VOID)
                        dungeon[y1][i] = CORRIDOR;
                }

                // trace next corridors in the Y axis
                if (y1 - y2 > 0) { // if not negative
                    for (int i = y2; i <= y1; i++) {
                        if (dungeon[i][x2] == VOID)
                            dungeon[i][x2] = CORRIDOR;
                    }
                }
                // if negative
                else {
                    for (int i = y1; i <= y2; i++) {
                        if (dungeon[i][x2] == VOID)
                            dungeon[i][x2] = CORRIDOR;
                    }
                }
            }

            // if negative
            else {
                for (int i = x1; i <= x2; i++) {
                    if (dungeon[y1][i] == VOID)
                        dungeon[y1][i] = CORRIDOR;
                }

                // trace next corridors in the Y axis
                if (y1 - y2 > 0) { // if not negative
                    for (int i = y2; i <= y1; i++) {
                        if (dungeon[i][x2] == VOID)
                            dungeon[i][x2] = CORRIDOR;
                    }
                }
                // if negative
                else {
                    for (int i = y1; i <= y2; i++) {
                        if (dungeon[i][x2] == VOID)
                            dungeon[i][x2] = CORRIDOR;
                    }
                }

            }
        }
    }

    private void placeEntities() {

        /*
            place player in the first room
            place enemies and other mission objectives in the other rooms
            place the goal in the last room

         */

        Random random = new Random();

        // randomize the rooms order
        ArrayList<Room> tempRooms = rooms;
        Collections.shuffle(tempRooms);

        enemies.clear();
        // create enemies for every room
        for (Room room : tempRooms) {

            if (tempRooms.get(0) == room) {
                playerPosition.setLocation(random.nextInt((room.getX() + room.getWidth() - 1) - (room.getX() + 1)) + room.getX() + 1
                        , random.nextInt((room.getY() + room.getHeight() - 1) - (room.getY() + 1)) + room.getY() + 1);
            } else {

                if (rooms.size() > 3) {
                    // create 2 enemies per room
                    for (int i = 2; i > 0; i--) {

                        int eX = random.nextInt((room.getWidth() + room.getX()) - room.getX()) + room.getX();
                        int eY = random.nextInt((room.getHeight() + room.getY()) - room.getY()) + room.getY();

                        Enemy enemy = new Enemy();
                        enemy.setX(eX);
                        enemy.setY(eY);
                        enemy.setName("Enemy " + room.getId() + "-" + i);

                        enemies.add(enemy);
                        System.out.println("new enemy added...");
                    }
                }
                // 3 or less rooms
                else {

                }
            }
            // add goal to the last room
            if (tempRooms.get(1) == room) {
                goalPosition.setLocation(random.nextInt((room.getX() + room.getWidth() - 1) - (room.getX() + 1)) + room.getX() + 1
                        , random.nextInt((room.getY() + room.getHeight() - 1) - (room.getY() + 1)) + room.getY() + 1);
            }
        }

        // loots and keys if rooms are more than 3
        if (rooms.size() > 3) {
            // place keys if rooms are 4 or more
            int keyCount = random.nextInt((rooms.size() - 2) + 1 - 2) + 2;

            // place all keys
            for (int i = keyCount; i > 0; i--) {
                Room room = tempRooms.get(i);

                Point key = new Point();
                key.setLocation(random.nextInt((room.getX() + room.getWidth() - 1) - (room.getX() + 1)) + room.getX() + 1
                        , random.nextInt((room.getY() + room.getHeight() - 1) - (room.getY() + 1)) + room.getY() + 1);

                keysCoordinates.add(key);
                System.out.println("Nakapag add ng keys sa x="+key.x + " y="+key.y);
            }

            int itemsCount = random.nextInt((rooms.size()/2) + 1 - 2) + 2;

            while (itemsCount > 0) {
                int roomNum = random.nextInt(rooms.size() - 1);
                Room room = tempRooms.get(roomNum);

                Point item = new Point();
                item.setLocation(random.nextInt((room.getX() + room.getWidth() - 1) - (room.getX() + 1)) + room.getX() + 1
                        , random.nextInt((room.getY() + room.getHeight() - 1) - (room.getY() + 1)) + room.getY() + 1);
                itemLoots.add(item);

                itemsCount--;
            }

        }

        // if rooms are 3 below
        else {
            int itemsCount = random.nextInt(4 + 1 - 2) + 2;

            while (itemsCount > 0) {
                int roomNum = random.nextInt(rooms.size() - 1) + 1;
                Room room = tempRooms.get(roomNum);

                Point item = new Point();
                item.setLocation(random.nextInt((room.getX() + room.getWidth() - 1) - (room.getX() + 1)) + room.getX() + 1
                        , random.nextInt((room.getY() + room.getHeight() - 1) - (room.getY() + 1)) + room.getY() + 1);
                itemLoots.add(item);

                itemsCount--;
            }
        }

    }

    private void addEnemyElements(ArrayList<Enemy> parents) {

        Random random = new Random();

        // plot element for each enemy
        // if this is the first generation of enemies, assign random element for each enemy
        // else if this is not the first generation, use the genetic algorithm
        if (firstGeneration) {
            for (Enemy e: enemies) {
                // assign random element and type to enemy

                int i = random.nextInt(3);
                if (i == 0) {
                    e.setTechnique(Technique.brute);
                } else if (i == 1) {
                    e.setTechnique(Technique.stable);
                } else {
                    e.setTechnique(Technique.cut);
                }

                int j = random.nextInt(3);
                if (j == 0) {
                    System.out.println("VARMINT [A] type set!");
                    e.setType(Type.a);
                } else if (j == 1) {
                    System.out.println("GUARDIAN [B] type set!");
                    e.setType(Type.b);
                } else {
                    System.out.println("WRAITH [C] type set!");
                    e.setType(Type.c);
                }
            }
        }

        else {
            // placeholder while genetic algorithm is still a work in progress
            Algorithm genetics = new Algorithm(parents, enemies);
            genetics.produce();
            enemies = genetics.getOffsprings();
        }

        for (Enemy enemy : enemies) {

            if (enemy.getType().equals(Type.a))
                enemy.setName("Varmint");
            if (enemy.getType().equals(Type.b))
                enemy.setName("Guardian");
            if (enemy.getType().equals(Type.c))
                enemy.setName("Wraith");
        }
    }

    private void plotRooms() {

        for (Room room : rooms) {

            for (int x = room.getX(); x < room.getX() + room.getWidth(); x++) {
                for (int y = room.getY(); y < room.getY() + room.getHeight(); y++) {
                    dungeon[y][x] = ROOM;
                }
            }

        }

    }

    private void plotDesign() {
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {

                // check first if tile has a room or corridor assigned
                if (dungeon[y][x] != VOID) {
                    // ROOM TILES
                    // if center room
                    if ( dungeon[y-1][x-1]==ROOM && dungeon[y-1][x]==ROOM && dungeon[y-1][x+1]==ROOM
                            && dungeon[y][x-1]==ROOM && dungeon[y][x+1] == ROOM
                            && dungeon[y+1][x-1]==ROOM && dungeon[y+1][x]==ROOM
                            && dungeon[y+1][x+1]==ROOM) {
                        designLayer1[y][x] = ROOM_CENTER;
                    }
                    // center left
                    else if ( (dungeon[y-1][x-1]==VOID || dungeon[y-1][x-1]==CORRIDOR )&& dungeon[y-1][x]==ROOM
                            && dungeon[y-1][x+1]==ROOM
                            && (dungeon[y][x-1]==VOID || dungeon[y][x-1]==CORRIDOR) && dungeon[y][x+1] == ROOM
                            && (dungeon[y+1][x-1]==VOID || dungeon[y+1][x-1]==CORRIDOR) && dungeon[y+1][x]==ROOM
                            && dungeon[y+1][x+1]==ROOM) {
                        designLayer1[y][x] = ROOM_LEFT;
                    }
                    // center right
                    else if ( dungeon[y-1][x-1]==ROOM && dungeon[y-1][x]==ROOM
                            && (dungeon[y-1][x+1]==VOID || dungeon[y-1][x+1]==CORRIDOR)
                            && dungeon[y][x-1]==ROOM && (dungeon[y][x+1] == VOID || dungeon[y][x+1] == CORRIDOR)
                            && dungeon[y+1][x-1]==ROOM && dungeon[y+1][x]==ROOM
                            && (dungeon[y+1][x+1]== VOID || dungeon[y+1][x+1]== CORRIDOR)) {
                        designLayer1[y][x] = ROOM_RIGHT;
                    }
                    // top center
                    else if ( (dungeon[y-1][x-1]==VOID || dungeon[y-1][x-1]==CORRIDOR)
                            && (dungeon[y-1][x]==VOID || dungeon[y-1][x]==CORRIDOR)
                            && (dungeon[y-1][x+1]==VOID || dungeon[y-1][x+1]==CORRIDOR)
                            && dungeon[y][x-1]==ROOM && dungeon[y][x+1] == ROOM
                            && dungeon[y+1][x-1]==ROOM && dungeon[y+1][x]==ROOM
                            && dungeon[y+1][x+1]==ROOM) {
                        designLayer1[y][x] = ROOM_TOP_CENTER;
                    }
                    // bottom center
                    else if ( dungeon[y-1][x-1]==ROOM && dungeon[y-1][x]==ROOM && dungeon[y-1][x+1]==ROOM
                            && dungeon[y][x-1]==ROOM && dungeon[y][x+1] == ROOM
                            && (dungeon[y+1][x-1]==VOID || dungeon[y+1][x-1]==CORRIDOR)
                            && (dungeon[y+1][x]==VOID || dungeon[y+1][x]==CORRIDOR)
                            && (dungeon[y+1][x+1]==VOID || dungeon[y+1][x+1]==CORRIDOR)) {
                        designLayer1[y][x] = ROOM_BOTTOM_CENTER;
                    }
                    // top right
                    else if ( (dungeon[y-1][x-1]==VOID || dungeon[y-1][x-1]==CORRIDOR)
                            && (dungeon[y-1][x]==VOID || dungeon[y-1][x]==CORRIDOR)
                            && (dungeon[y-1][x+1]==VOID || dungeon[y-1][x+1]==CORRIDOR)
                            && dungeon[y][x-1]==ROOM && (dungeon[y][x+1] == VOID || dungeon[y][x+1] == CORRIDOR)
                            && dungeon[y+1][x-1]==ROOM && dungeon[y+1][x]==ROOM
                            && (dungeon[y+1][x+1]==VOID || dungeon[y+1][x+1]==CORRIDOR)) {
                        designLayer1[y][x] = ROOM_TOP_RIGHT;
                    }
                    // top left
                    else if ( (dungeon[y-1][x-1]==VOID || dungeon[y-1][x-1]==CORRIDOR)
                            && (dungeon[y-1][x]==VOID || dungeon[y-1][x]==CORRIDOR)
                            && (dungeon[y-1][x+1]==VOID || dungeon[y-1][x+1]==CORRIDOR)
                            && (dungeon[y][x-1]==VOID || dungeon[y][x-1]==CORRIDOR) && dungeon[y][x+1] == ROOM
                            && (dungeon[y+1][x-1]==VOID || dungeon[y+1][x-1]==CORRIDOR) && dungeon[y+1][x]==ROOM
                            && dungeon[y+1][x+1]==ROOM) {
                        designLayer1[y][x] = ROOM_TOP_LEFT;
                    }
                    // bottom left
                    else if ( (dungeon[y-1][x-1]==VOID || dungeon[y-1][x-1]==CORRIDOR) && dungeon[y-1][x]==ROOM
                            && dungeon[y-1][x+1]==ROOM && (dungeon[y][x-1]==VOID || dungeon[y][x-1]==CORRIDOR)
                            && dungeon[y][x+1] == ROOM && (dungeon[y+1][x-1]==VOID || dungeon[y+1][x-1]==CORRIDOR)
                            && (dungeon[y+1][x]==VOID || dungeon[y+1][x]==CORRIDOR)
                            && (dungeon[y+1][x+1]==VOID || dungeon[y+1][x+1]==CORRIDOR)) {
                        designLayer1[y][x] = ROOM_BOTTOM_LEFT;
                    }
                    // bottom right
                    else if ( dungeon[y-1][x-1]==ROOM && dungeon[y-1][x]==ROOM &&
                            (dungeon[y-1][x+1]==VOID || dungeon[y-1][x+1]==CORRIDOR)
                            && dungeon[y][x-1]==ROOM && (dungeon[y][x+1] == VOID || dungeon[y][x+1] == CORRIDOR)
                            && (dungeon[y+1][x-1]==VOID || dungeon[y+1][x-1]==CORRIDOR)
                            && (dungeon[y+1][x]==VOID || dungeon[y+1][x]==CORRIDOR)
                            && (dungeon[y+1][x+1]==VOID || dungeon[y+1][x+1]==CORRIDOR)) {
                        designLayer1[y][x] = ROOM_BOTTOM_RIGHT;
                    }

                    // CORRIDORS

                    else if ( dungeon[y-1][x-1]==ROOM && dungeon[y-1][x]==ROOM &&
                            (dungeon[y-1][x+1]==VOID || dungeon[y-1][x+1]==CORRIDOR)
                            && dungeon[y][x-1]==ROOM && (dungeon[y][x+1] == VOID || dungeon[y][x+1] == CORRIDOR)
                            && (dungeon[y+1][x-1]==VOID || dungeon[y+1][x-1]==CORRIDOR)
                            && (dungeon[y+1][x]==VOID || dungeon[y+1][x]==CORRIDOR)
                            && (dungeon[y+1][x+1]==VOID || dungeon[y+1][x+1]==CORRIDOR)) {
                        designLayer1[y][x] = ROOM_BOTTOM_RIGHT;
                    }

                }
            }
        }
    }

    private void plotDesign2() {

        for (int x = 0; x < mapSize; x++) {
            for (int y = 1; y < mapSize; y++) {
                designLayer2[y][x] = 0;
            }
        }

        for (int x = 0; x < mapSize; x++) {
            for (int y = 1; y < mapSize; y++) {

                // check if tile has room or corridor tile
                if (dungeon[y][x] == VOID) {
                    // check if edge room tile
                    int tile = designLayer1[y - 1][x];
                    if (tile == ROOM_BOTTOM_LEFT) {
                        designLayer2[y][x] = 1;
                    } else if (tile == ROOM_BOTTOM_CENTER) {
                        designLayer2[y][x] = 2;
                    } else if (tile == ROOM_BOTTOM_RIGHT) {
                        designLayer2[y][x] = 3;
                    }
                    // check if corridor tile
                    else if (dungeon[y - 1][x] == CORRIDOR) {
                        designLayer2[y][x] = 4;
                    }
                }

            }
        }

    }


    public int[][] getDungeon() {
        return dungeon;
    }

    public int[][] getDesignLayer1() {
        return designLayer1;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public Point getPlayerPosition() {
        return playerPosition;
    }

    public Point getGoalPosition() {
        return goalPosition;
    }

    public int[][] getEnemyStats() {
        int[][] counts = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                counts[i][j] = 0;
            }
        }

        // [0][x] = brute, [1][x] = stable, [2][x] cut
        // [x][0] = A, [x][1] = B, [x][2] C


        for (Enemy e : enemies) {
            // brute
            if (e.getTechnique().getId().equals(Technique.brute.getId())) {
                if (e.getType().getId().equals(Type.a.getId())) {
                    counts[0][0]++;
                }
                else if (e.getType().getId().equals(Type.b.getId())) {
                    counts[0][1]++;
                }
                else if (e.getType().getId().equals(Type.c.getId())) {
                    counts[0][2]++;
                }
            }
            // stable
            else if (e.getTechnique().getId().equals(Technique.stable.getId())) {
                if (e.getType().getId().equals(Type.a.getId())) {
                    counts[1][0]++;
                }
                else if (e.getType().getId().equals(Type.b.getId())) {
                    counts[1][1]++;
                }
                else if (e.getType().getId().equals(Type.c.getId())) {
                    counts[1][2]++;
                }
            }
            // cut
            else if (e.getTechnique().getId().equals(Technique.cut.getId())) {
                if (e.getType().getId().equals(Type.a.getId())) {
                    counts[2][0]++;
                }
                else if (e.getType().getId().equals(Type.b.getId())) {
                    counts[2][1]++;
                }
                else if (e.getType().getId().equals(Type.c.getId())) {
                    counts[2][2]++;
                }
            }
        }

        return counts;
    }

    public int getROOM_TOP_LEFT() {
        return ROOM_TOP_LEFT;
    }

    public int getROOM_TOP_CENTER() {
        return ROOM_TOP_CENTER;
    }

    public int getROOM_TOP_RIGHT() {
        return ROOM_TOP_RIGHT;
    }

    public int getROOM_LEFT() {
        return ROOM_LEFT;
    }

    public int getROOM_CENTER() {
        return ROOM_CENTER;
    }

    public int getROOM_RIGHT() {
        return ROOM_RIGHT;
    }

    public int getROOM_BOTTOM_LEFT() {
        return ROOM_BOTTOM_LEFT;
    }

    public int getROOM_BOTTOM_CENTER() {
        return ROOM_BOTTOM_CENTER;
    }

    public int getROOM_BOTTOM_RIGHT() {
        return ROOM_BOTTOM_RIGHT;
    }

    public int[][] getDesignLayer2() {
        return designLayer2;
    }

    public ArrayList<Point> getKeysCoordinates() {
        return keysCoordinates;
    }

    public ArrayList<Point> getItemLoots() {
        return itemLoots;
    }
}
