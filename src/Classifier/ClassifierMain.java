package Classifier;

import GettingScores.GettingScores;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;

public class ClassifierMain {
    public static void main(String []args) {

        try {
            File folder = new File("src/resources/toclassify");
            File file = new File("src/resources/dataFile.csv");
            if(!file.exists())file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (final File fileEntry : folder.listFiles()) {
                BufferedReader br = new BufferedReader(new FileReader(fileEntry.getAbsolutePath()));
                String line = classify(br, fileEntry);
                br.close();
                if(line.split(",").length<11)
                    System.out.println("break");
                bw.write(line);
                bw.newLine();
                br = new BufferedReader(new FileReader(fileEntry.getAbsolutePath()));
                copyFile(br, fileEntry);
                br.close();
            }
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    static String classify(BufferedReader bufferedReader, File file) throws IOException {
        String[] line = bufferedReader.readLine().toLowerCase(Locale.ROOT).replaceAll("\\s", "").split(";");
        String name = line[4].split(":")[1];
        String color = line[5].split(":")[1];
        String situation = line[6].split(":")[1];
        bufferedReader.readLine();
        line = bufferedReader.readLine().toLowerCase(Locale.ROOT).replaceAll("\\s", "").split(";");
        String speedPm = line[15];
        String surfaceSt = line[16];
        String speedSt = line[17];
        String transit = line[18];
        String scSpeedPm = line[11];
        String scSurfaceSt = line[12];
        String scSpeedSt = line[13];
        String scTransit = line[14];
        bufferedReader.close();
        return name + ", " + situation + ", " + color + ", " + speedPm + ", " + surfaceSt + ", " + speedSt + ", " + transit + ", " +
                scSpeedPm + ", " + scSurfaceSt + ", " + scSpeedSt + ", " + scTransit;
//        Files.move(Paths.get(file.getAbsolutePath()), Paths.get("src/resources/" + name + "-"+ color + "-" + situation + "/" + file.getName()), StandardCopyOption.REPLACE_EXISTING);
//        file.renameTo(new File("src/resources/" + name + "-"+ color + "-" + situation + "/" + file.getName()));
    }

    static void copyFile(BufferedReader bufferedReader, File file) throws IOException {
        String[] line = bufferedReader.readLine().toLowerCase(Locale.ROOT).replaceAll("\\s", "").split(";");
        String name = line[4].split(":")[1];
        String color = line[5].split(":")[1];
        String situation = line[6].split(":")[1];
        createDirectory("src/resources/classifiedFiles");
        createDirectory("src/resources/classifiedFiles/" + name + "-"+ color + "-" + situation + "/");
        Files.copy(Paths.get(file.getAbsolutePath()), Paths.get("src/resources/classifiedFiles/" + name + "-"+ color + "-" + situation + "/" + file.getName()), StandardCopyOption.REPLACE_EXISTING);
//        file.renameTo(new File("src/resources/" + name + "-"+ color + "-" + situation + "/" + file.getName()));
    }

    static void createDirectory(String line){
        File dir = new File(line);
        if(!dir.exists())dir.mkdir();
    }

}
