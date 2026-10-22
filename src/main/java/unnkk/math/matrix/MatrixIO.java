package unnkk.math.matrix;

import unnkk.math.Fraction;
import unnkk.math.Matrix;

import java.math.BigInteger;

import static unnkk.math.matrix.MatrixMath.lineMultiplication;
import static unnkk.math.matrix.MatrixMath.lineSubstitution;

public class MatrixIO {
    public static void printGeneralSolution(Matrix reducedMatrix) {
        int rows = reducedMatrix.getRows();
        int cols = reducedMatrix.getColumns();
        int unknowns = cols - 1;
        int[] basicVars = new int[rows];

        for(int i = rows - 1; i >= 0; i--){
            int notZeroIndex = -1;
            for(int j = 0; j < unknowns; j++){
                if(!reducedMatrix.getElement(i, j).equals(new Fraction(BigInteger.ZERO))){
                    notZeroIndex = j;
                    basicVars[i] = j;
                    break;
                }
            }
            if(notZeroIndex <= 0) continue;
            for(int j = i - 1; j >= 0; j--) {
                if(!reducedMatrix.getElement(j, i).equals(new Fraction(BigInteger.ZERO))) {
                    Fraction[] result = lineSubstitution(reducedMatrix.getRow(j),
                            lineMultiplication(reducedMatrix.getRow(i), reducedMatrix.getElement(j, i)));
                    reducedMatrix.setRow(j, result);
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            StringBuilder str = new StringBuilder();
            str.append("x").append(basicVars[i] + 1).append(" = ");

            if(!reducedMatrix.getElement(i, cols - 1).equals(new Fraction(BigInteger.ZERO))){
                str.append(reducedMatrix.getElement(i, cols - 1));
            }

            for (int j = basicVars[i] + 1; j < unknowns; j++) {
                Fraction coef = reducedMatrix.getElement(i, j).negate();
                if (coef.compareTo(new Fraction(BigInteger.ZERO)) != 0) {
                    if(coef.compareTo(new Fraction(BigInteger.ZERO)) < 0) str.append(" - ");
                    else str.append(" + ");
                    str.append(coef.getAbs()).append("×x").append(j + 1);
                }

            }
            System.out.println(str);
        }

    }

    public static void printRoots(Matrix matrix){
        System.out.println("System roots are:");
        for(int i = 0; i < matrix.getRows(); i++) {
            System.out.printf("x%d = ", i + 1);
            System.out.println(matrix.getElement(i, matrix.getColumns() - 1));
        }
    }
}
