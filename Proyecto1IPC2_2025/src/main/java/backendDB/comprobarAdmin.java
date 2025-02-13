/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author herson
 */
public class comprobarAdmin {
    public static boolean existeAdministrador() {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE id_rol = (SELECT id_rol FROM Roles WHERE nombre_rol = 'Administrador')";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si hay al menos un administrador, retorna true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // No hay administradores
    }
 
}
