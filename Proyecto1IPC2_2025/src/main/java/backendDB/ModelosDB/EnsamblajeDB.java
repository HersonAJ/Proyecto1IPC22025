/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Ensamblaje;
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
public class EnsamblajeDB {

    // Método para registrar un ensamblaje de computadora
    public static boolean registrarEnsamblaje(Ensamblaje ensamblaje) throws SQLException {
        String sql = "INSERT INTO Ensamblaje (id_computadora, id_usuario, fecha_ensamblaje) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ensamblaje.getIdComputadora());
            stmt.setInt(2, ensamblaje.getIdUsuario());
            stmt.setDate(3, new java.sql.Date(ensamblaje.getFechaEnsamblaje().getTime()));
            int resultado = stmt.executeUpdate();
            return resultado > 0;
        }
    }

    // Método para obtener todos los ensamblajes de computadoras
    public static List<Ensamblaje> obtenerEnsamblajes() throws SQLException {
        List<Ensamblaje> ensamblajes = new ArrayList<>();
        String sql = "SELECT * FROM Ensamblaje";
        
        try (Connection conn = ConexionDB.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Ensamblaje ensamblaje = new Ensamblaje();
                ensamblaje.setIdEnsamblaje(rs.getInt("id_ensamblaje"));
                ensamblaje.setIdComputadora(rs.getInt("id_computadora"));
                ensamblaje.setIdUsuario(rs.getInt("id_usuario"));
                ensamblaje.setFechaEnsamblaje(rs.getDate("fecha_ensamblaje"));
                ensamblajes.add(ensamblaje);
            }
        }
        return ensamblajes;
    }
}
