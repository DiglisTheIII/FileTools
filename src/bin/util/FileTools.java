package bin.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileTools {
    
    private static File file;
    URI uri;
    Path path;

    public FileTools(File f) {
        file = f;
        uri = f.toURI();
        path = Paths.get(uri);
    }

    public FileTools(String path) {
        file = new File(path);
        uri = file.toURI();
        this.path = Paths.get(uri);
    }

    

    public boolean doesFileExist() {
        if(file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void createFile() {
        try {  
            if(!doesFileExist()) {
                file.createNewFile();
            } else {
                System.out.println("File already exists.");
            }
        } catch(IOException e) {
            System.out.println("Bad file. \n" + e.getLocalizedMessage() + "\nCheck if the path is accessible, or \nthat the location exists.");
        }
    }

    public void deleteFile() {
        file.delete();
        System.out.println("File: " + file.getAbsolutePath() + " deleted successfully.");
    }

    public int getLineCount() {
        try {
            List<String> lines = Files.readAllLines(path);
            return lines.size();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public URI getURI() {
        return this.uri;
    }

    public Path getPath() {
        return this.path;
    }

}