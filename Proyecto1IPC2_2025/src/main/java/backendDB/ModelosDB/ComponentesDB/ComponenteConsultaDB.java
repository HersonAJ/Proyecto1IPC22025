/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB.ComponentesDB;

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
public class ComponenteConsultaDB {

    // Método para obtener todos los componentes activos
    //lo utiliza GestionComponenteServlet
    //lo utiliza gestionTipoComputadoras.jsp
    public static List<Componente> obtenerComponentes() throws SQLException {
        List<Componente> componentes = new ArrayList<>();
        String query = "SELECT * FROM Componentes WHERE estado = 'activo'";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
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

    //metodos para consultar componentes que usara el ensamblador se usan en ConsultarComponentesServlet
    public static List<Componente> obtenerTodosLosComponentes() {
        String consultaSQL = "SELECT * FROM Componentes";
        List<Componente> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(consultaSQL); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Componente componente = new Componente();
                componente.setIdComponente(rs.getInt("id_componente"));
                componente.setNombre(rs.getString("nombre"));
                componente.setCosto(rs.getDouble("costo"));
                componente.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                componente.setEstado(rs.getString("estado"));
                lista.add(componente);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener todos los componentes: " + e.getMessage());
        }

        return lista;
    }

    public static List<Componente> buscarComponentesPorNombre(String nombre) {
        String consultaSQL = "SELECT * FROM Componentes WHERE nombre LIKE ?";
        List<Componente> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(consultaSQL)) {

            stmt.setString(1, "%" + nombre + "%"); // Coincidencia parcial
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Componente componente = new Componente();
                componente.setIdComponente(rs.getInt("id_componente"));
                componente.setNombre(rs.getString("nombre"));
                componente.setCosto(rs.getDouble("costo"));
                componente.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                componente.setEstado(rs.getString("estado"));
                lista.add(componente);
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar componentes por nombre: " + e.getMessage());
        }

        return lista;
    }

    // Método para obtener un componente activo por su ID
    //lo usa ensamblarComputadora.jsp
    public static Componente obtenerComponentePorId(int idComponente) throws SQLException {
        Componente componente = null;
        String query = "SELECT * FROM Componentes WHERE id_componente = ? AND estado = 'activo'";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
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
}
