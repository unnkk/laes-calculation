package unnkk;

import unnkk.math.Matrix;
import unnkk.math.matrix.MatrixUtils;

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
        String[] input = scan.nextLine().split("\\s+");
        int m, n;
        try{
            m = Integer.parseInt(input[0]);
            n = Integer.parseInt(input[1]);
        }catch (Exception e){
            System.out.println("FATAL: Failed to parse 'system.txt', please go and check it.");
            return;
        }

        Matrix matrix = new Matrix(m, n + 1);
        matrix.fill(scan);

        MatrixUtils.gauss(matrix); //magic happens here
    }


}