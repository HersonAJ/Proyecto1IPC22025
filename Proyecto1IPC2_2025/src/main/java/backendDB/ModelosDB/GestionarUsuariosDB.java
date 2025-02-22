/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import backendDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author herson
 */
public class GestionarUsuariosDB {
    
    //metodo para cambiar el ro de los usuarios
    public static boolean cambiarRolUsuario(int idUsuario, int nuevoRol) {
        String sql = "UPDATE Usuarios SET id_rol = ? WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nuevoRol);
            stmt.setInt(2, idUsuario);
            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
        }  catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //metodo para dar de baja a un usuario (pasarlo a estado inactivo)
    public static boolean darDeBajaUsuario(int idUsuario) {
        String sql = "UPDATE Usuarios SET estado = 'Inactivo' WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
