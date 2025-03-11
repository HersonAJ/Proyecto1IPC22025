/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB.ComponentesDB;

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
public class InventarioDB {

    // lo usa EnsamblarComputadoraServlet
    public static int obtenerCantidadDisponible(int idComponente) throws SQLException {
        int cantidadDisponible = 0;
        String query = "SELECT cantidad_disponible FROM Componentes WHERE id_componente = ?";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idComponente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cantidadDisponible = rs.getInt("cantidad_disponible");
                }
            }
        }
        return cantidadDisponible;
    }

    //lo utiliza EnsamblarComputadoraServlet
    public static boolean actualizarCantidad(int idComponente, int cantidad) throws SQLException {
        String query = "UPDATE Componentes SET cantidad_disponible = cantidad_disponible + ? WHERE id_componente = ?";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idComponente);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        }
    }

    //este metodo se usarara para verificar si hay una cantidad disponible para ensamblar una computadora usando el archivo
    //lo usa EnsamblarComputadora2Servlet
    //lo usa ProcesarEnsamblajeComputadoras
    public static List<String> validarInventarioComponentes(String nombreComputadora) {
        String consultaSQL = "SELECT c.nombre, ep.cantidad, c.cantidad_disponible "
                + "FROM Ensamblaje_Piezas ep "
                + "JOIN Componentes c ON ep.id_componente = c.id_componente "
                + "JOIN TiposComputadoras tc ON ep.id_tipo_computadora = tc.id_tipo_computadora "
                + "WHERE tc.nombre = ?";
        List<String> componentesInsuficientes = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(consultaSQL)) {
            stmt.setString(1, nombreComputadora);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int cantidadRequerida = rs.getInt("cantidad");
                    int cantidadDisponible = rs.getInt("cantidad_disponible");
                    if (cantidadDisponible < cantidadRequerida) {
                        componentesInsuficientes.add(rs.getString("nombre"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar inventario de componentes: " + e.getMessage());
        }
        return componentesInsuficientes;
    }

    //metodo para obtener los componentes con cantidada para ya ensamblar computadoras
    //lo usa EnsamblarComputadora2Servlet
    public static List<String[]> obtenerComponentesConCantidad(String nombreComputadora) {
        String consultaSQL = "SELECT c.nombre AS componente, ep.cantidad AS cantidad_requerida, c.cantidad_disponible "
                + "FROM Ensamblaje_Piezas ep "
                + "JOIN Componentes c ON ep.id_componente = c.id_componente "
                + "JOIN TiposComputadoras tc ON ep.id_tipo_computadora = tc.id_tipo_computadora "
                + "WHERE tc.nombre = ?";
        List<String[]> componentes = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(consultaSQL)) {
            stmt.setString(1, nombreComputadora);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nombreComponente = rs.getString("componente");
                    int cantidadRequerida = rs.getInt("cantidad_requerida");
                    int cantidadDisponible = rs.getInt("cantidad_disponible");
                    componentes.add(new String[]{nombreComponente, String.valueOf(cantidadRequerida), String.valueOf(cantidadDisponible)});
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener componentes para la computadora '" + nombreComputadora + "': " + e.getMessage());
        }
        return componentes;
    }

    //metodo para reducir la cantidad en el inventario de las piezas que se van a usar
    //lo usa EnsamblarComputadora2Servlet
    //lo usa ProcesarEnsamblajeComputadoras
    public static boolean actualizarInventario(String nombreComputadora) {
        String consultaSQL = "UPDATE Componentes c "
                + "JOIN Ensamblaje_Piezas ep ON c.id_componente = ep.id_componente "
                + "JOIN TiposComputadoras tc ON ep.id_tipo_computadora = tc.id_tipo_computadora "
                + "SET c.cantidad_disponible = c.cantidad_disponible - ep.cantidad "
                + "WHERE tc.nombre = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(consultaSQL)) {
            stmt.setString(1, nombreComputadora);
            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar inventario: " + e.getMessage());
        }
        return false;
    }

}
