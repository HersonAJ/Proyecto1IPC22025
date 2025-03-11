/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author herson
 */
public class Usuario {
    
    private int idUsuario;
    private String nombreUsuario;
    private String contraseña;
    private int idRol;
    private String rolNombre;
    private String estado;
    private int totalVentas;

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public int getRol() {
        return idRol;
    }
    
    public String getRolNombre(){
        return rolNombre;
    }
    
    public String getEstado() {
        return estado;
    }

    public int getIdRol() {
        return idRol;
    }

    public int getTotalVentas() {
        return totalVentas;
    }
    
    

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setRol(int idRol) {
        this.idRol = idRol;
    }
    
    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setTotalVentas(int totalVentas) {
        this.totalVentas = totalVentas;
    }
    
}
