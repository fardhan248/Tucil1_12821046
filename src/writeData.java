import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class writeData {
    public writeData(String nameFile, String[][] puzzle, boolean result) {
        try {
            FileWriter writer = new FileWriter(nameFile);
            if (result) {
                for (String[] rowVal : puzzle) {
                    for (String val : rowVal) {
                        writer.write(val);
                    }
                    writer.write("\n");
                }
            }
            else {
                writer.write("Tidak ada solusi.");
            }
            writer.close();
            System.out.println("File disimpan sebagai " + nameFile);
        }
        catch (IOException e) {
            System.out.println("An error occured!");
            e.printStackTrace();
        }
    }
}
