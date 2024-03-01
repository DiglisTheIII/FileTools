package bin.util.parsing;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;

import com.google.common.primitives.Bytes;

import bin.util.FileIO;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.Paths;

public class FileParser extends FileIO {

    private File userFile;

    public FileParser(File file) {
        super(file);

        userFile = file;
    }
    
    public FileParser(String path) {
        super(path);

        userFile = new File(path);
    }

    public String getCharVals() {
        //byte[] bytes = getFileBytes();
        String valueList = "";
        for(byte b : getFileBytes()) {
            valueList += (char) b + " : " + b + "\n";
        }
        return valueList;
    }

    public List<Byte> modifyCharacter(char prev, char after, int runs) {
        byte bPrev = (byte) prev;
        byte bAfter = (byte) after;

        List<Byte> chars = Arrays.asList(ArrayUtils.toObject(getFileBytes()));
        assert runs <= chars.size();
        for(int i = 0; i < runs; i++) {
            if(chars.get(i) == bPrev) {
                chars.set(i, bAfter);
            }
        }

        return chars;
    }

    public void modifyCharacterInFile(List<Byte> modified) {
        
        byte[] bytes = Bytes.toArray(modified);
        deleteFile();
        createFile();
        try {
            Thread.sleep(500);
            //userFile.createNewFile();
            Files.write(Paths.get(userFile.toURI()), bytes, StandardOpenOption.WRITE);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        
    }
}
