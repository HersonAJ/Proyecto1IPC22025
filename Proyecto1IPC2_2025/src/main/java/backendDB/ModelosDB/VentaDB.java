/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Cliente;
import Modelos.Computadora;
import Modelos.DetalleVenta;
import Modelos.Usuario;
import Modelos.Venta;
import backendDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author herson
 */
public class VentaDB {

    // Obtener todas las ventas en un intervalo de tiempo
    public static List<Venta> obtenerVentas(String fechaInicio, String fechaFin) throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String query = "SELECT * FROM Ventas WHERE fecha_venta BETWEEN ? AND ?";
        
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Venta venta = new Venta();
                    venta.setIdVenta(rs.getInt("id_venta"));
                    venta.setIdCliente(rs.getInt("id_cliente"));
                    venta.setIdUsuario(rs.getInt("id_usuario"));
                    venta.setFechaVenta(rs.getDate("fecha_venta"));
                    venta.setTotalVenta(rs.getDouble("total_venta"));
                    ventas.add(venta);
                }
            }
        }
        return ventas;
    }

    // Obtener detalles de una venta específica
    public static List<DetalleVenta> obtenerDetallesVenta(int idVenta) throws SQLException {
        List<DetalleVenta> detalles = new ArrayList<>();
        String query = "SELECT * FROM Detalle_Ventas WHERE id_venta = ?";
        
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idVenta);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                    detalle.setIdVenta(rs.getInt("id_venta"));
                    detalle.setIdComputadora(rs.getInt("id_computadora"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setSubtotal(rs.getDouble("subtotal"));
                    detalles.add(detalle);
                }
            }
        }
        return detalles;
    }

    // Obtener información del cliente
    public static Cliente obtenerCliente(int idCliente) throws SQLException {
        Cliente cliente = null;
        String query = "SELECT * FROM Clientes WHERE id_cliente = ?";
        
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idCliente);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    cliente.setNit(rs.getString("nit"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setDireccion(rs.getString("direccion"));
                }
            }
        }
        return cliente;
    }


    // Obtener información del usuario
    public static Usuario obtenerUsuario(int idUsuario) throws SQLException {
        Usuario usuario = null;
        String query = "SELECT Usuarios.id_usuario, Usuarios.nombre_usuario, Usuarios.contraseña, Usuarios.id_rol, Roles.nombre_rol, Usuarios.estado " +
                       "FROM Usuarios " +
                       "JOIN Roles ON Usuarios.id_rol = Roles.id_rol " +
                       "WHERE Usuarios.id_usuario = ?";
        
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idUsuario);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setContraseña(rs.getString("contraseña"));
                    usuario.setRol(rs.getInt("id_rol"));
                    usuario.setRolNombre(rs.getString("nombre_rol")); // Cambiado a "nombre_rol"
                    usuario.setEstado(rs.getString("estado"));
                }
            }
        }
        return usuario;
    }



    // Obtener información de la computadora
    public static Computadora obtenerComputadora(int idComputadora) throws SQLException {
        Computadora computadora = null;
        String query = "SELECT * FROM Computadoras WHERE id_computadora = ?";
        
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idComputadora);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    computadora = new Computadora();
                    computadora.setIdComputadora(rs.getInt("id_computadora"));
                    computadora.setNombre(rs.getString("nombre"));
                    computadora.setPrecioVenta(rs.getDouble("precio_venta"));
                    computadora.setCostoTotal(rs.getDouble("costo_total"));
                }
            }
        }
        return computadora;
    }
}