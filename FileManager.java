
package corejava;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String FOLDER = "data";

    public static void saveData(String fileName, List<String> data) {
        File folder = new File(FOLDER);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(new File(folder, fileName)))) {

            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }

            System.out.println(fileName + " saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static List<String> loadData(String fileName) {
        List<String> data = new ArrayList<>();
        File file = new File(FOLDER, fileName);

        if (!file.exists()) {
            return data;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                data.add(line);
            }

        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }

        return data;
    }
}
