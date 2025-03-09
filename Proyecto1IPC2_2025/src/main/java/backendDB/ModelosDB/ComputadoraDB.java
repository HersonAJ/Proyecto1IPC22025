/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Componente;
import Modelos.Computadora;
import backendDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author herson
 */
public class ComputadoraDB {

    // Método para registrar una computadora
    //lo usa el procesador de archivo
    public static boolean registrarComputadora(Computadora computadora) {
        String sql = "INSERT INTO Computadoras (nombre, precio_venta, costo_total) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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
    //lo usa ReporteGananciasServlet
    //lo usao VentaServlet
    public static Computadora obtenerComputadora(int idComputadora) throws SQLException {
        Computadora computadora = null;
        String query = "SELECT * FROM Computadoras WHERE id_computadora = ?";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
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

    // Método para obtener todas las computadoras
    //lo usa EliminarDetalleServlet
    //lo usa VentaServlet
    public static List<Computadora> obtenerComputadoras() throws SQLException {
        List<Computadora> computadoras = new ArrayList<>();
        String query = "SELECT * FROM Computadoras";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Computadora computadora = new Computadora();
                computadora.setIdComputadora(rs.getInt("id_computadora"));
                computadora.setNombre(rs.getString("nombre"));
                computadora.setPrecioVenta(rs.getDouble("precio_venta"));
                computadora.setCostoTotal(rs.getDouble("costo_total"));
                computadoras.add(computadora);
            }
        }
        return computadoras;
    }

    
    
    
    //metodo usado por el vendedor para obtener las computadoras disponibles 
    public static List<Map<String, Object>> obtenerComputadorasConDetalles() throws SQLException {
        List<Map<String, Object>> computadorasConDetalles = new ArrayList<>();
        String query
                = "SELECT "
                + "    c.id_computadora, "
                + "    c.nombre AS nombre_computadora, "
                + "    c.precio_venta, "
                + "    c.costo_total, "
                + "    c.estado, "
                + "    e.fecha_ensamblaje, "
                + "    u.nombre_usuario AS ensamblador, "
                + "    GROUP_CONCAT(CONCAT(cmp.nombre, ' (', ep.cantidad, ')') SEPARATOR ', ') AS componentes "
                + "FROM "
                + "    Computadoras c "
                + "LEFT JOIN "
                + "    Ensamblaje e ON c.id_computadora = e.id_computadora "
                + "LEFT JOIN "
                + "    Usuarios u ON e.id_usuario = u.id_usuario "
                + "LEFT JOIN "
                + "    Ensamblaje_Piezas ep ON c.id_computadora = ep.id_computadora "
                + "LEFT JOIN "
                + "    Componentes cmp ON ep.id_componente = cmp.id_componente "
                + "WHERE "
                + "    c.estado = 'En Sala de Venta' "
                + "GROUP BY "
                + "    c.id_computadora, c.nombre, c.precio_venta, c.costo_total, c.estado, e.fecha_ensamblaje, u.nombre_usuario";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> computadora = new HashMap<>();
                computadora.put("idComputadora", rs.getInt("id_computadora"));
                computadora.put("nombre", rs.getString("nombre_computadora"));
                computadora.put("precioVenta", rs.getDouble("precio_venta"));
                computadora.put("estado", rs.getString("estado"));
                computadora.put("ensamblador", rs.getString("ensamblador"));
                computadora.put("fechaEnsamblaje", rs.getDate("fecha_ensamblaje"));
                computadora.put("componentes", rs.getString("componentes")); // Lista de componentes concatenados
                computadorasConDetalles.add(computadora);
            }
        }

        return computadorasConDetalles;
    }
    
        // Método para obtener el nombre de una computadora por su ID
    //lo usa consultarComprasCliente.jsp
    public static String obtenerNombreComputadora(int idComputadora) {
        String sql = "SELECT nombre FROM Computadoras WHERE id_computadora = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idComputadora);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre"); // Devuelve el nombre de la computadora
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Nombre no encontrado"; // Valor predeterminado si no se encuentra la computadora
    }

}
