package dp1.grupo3.bonos;


public class Fitness {
    private double aglomeracion;
    private double distancia;

    private void calc_aglomeracion(Poblacion[] p, int num_agencias, int num_turnos){
        this.aglomeracion = 0;
        int num_turnos_agencias = num_agencias * num_turnos;
        int[] capacidad = new int[num_turnos_agencias];

        for (Poblacion poblacion : p) {
            int turn_agency_position = poblacion.getRecojo_turno().getId_punto_reocojo() *  num_turnos
                    + poblacion.getRecojo_turno().getTurno();
            capacidad[turn_agency_position] += 1;
        }

        double mean = 0;
        for (int j : capacidad) {
            mean += j;
        }
        mean /= capacidad.length;

        double accumulator = 0;
        for (int j : capacidad) {
            accumulator += Math.pow((j - mean), 2);
        }

        this.aglomeracion = 1.0 * accumulator / capacidad.length;
    }

    private void calc_distance(Poblacion[] p){
        this.distancia = 0;

        for (Poblacion poblacion : p) {
            this.distancia += poblacion.distancia();
        }

        this.distancia /= p.length;
    }

    public double getAglomeracion() {
        return aglomeracion;
    }

    public double getDistancia() {
        return distancia;
    }

    public double weighted_fitness(double w1, double w2) {
        return w1 * (1/ (1 + this.aglomeracion)) + w2 * (1 / (1 + this.distancia));
    }

    public void calc_fitness(Poblacion[] p, int num_agencias, int num_turnos) {
        this.calc_distance(p);
        this.calc_aglomeracion(p, num_agencias, num_turnos);
    }
}
