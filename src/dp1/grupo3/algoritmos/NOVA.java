package dp1.grupo3.algoritmos;

import dp1.grupo3.bonos.Beneficiario;
import dp1.grupo3.bonos.Fitness;
import dp1.grupo3.bonos.Poblacion;
import dp1.grupo3.bonos.PuntoRecojo;

import java.util.ArrayList;
import java.util.Random;

public class NOVA {
    /* Attributes */
    int num_epochs, num_elements, num_agencies, turns_per_agency, num_ubigeo, num_population;
    double mutate_factor, fitness_test, crossover_factor;
    Beneficiario[] beneficiarios;
    PuntoRecojo[] agencias;
    ArrayList<Poblacion[]> poblacion;
    ArrayList<Poblacion[]> pareto;
    Fitness[] fitness;

    Random r = new Random();


    /* Private methods */
    private double[] pareto() {

        double[] fitness_values = new double[2];

        /* Hallar miínima aglomeración*/
        int min_aglomeracion = 0;
        int min_distancia = 0;

        for ( int i = 1; i < fitness.length; i++ ) {
            if ( fitness[i].getAglomeracion() <= fitness[min_aglomeracion].getAglomeracion() )
                min_aglomeracion = i;
        }

        fitness_values[0] = fitness[min_aglomeracion].getAglomeracion();

        pareto.add(poblacion.get(min_aglomeracion));

        /* Hallar no dominados */
        for (int i = 0; i < fitness.length; i++) {
            if (fitness[i].getDistancia() >= fitness[min_aglomeracion].getDistancia())
                pareto.add(poblacion.get(i));
            if ( fitness[i].getDistancia() <= fitness[min_aglomeracion].getDistancia())
                min_distancia = i;
        }

        fitness_values[1] = fitness[min_distancia].getDistancia();

        return fitness_values;
    }


    private int argmax(double[] array) {
        if ( array == null || array.length == 0 )
            return -1;

        int largest = 0;
        for ( int i = 1; i < array.length; i++ ) {
            if ( array[i] > array[largest] )
                largest = i;
        }

        return largest;
    }



    private void initialise() {

        beneficiarios = new Beneficiario[this.num_elements];
        agencias = new PuntoRecojo[this.num_agencies * this.turns_per_agency];
        poblacion = new ArrayList<>(num_population);
        fitness = new Fitness[this.num_population];
        pareto = new ArrayList<>();

        /* Initialise delivery spots */
        for (int i = 0; i < this.num_agencies; i++) {
            for (int j = 0; j < this.turns_per_agency; j++) {
                int index = i * this.turns_per_agency + j;
                this.agencias[index].setId_punto_reocojo(i);
                this.agencias[index].setUbigeo(this.r.nextInt(this.num_ubigeo));
                this.agencias[index].setCapacidad(this.turns_per_agency);
                this.agencias[index].setTurno(j);
            }
        }

        /* Initialise population */
        for (int i = 0; i < this.num_elements; i++) {
            this.beneficiarios[i].setId_beneficiario(i);
            this.beneficiarios[i].setUbigeo(this.r.nextInt(this.num_ubigeo));
        }

        /* Initial population */
        for (int i = 0; i < this.num_population; i++) {
            Poblacion[] p = new Poblacion[this.num_elements];
            for (int j = 0; j < this.num_elements; j++) {
                p[j].setBeneficiario(this.beneficiarios[j]);
                p[j].setRecojo_turno(this.agencias[this.r.nextInt(this.agencias.length)]);
            }
            this.poblacion.add(p);
        }
    }

    private double[] evaluate() {
        for (int i = 0; i < num_population; i++) {
            fitness[i].calc_fitness(poblacion.get(i), this.num_agencies, this.turns_per_agency);
        }
        return this.pareto();
    }

    private int[] select() {
        double[] fx = new double[num_population];
        double[] tournament = new double[5];
        double w1 = r.nextDouble();
        double w2 = r.nextDouble();
        double acumulado = 0;
        int[] selected_indexes = new int[2];

        for (int i = 0; i < num_population; i++) {
            acumulado += fitness[i].weighted_fitness(w1, w2);
            fx[i] = acumulado;
        }

        for (int i = 0; i < 5; i++) {
            tournament[i] = fx[r.nextInt(fx.length)];
        }

        for (int i = 0; i < 2; i++) {
            selected_indexes[i] = this.argmax(tournament);
            tournament[selected_indexes[i]] = -1;
        }

        return selected_indexes;
    }

    private void crossover(int[] parents, ArrayList<Poblacion[]> news) {
        Poblacion[] result1 = new Poblacion[num_elements];
        Poblacion[] result2 = new Poblacion[num_elements];

        for (int i = 0; i < num_elements; i++) {
            double ni = r.nextDouble();
            if (ni >= this.crossover_factor){
                result1[i] = this.poblacion.get(parents[0])[i];
                result2[i] = this.poblacion.get(parents[1])[i];
            } else {
                result1[i] = this.poblacion.get(parents[1])[i];
                result2[i] = this.poblacion.get(parents[0])[i];
            }
        }

        news.add(result1);
        news.add(result2);
    }

    private void mutate(ArrayList<Poblacion[]> news) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < num_elements; j++) {
                double ni = r.nextDouble();

                if (ni >= this.mutate_factor)
                    news.get(i)[j].setRecojo_turno(this.agencias[this.r.nextInt(this.agencias.length)]);
            }
        }
    }

    private void update(ArrayList<Poblacion[]> news) {
        poblacion.remove(r.nextInt(num_population));
        poblacion.add(news.get(0));
        poblacion.remove(r.nextInt(num_population));
        poblacion.add(news.get(1));
    }

    private boolean termination_test(double[] values) {
        return (values[0] == fitness_test) && (values[1] == fitness_test);
    }


    /* Public methods */
    public NOVA(int num_epochs, double mutate_factor, int num_elements, int num_agencies, double fitness_test,
                int turns_per_agency, int num_ubigeo, int num_population, double crossover_factor) {
        this.num_epochs = num_epochs;
        this.mutate_factor = mutate_factor;
        this.num_elements = num_elements;
        this.num_agencies = num_agencies;
        this.fitness_test = fitness_test;
        this.turns_per_agency = turns_per_agency;
        this.num_ubigeo = num_ubigeo;
        this.num_population = num_population;
        this.crossover_factor = crossover_factor;
    }

    public void execute() {

        int epochs = 0;
        double[] values;
        this.initialise();

        do {
            int[] parents;
            ArrayList<Poblacion[]> news = new ArrayList<>();
            values = this.evaluate();
            parents = this.select();
            this.crossover(parents, news);
            this.mutate(news);
            this.update(news);

            epochs++;

            System.out.printf("Época %d/%d: Aglomeración = %6.3f -- Distancia = %6.3f \n",
                    epochs, num_epochs, values[0], values[1]);

        } while ((epochs < this.num_epochs) && !this.termination_test(values));
    }
}
