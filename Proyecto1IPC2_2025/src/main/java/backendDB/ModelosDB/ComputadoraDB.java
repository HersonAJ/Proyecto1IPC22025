/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Computadora;
import backendDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author herson
 */
public class ComputadoraDB {
    
    public static boolean registrarComputadora(Computadora computadora) {
        String sql ="INSERT INTO Computadoras (nombre, precio_venta, costo_total) VALUES (?, ?, ?)";
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
}
