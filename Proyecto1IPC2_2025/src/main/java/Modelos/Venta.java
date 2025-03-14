/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import java.util.Date;
import java.util.List;

/**
 *
 * @author herson
 */
public class Venta {
    
    private int idVenta;
    private int idCliente;
    private int idUsuario;
    private Date fechaVenta;
    private double totalVenta;
    private int numeroFactura;
    private int idComputadoraEnsamblada;
    private List<DetalleVenta> detallesVenta;
    private String nombreCliente;
    private String nombreVendedor;

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }
    
    public List<DetalleVenta> getDetallesVenta() {
        return detallesVenta;
    }

    public void setDetallesVenta(List<DetalleVenta> detallesVenta) {
        this.detallesVenta = detallesVenta;
    }
    
    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public int getIdComputadoraEnsamblada() {
        return idComputadoraEnsamblada;
    }
    
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public void setIdComputadoraEnsamblada(int idComputadoraEnsamblada) {
        this.idComputadoraEnsamblada = idComputadoraEnsamblada;
    }
    
}
