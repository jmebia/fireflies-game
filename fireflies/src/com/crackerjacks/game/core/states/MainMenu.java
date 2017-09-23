package com.crackerjacks.game.core.states;

import com.crackerjacks.game.core.Global;
import com.crackerjacks.game.core.io.Save;
import com.crackerjacks.game.core.io.SaveIO;
import javafx.application.Platform;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.io.IOException;

public class MainMenu extends GameState {

    private final Scene scene;
    private final GraphicsContext graphicsContext;
    private PerspectiveCamera camera;

    private int currentMarker = 1;
    private int minMarker = 1;
    private int maxMarker = 4;



    public MainMenu(Scene scene, GraphicsContext graphicsContext) {
        this.scene = scene;
        this.graphicsContext = graphicsContext;

        onEnter();
    }


    @Override
    void onEnter() {

        // update existing save object in Global kuu
        try {
            File file = new File(Global.getSaveFile());
            if (file.exists()) {
                System.out.println("A save file exists. " + file);
                Global.setSave(new SaveIO().deserialzeAddress(file.toString()));
                System.out.println(Global.getSave().getPlayer().getName() +"'s save file");
            } else {
                System.out.println("A save file doesn't exist!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("A save file doesn't exist!");
        }


        // set up camera
        camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-1000);
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        camera.setFieldOfView(33);
        camera.setTranslateX(400);
        camera.setTranslateY(300);
        scene.setCamera(camera);


        // set up key press events
        scene.setOnKeyPressed(event -> {

            // choose menu option
            if (event.getCode() == KeyCode.DOWN) {
                if (currentMarker < maxMarker) currentMarker++;
                else currentMarker = minMarker;
            } else if (event.getCode() == KeyCode.UP) {
                if (currentMarker > minMarker) currentMarker--;
                else currentMarker = maxMarker;
            }

            // trigger
            if (event.getCode() == KeyCode.ENTER) {
                switch (currentMarker) {
                    case 1:
                        System.out.println("New Game Selected!");
                        scene.setCamera(null);
                        scene.setOnKeyPressed(null);
                        GameStateManager.getStateList().add(new MainGame(scene, graphicsContext, true));
                        break;

                    case 2:
                        System.out.println("Load Game Selected!");
                        try {
                            Save save = Global.getSave();
                            if (!save.getPlayer().equals(null)){
                                scene.setCamera(null);
                                scene.setOnKeyPressed(null);
                                GameStateManager.getStateList().add(new MainGame(scene, graphicsContext, false));
                            } else {
                                System.out.println("An active game doesn't exist");
                            }
                        } catch (Exception e){

                            e.printStackTrace();
                            System.out.println("An active save file doesn't exist!");
                        }
                        break;

                    case 3:
                        System.out.println("About Selected!");
                        break;

                    case 4:
                        System.out.println("Quit Selected!");

                        Platform.exit();
                        break;

                    default:
                        System.out.println("Invalid option");
                        break;
                }
            }
        });

    }

    @Override
    void update(long time) {

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0,0,800,600);

        graphicsContext.setFill((currentMarker == 1? Color.BLACK
                : new Color(0,0,0, 0.5)));
        graphicsContext.setFont(Font.font("Verdana", FontWeight.NORMAL,
                (currentMarker == 1? 22 : 16)));
        graphicsContext.fillText("New Game", 120, 410);

        graphicsContext.setFill((currentMarker == 2? Color.BLACK
                : new Color(0,0,0, 0.5)));
        graphicsContext.setFont(Font.font("Verdana", FontWeight.NORMAL,
                (currentMarker == 2? 22 : 16)));
        graphicsContext.fillText("Load Game", 120, 440);

        graphicsContext.setFill((currentMarker == 3? Color.BLACK
                : new Color(0,0,0, 0.5)));
        graphicsContext.setFont(Font.font("Verdana", FontWeight.NORMAL,
                (currentMarker == 3? 22 : 16)));
        graphicsContext.fillText("About", 120, 470);

        graphicsContext.setFill((currentMarker == 4? Color.BLACK
                : new Color(0,0,0, 0.5)));
        graphicsContext.setFont(Font.font("Verdana", FontWeight.NORMAL,
                (currentMarker == 4? 22 : 16)));
        graphicsContext.fillText("Quit", 120, 500);



    }

    @Override
    void onExit() {

    }

}
