package unnkk.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Scanner;

public class MatrixUtils {
    private static final Scanner scan = new Scanner(System.in);
    public static final int PRECISION = 30;

    public static BigDecimal[][] GaussJordan(BigDecimal[][] matrix){
        int n = matrix.length;
        BigDecimal[][] result = new BigDecimal[n][n + 1]; //i don't really want to modify matrix
        for(int i = 0; i < n; i++){
            result[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }

        //forward elimination phase of Gaussian elimination
        for(int i = 0; i < n; i++){
            partialPivoting(result, i);
            normalizeToOne(result, i);
            for(int j = i + 1; j < n; j++) {
                if(!result[j][i].equals(BigDecimal.ZERO)) {
                    result[j] = lineSubstitution(result[j],
                            lineMultiplication(result[i], result[j][i]));
                }
            }
        }
        //back-substitution in Gaussian elimination
        for(int i = n - 1; i >= 0; i--){
            for(int j = i - 1; j >= 0; j--) {
                if(!result[j][i].equals(BigDecimal.ZERO)) {
                    result[j] = lineSubstitution(result[j],
                            lineMultiplication(result[i], result[j][i]));
                }
            }
        }

        return result;
    }

    private static void partialPivoting(BigDecimal[][] matrix, int i) {
        int maxIndex = i;
        for(int j = i + 1; j < matrix.length; j++){
            if(matrix[j][i].abs().compareTo(matrix[maxIndex][i].abs()) > 0){
                maxIndex = j;
            }
        }
        if(maxIndex != i){
            BigDecimal[] temp = matrix[i];
            matrix[i] = matrix[maxIndex];
            matrix[maxIndex] = temp;
        }
    }

    //this is really hard to work with fractions, what are resulted here
    //but, thank god, i've solved it in round() (losing precision, of course)
    private static void normalizeToOne(BigDecimal[][] matrix, int i) {
        if(!matrix[i][i].equals(BigDecimal.ONE)){
            matrix[i] = lineDivision(matrix[i], matrix[i][i]); //n divided by n is 1
        }
    }

    public static BigDecimal[] lineSubstitution(BigDecimal[] minuend, BigDecimal[] subtrahend) {
        int n = minuend.length;
        BigDecimal[] result = new BigDecimal[n];

        for(int i = 0; i < n; i++){
            result[i] =  minuend[i].subtract(subtrahend[i]);
        }

        return result;
    }

    //there's no cases where you multiply/divide lines
    //so we're multiplying/dividing every element by number
    public static BigDecimal[] lineMultiplication(BigDecimal[] multiplicand, BigDecimal multiplier){
        int n = multiplicand.length;
        BigDecimal[] result = new BigDecimal[n];

        for(int i = 0; i < n; i++){
            result[i] = multiplicand[i].multiply(multiplier);
        }

        return result;
    }

    //there's no cases where you multiply/divide lines
    //so we're multiplying/dividing every element by number
    public static BigDecimal[] lineDivision(BigDecimal[] dividend, BigDecimal divisor){
        int n = dividend.length;
        BigDecimal[] result = new BigDecimal[n];
        
        for(int i = 0; i < n; i++){
            result[i] = dividend[i].divide(divisor, PRECISION, RoundingMode.HALF_EVEN);
        }
        
        return result;
    }

    //generating matrix with n*(n + 1) size
    public static BigDecimal[][] getMatrix(int n){
        BigDecimal[][] matrix = new BigDecimal[n][n + 1];
        for(int i = 0; i < n; i++){
            System.out.println("Enter the " + (i + 1) + " line of matrix, separated by spaces:");
            BigDecimal[] input = Arrays.stream(scan.nextLine().trim().split("\\s+"))
                    .map(BigDecimal::new)
                    .limit(n + 1)
                    .toArray(BigDecimal[]::new);
            matrix[i] = input;
        }
        return matrix;
    }

    public static BigDecimal[][] getMatrix(int n, Scanner sc) {
        BigDecimal[][] matrix = new BigDecimal[n][n + 1];
        for(int i = 0; i < n; i++){
            BigDecimal[] input = Arrays.stream(sc.nextLine().trim().split("\\s+"))
                    .map(BigDecimal::new)
                    .limit(n + 1)
                    .toArray(BigDecimal[]::new);
            matrix[i] = input;
        }
        return matrix;
    }

    //generating matrix with n*(n + 1) size initiated with 'value' as every element
    public static BigDecimal[][] getMatrix(int n, BigDecimal value){
        BigDecimal[][] matrix = new BigDecimal[n][n + 1];
        for(int i = 0; i < n; i++){
            Arrays.fill(matrix[i], value);
        }
        return matrix;
    }

    public static void printMatrix(BigDecimal[][] matrix){
        int n = matrix.length;
        System.out.print("[");
        for(int i = 0; i < n; i++){
            System.out.print("[");
            for(int j = 0; j < n + 1; j++){
                System.out.print(matrix[i][j].setScale(1, RoundingMode.HALF_EVEN));
                if(j < n) System.out.print(", ");
            }
            System.out.print("]");
            if(i < n - 1) System.out.print(",\n");
        }
        System.out.println("]");
    }
}
