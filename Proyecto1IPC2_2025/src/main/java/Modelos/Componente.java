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
    private double consto;
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

    public double getConsto() {
        return consto;
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

    public void setConsto(double consto) {
        this.consto = consto;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
    
    
    
}
