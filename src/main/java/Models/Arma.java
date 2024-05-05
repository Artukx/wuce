package Models;

public class Arma {
    private String nombre;
    private String rango;
    private String ataques;
    private String impactar;
    private String herir;
    private String perforar;
    private String daño;

    public Arma(String nombre, String rango, String ataques, String impactar, String herir, String perforar, String daño) {
        this.nombre = nombre;
        this.rango = rango;
        this.ataques = ataques;
        this.impactar = impactar;
        this.herir = herir;
        this.perforar = perforar;
        this.daño = daño;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getAtaques() {
        return ataques;
    }

    public void setAtaques(String ataques) {
        this.ataques = ataques;
    }

    public String getImpactar() {
        return impactar;
    }

    public void setImpactar(String impactar) {
        this.impactar = impactar;
    }

    public String getHerir() {
        return herir;
    }

    public void setHerir(String herir) {
        this.herir = herir;
    }

    public String getPerforar() {
        return perforar;
    }

    public void setPerforar(String perforar) {
        this.perforar = perforar;
    }

    public String getDaño() {
        return daño;
    }

    public void setDaño(String daño) {
        this.daño = daño;
    }
}
