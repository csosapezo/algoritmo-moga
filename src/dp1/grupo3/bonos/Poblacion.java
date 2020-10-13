package dp1.grupo3.bonos;

public class Poblacion {
    private Beneficiario beneficiario;
    private PuntoRecojo recojo_turno;

    public Beneficiario getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
    }

    public PuntoRecojo getRecojo_turno() {
        return recojo_turno;
    }

    public void setRecojo_turno(PuntoRecojo recojo_turno) {
        this.recojo_turno = recojo_turno;
    }

    public int distancia() {
        return java.lang.Math.abs(beneficiario.getUbigeo() - recojo_turno.getUbigeo());
    }
}
