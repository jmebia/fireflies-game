package com.crackerjacks.game.core.input;

import com.crackerjacks.game.core.character.Enemy;
import com.crackerjacks.game.core.character.GameCharacter;
import com.crackerjacks.game.core.character.Player;
import com.crackerjacks.game.core.interactions.Element;
import com.crackerjacks.game.core.interactions.Interaction;
import javafx.scene.PerspectiveCamera;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Mover {

    private boolean attackMode = false;

    private String attackSide = "right";

    public void update(Controller controller, Player player, ArrayList<Enemy> enemies, ArrayList<Enemy> deadEnemies, int[][] tileMap, PerspectiveCamera camera) {
        LinkedList input = controller.getInputs();

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

                    if (tileMap[tempY][(int) player.getX()] > 0 && enemy == null) {
                        player.setY(tempY);
                    }

                    updateEnemy(enemies, player, tileMap);
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

                    if (tileMap[tempY][(int) player.getX()] > 0 && enemy == null) {
                        player.setY(tempY);
                    }

                    updateEnemy(enemies, player, tileMap);
                }

                // move left
                else if (input.getLast().equals("LEFT")) {
                    int tempX = (int) player.getX() - 1;

                    //check if there is an enemy in the direction
                    for (GameCharacter e : enemies) {
                        if (e.getX() == tempX && e.getY() == player.getY()) {
                            enemy = e;
                            break;
                        }
                    }

                    if (tileMap[(int) player.getY()][tempX] > 0 && enemy == null) {
                        player.setX(tempX);
                    }

                    updateEnemy(enemies, player, tileMap);
                }

                // move right
                else if (input.getLast().equals("RIGHT")) {
                    int tempX = (int) player.getX() + 1;

                    //check if there is an enemy in the direction
                    for (GameCharacter e : enemies) {
                        if (e.getX() == tempX && e.getY() == player.getY()) {
                            enemy = e;
                            break;
                        }
                    }

                    if (tileMap[(int) player.getY()][tempX] > 0 && enemy == null) {
                        player.setX(tempX);
                    }

                    updateEnemy(enemies, player, tileMap);
                }

                // initiate attack move
                else if (input.getLast().equals("SPACE")) {
                    attackMode = true;
                    System.out.println("ATTACK MODE ON");
                }

                // initiate element switch
                else if (input.contains("SHIFT")) {
                    System.out.println("shift pressed!");
                    int chips = player.getChipCount();
                    // activate rock
                    if (input.contains("Q")) {
                        System.out.println("shift + Q !");
                        if (chips >= 4) {
                            player.activateRock();
                            System.out.println("Rock Element Activated");
                            System.out.print("Chip Count, then: " + chips);
                            player.addChips(-4);
                            System.out.print(" now: " + chips);
                        } else {
                            System.out.println("Not enough chips!");
                        }
                    }
                    // activate paper
                    else if (input.contains("W")) {
                        System.out.println("shift + Q !");
                        if (chips >= 4) {
                            player.activatePaper();
                            System.out.println("Paper Element Activated");
                            System.out.print("Chip Count, then: " + chips);
                            player.addChips(-4);
                            System.out.println(" now: " + chips);
                        } else {
                            System.out.println("Not enough chips!");
                        }
                    }
                    //activate scissors
                    else if (input.contains("E")) {
                        System.out.println("shift + Q !");
                        if (chips >= 4) {
                            player.activateScissors();
                            System.out.println("Scissors Element Activated");
                            System.out.print("Chip Count, then: " + chips);
                            player.addChips(-4);
                            System.out.println(" now: " + chips);
                        } else {
                            System.out.println("Not enough chips!");
                        }
                    }
                }

                // generateDungeon new dungeon rooms
                if (input.getLast().equals("ENTER")) {
                    // generateNewDungeon();
                }

                /** FOR DEBUGGING PURPOSES, MIGHT BE TEMPORARY **/
                // zoom camera in
                if (input.getLast().equals("X")) {
                    camera.setFieldOfView(camera.getFieldOfView() - 1);
                    // System.out.println("Camera FOV: " + camera.getFieldOfView());
                }
                // zoom camera out of dungeon
                if (input.getLast().equals("Z")) {
                    camera.setFieldOfView(camera.getFieldOfView() + 1);
                    // System.out.println("Camera FOV: " + camera.getFieldOfView());
                }


            } catch (NoSuchElementException e) {
                // handle
            }


            controller.clearInputs();
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
                // rock attack
                else if (input.getLast().equals("Q")) {
                    attacking(player, enemies, deadEnemies, Element.rock.getId(), tileMap);
                }
                // paper attack
                else if (input.getLast().equals("W")) {
                    attacking(player, enemies, deadEnemies, Element.paper.getId(), tileMap);
                }
                // scissors attack
                else if (input.getLast().equals("E")) {
                    attacking(player, enemies, deadEnemies, Element.scissors.getId(), tileMap);
                }

                // go back to moving; cancel attack mode
                else if (input.getLast().equals("SPACE")) {
                    attackMode = false;
                    System.out.println("ATTACK MODE OFF");
                }

            } catch (NoSuchElementException e) {
                // handle
            }

            controller.clearInputs();
        }
    }

    private void updateEnemy(ArrayList<Enemy> enemies, Player player, int[][] tileMap) {
        // update enemies
        for (Enemy enemy: enemies) {
            enemy.update(player, enemies, tileMap);
        }

    }

    private void attacking(Player player, ArrayList<Enemy> enemies, ArrayList<Enemy> deadEnemies
            ,String attackElementID, int[][] tileMap) {

        Enemy enemy = null;

        if (attackSide == "left") {
            // check if there is an enemy for the player's attack to damage
            for(Enemy e : enemies) {
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
                    player.addChips(1);
                }
            }
        }

        else if (attackSide == "right") {
            // check if there is an enemy for the player's attack to damage
            for(Enemy e : enemies) {
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
                    player.addChips(1);
                }
            }
        }

        else if (attackSide == "up") {
            // check if there is an enemy for the player's attack to damage
            for(Enemy e : enemies) {
                if (player.getX() == e.getX() && player.getY() - 1  == e.getY()) {
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
                    player.addChips(1);
                }
            }
        }

        else if (attackSide == "down") {
            // check if there is an enemy for the player's attack to damage
            for(Enemy e : enemies) {
                if (player.getX() == e.getX() && player.getY() + 1  == e.getY()) {
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
                    player.addChips(1);
                }
            }
        }

        updateEnemy(enemies, player, tileMap);
        attackMode = false;
        System.out.println("ATTACK MODE OFF");
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
