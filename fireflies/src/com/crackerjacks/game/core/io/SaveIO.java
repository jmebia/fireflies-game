package com.crackerjacks.game.core.io;

import com.crackerjacks.game.core.Global;

import java.io.*;

public class SaveIO {

    public void serializeAddress(Save saveFile) throws IOException {


        File file = new File(Global.getSaveFile());

        if (file.exists()) {
            file.createNewFile();
        } else {
            System.out.println("file exists");
        }

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(file))) {

            oos.writeObject(saveFile);
            System.out.println("Done");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public Save deserialzeAddress(String filename) {

        Save saveFile = null;

        try (ObjectInputStream ois
                     = new ObjectInputStream(new FileInputStream(filename))) {

            saveFile = (Save) ois.readObject();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return saveFile;

    }

    private String getFile(String fileName) {

        //Get file from resources folder

         return getClass().getClassLoader().getResource(fileName).toString();

    }

}
