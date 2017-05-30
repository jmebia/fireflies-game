package com.crackerjacks.game.core.generator;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by jm on 5/26/17.
 *
 * A class that generates dungeon tile maps on a 2-dimensional array
 *
 */
public class DungeonGenerator {

    // non-traversable tiles
    final private int VOID = 0;

    // traversable tiles
    final private int ROOM = 1;
    final private int CORRIDOR = 2;
    final private int CENTER = 3;

    private int[][] dungeonMap;
    private Point playerPosition = new Point();
    private int[][] enemyMap;

    public void generateDungeon(int[][] arrayMap, int roomCount, int roomSize) {
        dungeonMap = arrayMap;
        enemyMap = new int[arrayMap.length][arrayMap.length];

        // populate array with all zeros
        for (int i = 0; i < arrayMap.length; i++) {
            for (int j = 0; j < arrayMap.length; j++) {
                arrayMap[j][i] = 0;
            }
        }

        generateRooms(roomCount, roomSize);
        generateCorridors(roomCount);

    }

    private void generateRooms(int rooms, int roomSize) {

        boolean playerPlaced = false;

        // generateDungeon dungeonMap rooms
        // rooms refers to the number of rooms that will be generated in the dungeonMap
        for (int i = rooms; i > 0; i--) {

            int roomPointX;
            int roomPointY;

            int minPosition = 1;
            int maxPosition = dungeonMap.length - roomSize - minPosition;

            while (true) {
                int positionX = new Random().nextInt(maxPosition - minPosition) + minPosition;
                int positionY = new Random().nextInt(maxPosition - minPosition) + minPosition;

                // check all corner points of the room to prevent collision with other rooms
                if (dungeonMap[positionY][positionX] == VOID &&
                        dungeonMap[positionY + roomSize][positionX + roomSize] == VOID &&
                            dungeonMap[positionY][positionX+roomSize] == VOID &&
                                dungeonMap[positionY+roomSize][positionX] == VOID ) {
                    roomPointX = positionX;
                    roomPointY = positionY;

                    if (playerPlaced == false) {
                        placePlayer(positionX + 1, positionY + 1, positionX + roomSize - 1, positionY + roomSize - 1);
                        playerPlaced = true;
                    } else {
                        placeEnemies(positionX + 1, positionY + 1, positionX + roomSize - 1, positionY + roomSize - 1, 3);
                    }

                    break;
                } else {
                    // continue creating dungeonMap rooms
                    continue;
                }
            }

            for (int k = roomPointX; k < roomPointX + roomSize; k++) {
                for (int l = roomPointY; l < roomPointY + roomSize; l++) {

                    if (k == roomPointX + roomSize / 2 && l == roomPointY + roomSize / 2)
                        dungeonMap[l][k] = CENTER; // plot center of the room
                    else
                        dungeonMap[l][k] = ROOM;
                }
            }

        }
    }

    private void generateCorridors(int rooms) {

        System.out.println("Generating rooms");
        // this list will contain all center points of the dungeonMap rooms
        LinkedList<Point> points = new LinkedList<>();

        // find all room centers which are denoted by the integer 2
        for (int i = 0; i < dungeonMap.length; i++) { // y
            for (int j = 0; j < dungeonMap.length; j++) { // x
                if (dungeonMap[i][j] == CENTER) {
                    System.out.println("Center found at ("+j+","+i+")");
                    // store center coordinates
                    points.add(new Point(j, i));
                }
            }
        }

        // trace all centers from the list

        int counter = 0;

        for (Point center : points) {
            System.out.println("Checking Point " + points.indexOf(center));
            counter++;
            if (counter < rooms)  {

                int x1 = (int) center.getX();
                int x2 = (int) points.get(points.indexOf(center) + 1).getX();

                int y1 = (int) center.getY();
                int y2 = (int) points.get(points.indexOf(center) + 1).getY();

                // trace corridor first in the X axis
                if (x1 - x2 > 0) { // if not negative
                    for (int i = x2; i <= x1; i++) {
                        if (dungeonMap[y1][i] == VOID)
                            dungeonMap[y1][i] = CORRIDOR;
                    }

                    // trace next corridors in the Y axis
                    if (y1 - y2 > 0) { // if not negative
                        for (int i = y2; i <= y1; i++) {
                            if (dungeonMap[i][x2] == VOID)
                                dungeonMap[i][x2] = CORRIDOR;
                        }
                    }
                    // if negative
                    else {
                        for (int i = y1; i <= y2; i++) {
                            if (dungeonMap[i][x2] == VOID)
                                dungeonMap[i][x2] = CORRIDOR;
                        }
                    }

                }
                // if negative
                else {
                    for (int i = x1; i <= x2; i++) {
                        if (dungeonMap[y1][i] == VOID)
                            dungeonMap[y1][i] = CORRIDOR;
                    }

                    // trace next corridors in the Y axis
                    if (y1 - y2 > 0) { // if not negative
                        for (int i = y2; i <= y1; i++) {
                            if (dungeonMap[i][x2] == VOID)
                                dungeonMap[i][x2] = CORRIDOR;
                        }
                    }
                    // if negative
                    else {
                        for (int i = y1; i <= y2; i++) {
                            if (dungeonMap[i][x2] == VOID)
                                dungeonMap[i][x2] = CORRIDOR;
                        }
                    }

                }
            }
        }
    }

    private void placePlayer(int x1, int y1, int x2, int y2) {

        int x = new Random().nextInt(x2 - x1) + x1;
        int y = new Random().nextInt(y2 - y1) + y1;

        playerPosition.setLocation(x, y);

    }

    private void placeEnemies (int x1, int y1, int x2, int y2, int enemyCount) {
        for (int i = 0; i < enemyCount; i++) {
            while(true) {
                int x = new Random().nextInt(x2 - x1) + x1;
                int y = new Random().nextInt(y2 - y1) + y1;

                if (enemyMap[y][x] != 1) {
                    enemyMap[y][x] = 1;
                    break;
                } else {
                    continue;
                }
            }
        }
    }

    public Point getPlayerPosition() {
        return playerPosition;
    }

    public int[][] getDungeonMap() {
        return dungeonMap;
    }

    public int[][] getEnemyMap() {
        return enemyMap;
    }

}
