/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Usuario;
import backendDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author herson
 */
public class UsuarioDB {

    public static boolean registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (nombre_usuario, contraseña, id_rol) VALUES (?, ?, ?)"; 
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getContraseña());
            stmt.setInt(3, usuario.getRol()); 
            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static Usuario verificarCredenciales(String nombreUsuario, String contraseña) {
        Usuario usuario = null;
        String sql = "SELECT * FROM Usuarios WHERE nombre_usuario = ? AND contraseña = ?";
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contraseña);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setContraseña(rs.getString("contraseña"));
                    usuario.setRol(rs.getInt("id_rol")); //modifique setRol de setIdRol
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }
}

