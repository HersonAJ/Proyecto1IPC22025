/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Computadora;
import backendDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author herson
 */
public class ComputadoraDB {
    
    // Método para registrar una computadora
    public static boolean registrarComputadora(Computadora computadora) {
        String sql = "INSERT INTO Computadoras (nombre, precio_venta, costo_total) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, computadora.getNombre());
            stmt.setDouble(2, computadora.getPrecioVenta());
            stmt.setDouble(3, computadora.getCostoTotal());
            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener una computadora específica por su ID
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

