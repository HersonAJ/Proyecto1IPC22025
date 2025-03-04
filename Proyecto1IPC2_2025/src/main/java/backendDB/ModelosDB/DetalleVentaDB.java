/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.DetalleVenta;
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
public class DetalleVentaDB {

    // Método para registrar un detalle de venta
    public static boolean registrarDetalleVenta(DetalleVenta detalleVenta) {
        String sql = "INSERT INTO Detalle_Ventas (id_venta, id_computadora, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalleVenta.getIdVenta());
            stmt.setInt(2, detalleVenta.getIdComputadora());
            stmt.setInt(3, detalleVenta.getCantidad());
            stmt.setDouble(4, detalleVenta.getSubtotal());

            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener un detalle de venta específico por su ID
    public static DetalleVenta obtenerDetalleVenta(int idDetalleVenta) {
        String sql = "SELECT * FROM Detalle_Ventas WHERE id_detalle_venta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDetalleVenta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                detalleVenta.setIdVenta(rs.getInt("id_venta"));
                detalleVenta.setIdComputadora(rs.getInt("id_computadora"));
                detalleVenta.setCantidad(rs.getInt("cantidad"));
                detalleVenta.setSubtotal(rs.getDouble("subtotal"));
                return detalleVenta;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para obtener todos los detalles de una venta específica
    public static List<DetalleVenta> obtenerDetallesVenta(int idVenta) {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM Detalle_Ventas WHERE id_venta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                detalleVenta.setIdVenta(rs.getInt("id_venta"));
                detalleVenta.setIdComputadora(rs.getInt("id_computadora"));
                detalleVenta.setCantidad(rs.getInt("cantidad"));
                detalleVenta.setSubtotal(rs.getDouble("subtotal"));
                detalles.add(detalleVenta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detalles;
    }

    // Método para actualizar un detalle de venta
    public static boolean actualizarDetalleVenta(DetalleVenta detalleVenta) {
        String sql = "UPDATE Detalle_Ventas SET id_venta = ?, id_computadora = ?, cantidad = ?, subtotal = ? WHERE id_detalle_venta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalleVenta.getIdVenta());
            stmt.setInt(2, detalleVenta.getIdComputadora());
            stmt.setInt(3, detalleVenta.getCantidad());
            stmt.setDouble(4, detalleVenta.getSubtotal());
            stmt.setInt(5, detalleVenta.getIdDetalleVenta());

            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar un detalle de venta
    public static boolean eliminarDetalleVenta(int idDetalleVenta) {
        String sql = "DELETE FROM Detalle_Ventas WHERE id_detalle_venta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDetalleVenta);

            int filasEliminadas = stmt.executeUpdate();
            return filasEliminadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

