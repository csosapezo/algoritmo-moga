package dp1.grupo3.bonos;


public class Fitness {
    private double aglomeracion;
    private double distancia;

    private void calc_aglomeracion(Poblacion[] p, int num_turnos_agencias){
        this.aglomeracion = 0;

        int[] capacidad = new int[num_turnos_agencias];

        for (int i = 0; i < p.length; i++) {
            turn_agency_position = p.getRecojo_turno().getCapacidad()*p.getRecojo_turno().getId_punto_reocojo()+p.getRecojo_turno().getTurno()
            capacidad[turn_agency_position] += 1
        }

        double mean = 0;
        for (int i = 0; i < capacidad.length; i++) {
            mean += capacidad[i];
        }
        mean /= capacidad.length;

        double accumulator = 0;
        for (int i = 0; i < capacidad.length; i++) {
            accumulator += Math.pow((capacidad[i]-mean), 2);
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

    public void setAglomeracion(double aglomeracion) {
        this.aglomeracion = aglomeracion;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double weighted_fitness(double w1, double w2) {
        return w1 * (1/ (1 + this.aglomeracion)) + w2 * this.distancia;
    }

    public void calc_fitness(Poblacion[] p, int num_turnos_agencias) {
        this.calc_distance(p);
        this.calc_aglomeracion(p, num_turnos_agencias);
    }
}
