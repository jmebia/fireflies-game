package com.crackerjacks.game.core.Generator;

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

}
