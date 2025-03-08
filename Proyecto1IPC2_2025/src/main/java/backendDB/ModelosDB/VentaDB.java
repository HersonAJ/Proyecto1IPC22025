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

    // Obtener una venta específica por su ID
    public static Venta obtenerVenta(int idVenta) throws SQLException {
        Venta venta = null;
        String query = "SELECT * FROM Ventas WHERE id_venta = ?";
        
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idVenta);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    venta = new Venta();
                    venta.setIdVenta(rs.getInt("id_venta"));
                    venta.setIdCliente(rs.getInt("id_cliente"));
                    venta.setIdUsuario(rs.getInt("id_usuario"));
                    venta.setFechaVenta(rs.getDate("fecha_venta"));
                    venta.setTotalVenta(rs.getDouble("total_venta"));
                }
            }
        }
        return venta;
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
    
    //metodos para funciones del vendedor 

    
    // Método para registrar una venta
public static boolean registrarVenta(Venta venta) {
    String sql = "INSERT INTO Ventas (id_cliente, id_usuario, fecha_venta, total_venta, numero_factura) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
        
        // Configurar los valores para la consulta de inserción
        stmt.setInt(1, venta.getIdCliente());
        stmt.setInt(2, venta.getIdUsuario());
        stmt.setDate(3, new java.sql.Date(venta.getFechaVenta().getTime()));
        stmt.setDouble(4, venta.getTotalVenta());
        stmt.setInt(5, venta.getNumeroFactura()); // Inicialmente será 0

        // Ejecutar la inserción
        int filasInsertadas = stmt.executeUpdate();
        if (filasInsertadas > 0) {
            // Obtener el ID generado automáticamente
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idVentaGenerado = generatedKeys.getInt(1);
                venta.setIdVenta(idVentaGenerado);
                venta.setNumeroFactura(idVentaGenerado);
            }

            // Actualizar el campo numero_factura en la base de datos
            String updateQuery = "UPDATE Ventas SET numero_factura = ? WHERE id_venta = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, venta.getNumeroFactura());
                updateStmt.setInt(2, venta.getIdVenta());
                updateStmt.executeUpdate();
            }
            return true;
        }
        return false;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    // Método existente para obtener una venta (ajustado)
    public static Venta obtenerVentaPorID(int idVenta) {
        String sql = "SELECT * FROM Ventas WHERE id_venta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setIdCliente(rs.getInt("id_cliente"));
                venta.setIdUsuario(rs.getInt("id_usuario"));
                venta.setFechaVenta(rs.getDate("fecha_venta"));
                venta.setTotalVenta(rs.getDouble("total_venta"));
                return venta;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
        // Método para actualizar una venta
    public static boolean actualizarVenta(Venta venta) {
        String sql = "UPDATE Ventas SET id_cliente = ?, id_usuario = ?, fecha_venta = ?, total_venta = ? WHERE id_venta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, venta.getIdCliente());
            stmt.setInt(2, venta.getIdUsuario());
            stmt.setDate(3, new java.sql.Date(venta.getFechaVenta().getTime()));
            stmt.setDouble(4, venta.getTotalVenta());
            stmt.setInt(5, venta.getIdVenta());

            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar una venta
    public static boolean eliminarVenta(int idVenta) {
        String sql = "DELETE FROM Ventas WHERE id_venta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);

            int filasEliminadas = stmt.executeUpdate();
            return filasEliminadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean registrarDetalleVenta(DetalleVenta detalle) {
    String sql = "INSERT INTO Detalle_Ventas (id_venta, id_computadora, cantidad, subtotal) VALUES (?, ?, ?, ?)";
    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, detalle.getIdVenta());
        stmt.setInt(2, detalle.getIdComputadora());
        stmt.setInt(3, detalle.getCantidad());
        stmt.setDouble(4, detalle.getSubtotal());

        int filasInsertadas = stmt.executeUpdate();
        return filasInsertadas > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}



}
