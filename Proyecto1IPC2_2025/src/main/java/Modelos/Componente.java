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
    private int idCategoria;
    private double costo;
    private int cantidadDisponible;

    public int getIdComponente() {
        return idComponente;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdCategoria() {
        return idCategoria;
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

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setCosto(double consto) {
        this.costo = consto;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
    
    
    
}
