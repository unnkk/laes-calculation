package unnkk.math;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Matrix {
    private final int rows;
    private final int columns;
    private final Fraction[][] matrixArray;

    public Matrix(int rows, int columns, Fraction value){
        this.rows = rows;
        this.columns = columns;
        matrixArray = new Fraction[rows][columns];

        for(int i = 0; i < rows; i++){
            Arrays.fill(matrixArray[i], value);
        }
    }

    public Matrix(int rows, int columns){
        this(rows, columns, new Fraction(BigInteger.ZERO));
    }

    public Matrix(Matrix original){
        this(original.getRows(), original.getColumns());
        for(int i = 0; i < original.getRows(); i++){
            this.setRow(i, Arrays.copyOf(original.getRow(i), original.getColumns()));
        }
    }

    public void fill(Fraction value){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                matrixArray[i][j] = value;
            }
        }
    }

    public void fill(Scanner scan){
        for(int i = 0; i < rows; i++){
            Fraction[] input = Arrays.stream(scan.nextLine().trim().split("\\s+"))
                    .map(Fraction::new)
                    .limit(columns)
                    .toArray(Fraction[]::new);
            if(input.length != columns) {
                throw new IllegalArgumentException("Ожидалось " + columns + " элементов, получено " + input.length);
            }
            matrixArray[i] = input;
        }
    }

    public void swapRows(int a, int b){
        Fraction[] temp = matrixArray[a];
        matrixArray[a] = matrixArray[b];
        matrixArray[b] = temp;
    }



    //OVERRIDE AREA
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[");
        for(int i = 0; i < rows; i++){
            result.append("[");
            for(int j = 0; j < columns; j++){
                result.append(matrixArray[i][j]);

                if(j < columns - 1) result.append(", ");
            }
            result.append("]");
            if(i < rows - 1) result.append(",\n");
        }
        result.append("]\n");
        return result.toString();
    }

    //GETTER/SETTER AREA
    //Getters
    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Fraction getElement(int row, int column){
        if(row >= rows || row < 0 || column >= columns || column < 0) return null;
        return matrixArray[row][column];
    }

    public Fraction[] getRow(int row){
        return matrixArray[row];
    }

    //Setters
    public boolean setElement(int row, int column, Fraction e){
        if(row < rows && row >= 0
                && column < columns && column >= 0){
            matrixArray[row][column] = e;
            return true;
        }
        return false;
    }

    public boolean setRow(int row, Fraction[] elements){
        if(elements.length == columns && row < rows && row >= 0){
            matrixArray[row] = elements;
            return true;
        }
        return false;
    }
}