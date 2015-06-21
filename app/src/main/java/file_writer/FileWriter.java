package file_writer;


import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileWriter {

    private java.io.FileWriter writer;

    public FileWriter(String participant) {
        File root = Environment.getExternalStorageDirectory();
        File folder = new File(root + "/biofeedback");
        if(!folder.exists() || !folder.isDirectory()) {
            folder.mkdir();
        }
        File gpxfile = new File(folder, "biofeedback_" + participant + ".csv");
        try {
            writer = new java.io.FileWriter(gpxfile);
            writeCsvData("N° Participant:" + participant , " ", " ");
            writeCsvData("RR interval", "Time marker", "Event");
        } catch (IOException e) {
            System.out.println("Erreur lor de la création du fichier");
        }
    }

    public void writeCsvData(String d, String e, String f) {
        String line = String.format("%s,%s,%s\n", d, e, f);
        try {
            writer.write(line);
        } catch (IOException e1) {
            System.out.println("Impossible d'écrire dans le fichier");
        }
    }

    public void close() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la fermeture du fichier");
        }
    }

}
