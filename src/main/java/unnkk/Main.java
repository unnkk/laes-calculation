package unnkk;

import unnkk.utils.MatrixUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
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
        int n, m;
        try{
            n = Integer.parseInt(input[0]);
            m = Integer.parseInt(input[1]);
        }catch (Exception e){
            System.out.println("FATAL: Failed to parse 'system.txt', please go and check it.");
            return;
        }

        if(n < m) {
            System.out.println("Count of equations is not enough, no solution for now.");
            return;
        }

        BigDecimal[][] matrix = MatrixUtils.getMatrix(n, m, scan); //generating square matrix

        MatrixUtils.GaussJordan(matrix); //magic happens here
    }


}