package it.filippocavallari.cavaanticheat.server;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class FileLogger {

    private final String fileName;
    private FileWriter fileWriter;

    public FileLogger(String fileName) throws IOException {
        this(new File(fileName));
    }

    public FileLogger(File file) throws IOException {
        fileName = file.getName();
        fileWriter = new FileWriter(file, true);
    }

    public void log(String message){
        try{
            fileWriter.append(message);
            fileWriter.flush();
        }catch (IOException e){
            Bukkit.getLogger().log(Level.SEVERE, "Error while logging on file: " + message);
        }
    }

    public void close(){
        try{
            fileWriter.close();
        }catch (IOException e){
            Bukkit.getLogger().log(Level.SEVERE, "Error while closing the logger stream");
        }
    }

    public void open() throws IOException {
        fileWriter = new FileWriter(fileName, true);
    }
}
