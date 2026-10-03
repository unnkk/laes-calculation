package unnkk.math.matrix;

import unnkk.math.Fraction;
import unnkk.math.Matrix;

import java.math.BigInteger;

public class MatrixIO {
    public static void printGeneralSolution(Matrix reducedMatrix) {
        int rows = reducedMatrix.getRows();
        int cols = reducedMatrix.getColumns();
        int unknowns = cols - 1;

        // 1. Находим базисные переменные (первые ненулевые элементы в строках)
        int[] basicVar = new int[rows];
        java.util.Arrays.fill(basicVar, -1);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < unknowns; j++) {
                if (!reducedMatrix.getElement(i, j).equals(new Fraction(BigInteger.ZERO))) {
                    basicVar[i] = j;
                    break;
                }
            }
        }

        // 2. Определяем, какие переменные свободные
        boolean[] isFree = new boolean[unknowns];
        java.util.Arrays.fill(isFree, true);
        for (int i = 0; i < rows; i++) {
            if (basicVar[i] != -1) isFree[basicVar[i]] = false;
        }

        // 3. Вывод
        for (int i = 0; i < rows; i++) {
            if (basicVar[i] == -1) continue; // Пропускаем нулевые строки

            StringBuilder str = new StringBuilder();
            str.append("x").append(basicVar[i] + 1).append(" = ");

            // Свободный член (последний столбец)
            str.append(reducedMatrix.getElement(i, unknowns));

            // Коэффициенты при свободных переменных
            for (int j = 0; j < unknowns; j++) {
                if (isFree[j]) {
                    Fraction coef = reducedMatrix.getElement(i, j).negate();
                    if (coef.compareTo(new Fraction(BigInteger.ZERO)) != 0) {
                        // Твой toString() теперь корректно отображает знак,
                        // поэтому можно просто добавлять через "+"
                        str.append(" + ").append(coef).append("×x").append(j + 1);
                    }
                }
            }
            System.out.println(str.toString().replace("+ -", "- ")); // Небольшой хак для красоты вывода
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
