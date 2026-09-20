package unnkk.utils;

import java.util.Arrays;
import java.util.Scanner;

public class MatrixUtils {
    private static final Scanner scan = new Scanner(System.in);
    public static double[][] GaussJordan(double[][] matrix){
        int n = matrix.length;
        double[][] result = matrix; //i don't really want to modify matrix

        //forward elimination phase of Gaussian elimination
        for(int i = 0; i < n; i++){
            normalizeToOne(result, i);
            for(int j = i + 1; j < n; j++) {
                result[j] = lineSubtitution(result[j],
                        lineMultiplication(result[i], result[j][i] / result[i][i]));
            }
        }
        //back-substitution in Gaussian elimination
        for(int i = n - 1; i >= 0; i--){
            for(int j = i - 1; j >= 0; j--) {
                result[j] = lineSubtitution(result[j],
                        lineMultiplication(result[i], result[j][i] / result[i][i]));
            }
        }

        return result;
    }

    //this is really hard to work with fractions, what are resulted here
    //but, thank god, i've solved it in round() (losing precision, of course)
    private static void normalizeToOne(double[][] matrix, int i) {
        int n = matrix.length;
        if(matrix[i][i] != 1){
            //if matrix[i][i] is zero, you cannot just divide it to make 1, so we're creating non-zero value
            for(int j = i + 1; matrix[i][i] == 0 && j < n; j++){
                if(matrix[j][i] != 0){
                    matrix[i] = lineSubtitution(matrix[i], matrix[j]);
                }
            }
            for(int j = i + 1; matrix[i][i] == 0 && j < n + 1; j++){
                if(matrix[i][j] != 0){
                    matrix[i] = lineSubtitution(matrix[i], matrix[j]);
                }
            }

            matrix[i] = lineDivision(matrix[i], matrix[i][i]); //n divided by n is 1
        }
    }

    public static double[] lineSubtitution(double[] minuend, double[] subtrahend) {
        int n = minuend.length;
        double[] result = new double[n];

        for(int i = 0; i < n; i++){
            result[i] =  round(minuend[i] - subtrahend[i]);
        }

        return result;
    }

    //there's no cases where you multiply/divide lines
    //so we're multiplying/dividing every element by number
    public static double[] lineMultiplication(double[] multiplicand, double multiplier){
        int n = multiplicand.length;
        double[] result = new double[n];

        for(int i = 0; i < n; i++){
            result[i] = round(multiplicand[i] * multiplier);
        }

        return result;
    }

    //there's no cases where you multiply/divide lines
    //so we're multiplying/dividing every element by number
    public static double[] lineDivision(double[] dividend, double divisor){
        int n = dividend.length;
        double[] result = new double[n];
        
        for(int i = 0; i < n; i++){
            result[i] = round(dividend[i] / divisor);
        }
        
        return result;
    }

    //generating matrix with n*(n + 1) size
    public static double[][] getMatrix(int n){
        double[][] matrix = new double[n][n + 1];
        for(int i = 0; i < n; i++){
            System.out.println("Enter the " + (i + 1) + " line of matrix, separated by spaces:");
            double[] input = Arrays.stream(scan.nextLine().trim().split("\\s+"))
                    .mapToDouble(Double::parseDouble)
                    .limit(n + 1)
                    .toArray();
            matrix[i] = input;
        }
        return matrix;
    }

    public static double[][] getMatrix(int n, Scanner sc) {
        double[][] matrix = new double[n][n + 1];
        for(int i = 0; i < n; i++){
            double[] input = Arrays.stream(sc.nextLine().trim().split("\\s+"))
                    .mapToDouble(Double::parseDouble)
                    .limit(n + 1)
                    .toArray();
            matrix[i] = input;
        }
        return matrix;
    }

    //generating matrix with n*(n + 1) size initiated with 'value' as every element
    public static double[][] getMatrix(int n, int value){
        double[][] matrix = new double[n][n + 1];
        for(int i = 0; i < n; i++){
            Arrays.fill(matrix[i], value);
        }
        return matrix;
    }

    public static void printMatrix(double[][] matrix){
        int n = matrix.length;
        System.out.print("[");
        for(int i = 0; i < n; i++){
            System.out.print("[");
            for(int j = 0; j < n + 1; j++){
                System.out.print(matrix[i][j]);
                if(j < n) System.out.print(", ");
            }
            System.out.print("]");
            if(i < n - 1) System.out.print(",\n");
        }
        System.out.println("]");
    }

    //ts is ass, rounding
    private static double round(double value) {
        if (value != 0 && Math.abs(value % 1) >= 1/Math.pow(10, 3)) {
            return Math.round(value * Math.pow(10, 4)) / Math.pow(10, 4);
        } else return (int)value;
    }

    //ass with control of places for rounding
    private static double round(double value, int places) {
        if (value != 0 && Math.abs(value % 1) >= 1/Math.pow(10, places - 1)) {
            return Math.round(value * Math.pow(10, places)) / Math.pow(10, places);
        } else return (int)value;
    }
}
