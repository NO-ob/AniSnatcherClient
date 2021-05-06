package Utils;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
public final class SettingsHandler {
    public static String downloadPath = "", serverIP = "localhost",serverPort = "6969",settingsPath;
    private static File settingsFile;
    private static SettingsHandler settingsHandlerInstance = null;

    /**
     * Creates and returns a settingsHandler instance
     */
    public static SettingsHandler Inst(){
        if (settingsHandlerInstance == null) {
            settingsHandlerInstance = new SettingsHandler();
            if (!settingsHandlerInstance.settingsFileExists()){
                settingsHandlerInstance.makeFile();
                settingsHandlerInstance.writeSettings();
            }
            settingsHandlerInstance.loadSettings();
        }
        return settingsHandlerInstance;
    }

    /**
     * Load settings from a text file
     */
    private void loadSettings() {
        String input;
        try {
            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.home") + "/.AniSnatcher/client/settings.conf"));
            System.out.println("reading settings");
            try {
                while ((input = br.readLine()) != null) {
                    //Splits line and then switches on the option name
                    if (input.split(" = ").length > 1) {
                        switch (input.split(" = ")[0]) {
                            case ("Download Path"):
                                downloadPath = input.split(" = ")[1];
                                break;
                            case ("Server IP"):
                                serverIP = input.split(" = ")[1];
                                break;
                            case ("Server Port"):
                                serverPort = input.split(" = ")[1];
                                break;
                        }
                    }
                }
            } catch (IOException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Exception when loading settings" + e);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Settings File not found");
        }
        if (serverPort.isEmpty()){
            serverPort = "6969";
        }
        if (serverIP.isEmpty()){
            serverIP = "localhost";
        }
    }
    private Boolean settingsFileExists(){
        File settingsFile = new File(System.getProperty("user.home") + "/.AniSnatcher/client/settings.conf");
        if (settingsFile.exists() && !settingsFile.isDirectory()){
            return true;
        } else {
            return false;
        }
    }
    /**
     * Write settings to a text file
     */
    private void writeSettings(){
        try {
            FileWriter fw = new FileWriter(settingsFile);
            fw.write("Download Path" + " = " + downloadPath);
            fw.write(System.lineSeparator());
            fw.write("Server IP" + " = " + serverIP);
            fw.write(System.lineSeparator());
            fw.write("Server Port" + " = " + serverPort);
            fw.write(System.lineSeparator());
            fw.flush();
            fw.close();
        } catch(IOException e) {
            System.out.println("Failed to write settings " + e);
        }
    }
    /**
     * Creates a new settings file in the users home directory
     */
    private void makeFile(){
        try {
            settingsPath = System.getProperty("user.home") + "/.AniSnatcher/client/";
            new File(settingsPath).mkdirs();
            settingsFile = new File(settingsPath +"settings.conf");
            if (settingsFile.createNewFile()) {
                System.out.println("File created: " + settingsFile.getName());
            } else {
                System.out.println("Settings File already exists.");
            }
        } catch (IOException e){
            System.out.println("SettingsHandler makeFile exception" + e);
        }
    }
}

