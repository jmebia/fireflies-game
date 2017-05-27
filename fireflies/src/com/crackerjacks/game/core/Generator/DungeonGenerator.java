package com.crackerjacks.game.core.Generator;

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
    */

    int[][] dungeon;

    public int[][] generate(int[][] arrayMap, int roomNumber) {
        dungeon = arrayMap;

        // populate array withh all zeros
        for (int i = 0; i < arrayMap.length; i++) {
            for (int j = 0; j < arrayMap.length; j++) {
                arrayMap[j][i] = 0;
            }
        }

        generateRooms(roomNumber);
        generateCorridors(roomNumber);

        return dungeon;
    }

    private void generateRooms(int rooms) {

        int roomSize = 5;

        // generate dungeon rooms
        // rooms refers to the number of rooms that will be generated in the dungeon
        for (int i = rooms; i > 0; i--) {

            int roomPointX;
            int roomPointY;

            while (true) {
                int positionX = new Random().nextInt(dungeon.length - roomSize);
                int positionY = new Random().nextInt(dungeon.length - roomSize);

                // check all corner points of the room to prevent collision with other rooms
                if (dungeon[positionY][positionX] == 0 &&
                        dungeon[positionY+5][positionX+5] == 0 &&
                            dungeon[positionY][positionX+5] == 0 &&
                                dungeon[positionY+5][positionX] == 0 ) {
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
                        dungeon[l][k] = 2; // plot center of the room
                    else
                        dungeon[l][k] = 1;
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
                if (dungeon[i][j] == 2) {
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
                        dungeon[y1][i] = 2;
                    }

                    // trace next corridors in the Y axis
                    if (y1 - y2 > 0) { // if not negative
                        for (int i = y2; i <= y1; i++) {
                            dungeon[i][x1] = 2;
                        }
                    }
                    // if negative
                    else {
                        for (int i = y1; i <= y2; i++) {
                            dungeon[i][x1] = 2;
                        }
                    }

                }
                // if negative
                else {
                    for (int i = x1; i <= x2; i++) {
                        dungeon[y1][i] = 2;
                    }

                    // trace next corridors in the Y axis
                    if (y1 - y2 > 0) { // if not negative
                        for (int i = y2; i <= y1; i++) {
                            dungeon[i][x2] = 2;
                        }
                    }
                    // if negative
                    else {
                        for (int i = y1; i <= y2; i++) {
                            dungeon[i][x2] = 2;
                        }
                    }

                }
            }
        }
    }

}
