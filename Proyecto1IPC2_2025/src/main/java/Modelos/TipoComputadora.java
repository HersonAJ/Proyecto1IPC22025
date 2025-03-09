/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author herson
 */
public class TipoComputadora {
    
    private int idTipoComputadora;
    private String nombre;
    private double precioVenta;
    
    
    public int getIdTipoComputadora() {
        return idTipoComputadora;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setIdTipoComputadora(int idTipoComputadora) {
        this.idTipoComputadora = idTipoComputadora;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
    
}
