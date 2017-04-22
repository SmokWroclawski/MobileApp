package ovh.olo.smok.smokwroclawski.Worker;

import android.os.Environment;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import ovh.olo.smok.smokwroclawski.Object.WeatherData;
import ovh.olo.smok.smokwroclawski.Parser.JsonParser;

/**
 * This class contains methods for manipulating file.
 *
 * @author Michal Popek
 * @author Jakub Obacz
 */
public class FileWorker {

    private JsonParser jsonParser;

    public FileWorker() {
        jsonParser = new JsonParser();
    }

    /**
     * This method append text to file.
     *
     * @param file  Name of file
     * @param text  Text to append
     * @throws IOException
     */
    public void appendToFile(String file, String text) throws IOException {
        File logFile = new File(Environment.getExternalStorageDirectory() +  "/" + file);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
        buf.append(text);
        buf.newLine();
        buf.close();
    }

    /**
     * This method read file line by line and parse String to WeatherData object.
     *
     * @param fileName  Name of file
     * @return          List of WeatherData parsed from file
     * @throws IOException
     */
    public List<WeatherData> readFile(String fileName) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory() +  "/" + fileName);
        StringBuilder text = new StringBuilder();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        br.close();

        try {
            return jsonParser.getWeatherDatasFromJson(text.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    /**
     * Delete file
     *
     * @param file  Name of file
     */
    public void deleteFile(String file) {
        File logFile = new File(Environment.getExternalStorageDirectory() +  "/" + file);
        if(!logFile.exists()) return;

        logFile.delete();
    }

    /**
     * This method checks if file exist
     *
     * @param file
     * @return          true if file exist, false otherwise
     */
    public boolean checkIfFileExist(String file) {
        return new File(Environment.getExternalStorageDirectory() +  "/" + file).exists();
    }

    /**
     * This method removes line by line number from file
     *
     * @param file          Name of file
     * @param lineNumber    Number of line to delete
     */
    public void removeLineFromFile(String file, int lineNumber) throws IOException {

        File inFile = new File(file);
        if (!inFile.isFile()) {
            System.out.println("Parameter is not an existing file");
            return;
        }
        File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
        BufferedReader br = new BufferedReader(new FileReader(file));
        PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
        String line ;
        int counter = 0;
        while ((line = br.readLine()) != null) {
            if (counter == lineNumber) {
                pw.println(line);
                pw.flush();
                counter++;
            }
        }
        pw.close();
        br.close();

        if (!inFile.delete()) {
            System.out.println("Could not delete file");
            return;
        }

        if (!tempFile.renameTo(inFile))
            System.out.println("Could not rename file");

    }
}
