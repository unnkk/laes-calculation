import unnkk.utils.MatrixUtils;

import java.io.File;
import java.io.FileNotFoundException;
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
        System.out.println("This program calculates the determinant of square matrix with n*n size and B column");
        int n = Integer.parseInt(scan.nextLine()); //no checks, 4sure

        double[][] matrix = MatrixUtils.getMatrix(n, scan); //generating square matrix

        matrix = MatrixUtils.GaussJordan(matrix); //magic happens here

        MatrixUtils.printMatrix(matrix);

        System.out.println("System roots are:");
        for(int i = 0; i < n; i++){
            System.out.printf("x%d = %.3f\n", i + 1, matrix[i][n]);
        }
    }


}