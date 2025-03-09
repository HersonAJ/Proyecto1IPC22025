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



    // Método para obtener todos los detalles de una venta específica
    //lo usa DetallesComprasClienteServlet
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
    
        // Obtener detalles de una venta específica
    //lo usa ReporteGananciasServlet
    //lo usa ReporteVentasServlet
    public static List<DetalleVenta> obtenerDetallesVentaReportes(int idVenta) throws SQLException {
        List<DetalleVenta> detalles = new ArrayList<>();
        String query = "SELECT * FROM Detalle_Ventas WHERE id_venta = ?";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
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
    
        //lo usa registrarVentaServlet
    public static boolean registrarDetalleVenta(DetalleVenta detalle) {
        String sql = "INSERT INTO Detalle_Ventas (id_venta, id_computadora, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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

