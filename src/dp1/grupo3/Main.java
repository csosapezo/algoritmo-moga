package dp1.grupo3;

import dp1.grupo3.algoritmos.NOVA;

public class Main {

    public static void main(String[] args) {
	// write your code here
        int[] num_agencies = {100, 1000, 5000};
        int[] num_elements = {1000000};
        int[] num_turns = {240, 480};
        int[] num_ubigeo = {5, 20, 40};

        for (int a: num_agencies) {
            for (int e: num_elements) {
                for (int t: num_turns) {
                    for (int u: num_ubigeo) {
                        NOVA moga = new NOVA(50, 1, e, a, 0,
                                t, u, 50, 0.5, false);
                        System.out.printf("A: %d - E: %d - T: %d - U: %d ", a, e, t, u);
                        moga.execute();
                    }
                }
            }
        }
    }
}
