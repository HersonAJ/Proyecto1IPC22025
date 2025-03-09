/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import java.sql.Date;



/**
 *
 * @author herson
 */
public class ComputadoraEnsamblada {

    private int idComputadora;
    private TipoComputadora tipoComputadora;
    private double costoTotal;
    private Date fechaEnsamblaje;
    private Usuario usuarioEnsamblador;
    private String estado;

    public int getIdComputadora() {
        return idComputadora;
    }

    public TipoComputadora getTipoComputadora() {
        return tipoComputadora;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public Date getFechaEnsamblaje() {
        return fechaEnsamblaje;
    }

    public Usuario getUsuarioEnsamblador() {
        return usuarioEnsamblador;
    }

    public String getEstad() {
        return estado;
    }

    public void setIdComputadora(int idComputadora) {
        this.idComputadora = idComputadora;
    }

    public void setTipoComputadora(TipoComputadora tipoComputadora) {
        this.tipoComputadora = tipoComputadora;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public void setFechaEnsamblaje(Date fechaEnsamblaje) {
        this.fechaEnsamblaje = fechaEnsamblaje;
    }

    public void setUsuarioEnsamblador(Usuario usuarioEnsamblador) {
        this.usuarioEnsamblador = usuarioEnsamblador;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}
