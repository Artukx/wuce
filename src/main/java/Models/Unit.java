package Models;

import java.util.List;
import java.util.Map;

public class Unit {
    private int puntos;
    private String id;
    private String rol;
    private String nombre;
    private String movimiento;
    private String salva;
    private int coraje;
    private int heridas;
    private List<String> habilidades;
    private List<String> claves;
    private List<String> command_abilities;
    private List<String> magia;
    private List<String> tablaDaño;
    private List<String> armasMelee;
    private List<String> missileWeapons;

    /*public Unit(Map<String, Object> data) {
        this.puntos = (int) data.get("puntos");
        this.id = (String) data.get("id");
        this.rol = (String) data.get("rol");
        this.nombre = (String) data.get("nombre");
        this.movimiento = (String) data.get("movimiento");
        this.salva = (String) data.get("salva");
        this.coraje = (int) data.get("coraje");
        this.heridas = (int) data.get("heridas");
        this.habilidades = (List<String>) data.get("habilidades");
        this.claves = (List<String>) data.get("claves");
        this.command_abilities = (List) data.get("command_abilities");
        this.magia = (List) data.get("magia");
        this.tablaDaño = (List) data.get("tablaDaño");
        this.armasMelee = (List) data.get("armasMelee");
        this.missileWeapons = (List) data.get("missileWeapons");
    }*/

    public Unit(String nombre, String movimiento, String salva, int puntos, int heridas, int coraje, List<String> habilidades, List<String> claves, List<String> armasMelee, List<String> missileWeapons) {
        this.nombre = nombre;
        this.movimiento = movimiento;
        this.salva = salva;
        this.puntos = puntos;
        this.heridas = heridas;
        this.coraje = coraje;
        this.habilidades = habilidades;
        this.claves = claves;
        this.armasMelee = armasMelee;
        this.missileWeapons = missileWeapons;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public String getSalva() {
        return salva;
    }

    public void setSalva(String salva) {
        this.salva = salva;
    }

    public int getCoraje() {
        return coraje;
    }

    public void setCoraje(int coraje) {
        this.coraje = coraje;
    }

    public int getHeridas() {
        return heridas;
    }

    public void setHeridas(int heridas) {
        this.heridas = heridas;
    }

    public List<String> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(List<String> habilidades) {
        this.habilidades = habilidades;
    }

    public List<String> getClaves() {
        return claves;
    }

    public void setClaves(List<String> claves) {
        this.claves = claves;
    }

    public List<String> getCommand_abilities() {
        return command_abilities;
    }

    public void setCommand_abilities(List<String> command_abilities) {
        this.command_abilities = command_abilities;
    }

    public List<String> getMagia() {
        return magia;
    }

    public void setMagia(List<String> magia) {
        this.magia = magia;
    }

    public List<String> getTablaDaño() {
        return tablaDaño;
    }

    public void setTablaDaño(List<String> tablaDaño) {
        this.tablaDaño = tablaDaño;
    }

    public List<String> getArmasMelee() {
        return armasMelee;
    }

    public void setArmasMelee(List<String> armasMelee) {
        this.armasMelee = armasMelee;
    }

    public List<String> getMissileWeapons() {
        return missileWeapons;
    }

    public void setMissileWeapons(List<String> missileWeapons) {
        this.missileWeapons = missileWeapons;
    }
}

