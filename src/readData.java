import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class readData {
    int M = 0; int N = 0; int P = 0;
    String kasus = null;
    Map<Character, List<String[]>> blocks = new HashMap<>();

    public readData(String pathFile) {
        try {
            File caseFile = new File(pathFile);
            Scanner reader = new Scanner(caseFile);

            Character dummyChar = null;
            List<String[]> dummy_matrix = new ArrayList<>();

            int rowNumber = 0;

            while (reader.hasNextLine()) {
                String rowLine = reader.nextLine();
                rowNumber++;

                if (rowNumber == 1) {
                    String[] oneLine = rowLine.split(" ");
                    this.M = Integer.parseInt(oneLine[0]);
                    this.N = Integer.parseInt(oneLine[1]);
                    this.P = Integer.parseInt(oneLine[2]);
                }
                else if (rowNumber == 2) {
                    this.kasus = rowLine;
                }
                else {
                    char firstChar = rowLine.trim().charAt(0);

                    if (dummyChar == null || firstChar != dummyChar) {
                        if (dummyChar != null) {
                            this.blocks.put(dummyChar, dummy_matrix);
                            dummy_matrix = new ArrayList<>();
                        }
                        dummyChar = firstChar;
                    }
                    dummy_matrix.add(rowLine.split(""));
                }
            }
            if (dummyChar != null) {
                this.blocks.put(dummyChar, dummy_matrix);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
    }
}

