/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author herson
 */
public class Componente {
    
    private int idComponente;
    private String nombre;
    private double costo;
    private int cantidadDisponible;
    private String estado;

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public int getIdComponente() {
        return idComponente;
    }

    public String getNombre() {
        return nombre;
    }

    public double getCosto() {
        return costo;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setIdComponente(int idComponente) {
        this.idComponente = idComponente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

public void setCosto(double costo) {
    if (costo < 0) {
        throw new IllegalArgumentException("El costo no puede ser negativo.");
    }
    this.costo = costo;
}

public void setCantidadDisponible(int cantidadDisponible) {
    if (cantidadDisponible < 0) {
        throw new IllegalArgumentException("La cantidad disponible no puede ser negativa.");
    }
    this.cantidadDisponible = cantidadDisponible;
}

}
