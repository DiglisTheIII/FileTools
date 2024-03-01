package bin.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class FileIO extends FileTools {

    File file;

    FileWriter wr;

    byte[] fileBytes;
    String byteToString;

    public FileIO(File file) {
        super(file);

        this.file = file;
    }

    public FileIO(String path) {
        super(path);

        this.file = new File(path);
    }


    /*
     * If doAppend, the text will be added to the last line of the file, 
     * otherwise it will create a new line. This can be used on an empty file
     * but I would generally recommend to use it only when you need to append.
     * 
     * I will eventually add a writeFileLine to modify specific parts of a file.
     */
    public void writeToFile(String text, boolean doAppend) {
        try {
            if(doAppend) {
                Files.write(getPath(), text.getBytes(), StandardOpenOption.APPEND);
            } else {
                text = "\n" + text;
                Files.write(getPath(), text.getBytes(), StandardOpenOption.APPEND);
            }
            String log = "Wrote '" + text + "' to: " + file.getPath();

            System.out.println(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Converting a string array into bytes and sending it to the file. 
     * 
     * Since ascii has values for \n and \r, I can filter those out and get the raw data,
     * which I will be parsing in another class.
     *
     */
    public void writeToFile(String[] text) {
        try {
            ArrayList<Byte> bytes = new ArrayList<Byte>();
            byte[] byteArr;

            for(int i = 0; i < text.length; i++) {
                char[] temp = text[i].toCharArray();

                for(int j = 0; j < temp.length; j++) {
                    bytes.add((byte) temp[j]);
                }
            }

            byteArr = new byte[bytes.size()];
            
            for(int i = 0; i < bytes.size(); i++) {
                byteArr[i] = bytes.get(i);
            }


            Files.write(getPath(), byteArr, StandardOpenOption.WRITE);
            System.out.println("Wrote to file successfully.");
        }catch(IOException e) {
            System.out.println("File may not exist! Try using FileTools#doesFileExist or FileTools#createFile");
        }
    }

    /*
     * Converts byte array to a string object.
     * This should be more efficient than just reading the file line by line.
     */
    public String readFile() {
        byteToString = new String(getFileBytes(), StandardCharsets.UTF_8);
        return byteToString;
    }

    /*
     * Returns a specific line from a file. Line indexes
     * start at 0 (first line), so count from 0.
     */
    public String readFileLine(int line) {
        String[] step = readFile().replaceAll("\\r|\\n", ":").split(":");
        List<String> temp = Arrays.asList(step);
        ArrayList<String> lines = new ArrayList<String>();

        for(int i = 0; i < temp.size(); i++) {
            if(!temp.get(i).isEmpty()) {
                lines.add(temp.get(i));
            }
        }
        temp = null;

        if(line > lines.size()) return "Index out of bounds for lines in " + file.getName() + ".\n You can check the max index by looking at the last line in your file - 1."
            +"\nYou can also check with FileTools#getLineCount";

        return lines.get(line);
    }

    /*
     * Modifies the specified line in file. Is probably only useful for small files
     * but it works, and works well for small scale files.
     */
    public void modifyLineInFile(String text, int line) {
        try {
            List<String> lines = Files.readAllLines(getPath());
            lines.set(line, text);
            deleteFile();
            createFile();

            for(int i = 0; i < lines.size(); i++) {
                lines.set(i, lines.get(i) + "\n");
            }

            writeToFile(lines.toArray(new String[lines.size()]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Getters for any useful file info.
     * 
     * Returns all bytes from a file. Decided to use
     * bytes since they are more efficient for large scale files.
     */

     public byte[] getFileBytes() {
        try {
            fileBytes = Files.readAllBytes(getPath());
            return fileBytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Static methods for direct calls
     */

    public static void createFile(String path) {
        try {
            new File(path).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String[] text, File file, String path) throws IOException {
        ArrayList<Byte[]> bytes = new ArrayList<Byte[]>();

        for(int i = 0; i < text.length; i++) {
            bytes.add(ArrayUtils.toObject(text[i].getBytes()));
        }

        for(int i = 0; i < bytes.size(); i++) {
            Files.write(Paths.get(file.toURI()), ArrayUtils.toPrimitive(bytes.get(i)), StandardOpenOption.WRITE);
        }
    }

}
