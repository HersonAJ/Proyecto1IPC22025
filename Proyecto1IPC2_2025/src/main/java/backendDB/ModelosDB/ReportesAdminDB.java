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
import java.util.List;

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

}
