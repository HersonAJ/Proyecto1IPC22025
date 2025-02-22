package backendDB.ModelosDB;

import Modelos.Usuario;
import backendDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author
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
        String sql = "SELECT u.*, r.nombre_rol FROM Usuarios u JOIN Roles r ON u.id_rol = r.id_rol WHERE u.nombre_usuario = ? AND u.contraseña = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contraseña);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setContraseña(rs.getString("contraseña"));
                    usuario.setRol(rs.getInt("id_rol"));
                    usuario.setRolNombre(rs.getString("nombre_rol")); // Establecer el nombre del rol
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    // Método para obtener la lista de usuarios
    public static List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConnection();

            // Consulta SQL para obtener todos los usuarios y sus roles
            String sql = "SELECT u.id_usuario, u.nombre_usuario, r.nombre_rol, u.estado FROM Usuarios u JOIN Roles r ON u.id_rol = r.id_rol";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            // Procesar los resultados de la consulta
            while (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                String nombreUsuario = rs.getString("nombre_usuario");
                String nombreRol = rs.getString("nombre_rol");
                String estado = rs.getString("estado");
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(idUsuario);
                usuario.setNombreUsuario(nombreUsuario);
                usuario.setRolNombre(nombreRol);
                usuario.setEstado(estado);  // Establecer el estado del usuario
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar los recursos
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return usuarios;
    }
}
