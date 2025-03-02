/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import java.util.Date;

/**
 *
 * @author herson
 */
public class Ensamblaje {
    
    private int idEnsamblaje;
    private int idComputadora;
    private int idUsuario;
    private Date fechaEnsamblaje;

    public int getIdEnsamblaje() {
        return idEnsamblaje;
    }

    public int getIdComputadora() {
        return idComputadora;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public Date getFechaEnsamblaje() {
        return fechaEnsamblaje;
    }

    public void setIdEnsamblaje(int idEnsamblaje) {
        this.idEnsamblaje = idEnsamblaje;
    }

    public void setIdComputadora(int idComputadora) {
        this.idComputadora = idComputadora;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setFechaEnsamblaje(Date fechaEnsamblaje) {
        this.fechaEnsamblaje = fechaEnsamblaje;
    }
    
}
