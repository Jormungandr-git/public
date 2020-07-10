import java.util.Scanner;


class Matrix {
	private int[][] matrix;
    public int n;
    public int m;

	public Matrix(int n, int m){
        this.n = n;
        this.m = m;
        this.matrix = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                this.matrix[i][j] = 0;
            }
        }
	}

	public void setV(int i, int j, int val){
		matrix[i][j] = val;
	}

	public int getV(int i, int j){
		return matrix[i][j];
	}

	public Matrix getSubmatrix(int startRow, int startCol, int dimN, int dimM){

		Matrix subM = new Matrix(dimN, dimM);
		for (int i = 0; i<dimN ; i++ )
			for (int j=0;j<dimM ; j++ )
				subM.setV(i,j, matrix[startRow+i][startCol+j]);
		return subM;
	}

	public void putSubmatrix(int startRow, int startCol, Matrix b){

		for (int i = 0; i<b.n ; i++ )
			for (int j=0;j<b.m ; j++ )
				this.setV(startRow+i,startCol+j, b.getV(i,j));
	}

	public Matrix sum(Matrix b){

		Matrix c = new Matrix(n,m);
		for(int i = 0; i< n;i++){
			for(int j = 0; j<m;j++){
				c.setV(i, j, matrix[i][j]+b.getV(i, j));
			}
		}
		return c;
	}

	public Matrix sub(Matrix b){

		Matrix c = new Matrix(n,m);
		for(int i = 0; i< n;i++){
			for(int j = 0; j<m;j++){
				c.setV(i, j, matrix[i][j]-b.getV(i, j));
			}
		}
		return c;
	}

	public Matrix mult(Matrix b){
        int dimN = this.n;
        int dimM = this.m;
        Matrix c = new Matrix(dimN, dimM);
        for (int i = 0; i < dimN; i++) {
            for (int j = 0; j < dimM; j++) {
                c.setV(i, j, 0);
                for (int k = 0; k < dimM; k++) {
                    c.setV(i, j, ( c.getV(i, j) +  ( this.getV(i, k) * b.getV(k, j) ) ) );
                }
            }
        }
        return c;
	}

	public Matrix multStrassen(Matrix b, int cutoff){
        

        
        //// sqaring MATRIX 1
        int dim1 = Math.max(this.n,this.m);
        int dim2 = Math.max(b.n,b.m);
        int dim = Math.max(dim1,dim2);


        dim = (int) Math.pow(2,Math.round(Math.log(dim) / Math.log(2)));
        Matrix matrix1 = new Matrix(dim, dim);
        Matrix matrix2 = new Matrix(dim, dim);


        matrix1.putSubmatrix(0,0,this);
        matrix2.putSubmatrix(0,0,b);










        if (matrix2.n != cutoff) {
            Matrix a11 = new Matrix(matrix1.n/2,matrix1.n/2);
            Matrix a12 = new Matrix(matrix1.n/2,matrix1.n/2);
            Matrix a21 = new Matrix(matrix1.n/2,matrix1.n/2);
            Matrix a22 = new Matrix(matrix1.n/2,matrix1.n/2);

            Matrix b11 = new Matrix(matrix2.n/2,matrix2.n/2);
            Matrix b12 = new Matrix(matrix2.n/2,matrix2.n/2);
            Matrix b21 = new Matrix(matrix2.n/2,matrix2.n/2);
            Matrix b22 = new Matrix(matrix2.n/2,matrix2.n/2);

            a11 = matrix1.getSubmatrix(0, 0, matrix1.n/2,matrix1.n/2);
            a12 = matrix1.getSubmatrix(0, matrix1.n/2, matrix1.n/2,matrix1.n/2);
            a21 = matrix1.getSubmatrix(matrix1.n/2,0,matrix1.n/2,matrix1.n/2);
            a22 = matrix1.getSubmatrix(matrix1.n/2, matrix1.n/2, matrix1.n/2,matrix1.n/2);

            b11 = matrix2.getSubmatrix(0, 0, matrix2.n/2,matrix2.n/2);
            b12 = matrix2.getSubmatrix(0, matrix2.n/2, matrix2.n/2,matrix2.n/2);
            b21 = matrix2.getSubmatrix(matrix2.n/2,0,matrix2.n/2,matrix2.n/2);
            b22 = matrix2.getSubmatrix(matrix2.n/2, matrix2.n/2, matrix2.n/2,matrix2.n/2);




            Matrix p1 = a11.multStrassen(b12.sub(b22),cutoff);
            System.out.println(p1.internalSum());

            Matrix p2 = (a11.sum(a12)).multStrassen(b22,cutoff);
            System.out.println(p2.internalSum());
            

            Matrix p3 = (a21.sum(a22)).multStrassen(b11,cutoff);
            System.out.println(p3.internalSum());

            Matrix p4 = a22.multStrassen(b21.sub(b11),cutoff);
            System.out.println(p4.internalSum());


            Matrix p5 = (a11.sum(a22)).multStrassen(b11.sum(b22),cutoff);
            System.out.println(p5.internalSum());


            Matrix p6 = (a12.sub(a22)).multStrassen(b21.sum(b22),cutoff);
            System.out.println(p6.internalSum());

            Matrix p7 = (a11.sub(a21)).multStrassen(b11.sum(b12),cutoff);
            System.out.println(p7.internalSum());



            Matrix c11 = ( ( p5.sum(p4) ).sub(p2) ).sum(p6);
            Matrix c12 = p1.sum(p2);
            Matrix c21 = p3.sum(p4);
            Matrix c22 = ( ( p1.sum(p5) ).sub(p3) ).sub(p7);

            Matrix result = new Matrix(matrix1.n,matrix2.m);
            
            result.putSubmatrix(0, 0, c11);
            result.putSubmatrix(0, matrix2.n/2, c12);
            result.putSubmatrix(matrix2.n/2, 0, c21);
            result.putSubmatrix(matrix2.n/2, matrix2.n/2, c22);
        
            return result;
            
        }else{
            return matrix1.mult(matrix2);
        }
	}

    public int internalSum(){
        int sum = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                sum += this.getV(i, j);
            }
        }
        return sum;
    }
    
    public void printMatrix(){
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.m; j++) {
                System.out.print(this.getV(i, j) + " ");
            }
            System.out.println();
        }
    }
}

public class naloga2matrices {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        String algorithm = sc.next();


        switch (algorithm) {
            case "os":
                /// INIT
                int n1 = sc.nextInt();
                int m1 = sc.nextInt();
                int[][] matrix1 = new int[n1][m1];
                for (int i = 0; i < n1; i++) {
                    for (int j = 0; j < m1; j++) {
                        matrix1[i][j] = sc.nextInt();
                    }
                }
                int n2 = sc.nextInt();
                int m2 = sc.nextInt();
                int[][] matrix2 = new int[n2][m2];
                for (int i = 0; i < n2; i++) {
                    for (int j = 0; j < m2; j++) {
                        matrix2[i][j] = sc.nextInt();
                    }
                }
                
                /// FUNCTION
                int[][] result = normalMultiplication(matrix1, n1, matrix2 , m2, n2);
                
                ///PRINT OUT
                printOutput(result);
                break;

            case "bl":

               /// INIT
               int variableBL = sc.nextInt();
               int n11 = sc.nextInt();
               int m11 = sc.nextInt();
               Matrix matrix11 = new Matrix(n11,m11);
               for (int i = 0; i < n11; i++) {
                   for (int j = 0; j < m11; j++) {
                       matrix11.setV(i, j, sc.nextInt());
                   }
               }
               int n22 = sc.nextInt();
               int m22 = sc.nextInt();                                                                                                                                                                                          
               Matrix matrix22 = new Matrix(n22,m22);
               for (int i = 0; i < n22; i++) {
                   for (int j = 0; j < m22; j++) {
                       matrix22.setV(i, j, sc.nextInt());
                   }
               } 
               Matrix resultt = blocMultiplication(matrix11, matrix22, variableBL);
               System.out.println("DIMS: " + resultt.n + "x" + resultt.m);
               resultt.printMatrix();
                break;
            case "dv":

                /// INIT
                int variableDV = sc.nextInt();
                int n111 = sc.nextInt();
                int m111 = sc.nextInt();
                Matrix matrix111 = new Matrix(n111,m111);
                for (int i = 0; i < n111; i++) {
                    for (int j = 0; j < m111; j++) {
                        matrix111.setV(i, j, sc.nextInt());
                    }
                }
                int n222 = sc.nextInt();
                int m222 = sc.nextInt();                                                                                                                                                                                          
                Matrix matrix222 = new Matrix(n222,m222);
                for (int i = 0; i < n222; i++) {
                    for (int j = 0; j < m222; j++) {
                        matrix222.setV(i, j, sc.nextInt());
                    }
                }
                Matrix resulttt = divideMultiplication(matrix111, matrix222, variableDV);

                System.out.println("DIMS: " + resulttt.n + "x" + resulttt.m);
                resulttt.printMatrix();
                
                break;
            case "st":

            /// INIT
            int variableST = sc.nextInt();
            int n1111 = sc.nextInt();
            int m1111 = sc.nextInt();
            Matrix matrix1111 = new Matrix(n1111,m1111);
            for (int i = 0; i < n1111; i++) {
                for (int j = 0; j < m1111; j++) {
                    matrix1111.setV(i, j, sc.nextInt());
                }
            }
            int n2222 = sc.nextInt();
            int m2222 = sc.nextInt();                                                                                                                                                                                          
            Matrix matrix2222 = new Matrix(n2222,m2222);
            for (int i = 0; i < n2222; i++) {
                for (int j = 0; j < m2222; j++) {
                    matrix2222.setV(i, j, sc.nextInt());
                }
            }
            Matrix resultttt = matrix1111.multStrassen(matrix2222, variableST);

            System.out.println("DIMS: " + resultttt.n + "x" + resultttt.m);
            resultttt.printMatrix();


                
                break;
        
            default:
                break;
        }
    }

    public static int[][] normalMultiplication(int[][] matrix1, int n, int[][] matrix2, int m, int common){

        int[][] result = new int[n][m];
        for (int i = 0; i < n; i++) {   // rows from matrix A
            for (int j = 0; j < m; j++) {  // rows from matrixx B
                
                int sum = 0;
                for (int k = 0; k < common; k++) {  // elements
                    sum += matrix1[i][k] * matrix2[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    public static Matrix normalMultiplicationForMatrixes(Matrix[][] matrix1, int n, Matrix[][] matrix2, int m, int var){

        Matrix result = new Matrix(matrix1.length * var,matrix2[0].length * var);
        for (int i = 0; i < matrix1.length; i++) {   // rows from matrix A
            for (int j = 0; j < matrix2[0].length; j++) {  // rows from matrixx B
                
                Matrix suma = new Matrix(var,var);
                Matrix prod = new Matrix(var,var);
                for (int k = 0; k < matrix1[0].length; k++) {  // elements
                    prod = matrix1[i][k].mult(matrix2[k][j]);
                    suma = suma.sum(prod);  // my matrix2 is actualy transposed 
                    System.out.println(prod.internalSum() + " ");
                }
                result.putSubmatrix(i * var, j * var, suma);
            }
        }
        return result;
    }

    public static void printOutput(int[][] result){

        System.out.println("DIMS: " +  result.length + "x" + result[0].length);
        for (int[] array: result) {
            for (int elem: array) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
    }

    public static Matrix blocMultiplication(Matrix matrix1, Matrix matrix2, int var){

        int no_of_sub_matrixes1V =(int) Math.ceil(matrix1.n / (double) var);
        int no_of_sub_matrixes1H =(int) Math.ceil(matrix1.m/ (double) var);
        int no_of_sub_matrixes2V = (int) Math.ceil(matrix2.n / (double) var);
        int no_of_sub_matrixes2H = (int) Math.ceil(matrix2.m / (double) var);

        Matrix[][] subMatrixes1 = new Matrix[no_of_sub_matrixes1V][no_of_sub_matrixes1H];
        Matrix[][] subMatrixes2 = new Matrix[no_of_sub_matrixes2V][no_of_sub_matrixes2H];

        ////PARTITIONING MATRIX 1
        for (int i = 0; i < matrix1.n; i += var) {
          for (int j = 0; j < matrix1.m; j += var) {

            if (i <= matrix1.n - var && j <= matrix1.m - var) {
                subMatrixes1[(int) Math.ceil(i/var)][(int) Math.ceil(j/var)] = new Matrix(var,var);
                subMatrixes1[(int) Math.ceil(i/var)][(int) Math.ceil(j/var)] = matrix1.getSubmatrix(i, j, var,var); 
            }else{  /// if i have to add 0's
                subMatrixes1[(int) Math.ceil(i/var)][(int) Math.ceil(j/var)] = new Matrix(var,var);
                int posI = 0;
                int posJ = 0;
                for (int i2 = i; i2 < i + var; i2++) {
                    posJ = 0;
                    for (int j2 = j; j2 < j + var ; j2++) {
                        if (i2 < matrix1.n && j2 < matrix1.m) { /// if inside matrix
                            subMatrixes1[(int) Math.ceil(i/var)][(int) Math.ceil(j/var)].setV(posI, posJ, matrix1.getV(i2, j2));
                        }else{
                            subMatrixes1[(int) Math.ceil(i/var)][(int) Math.ceil(j/var)].setV(posI, posJ, 0);
                        }
                        posJ++;
                    }
                    posI++;
                }
            }  
          }  
        }
        ////PARTITIONING MATRIX 2
        for (int i = 0; i < matrix2.n; i += var) {
            for (int j = 0; j < matrix2.m; j += var) {

            if (i <= matrix2.n - var && j <= matrix2.m - var) {
                subMatrixes2[(int) Math.ceil(i/var)][(int) Math.ceil(j/var)] = new Matrix(var,var);
                subMatrixes2[(int) Math.ceil(i/var)][(int) Math.ceil(j/var)] = matrix2.getSubmatrix(i, j, var,var); 
            }else{  /// if i have to add 0's
                subMatrixes2[(int) Math.ceil(i/var)][(int) Math.ceil(j/var)] = new Matrix(var,var);
                int posI = 0;
                int posJ = 0;
                for (int i2 = i; i2 < i + var; i2++) {
                    posJ = 0;
                    for (int j2 = j; j2 < j + var ; j2++) {
                        if (i2 < matrix2.n && j2 < matrix2.m) { /// if inside matrix
                            subMatrixes2[(int) Math.ceil(i/var)][(int) Math.ceil(j/var)].setV(posI, posJ, matrix2.getV(i2, j2));
                        }else{
                            subMatrixes2[(int) Math.ceil(i/var)][(int) Math.ceil(j/var)].setV(posI, posJ, 0);
                        }
                        posJ++;
                    }
                    posI++;
                }
            }  
            }  
        }


        Matrix resultttt = normalMultiplicationForMatrixes(subMatrixes1, matrix1.n, subMatrixes2, matrix2.m, var);

        Matrix finalResult = new Matrix(matrix1.n,matrix2.m);
        for (int i = 0; i < finalResult.n; i++) {
            for (int j = 0; j < finalResult.m; j++) {
                finalResult.setV(i, j, resultttt.getV(i, j));
            }
        }
        return finalResult;
    }

    public static Matrix divideMultiplication( Matrix m1, Matrix m2,int var){

        Matrix matrix1;
        Matrix matrix2;
    
        //// sqaring MATRIX 1
        int dim1 = Math.max(m1.n,m1.m);
        int dim2 = Math.max(m2.n,m2.m);
        int dim = Math.max(dim1,dim2);


        dim = (int) Math.pow(2,Math.round(Math.log(dim) / Math.log(2)));
        matrix1 = new Matrix(dim, dim);
        matrix2 = new Matrix(dim, dim);

        
        matrix1.putSubmatrix(0,0,m1);
        matrix2.putSubmatrix(0,0,m2);

        if (matrix1.n == var && matrix1.m == var) {
            Matrix result = new Matrix(var,var);
            result = matrix1.mult(matrix2);
            return result;
            
        }else{


            /// partitioning matrix1
            Matrix a11 = matrix1.getSubmatrix(0,0,matrix1.n/2,matrix1.m/2);
            Matrix a12 = matrix1.getSubmatrix(0,matrix1.m/2,matrix1.n/2,matrix1.m/2);
            Matrix a21 = matrix1.getSubmatrix(matrix1.n/2,0,matrix1.n/2,matrix1.m/2);
            Matrix a22 = matrix1.getSubmatrix(matrix1.n/2,matrix1.m/2,matrix1.n/2,matrix1.m/2);

            /// partitioning matrix2
            Matrix b11 = matrix2.getSubmatrix(0,0,matrix2.n/2,matrix2.m/2);
            Matrix b12 = matrix2.getSubmatrix(0,matrix2.m/2,matrix2.n/2,matrix2.m/2);
            Matrix b21 = matrix2.getSubmatrix(matrix2.n/2,0,matrix2.n/2,matrix2.m/2);
            Matrix b22 = matrix2.getSubmatrix(matrix2.n/2,matrix2.m/2,matrix2.n/2,matrix2.m/2);



            /// recursive calls
            Matrix[][] result = new Matrix[2][2];
            Matrix left;
            Matrix right;
            left = divideMultiplication(a11, b11,var);
            System.out.println(left.internalSum());
            right = divideMultiplication(a12, b21,var);
            System.out.println(right.internalSum());
            result[0][0] = new Matrix(matrix1.n/2, matrix1.m/2);
            result[0][0] = (left).sum(right) ;
    


            
            left = divideMultiplication(a11, b12,var);
            System.out.println(left.internalSum());
            right = divideMultiplication(a12, b22,var);
            System.out.println(right.internalSum());
            result[0][1] = new Matrix(matrix1.n/2, matrix1.m/2);
            result[0][1] = (left).sum(right) ;


            
            left = divideMultiplication(a21, b11,var);
            System.out.println(left.internalSum());
            right = divideMultiplication(a22, b21,var);
            System.out.println(right.internalSum());
            result[1][0] = new Matrix(matrix1.n/2, matrix1.m/2);
            result[1][0] = (left).sum(right) ;

            left = divideMultiplication(a21, b12,var);
            System.out.println(left.internalSum());
            right = divideMultiplication(a22, b22,var);
            System.out.println(right.internalSum());
            result[1][1] = new Matrix(matrix1.n/2, matrix1.m/2);
            result[1][1] = (left).sum(right) ;




            Matrix finalResult = new Matrix(matrix1.n,matrix2.m);

            finalResult.putSubmatrix(0,0, result[0][0]);
            finalResult.putSubmatrix(0,finalResult.m/2, result[0][1]);
            finalResult.putSubmatrix(finalResult.n/2,0, result[1][0]);
            finalResult.putSubmatrix(finalResult.n/2,finalResult.m/2, result[1][1]);

            return finalResult;
        }

    }


}