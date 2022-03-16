//import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class MatrixProduct {

    public static void main(String[] args) throws IOException {
        /*
        int lin, col, option;

        System.out.println("1. Multiplication");
        System.out.println("2. Line Multiplication");
        System.out.println("Selection?:");

        try (Scanner in = new Scanner(System.in)) {
            option = in.nextInt();
            if (option != 1 && option != 2) {
                System.out.println("Exiting...");
                return;
            }

            System.out.println("Dimensions: lin=col ?");
            lin = in.nextInt();
            col = lin;

            switch (option) {
                case 1:
                    OnMult(lin, col);
                    break;
                case 2:
                    OnMultLine(lin, col);
                    break;
            }
        }
        */

        FileWriter testFile = new FileWriter("test.txt");

        testFile.write("TESTING OnMult:\n\n");
        System.out.println("TESTING OnMult:");
        System.out.println();
        for (int i = 600; i <= 3000; i+=400) {
            testFile.write("matrix with size: " + i + "\n");
            System.out.println("matrix with size: " + i);
            double time = OnMult(i, i);
            testFile.write("time: " + time + " seconds\n\n");
            System.out.println();
        }

        testFile.write("TESTING OnMultLine:\n\n");
        System.out.println("TESTING OnMultLine:");
        System.out.println();
        for (int i = 600; i <= 3000; i+=400) {
            testFile.write("matrix with size: " + i + "\n");
            System.out.println("matrix with size: " + i);
            double time = OnMultLine(i, i);
            testFile.write("time: " + time + " seconds\n\n");
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

        System.out.println("Time: " + elapsed + " seconds");
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

        System.out.println("Time: " + elapsed + " seconds");
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
