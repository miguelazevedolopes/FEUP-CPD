import java.util.Scanner;

public class MatrixProduct {

    public static void main(String[] args) {
        int lin, col, blockSize, option;

        System.out.println("1. Multiplication");
        System.out.println("2. Line Multiplication");
        System.out.println("3. Block Multiplication");
        System.out.println("Selection?:");

        try (Scanner in = new Scanner(System.in)) {
            option = in.nextInt();
            if (option != 1 && option != 2 && option != 3) {
                System.out.println("Exiting...");
                return;
            }

            System.out.println("Dimensions: lins=cols ?");
            lin = in.nextInt();
            col = lin;

            switch (option) {
                case 1:
                    OnMult(lin, col);
                    break;
                case 2:
                    OnMultLine(lin, col);
                    break;
                case 3:
                    System.out.println("Block Size?");
                    blockSize = in.nextInt();
                    OnMultBlock(lin, col, blockSize);
                    break;
            }
        }
    }

    public static void OnMult(int mA, int mB) {
        int i, j, k;
        double start, finish, elapsed;
        double temp;
        double[] a = new double[mA*mA];
        double[] b = new double[mB*mB];
        double[] c = new double[mA*mA];

        for (i = 0; i < mA; i++) {
            for (j = 0; j < mA; j++) {
                a[i*mA + j] = 1.0;
            }
        }

        for (i = 0; i < mB; i++) {
            for (j = 0; j < mB; j++) {
                b[i*mB + j] = i + 1;
            }
        }

        start = System.nanoTime();

        for (i = 0; i < mA; i++) {
            for (j = 0; j < mB; j++) {
                temp = 0;
                for (k = 0; k < mA; k++) {
                    temp += a[i*mA + k] * b[k*mB + j];
                }
                c[i*mA + j] = temp;
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
    }

    public static void OnMultLine(int mA, int mB) {
        int i, j, k;
        double start, finish, elapsed;
        double[] a = new double[mA*mA];
        double[] b = new double[mB*mB];
        double[] c = new double[mA*mA];

        for (i = 0; i < mA; i++) {
            for (j = 0; j < mA; j++) {
                a[i*mA + j] = 1.0;
            }
        }

        for (i = 0; i < mB; i++) {
            for (j = 0; j < mB; j++) {
                b[i*mB + j] = i + 1;
            }
        }

        start = System.nanoTime();

        for (i = 0; i < mA; i++) {
            for (k = 0; k < mB; k++) {
                for (j = 0; j < mA; j++) {
                    c[i*mA + j] += a[i*mA + k] * b[k*mB + j];
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
    }

    public static void OnMultBlock(int mA, int mB, int blockSize) {

    }
}
