/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Componente;
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
public class ComponenteDB {

    public static boolean registrarComponente(Componente componente) {
        String sql = "INSERT INTO Componentes (nombre, costo, cantidad_disponible) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, componente.getNombre());
            stmt.setDouble(2, componente.getCosto());
            stmt.setInt(3, componente.getCantidadDisponible());
            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Método para obtener todos los componentes activos
    public static List<Componente> obtenerComponentes() throws SQLException {
        List<Componente> componentes = new ArrayList<>();
        String query = "SELECT * FROM Componentes WHERE estado = 'activo'";

        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Componente componente = new Componente();
                componente.setIdComponente(rs.getInt("id_componente"));
                componente.setNombre(rs.getString("nombre"));
                componente.setCosto(rs.getDouble("costo"));
                componente.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                componente.setEstado(rs.getString("estado"));
                componentes.add(componente);
            }
        }
        return componentes;
    }


    // Método para obtener un componente activo por su ID
    public static Componente obtenerComponentePorId(int idComponente) throws SQLException {
        Componente componente = null;
        String query = "SELECT * FROM Componentes WHERE id_componente = ? AND estado = 'activo'";

        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idComponente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    componente = new Componente();
                    componente.setIdComponente(rs.getInt("id_componente"));
                    componente.setNombre(rs.getString("nombre"));
                    componente.setCosto(rs.getDouble("costo"));
                    componente.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                    componente.setEstado(rs.getString("estado"));
                }
            }
        }
        return componente;
    }


    // Método para actualizar un componente
    public static boolean actualizarComponente(Componente componente) throws SQLException {
        String query = "UPDATE Componentes SET nombre = ?, costo = ?, cantidad_disponible = ? WHERE id_componente = ?";
        
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, componente.getNombre());
            ps.setDouble(2, componente.getCosto());
            ps.setInt(3, componente.getCantidadDisponible());
            ps.setInt(4, componente.getIdComponente());
            int resultado = ps.executeUpdate();
            return resultado > 0;
        }
    }

    // Método para eliminar un componente con soft delete
    public static boolean eliminarComponente(int idComponente) throws SQLException {
        String query = "UPDATE Componentes SET estado = 'eliminado' WHERE id_componente = ?";

        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idComponente);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        }
    }

}
