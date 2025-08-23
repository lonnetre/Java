public class Matrix_L {

    /**
     * Prints a 2D array with each of its rows in a new line
     * and the columns separated by a space
     * @param matrix a 2D array (first dimension is the row, second dimension is the column)
     */
    public static void print(double[][] matrix) {
        if (matrix == null) {
            return;
        }

        for (int r = 0; r < matrix.length; r++) {
            Matrix_L.print(matrix[r]);
        }
    }

    /**
     * Prints a 1D array in one line with each entry separated by a space
     * @param matrixRow a 1D array
     */
    private static void print(double[] matrixRow) {
        if (matrixRow != null) {

            // Print all entries but the last one with a space behind it
            for (int c = 0; c < matrixRow.length - 1; c++) {
                System.out.print(matrixRow[c] + " ");
            }

            // Print the last entry (without a space)
            if (matrixRow.length > 0) {
                System.out.print(matrixRow[matrixRow.length - 1]);
            }
        }

        System.out.println();
    }


    /**
     * Multiplies all elements in the 2D array by the given factor
     * @param matrix a 2D array (first dimension is the row, second dimension is the column)
     * @param factor a factor to multiply the array with
     * @return a new array whose elements are the product of the given matrix and the factor.
     * The method returns <code>null</code> if the given matrix is <code>null</code>.
     */
    public static double[][] multiply(double[][] matrix, double factor) {
        if (matrix == null) {
            return null;
        }

        // New array with same number of rows as the input array
        double[][] result = new double[matrix.length][];

        for (int r = 0; r < matrix.length; r++) {
            double[] matrixRow = matrix[r];

            // If column entries for this row actually exist...
            if (matrixRow != null) {

                // ... create an equally sized row for the result array...
                result[r] = new double[matrixRow.length];

                // ... and save the multiplication result there
                for (int c = 0; c < matrixRow.length; c++) {
                    result[r][c] = matrix[r][c] * factor;
                }
            }
        }

        return result;
    }


    /**
     * Computes the average per row.
     * @param matrix a 2D array (first dimension is the row, second dimension is the column)
     * @return a 1D array whose elements correspond to the average of the respective row in
     * the given matrix. The method returns <code>null</code> if the given matrix is <code>null</code>.
     */
    public static double[] computeRowMeans(double[][] matrix) {
        if (matrix == null) {
            return null;
        }

        // Create a new array to store the mean for every row
        double[] result = new double[matrix.length];

        for (int r = 0; r < result.length; r++) {
            double[] matrixRow = matrix[r];
            if (matrixRow == null) {
                continue;
            }

            // ... sum up all the entries in this row...
            double sum = 0;
            for (int c = 0; c < matrixRow.length; c++) {
                sum += matrixRow[c];
            }

            // ... and divide by their number to compute the average
            if (matrixRow.length == 0) {
                result[r] = 0;
            } else {
                result[r] = sum / matrixRow.length;
            }
        }

        return result;
    }


    /**
     * Computes the average per column.
     * @param matrix a 2D array (first dimension is the row, second dimension is the column)
     * @return a 1D array whose elements correspond to the average of the respective column in
     * the given matrix. The method returns <code>null</code> if the given matrix is <code>null</code>.
     */
    public static double[] computeColumnMeans(double[][] matrix) {
        if (matrix == null) {
            return null;
        }

        int numColumns = Matrix_L.getNumColumns(matrix);

        // If there is not a single existing column entry, return an empty array of size 0
        if (numColumns < 0) {
            return new double[0];
        }

        double[] columnMeans = new double[numColumns];
        int[] numColumnEntries = new int[numColumns];

        for (int r = 0; r < matrix.length; r++) {
            double[] matrixRow = matrix[r];
            if (matrixRow == null) {
                continue;
            }

            // Sum up the column entries directly in the columnMeans array
            for (int c = 0; c < matrixRow.length; c++) {
                columnMeans[c] += matrixRow[c];
                numColumnEntries[c] += 1;
            }
        }

        // Divide the sums per column by the number of elements per column
        for (int i = 0; i < columnMeans.length; i++) {
            columnMeans[i] = columnMeans[i] / numColumnEntries[i];
        }

        return columnMeans;
    }


    /**
     * Returns the number of columns of this matrix by finding the longest row
     * @param matrix a 2D array (first dimension is the row, second dimension is the column)
     * @return the number of columns or <code>-1</code> if every row entry is <code>null</code>
     */
    private static int getNumColumns(double[][] matrix) {
        int numColumns = -1;

        for (int r = 0; r < matrix.length; r++) {
            double[] matrixRow = matrix[r];

            if (matrixRow != null && matrixRow.length > numColumns) {
                numColumns = matrixRow.length;
            }
        }

        return numColumns;
    }


    /***
     * Computes a new 1D array whose elements correspond to the values
     * from the given matrix
     * @param matrix a 2D array (first dimension is the row, second dimension is the column)
     * @param linearizeByCols <code>true</code> if the values should be ordered in the column-wise
     *                        reading direction of the matrix or <code>false</code> if row-wise
     * @return the new 1D array
     */
    public static double[] linearize(double[][] matrix, boolean linearizeByCols) {
        if (matrix == null) {
            return null;
        }

        // Differentiate between row and column case based on parameter linearizeByCols
        if (!linearizeByCols) {
            return Matrix_L.linearizeByRow(matrix);
        } else {
            return Matrix_L.linearizeByColumn(matrix);
        }
    }

    /**
     * Computes a new 1D array whose elements correspond to the values
     * from the given matrix, read in row-wise order
     * @param matrix a 2D array (first dimension is the row, second dimension is the column)
     * @return the new 1D array
     */
    private static double[] linearizeByRow(double[][] matrix) {
        double[] result = new double[Matrix_L.getNumElements(matrix)];
        int index = 0;

        for (int r = 0; r < matrix.length; r++) {
            double[] matrixRow = matrix[r];
            if (matrixRow == null) {
                continue;
            }

            for (int c = 0; c < matrixRow.length; c++) {
                result[index] = matrixRow[c];
                index += 1;
            }
        }

        return result;
    }


    /**
     * Computes a new 1D array whose elements correspond to the values
     * from the given matrix, read in column-wise order
     * @param matrix a 2D array (first dimension is the row, second dimension is the column)
     * @return the new 1D array
     */
    private static double[] linearizeByColumn(double[][] matrix) {

        int numColumns = getNumColumns(matrix);
        if (numColumns <= 0) {
            return new double[0];
        }

        double[] result = new double[Matrix_L.getNumElements(matrix)];
        int index = 0;

        for (int c = 0; c < numColumns; c++) {
            for (int r = 0; r < matrix.length; r++) {

                if (matrix[r] != null && c < matrix[r].length) {
                    result[index] = matrix[r][c];
                    index += 1;
                }
            }
        }

        return result;
    }


    /**
     * Computes the number of elements in the given matrix
     * @param matrix a 2D array (first dimension is the row, second dimension is the column)
     * @return the number of elements
     */
    private static int getNumElements(double[][] matrix) {
        int numElements = 0;

        for (int r = 0; r < matrix.length; r++) {
            double[] matrixRow = matrix[r];
            if (matrixRow == null) {
                continue;
            }

            numElements += matrixRow.length;
        }

        return numElements;
    }

    /**
     * Helper method to test the above methods with several settings.
     * Note: Test code should normally be put in a separate class. In this example,
     * the Matrix code and the test code are in the same class, such that only a
     * single file has to be downloaded for your convenience.
     * @param matrix a 2D array (first dimension is the row, second dimension is the column)
     * @param factor the factor to multiply the matrix entries with
     */
    private static void testImplementation(double[][] matrix, double factor) {
        System.out.println("--- NEW TEST RUN ---");

        System.out.println("--- print ---");
        Matrix_L.print(matrix);
        System.out.println();

        System.out.println("--- multiply ---");
        double[][] multiplyResult = Matrix_L.multiply(matrix, factor);
        Matrix_L.print(multiplyResult);
        System.out.println();

        System.out.println("--- computeRowMeans ---");
        double[] rowMeansResult = Matrix_L.computeRowMeans(matrix);
        Matrix_L.print(rowMeansResult);
        System.out.println();

        System.out.println("--- computeColumnMeans ---");
        double[] columnMeansResult = Matrix_L.computeColumnMeans(matrix);
        Matrix_L.print(columnMeansResult);
        System.out.println();

        System.out.println("--- linearize (row) ---");
        double[] linearizeRowResult = Matrix_L.linearize(matrix, false);
        Matrix_L.print(linearizeRowResult);
        System.out.println();

        System.out.println("--- linearize (col) ---");
        double[] linearizeColResult = Matrix_L.linearize(matrix, true);
        Matrix_L.print(linearizeColResult);
    }

    public static void main(String[] args) {

        double[][] matrix = {
                { 6.2, 5.2 },
                { 1.0, -2.0 }
        };
        double factor = 10;
        Matrix_L.testImplementation(matrix, factor);

        double[][] matrix2 = {
                { 1, 2 },
                { 3, 4, 5, 6 },
                { 7 },
                {},
                null,
                {8, 9, 10}
        };
        double factor2 = 2;
        Matrix_L.testImplementation(matrix2, factor2);

        double[][] matrix3 = {
                { },
                { }
        };
        double factor3 = 3;
        Matrix_L.testImplementation(matrix3, factor3);

    }
}
