/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author herson
 */
public class DetalleDevolucion {
    
    private int idDetalleDevolucion;
    private int idDevolucion;
    private int idComputadora;
    private int cantidad;
    private double subtotal;

    public int getIdDetalleDevolucion() {
        return idDetalleDevolucion;
    }

    public int getIdDevolucion() {
        return idDevolucion;
    }

    public int getIdComputadora() {
        return idComputadora;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setIdDetalleDevolucion(int idDetalleDevolucion) {
        this.idDetalleDevolucion = idDetalleDevolucion;
    }

    public void setIdDevolucion(int idDevolucion) {
        this.idDevolucion = idDevolucion;
    }

    public void setIdComputadora(int idComputadora) {
        this.idComputadora = idComputadora;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
}
