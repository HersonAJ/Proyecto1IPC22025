/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.DetalleDevolucion;
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
public class DetalleDevolucionDB {

    // Obtener detalles de una devolución específica
    public static List<DetalleDevolucion> obtenerDetallesDevolucion(int idDevolucion) throws SQLException {
        List<DetalleDevolucion> detalles = new ArrayList<>();
        String sql = "SELECT * FROM Detalle_Devoluciones WHERE id_devolucion = ?";
        
        try (Connection conn = ConexionDB.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDevolucion);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DetalleDevolucion detalle = new DetalleDevolucion();
                    detalle.setIdDetalleDevolucion(rs.getInt("id_detalle_devolucion"));
                    detalle.setIdDevolucion(rs.getInt("id_devolucion"));
                    detalle.setIdComputadora(rs.getInt("id_computadora"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setSubtotal(rs.getDouble("subtotal"));
                    detalles.add(detalle);
                }
            }
        }
        return detalles;
    }

    // Insertar un nuevo detalle de devolución
    public static void insertarDetalleDevolucion(DetalleDevolucion detalleDevolucion) throws SQLException {
        String query = "INSERT INTO Detalle_Devoluciones (id_devolucion, id_computadora, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, detalleDevolucion.getIdDevolucion());
            ps.setInt(2, detalleDevolucion.getIdComputadora());
            ps.setInt(3, detalleDevolucion.getCantidad());
            ps.setDouble(4, detalleDevolucion.getSubtotal());
            ps.executeUpdate();
        }
    }
}

