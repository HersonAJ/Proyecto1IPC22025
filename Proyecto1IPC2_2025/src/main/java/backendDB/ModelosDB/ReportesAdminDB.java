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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author herson
 */
public class ReportesAdminDB {

    public static List<Usuario> obtenerUsuariosConVentas(String fechaInicio, String fechaFin) {
        List<Usuario> reporteUsuarios = new ArrayList<>();

        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.estado, r.nombre_rol, "
                + "       IFNULL(COUNT(v.id_venta), 0) AS total_ventas "
                + "FROM Usuarios u "
                + "LEFT JOIN Ventas v ON u.id_usuario = v.id_usuario "
                + "                 AND v.fecha_venta BETWEEN ? AND ? "
                + "JOIN Roles r ON u.id_rol = r.id_rol "
                + "WHERE u.id_rol = 3 AND u.estado = 'Activo' "
                + "GROUP BY u.id_usuario, u.nombre_usuario, u.estado, r.nombre_rol "
                + "ORDER BY total_ventas DESC";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Establecer los parámetros de fecha
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);

            System.out.println("Ejecutando consulta para el reporte de vendedores activos con más ventas...");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setEstado(rs.getString("estado")); // Estado del usuario (Activo)
                    usuario.setRolNombre(rs.getString("nombre_rol")); // Nombre del rol (Vendedor)
                    usuario.setTotalVentas(rs.getInt("total_ventas")); // Total de ventas
                    reporteUsuarios.add(usuario);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el reporte de vendedores con más ventas.");
            e.printStackTrace();
        }

        return reporteUsuarios;
    }

    public static List<Usuario> obtenerUsuariosConGanancias(String fechaInicio, String fechaFin) {
        List<Usuario> reporteUsuarios = new ArrayList<>();

        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.estado, r.nombre_rol, "
                + "       IFNULL(SUM(dv.subtotal), 0) AS total_ganancias "
                + "FROM Usuarios u "
                + "LEFT JOIN Ventas v ON u.id_usuario = v.id_usuario "
                + "LEFT JOIN Detalle_Ventas dv ON v.id_venta = dv.id_venta "
                + "JOIN Roles r ON u.id_rol = r.id_rol "
                + "WHERE u.id_rol = 3 AND u.estado = 'Activo' "
                + "AND (v.fecha_venta BETWEEN ? AND ? OR v.id_venta IS NULL) "
                + "GROUP BY u.id_usuario, u.nombre_usuario, u.estado, r.nombre_rol "
                + "ORDER BY total_ganancias DESC";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Establecer los parámetros de fecha
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);

            System.out.println("Ejecutando consulta para el reporte de ganancias por vendedor...");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setEstado(rs.getString("estado")); // Estado del usuario (Activo)
                    usuario.setRolNombre(rs.getString("nombre_rol")); // Nombre del rol (Vendedor)
                    usuario.setTotalVentas(rs.getInt("total_ganancias")); // Asignamos las ganancias totales como totalVentas
                    reporteUsuarios.add(usuario);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el reporte de ganancias por vendedor.");
            e.printStackTrace();
        }

        return reporteUsuarios;
    }
    
public static List<Map<String, Object>> obtenerComputadorasVendidas(String fechaInicio, String fechaFin) {
    List<Map<String, Object>> listaComputadoras = new ArrayList<>();
    String sql = "";

    if (fechaInicio != null && !fechaInicio.isEmpty() && fechaFin != null && !fechaFin.isEmpty()) {
        // Consulta con filtro de fechas
        sql = "SELECT tc.nombre AS tipo_computadora, " +
              "       SUM(dv.cantidad) AS total_vendidas " +
              "FROM TiposComputadoras tc " +
              "JOIN ComputadorasEnsambladas ce ON tc.id_tipo_computadora = ce.id_tipo_computadora " +
              "JOIN Detalle_Ventas dv ON ce.id_computadora = dv.id_computadora " +
              "JOIN Ventas v ON dv.id_venta = v.id_venta " +
              "WHERE v.fecha_venta BETWEEN ? AND ? " +
              "GROUP BY tc.nombre " +
              "ORDER BY total_vendidas DESC";
    } else {
        // Consulta global (sin filtro de fechas)
        sql = "SELECT tc.nombre AS tipo_computadora, " +
              "       SUM(dv.cantidad) AS total_vendidas " +
              "FROM TiposComputadoras tc " +
              "JOIN ComputadorasEnsambladas ce ON tc.id_tipo_computadora = ce.id_tipo_computadora " +
              "JOIN Detalle_Ventas dv ON ce.id_computadora = dv.id_computadora " +
              "GROUP BY tc.nombre " +
              "ORDER BY total_vendidas DESC";
    }

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        if (fechaInicio != null && !fechaInicio.isEmpty() && fechaFin != null && !fechaFin.isEmpty()) {
            // Establecer los parámetros si hay un rango de fechas
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
        }

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Crear un mapa para almacenar cada fila
                Map<String, Object> computadora = new HashMap<>();
                computadora.put("tipo_computadora", rs.getString("tipo_computadora"));
                computadora.put("total_vendidas", rs.getInt("total_vendidas"));
                listaComputadoras.add(computadora);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener las computadoras más vendidas.");
        e.printStackTrace();
    }

    return listaComputadoras;
}


}
