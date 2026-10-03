package unnkk.math.matrix;

import unnkk.math.Fraction;
import unnkk.math.Matrix;

import java.math.BigInteger;
import java.util.ArrayList;

public class MatrixMath {
    public static Fraction[] lineSubstitution(Fraction[] minuend, Fraction[] subtrahend) {
        int m = minuend.length;
        Fraction[] result = new Fraction[m];

        for(int i = 0; i < m; i++){
            result[i] =  minuend[i].subtract(subtrahend[i]);
        }

        return result;
    }

    //there's no cases where you multiply/divide lines
    //so we're multiplying/dividing every element by number
    public static Fraction[] lineMultiplication(Fraction[] multiplicand, Fraction multiplier){
        int m = multiplicand.length;
        Fraction[] result = new Fraction[m];

        for(int i = 0; i < m; i++){
            result[i] = multiplicand[i].multiply(multiplier);
        }

        return result;
    }

    //there's no cases where you multiply/divide lines
    //so we're multiplying/dividing every element by number
    public static Fraction[] lineDivision(Fraction[] dividend, Fraction divisor){
        int m = dividend.length;
        Fraction[] result = new Fraction[m];

        for(int i = 0; i < m; i++){
            result[i] = dividend[i].divide(divisor);
        }

        return result;
    }

    public static Matrix removeZeroedRows(Matrix matrix) {
        ArrayList<Fraction[]> strings = new ArrayList<>();
        int rows = 0;
        for(int i = 0; i < matrix.getRows(); i++){
            boolean allZero = true;
            // Проверяем все столбцы, кроме последнего (свободного члена)
            for(int j = 0; j < matrix.getColumns() - 1; j++){
                if(!matrix.getElement(i, j).equals(new Fraction(BigInteger.ZERO))) {
                    allZero = false;
                    break;
                }
            }

            // Если не вся строка нулевая (или если нулевая, но свободный член тоже 0 - это 0=0, удаляем)
            // В твоей логике: если строка не нулевая, оставляем.
            if(!allZero) {
                rows++;
                strings.add(matrix.getRow(i));
            }
        }

        Matrix result = new Matrix(rows, matrix.getColumns());
        for(int i = 0; i < strings.size(); i++){
            result.setRow(i, strings.get(i));
        }
        return result;
    }

    public static long gcd(long a, long b){
        a = java.lang.Math.abs(a);
        b = java.lang.Math.abs(b);
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }
}
