package com.crackerjacks.game.core.generator;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by jm on 5/26/17.
 */
public class DungeonGenerator {

    /**
     0 = non-traversable
     1 = room / traversable
     2 = corridor / traversable
     3 = room center
     -1 = wall
    */

    // non-traversable tiles
    final private int VOID = 0;

    // traversable tiles
    final private int ROOM = 1;
    final private int CORRIDOR = 2;
    final private int CENTER = 3;

    int[][] dungeon;

    public int[][] generate(int[][] arrayMap, int roomCount, int roomSize) {
        dungeon = arrayMap;

        // populate array with all zeros
        for (int i = 0; i < arrayMap.length; i++) {
            for (int j = 0; j < arrayMap.length; j++) {
                arrayMap[j][i] = 0;
            }
        }

        generateRooms(roomCount, roomSize);
        generateCorridors(roomCount);

        return dungeon;
    }

    private void generateRooms(int rooms, int roomSize) {

        // generate dungeon rooms
        // rooms refers to the number of rooms that will be generated in the dungeon
        for (int i = rooms; i > 0; i--) {

            int roomPointX;
            int roomPointY;

            while (true) {
                int positionX = new Random().nextInt(dungeon.length - roomSize);
                int positionY = new Random().nextInt(dungeon.length - roomSize);

                // check all corner points of the room to prevent collision with other rooms
                if (dungeon[positionY][positionX] == VOID &&
                        dungeon[positionY + roomSize][positionX + roomSize] == VOID &&
                            dungeon[positionY][positionX+roomSize] == VOID &&
                                dungeon[positionY+roomSize][positionX] == VOID ) {
                    roomPointX = positionX;
                    roomPointY = positionY;
                    break;
                } else {
                    // continue creating dungeon rooms
                    continue;
                }
            }

            for (int k = roomPointX; k < roomPointX + roomSize; k++) {
                for (int l = roomPointY; l < roomPointY + roomSize; l++) {

                    if (k == roomPointX + roomSize / 2 && l == roomPointY + roomSize / 2)
                        dungeon[l][k] = CENTER; // plot center of the room
                    else
                        dungeon[l][k] = ROOM;
                }
            }

        }
    }

    private void generateCorridors(int rooms) {

        System.out.println("Generating rooms");
        // this list will contain all center points of the dungeon rooms
        LinkedList<Point> points = new LinkedList<>();

        // find all room centers which are denoted by the integer 2
        for (int i = 0; i < dungeon.length; i++) { // y
            for (int j = 0; j < dungeon.length; j++) { // x
                if (dungeon[i][j] == CENTER) {
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
    }

}
