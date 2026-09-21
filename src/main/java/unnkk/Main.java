package unnkk;

import unnkk.utils.MatrixUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class Main {
    private static final Scanner scan;

    static {
        try {
            scan = new Scanner(new File("system.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(scan.nextLine()); //no checks, 4sure


        BigDecimal[][] matrix = MatrixUtils.getMatrix(n, scan); //generating square matrix

        matrix = MatrixUtils.GaussJordan(matrix); //magic happens here

        if(matrix == null) return;
        MatrixUtils.printMatrix(matrix);

        System.out.println("System roots are:");
        for(int i = 0; i < n; i++) {
            System.out.printf("x%d = %.1f\n", i + 1, matrix[i][n].setScale(1, RoundingMode.HALF_EVEN));
        }
    }


}