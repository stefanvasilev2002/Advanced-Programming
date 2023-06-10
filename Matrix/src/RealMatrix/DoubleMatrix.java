package RealMatrix;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

class InsufficientElementsException extends Exception{
    public InsufficientElementsException(String message) {
        super(message);
    }
}
class InvalidRowNumberException extends Exception {
    public InvalidRowNumberException(String message) {
        super(message);
    }
}
class InvalidColumnNumberException extends Exception {
    public InvalidColumnNumberException(String message) {
        super(message);
    }
}
class DoubleMatrix {
    private double[][] a;
    private int m;
    private int n;

    public DoubleMatrix(double[] a, int m, int n) throws InsufficientElementsException {
        this.m = m;
        this.n = n;
        if(a.length<m*n)
        {
            throw new InsufficientElementsException("Insufficient number of elements");

        }
        else if(a.length>m*n)
        {
            int counter=a.length-m*n;
            this.a=new double[m][n];
            for(int i=0; i<m; i++)
            {
                for(int j=0; j<n; j++)
                {
                    this.a[i][j]=a[counter++];
                }
            }
        }
        else {
            int counter=0;
            this.a=new double[m][n];
            for(int i=0; i<m; i++)
            {
                for(int j=0; j<n; j++)
                {
                    this.a[i][j]=a[counter++];
                }
            }
        }
    }
    public String getDimensions()
    {
        return String.format("[%d x %d]",m,n);
    }
    public int rows() {
        return m;
    }
    public int columns() {
        return n;
    }
    public double maxElement(double[] arr) {
        return Arrays.stream(arr).max().getAsDouble();
    }
    public double maxElementAtRow(int row)throws InvalidRowNumberException
    {
        if(row>m || row<1)
        {
            throw new InvalidRowNumberException("Invalid row number");
        }
        return maxElement(this.a[row-1]);
    }
    public double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        if(column>n || column<1) throw new InvalidColumnNumberException("Invalid column number");
        double[] maxArr = new double[m];
        for(int i=0;i<m;i++){
            maxArr[i] = this.a[i][column-1];
        }
        return maxElement(maxArr);
    }
    public double sum() {
        double sum = 0.0;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++) {
                sum+=a[i][j];
            }
        }
        return sum;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DoubleMatrix that = (DoubleMatrix) o;
        return Arrays.deepEquals(a, that.a);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(a);
    }
    @Override
    public String toString() {
        String finalString = "";
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++) {
                finalString += String.format("%.2f",a[i][j]);
                if(j!=n-1) finalString+="\t";
            }
            if(i!=m-1) finalString+="\n";
        }
        return finalString;
    }
    public double[] toSortedArray() {
        double[] fullArr = new double[m*n];
        int fullCounter = 0;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++) {
                fullArr[fullCounter] = a[i][j];
                fullCounter++;
            }
        }
        Arrays.sort(fullArr);
        for(int i = 0; i < fullArr.length / 2; i++)
        {
            double temp = fullArr[i];
            fullArr[i] = fullArr[fullArr.length - i - 1];
            fullArr[fullArr.length - i - 1] = temp;
        }
        return fullArr;
    }
}

class MatrixReader {
    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException {
        Scanner sc = new Scanner(input);
        int m = sc.nextInt();
        int n = sc.nextInt();
        double[] matrix = new double[m * n];
        for (int i = 0; i < m * n; i++) {
            matrix[i] = sc.nextDouble();
        }
        return new DoubleMatrix(matrix, m, n);
    }
    }