package com.crackerjacks.game.core.input;

import com.crackerjacks.game.core.Global;
import com.crackerjacks.game.core.interactions.Technique;
import com.crackerjacks.game.core.objects.Enemy;
import com.crackerjacks.game.core.objects.GameCharacter;
import com.crackerjacks.game.core.objects.Player;
import com.crackerjacks.game.core.interactions.Interaction;
import com.crackerjacks.game.core.states.GameMenu;
import com.crackerjacks.game.core.states.GameStateManager;
import com.crackerjacks.game.core.states.InventoryMenu;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class MainGameController {

    private boolean attackMode = false;

    private String attackSide = "right";

    public void update(InputHandler inputHandler, Player player, ArrayList<Enemy> enemies,
                       ArrayList<Enemy> deadEnemies, int[][] tileMap,
                       Scene scene, GraphicsContext gc) {
        LinkedList input = inputHandler.getInputs();

        inputHandler.setDisabled(true);

        // movement mode
        if (attackMode == false) {
            try {

                GameCharacter enemy = null;

                // move up
                if (input.getLast().equals("UP")) {
                    int tempY = (int) player.getY() - 1;

                    //check if there is an enemy in the direction
                    for (GameCharacter e : enemies) {
                        if (e.getX() == player.getX() && e.getY() == tempY) {
                            enemy = e;
                            break;
                        }
                    }

                    if (player.getStun() <= 0) {
                        if (tileMap[tempY][(int) player.getX()] > 0 && enemy == null) {
                            player.setY(tempY);
                        }
                    } else {
                        Global.addHistoryText(player.getName().toUpperCase() + " is STUNNED");
                    }

                    updateEnemy(enemies, player, tileMap);
                    player.updateStatus();
                }

                // move down
                else if (input.getLast().equals("DOWN")) {
                    int tempY = (int) player.getY() + 1;

                    //check if there is an enemy in the direction
                    for (GameCharacter e : enemies) {
                        if (e.getX() == player.getX() && e.getY() == tempY) {
                            enemy = e;
                            break;
                        }
                    }

                    if (player.getStun() <= 0) {
                        if (tileMap[tempY][(int) player.getX()] > 0 && enemy == null) {
                            player.setY(tempY);
                        }
                    } else {
                        Global.addHistoryText(player.getName().toUpperCase() + " is STUNNED");
                    }

                    updateEnemy(enemies, player, tileMap);

                    player.updateStatus();
                }

                // move left
                else if (input.getLast().equals("LEFT")) {

                    player.getSprite().setInitialOffset(0);

                    int tempX = (int) player.getX() - 1;

                    //check if there is an enemy in the direction
                    for (GameCharacter e : enemies) {
                        if (e.getX() == tempX && e.getY() == player.getY()) {
                            enemy = e;
                            break;
                        }
                    }

                    if (player.getStun() <= 0) {
                        if (tileMap[(int) player.getY()][tempX] > 0 && enemy == null) {
                            player.setX(tempX);
                        }
                    } else {
                        Global.addHistoryText(player.getName().toUpperCase() + " is STUNNED");
                    }

                    updateEnemy(enemies, player, tileMap);

                    player.updateStatus();
                }

                // move right
                else if (input.getLast().equals("RIGHT")) {

                    player.getSprite().setInitialOffset(2);

                    int tempX = (int) player.getX() + 1;

                    //check if there is an enemy in the direction
                    for (GameCharacter e : enemies) {
                        if (e.getX() == tempX && e.getY() == player.getY()) {
                            enemy = e;
                            break;
                        }
                    }

                    if (player.getStun() <= 0) {
                        if (tileMap[(int) player.getY()][tempX] > 0 && enemy == null) {
                            player.setX(tempX);
                        }
                    } else {
                        Global.addHistoryText(player.getName().toUpperCase() + " is STUNNED");
                    }

                    updateEnemy(enemies, player, tileMap);

                    player.updateStatus();
                }

                // initiate attack move
                else if (input.getLast().equals("SPACE")) {
                    if (player.getStun() <= 0) {
                        attackMode = true;
                        System.out.println("ATTACK MODE ON");
                    } else {
                        Global.addHistoryText(player.getName().toUpperCase() + " is STUNNED");
                        updateEnemy(enemies, player, tileMap);
                        player.updateStatus();
                    }
                }

                // activate brute
                else if (input.getLast().equals("J")) {
                    if (!player.getProficientTechnique().equals(Technique.brute)) {
                        if (player.getFireflies() >= player.getTechniqueCost()) {
                            player.setFireflies(player.getFireflies() - player.getTechniqueCost());
                            player.setTechniqueCost(player.getTechniqueCost() * 2);
                            player.setProficientTechnique(Technique.brute);
                            Global.addHistoryText("Switched proficiency to BRUTE technique.");
                        } else {
                            Global.addHistoryText("Not enough Fireflies essence!");
                        }
                    } else {
                        Global.addHistoryText("You are already proficient in BRUTE technique!");
                    }
                }

                // activate stable
                else if (input.getLast().equals("K")) {
                    if (!player.getProficientTechnique().equals(Technique.stable)) {
                        if (player.getFireflies() >= player.getTechniqueCost()) {
                            player.setFireflies(player.getFireflies() - player.getTechniqueCost());
                            player.setTechniqueCost(player.getTechniqueCost() * 2);
                            player.setProficientTechnique(Technique.stable);
                            Global.addHistoryText("Switched proficiency to STABLE technique.");
                        } else {
                            Global.addHistoryText("Not enough Fireflies essence!");
                        }
                    } else {
                        Global.addHistoryText("You are already proficient in STABLE technique!");
                    }
                }

                // activate cut
                else if (input.getLast().equals("L")) {
                    if (!player.getProficientTechnique().equals(Technique.cut)) {
                        if (player.getFireflies() >= player.getTechniqueCost()) {
                            player.setFireflies(player.getFireflies() - player.getTechniqueCost());
                            player.setTechniqueCost(player.getTechniqueCost() * 2);
                            player.setProficientTechnique(Technique.cut);
                            Global.addHistoryText("Switched proficiency to CUT technique.");
                        } else {
                            Global.addHistoryText("Not enough Fireflies essence!");
                        }
                    } else {
                        Global.addHistoryText("You are already proficient in CUT technique!");
                    }
                }

                // open in-game sub states

                // open inventory
                if (input.contains("I")) {
                    GameStateManager.addState(new InventoryMenu(scene, gc));
                }
                // open game menu
                else if (input.contains("ESCAPE")) {
                    GameStateManager.addState(new GameMenu(scene, gc));
                }

                // generateDungeon new dungeon rooms
                if (input.getLast().equals("F10")) {
                    // generateNewDungeon();
                    GameStateManager.removeLast();
                }

                /*
                // FOR DEBUGGING PURPOSES
                // zoom camera in
                if (input.getLast().equals("X")) {
                    camera.setFieldOfView(camera.getFieldOfView() - 1);
                    // System.out.println("Camera FOV: " + camera.getFieldOfView());
                }
                // zoom camera out of dungeon
                if (input.getLast().equals("Z")) {
                    camera.setFieldOfView(camera.getFieldOfView() + 1);
                    // System.out.println("Camera FOV: " + camera.getFieldOfView());
                } */

            } catch (NoSuchElementException e) {
                // handle
            }

            inputHandler.clearInputs();
        }

        // attack mode
        else {
            try {
                // set attack upwards
                if (input.getLast().equals("UP")) {
                    attackSide = "up";
                    System.out.println("attacking up");
                }

                // set attack downwards
                else if (input.getLast().equals("DOWN")) {
                    attackSide = "down";
                    System.out.println("attacking down");
                }

                // set attack to left side
                else if (input.getLast().equals("LEFT")) {
                    attackSide = "left";
                    System.out.println("attacking left");
                }

                // set attack to right side
                else if (input.getLast().equals("RIGHT")) {
                    attackSide = "right";
                    System.out.println("Attacking right");
                }

                // the following are attack inputs
                // brute attack
                else if (input.getLast().equals("Q")) {
                    attacking(player, enemies, deadEnemies, Technique.brute.getId(), tileMap);
                }
                // stable attack
                else if (input.getLast().equals("W")) {
                    attacking(player, enemies, deadEnemies, Technique.stable.getId(), tileMap);
                }
                // cut attack
                else if (input.getLast().equals("E")) {
                    attacking(player, enemies, deadEnemies, Technique.cut.getId(), tileMap);
                }

                // go back to moving; cancel attack mode
                else if (input.getLast().equals("SPACE")) {
                    attackMode = false;
                    System.out.println("ATTACK MODE OFF");
                }

            } catch (NoSuchElementException e) {
                // handle
            }
        }

        inputHandler.clearInputs();

    }

    private void updateEnemy(ArrayList<Enemy> enemies, Player player, int[][] tileMap) {

//        System.out.println("<============= Updating enemies =============>");

        // container for checked enemies
        ArrayList<Enemy> checked = new ArrayList<>();

        // update enemies
        for (Enemy enemy: enemies) {
            // checks if the enemy is already updated for this turn
            if ( !(checked.contains(enemy)) ) {
                enemy.update(player, enemies, tileMap);
                checked.add(enemy);
            }
        }

    }

    private void attacking(Player player, ArrayList<Enemy> enemies, ArrayList<Enemy> deadEnemies
            ,String attackElementID, int[][] tileMap) {

        Enemy enemy = null;

        if (player.getDisarm() <= 0) {
            if (attackSide == "left") {
                // check if there is an enemy for the player's attack to damage
                for (Enemy e : enemies) {
                    if (player.getX() - 1 == e.getX() && player.getY() == e.getY()) {
                        enemy = e;
                        break;
                    }
                }

                // check if enemy is empty or not
                if (enemy != null) {
                    // damage enemy health by player
                    new Interaction().attackMove(player, enemy, attackElementID);
                    if (enemy.getCurrentHealth() <= 0) {
                        deadEnemies.add(enemy);
                        enemies.remove(enemy);
                        player.addFireflies(1);
                    }
                }
            } else if (attackSide == "right") {
                // check if there is an enemy for the player's attack to damage
                for (Enemy e : enemies) {
                    if (player.getX() + 1 == e.getX() && player.getY() == e.getY()) {
                        enemy = e;
                        break;
                    }
                }

                // check if enemy is empty or not
                if (enemy != null) {
                    // damage enemy health by player
                    new Interaction().attackMove(player, enemy, attackElementID);
                    if (enemy.getCurrentHealth() <= 0) {
                        deadEnemies.add(enemy);
                        enemies.remove(enemy);
                        player.addFireflies(1);
                    }
                }
            } else if (attackSide == "up") {
                // check if there is an enemy for the player's attack to damage
                for (Enemy e : enemies) {
                    if (player.getX() == e.getX() && player.getY() - 1 == e.getY()) {
                        enemy = e;
                        break;
                    }
                }

                // check if enemy is empty or not
                if (enemy != null) {
                    // damage enemy health by player
                    new Interaction().attackMove(player, enemy, attackElementID);
                    if (enemy.getCurrentHealth() <= 0) {
                        deadEnemies.add(enemy);
                        enemies.remove(enemy);
                        player.addFireflies(1);
                    }
                }
            } else if (attackSide == "down") {
                // check if there is an enemy for the player's attack to damage
                for (Enemy e : enemies) {
                    if (player.getX() == e.getX() && player.getY() + 1 == e.getY()) {
                        enemy = e;
                        break;
                    }
                }

                // check if enemy is empty or not
                if (enemy != null) {
                    // damage enemy health by player
                    new Interaction().attackMove(player, enemy, attackElementID);
                    if (enemy.getCurrentHealth() <= 0) {
                        deadEnemies.add(enemy);
                        enemies.remove(enemy);
                        player.addFireflies(1);
                    }
                }
            }
        } else {
            Global.addHistoryText(player.getName().toUpperCase() + " is DISARMED");
        }

        // check if an enemy is dead
        for (Enemy e : enemies) {
            if (e.getCurrentHealth() <= 0) {
                deadEnemies.add(e);
                enemies.remove(e);
            }
        }

        updateEnemy(enemies, player, tileMap);
        player.updateStatus();
        attackMode = false;
        System.out.println("ATTACK MODE OFF");
    }

    public void unfog(Player player, int[][] map, InputHandler input) {
        // System.out.println("Running un-fogger...");
        double playerX = player.getX();
        double playerY = player.getY();
        double LoS = player.getLineOfSight();
        for (int y = 0; y < map.length; y++) { // iterate through the rows
            for (int x = 0; x < map.length; x++) { // iterate through the columns
                // check if fog point is not within player's range of sight
                if (!(x >= playerX - LoS && x <= playerX + LoS)
                        || !(y <= playerY + LoS && y >= playerY - LoS)
                        // check if point is located in an edge
                        // upper left
                        || (x == playerX - LoS && y == playerY - LoS)
                        || (x == playerX - LoS + 1 && y == playerY - LoS)
                        || (x == playerX - LoS && y == playerY - LoS + 1)
                        // upper right
                        || (x == playerX + LoS && y == playerY - LoS)
                        || (x == playerX + LoS - 1 && y == playerY - LoS)
                        || (x == playerX + LoS && y == playerY - LoS + 1)
                        // lower left
                        || (x == playerX - LoS && y == playerY + LoS)
                        || (x == playerX - LoS + 1 && y == playerY + LoS)
                        || (x == playerX - LoS && y == playerY + LoS - 1)
                        // lower right
                        || (x == playerX + LoS && y == playerY + LoS)
                        || (x == playerX + LoS - 1 && y == playerY + LoS)
                        || (x == playerX + LoS && y == playerY + LoS - 1)) {
                    // check if point is located in the edge
                    if (map[y][x] == 2) {
                        map[y][x] = 1;
                    }
                }
                // point is within player's sight
                else {
                    map[y][x] = 2;
                }
            }
        }
    }



    public boolean getAttackMode() {
        return attackMode;
    }

    public String getAttackSide() {
        return attackSide;
    }

    public void setAttackMode(boolean b) {
        attackMode = b;
    }

}
