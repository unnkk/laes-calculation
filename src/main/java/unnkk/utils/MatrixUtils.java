package unnkk.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MatrixUtils {
    public static final int PRECISION = 30;
    private static final BigDecimal EPSILON = new BigDecimal("0E-30");

    private static void gaussFwd(BigDecimal[][] matrix){
        int n = matrix.length;
        int m = matrix[0].length - 1;

        for(int i = 0; i < m; i++){
            partialPivoting(matrix, i);
            normalizeToOne(matrix, i);
            for(int j = i + 1; j < n; j++) {
                if(!matrix[j][i].equals(BigDecimal.ZERO)) {
                    matrix[j] = lineSubstitution(matrix[j],
                            lineMultiplication(matrix[i], matrix[j][i]));
                }
            }
        }
    }

    private static void gaussBwd(BigDecimal[][] matrix){
        int n = matrix.length;

        for(int i = n - 1; i >= 0; i--){
            for(int j = i - 1; j >= 0; j--) {
                if(!matrix[j][i].equals(BigDecimal.ZERO)) {
                    matrix[j] = lineSubstitution(matrix[j],
                            lineMultiplication(matrix[i], matrix[j][i]));
                }
            }
        }
    }

    public static void gauss(BigDecimal[][] matrix){
        int m = matrix.length;
        int n = matrix[0].length - 1;

        BigDecimal[][] result = new BigDecimal[m][n + 1]; //i don't really want to modify matrix
        for(int i = 0; i < m; i++){
            result[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }

        gaussFwd(result);
        if(!checkConsistency(result)){
            printMatrix(result);
            System.out.println("System is incompatible. X (solution vector) is empty.");
            return;
        }
        result = removeZeroRows(result);

        //back-substitution in Gaussian elimination
        gaussBwd(result);

        printMatrix(result);
        if(matrix.length == matrix[1].length - 1) {
            printRoots(result);
        }
        else{
            printGeneralSolution(result);
        }
    }

    private static void printGeneralSolution(BigDecimal[][] matrix) {
        for(int i = 0; i < matrix.length; i++){
            StringBuilder str = new StringBuilder();
            str.append("x").append(i + 1).append(" = ").append(matrix[i][matrix[0].length - 1].doubleValue());
            for(int j = 0; j < matrix[0].length - 1; j++){
                if(j == i) continue;
                BigDecimal temp = matrix[i][j].negate();
                if(temp.compareTo(BigDecimal.ZERO) < 0){
                    str.append(" - ").append(temp.abs().doubleValue()).append("×x").append(j + 1);
                }else if(temp.compareTo(BigDecimal.ZERO) > 0){
                    str.append(" + ").append(temp.doubleValue()).append("×x").append(j + 1);
                }
            }
            System.out.println(str);
        }
    }

    private static BigDecimal[][] removeZeroRows(BigDecimal[][] matrix) {
        ArrayList<BigDecimal[]> strings = new ArrayList<>();
        int m = 0;
        for(int i = 0; i < matrix.length; i++){
            if(matrix[i][i].equals(BigDecimal.ZERO)){
                for(int j = i + 1; j < matrix[0].length - 1; j++){
                    if(!matrix[i][j].equals(BigDecimal.ZERO)) {
                        m++;
                        strings.add(matrix[i]);
                        break;
                    }
                }
            }else {
                m++;
                strings.add(matrix[i]);
            }
        }

        BigDecimal[][] result = new BigDecimal[m][matrix[0].length];
        for(int i = 0; i < strings.size(); i++){
            result[i] = strings.get(i);
        }
        return result;
    }

    private static boolean checkConsistency(BigDecimal[][] matrix) {
        for (BigDecimal[] bigDecimals : matrix) {
            if (bigDecimals[0].equals(BigDecimal.ZERO)) {
                boolean zeroed = true;
                for (int j = 1; j < bigDecimals.length - 1; j++) {
                    if (!bigDecimals[j].equals(BigDecimal.ZERO)) {
                        zeroed = false;
                        break;
                    }
                }

                if (zeroed && !bigDecimals[bigDecimals.length - 1].equals(BigDecimal.ZERO)) return false;
            }
        }
        return true;
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
        if(!matrix[i][i].equals(BigDecimal.ONE) && !matrix[i][i].equals(BigDecimal.ZERO)){
            matrix[i] = lineDivision(matrix[i], matrix[i][i]); //n divided by n is 1
        }
    }

    public static BigDecimal[] lineSubstitution(BigDecimal[] minuend, BigDecimal[] subtrahend) {
        int m = minuend.length;
        BigDecimal[] result = new BigDecimal[m];

        for(int i = 0; i < m; i++){
            result[i] =  minuend[i].subtract(subtrahend[i]);
            if(result[i].abs().compareTo(new BigDecimal("0E-30")) <= 0) result[i] = BigDecimal.ZERO;
        }

        return result;
    }

    //there's no cases where you multiply/divide lines
    //so we're multiplying/dividing every element by number
    public static BigDecimal[] lineMultiplication(BigDecimal[] multiplicand, BigDecimal multiplier){
        int m = multiplicand.length;
        BigDecimal[] result = new BigDecimal[m];

        for(int i = 0; i < m; i++){
            result[i] = multiplicand[i].multiply(multiplier);
        }

        return result;
    }

    //there's no cases where you multiply/divide lines
    //so we're multiplying/dividing every element by number
    public static BigDecimal[] lineDivision(BigDecimal[] dividend, BigDecimal divisor){
        int m = dividend.length;
        BigDecimal[] result = new BigDecimal[m];
        
        for(int i = 0; i < m; i++){
            result[i] = dividend[i].divide(divisor, PRECISION, RoundingMode.HALF_EVEN);
            if(result[i].abs().compareTo(EPSILON) <= 0) result[i] = BigDecimal.ZERO;
        }
        
        return result;
    }

    public static BigDecimal[][] getMatrix(int n, int m, Scanner sc) {
        BigDecimal[][] matrix = new BigDecimal[m][n + 1];
        for(int i = 0; i < m; i++){
            BigDecimal[] input = Arrays.stream(sc.nextLine().trim().split("\\s+"))
                    .map(BigDecimal::new)
                    .limit(n + 1)
                    .toArray(BigDecimal[]::new);
            matrix[i] = input;
        }
        return matrix;
    }

    //generating matrix with m*(n + 1) size initiated with 'value' as every element
    public static BigDecimal[][] getMatrix(int n, int m, BigDecimal value){
        BigDecimal[][] matrix = new BigDecimal[m][n + 1];
        for(int i = 0; i < m; i++){
            Arrays.fill(matrix[i], value);
        }
        return matrix;
    }

    public static void printMatrix(BigDecimal[][] matrix){
        int m = matrix.length;
        int n = matrix[0].length - 1;
        System.out.print("[");
        for(int i = 0; i < m; i++){
            System.out.print("[");
            for(int j = 0; j < n + 1; j++){
                BigDecimal toPrint = matrix[i][j];
                if(!toPrint.equals(BigDecimal.ZERO))
                    System.out.print(matrix[i][j].setScale(1, RoundingMode.HALF_EVEN));
                else System.out.print(0);
                if(j < n) System.out.print(", ");
            }
            System.out.print("]");
            if(i < m - 1) System.out.print(",\n");
        }
        System.out.println("]");
    }

    private static void printRoots(BigDecimal[][] matrix){
        int n = matrix[0].length;
        int m = matrix.length;
        System.out.println("System roots are:");
        for(int i = 0; i < m; i++) {
            System.out.printf("x%d = %.1f\n", i + 1, matrix[i][n - 1].setScale(2, RoundingMode.HALF_EVEN));
        }
    }
}