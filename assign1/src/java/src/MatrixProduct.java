import java.io.FileWriter;
import java.io.IOException;

public class MatrixProduct {

    public static void main(String[] args) throws IOException {
        FileWriter testFile = new FileWriter("test_miguel.txt");

        for (int i = 600; i <= 3000; i+=400) {
            testFile.write("matrix with size: " + i + "x" + i + "\n");
            System.out.println("matrix with size: " + i + "x" + i);
            double timeMult = OnMult(i, i);
            testFile.write("time mult: " + timeMult + " seconds\n");
            double timeMultLine = OnMultLine(i, i);
            testFile.write("time mult line: " + timeMultLine + " seconds\n\n");
            System.out.println();
        }

        testFile.close();
    }

    public static double OnMult(int mA, int mB) {
        int i, j, k;
        double start, finish, elapsed;
        double temp;
        double[] a = new double[mA * mA];
        double[] b = new double[mB * mB];
        double[] c = new double[mA * mA];

        for (i = 0; i < mA; i++) {
            for (j = 0; j < mA; j++) {
                a[i * mA + j] = 1.0;
            }
        }

        for (i = 0; i < mB; i++) {
            for (j = 0; j < mB; j++) {
                b[i * mB + j] = i + 1;
            }
        }

        start = System.nanoTime();

        for (i = 0; i < mA; i++) {
            for (j = 0; j < mB; j++) {
                temp = 0;
                for (k = 0; k < mA; k++) {
                    temp += a[i * mA + k] * b[k * mB + j];
                }
                c[i * mA + j] = temp;
            }
        }

        finish = System.nanoTime();
        elapsed = (finish - start) / 1000000000;

        System.out.println("Time Mult: " + elapsed + " seconds");
        System.out.println("Result Matrix:");
        for (i = 0; i < 1; i++) {
            for (j = 0; j < Math.min(10, mB); j++) {
                System.out.print(c[j] + "  ");
            }
        }
        System.out.println();
        return elapsed;
    }

    public static double OnMultLine(int mA, int mB) {
        int i, j, k;
        double start, finish, elapsed;
        double[] a = new double[mA * mA];
        double[] b = new double[mB * mB];
        double[] c = new double[mA * mA];

        for (i = 0; i < mA; i++) {
            for (j = 0; j < mA; j++) {
                a[i * mA + j] = 1.0;
            }
        }

        for (i = 0; i < mB; i++) {
            for (j = 0; j < mB; j++) {
                b[i * mB + j] = i + 1;
            }
        }

        for (i = 0; i < mA; i++) {
            for (j = 0; j < mA; j++) {
                c[i * mA + j] = 0.0;
            }
        }

        start = System.nanoTime();

        for (i = 0; i < mA; i++) {
            for (k = 0; k < mB; k++) {
                for (j = 0; j < mA; j++) {
                    c[i * mA + j] += a[i * mA + k] * b[k * mB + j];
                }
            }
        }

        finish = System.nanoTime();
        elapsed = (finish - start) / 1000000000;

        System.out.println("Time Mult Line: " + elapsed + " seconds");
        System.out.println("Result Matrix:");
        for (i = 0; i < 1; i++) {
            for (j = 0; j < Math.min(10, mB); j++) {
                System.out.print(c[j] + "  ");
            }
        }
        System.out.println();
        return elapsed;
    }
}
