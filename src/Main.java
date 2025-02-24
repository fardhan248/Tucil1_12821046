import java.util.*;

public class Main {
    public static final String Reset = "\u001B[0m";

    public static final String[] Color = {
            "\u001B[30m", // Black
            "\u001B[31m", // Red
            "\u001B[32m", // Green
            "\u001B[33m", // Yellow
            "\u001B[34m", // Blue
            "\u001B[35m", // Purple
            "\u001B[36m", // Cyan
            "\u001B[37m", // White
    };

    static int totalAttempts = 0;

    public static void main(String[] args) {
        String caseFile = "case_1.txt";
        String outputFile = "output_case_1.txt";

        readData data = getData("src\\" + caseFile);
        int M = data.M;
        int N = data.N;
        int P = data.P;
        String kasus = data.kasus;
        Map<Character, List<String[]>> blocks_data = data.blocks;

        List<String[][]> blocks = new ArrayList<>();
        for (List<String[]> block : blocks_data.values()) {
            blocks.add(block.toArray(new String[0][]));
        }

        String[][] puzzle_board = new String[M][N];

        List<List<String[][]>> transforms = generateTransform(blocks);

        boolean result = false;
        int index = 0;

        totalAttempts = 0;

        long sTime = System.nanoTime();
        for (int i = 0; i < transforms.size(); i++) { //
            puzzle_board = generateBoard(M, N);
            if (i == 0) {
                printMatrix(puzzle_board);
            }

            result = placeBlocks(puzzle_board, transforms, index);
            if (result) { // Jika solusi sudah ditemukan, keluar dari loop shift
                break;
            }
            shiftBlocks(transforms); // Jika solusi belum ditemukan, geser urutan penempatan block
        }                            // (block A menjadi urutan akhir, block B jadi urutan pertama, dst.)
        long eTime = System.nanoTime();

        System.out.println("\nSolusi:");
        if (result) {
            printMatrix(puzzle_board);
        }
        else {
            System.out.println("Puzzle tidak memiliki solusi.");
        }

        System.out.println("\nWaktu pencarian " + (eTime - sTime)/1000000 + " ms.");

        System.out.println("\nTotal Kasus: " + totalAttempts + "\n");

        new writeData("test\\" + outputFile, puzzle_board, result);
    }

    public static readData getData(String pathFile) {
        return new readData(pathFile);
    }

//    public static writeData importOutput(String namefile, String[][] puzzle) {
//
//    }

    public static String[][] cloneBoard(String[][] board) {
        int rows = board.length, cols = board[0].length;
        String[][] newBoard = new String[rows][cols];

        for (int i = 0; i < rows; i++) {
            newBoard[i] = Arrays.copyOf(board[i], cols);
        }

        return newBoard;
    }


    public static String[][] generateBoard(int m, int n) {
        String[][] puzzle_board = new String[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(puzzle_board[i], " ");
        }
        return puzzle_board;
    }

    public static String coloredFont(String val) {
        if (!val.equals(" ")) {
            return Color[Math.abs(val.hashCode()) % Color.length];
        }
        return "#";
    }

    public static void printMatrix(String[][] matrix) {
        for (String[] row : matrix) {
            for (String value : row) {
                System.out.print(coloredFont(value) + value + Reset);
            }
            System.out.println();
        }
    }

    public static boolean placeBlocks(String[][] puzzle, List<List<String[][]>> transform, int index) {
        if (index == transform.size()) {   // Jika sudah semua block berhasil ditempatkan, akan diperiksa,
            return (!stillEmpty(puzzle));  // apakah masih ada cell yang kosong.
        }

        for (String[][] block : transform.get(index)) {
            int nRows = block.length, nCols = block[0].length;

            for (int row = 0; row <= (puzzle.length - nRows); row++) {
                for (int col = 0; col <= (puzzle[0].length - nCols); col++) {
                    totalAttempts++;

                    if (canPlace(puzzle, block, row, col)) {     // Jika block dapat diletakkan di cell papan,
                        placeBlock(puzzle, block, row, col);     // tempatkan block di cell tersebut

                        if (placeBlocks(puzzle, transform, index + 1)) { // memeriksa block selanjutnya
                            return true; // true jika sudah tidak ada cell yang kosong
                        }

                        removeBlock(puzzle,block,row,col); // hapus block jika masih ada cell yang kosong,
                    }                                      // lalu periksa block tersebut di cell selanjutnya
                }
            }
        } // Jika masih tidak ada yang cocok, maka coba periksa variasi block tersebut

        return false; // false jika masih ada cell yang kosong
    }

    public static boolean canPlace(String[][] puzzle, String[][] piece, int sRow, int sCol) {
        int nRow = piece.length, nCol = piece[0].length;

        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (!piece[i][j].equals(" ") && !puzzle[sRow+i][sCol+j].equals(" ")) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void placeBlock(String[][] puzzle, String[][] piece, int sRow, int sCol) {
        int nRow = piece.length, nCol = piece[0].length;
        Set<String> puzzle_unique = new HashSet<>();
        Set<String> piece_unique = new HashSet<>();

        for (String[] puzzleRow : puzzle) {
            puzzle_unique.addAll(Arrays.asList(puzzleRow));
        }

        for (String[] pieceRow : piece) {
            piece_unique.addAll(Arrays.asList(pieceRow));
        }

        if (!(puzzle_unique.containsAll(piece_unique))) { // mencegah block duplikat
            for (int i = 0; i < nRow; i++) {
                for (int j = 0; j < nCol; j++) {
                    if (!piece[i][j].equals(" ")) {
                        puzzle[sRow + i][sCol + j] = piece[i][j];
                    }
                }
            }
        }
    }

    public static void removeBlock(String[][] board, String[][] block, int row, int col) {
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[0].length; j++) {
                if (!block[i][j].equals(" ")) {
                    if (board[row + i][col + j].equals(block[i][j])) {
                        board[row + i][col + j] = " ";
                    }
                    //board[row + i][col + j] = " ";
                }
            }
        }
    }

    public static boolean stillEmpty(String[][] puzzle) {
        for (String[] puzzleRow : puzzle) {
            for (String element : puzzleRow) {
                if (element.equals(" ")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void shiftBlocks(List<List<String[][]>> blocks) {
        blocks.add(blocks.removeFirst());
    }

    public static List<List<String[][]>> generateTransform(List<String[][]> blocks) {
        List<List<String[][]>> transform = new ArrayList<>();
        for (String[][] block : blocks) {
            List<String[][]> otherBlock = new ArrayList<>();
            otherBlock.add(block);
            otherBlock.add(rotate90(block));
            otherBlock.add(rotate180(block));
            otherBlock.add(rotate270(block));
            String[][] flipped = flipH(rotate270(block));
            otherBlock.add(flipped);
            otherBlock.add(rotate90(flipped));
            otherBlock.add(rotate180(flipped));
            otherBlock.add(rotate270(flipped));
            transform.add(otherBlock);
        }
        return transform;
    }

    public static String[][] rotate90(String[][] block) {
        int nRows = block.length, nCols = block[0].length;
        String[][] rotatedBlock = new String[nCols][nRows];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                rotatedBlock[j][nRows - 1 - i] = block[i][j];
            }
        }
        return rotatedBlock;
    }

    public static String[][] rotate180(String[][] block) {
        return rotate90(rotate90(block));
    }

    public static String[][] rotate270(String[][] block) {
        return rotate90(rotate180(block));
    }

    public static String[][] flipH(String[][] block) {
        int nRows = block.length, nCols = block[0].length;
        String[][] flippedBlock = new String[nRows][nCols];
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                flippedBlock[i][nCols - 1 - j] = block[i][j];
            }
        }
        return flippedBlock;
    }

}