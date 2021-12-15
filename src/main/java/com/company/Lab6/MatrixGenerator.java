package com.company.Lab6;

public class MatrixGenerator {
    public static double[][] generateMatrix(int rows, int cols){
        double[][] res = new double[rows][cols];
        for(int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                res[i][j] = Math.random()*10;
            }
        }
        return res;
    }
    public static double[] matrixToArray(double[][] matrix){
        double[] arr = new double[matrix.length * matrix.length];
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                arr[i*matrix.length+j] = matrix[i][j];
            }
        }
        return arr;
    }
    public static double[][] generateDummy (int rows, int cols, boolean transpose){
        var res = new double[rows][cols];
        for (int i=0; i<rows; i++)
            for (int j=0; j<cols; j++) {
                if(transpose) res[i][j] = j*10 + i;
                else res[i][j]= i*10 + j;
            }
        return res;
    }
}
