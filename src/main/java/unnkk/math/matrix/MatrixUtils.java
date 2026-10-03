package unnkk.math.matrix;

import unnkk.math.Fraction;
import unnkk.math.Matrix;

import java.math.BigInteger;

import static unnkk.math.matrix.MatrixMath.*;
import static unnkk.math.matrix.MatrixIO.printGeneralSolution;
import static unnkk.math.matrix.MatrixIO.printRoots;

public class MatrixUtils {
    private static void gaussFwd(Matrix matrix){
        int limit = Math.min(matrix.getRows(), matrix.getColumns() - 1);
        for(int i = 0; i < limit; i++){
            partialPivoting(matrix, i);
            normalizeToOne(matrix, i);
            for(int j = i + 1; j < matrix.getRows(); j++) {
                if(!matrix.getElement(j, i).equals(new Fraction(BigInteger.ZERO))) {
                    Fraction[] result = lineSubstitution(matrix.getRow(j),
                            lineMultiplication(matrix.getRow(i), matrix.getElement(j, i)));
                    matrix.setRow(j, result);
                }
            }
        }
    }

    private static void gaussBwd(Matrix matrix){
        for(int i = matrix.getRows() - 1; i >= 0; i--){
            for(int j = i - 1; j >= 0; j--) {
                if(!matrix.getElement(j, i).equals(new Fraction(BigInteger.ZERO))) {
                    Fraction[] result = lineSubstitution(matrix.getRow(j),
                            lineMultiplication(matrix.getRow(i), matrix.getElement(j, i)));
                    matrix.setRow(j, result);
                }
            }
        }
    }

    public static void gauss(Matrix matrix){
        Matrix result = new Matrix(matrix); //i don't really want to modify matrix

        gaussFwd(result);
        if(!checkConsistency(result)){
            System.out.println(result);
            System.out.println("System is incompatible. X (solution vector) is empty.");
            return;
        }
        result = removeZeroedRows(result);


        if(result.getRows() == result.getColumns() - 1) {
            //back-substitution in Gaussian elimination
            gaussBwd(result);
            System.out.println(result);
            printRoots(result);
        }
        else{
            System.out.println(result);
            printGeneralSolution(result);
        }
    }

    private static boolean checkConsistency(Matrix matrix) {
        for (int i = 0; i < matrix.getRows(); i++) {
            Fraction[] row = matrix.getRow(i);
            if (row[0].equals(new Fraction(BigInteger.ZERO))) {
                boolean zeroed = true;
                for (int j = 1; j < row.length - 1; j++) {
                    if (!row[j].equals(new Fraction(BigInteger.ZERO))) {
                        zeroed = false;
                        break;
                    }
                }

                if (zeroed && !row[row.length - 1].equals(new Fraction(BigInteger.ZERO))) return false;
            }
        }
        return true;
    }

    private static void partialPivoting(Matrix matrix, int i) {
        int maxIndex = i;
        for(int j = i + 1; j < matrix.getRows(); j++){
            if( matrix.getElement(j, i).getAbs() > matrix.getElement(maxIndex, i).getAbs()){
                maxIndex = j;
            }
        }
        if(maxIndex != i){
            matrix.swapRows(maxIndex, i);
        }
    }

    //this is really hard to work with fractions, what are resulted here
    //but, thank god, i've solved it in round() (losing precision, of course)
    private static void normalizeToOne(Matrix matrix, int i) {
        if(!matrix.getElement(i, i).equals(new Fraction(BigInteger.ONE)) &&
                !matrix.getElement(i, i).equals(new Fraction(BigInteger.ZERO))){
            Fraction[] result = lineDivision(matrix.getRow(i), matrix.getElement(i, i));
            matrix.setRow(i, result); //n divided by n is 1
        }
    }
}