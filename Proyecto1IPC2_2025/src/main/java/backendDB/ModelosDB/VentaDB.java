/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Cliente;
import Modelos.DetalleVenta;
import Modelos.Usuario;
import Modelos.Venta;
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
public class VentaDB {

    // Obtener todas las ventas en un intervalo de tiempo
    //lo usa reporteGananciasServlet
    //lo usa ReporteVentasServlet
    public static List<Venta> obtenerVentas(String fechaInicio, String fechaFin) throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String query = "SELECT * FROM Ventas WHERE fecha_venta BETWEEN ? AND ?";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Venta venta = new Venta();
                    venta.setIdVenta(rs.getInt("id_venta"));
                    venta.setIdCliente(rs.getInt("id_cliente"));
                    venta.setIdUsuario(rs.getInt("id_usuario"));
                    venta.setFechaVenta(rs.getDate("fecha_venta"));
                    venta.setTotalVenta(rs.getDouble("total_venta"));
                    ventas.add(venta);
                }
            }
        }
        return ventas;
    }

    //metodos para funciones del vendedor 
    // Método para registrar una venta
    public static boolean registrarVenta(Venta venta) {
        String sql = "INSERT INTO Ventas (id_cliente, id_usuario, fecha_venta, total_venta, numero_factura) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Configurar los valores para la consulta de inserción
            stmt.setInt(1, venta.getIdCliente());
            stmt.setInt(2, venta.getIdUsuario());
            stmt.setDate(3, new java.sql.Date(venta.getFechaVenta().getTime()));
            stmt.setDouble(4, venta.getTotalVenta());
            stmt.setInt(5, venta.getNumeroFactura()); // Inicialmente será 0

            // Ejecutar la inserción
            int filasInsertadas = stmt.executeUpdate();
            if (filasInsertadas > 0) {
                // Obtener el ID generado automáticamente
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idVentaGenerado = generatedKeys.getInt(1);
                    venta.setIdVenta(idVentaGenerado);
                    venta.setNumeroFactura(idVentaGenerado);
                }

                // Actualizar el campo numero_factura en la base de datos
                String updateQuery = "UPDATE Ventas SET numero_factura = ? WHERE id_venta = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, venta.getNumeroFactura());
                    updateStmt.setInt(2, venta.getIdVenta());
                    updateStmt.executeUpdate();
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
//metodo para el registro de ventas del dia del vendedor
    public static List<Map<String, Object>> obtenerVentasPorFecha(String fecha) throws SQLException {
        List<Map<String, Object>> ventas = new ArrayList<>();
        String query
                = "SELECT "
                + "    v.id_venta, v.fecha_venta, v.total_venta, v.numero_factura, "
                + "    c.nombre AS nombre_cliente, c.nit AS nit_cliente, "
                + "    u.nombre_usuario AS vendedor, "
                + "    GROUP_CONCAT(CONCAT(comp.nombre, ' (', det.cantidad, ' unidades, Subtotal: Q', FORMAT(det.subtotal, 2), ')') SEPARATOR ' | ') AS detalles_computadoras "
                + "FROM Ventas v "
                + "JOIN Clientes c ON v.id_cliente = c.id_cliente "
                + "JOIN Usuarios u ON v.id_usuario = u.id_usuario "
                + "JOIN Detalle_Ventas det ON v.id_venta = det.id_venta "
                + "JOIN Computadoras comp ON det.id_computadora = comp.id_computadora "
                + "WHERE v.fecha_venta = ? "
                + "GROUP BY v.id_venta, v.fecha_venta, v.total_venta, v.numero_factura, c.nombre, c.nit, u.nombre_usuario";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, fecha); // Establecer la fecha proporcionada

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crear un mapa para almacenar los datos de la venta
                    Map<String, Object> venta = new HashMap<>();

                    // Datos generales de la venta
                    venta.put("idVenta", rs.getInt("id_venta"));
                    venta.put("fechaVenta", rs.getDate("fecha_venta"));
                    venta.put("totalVenta", rs.getDouble("total_venta"));
                    venta.put("numeroFactura", rs.getInt("numero_factura"));

                    // Datos del cliente
                    venta.put("nombreCliente", rs.getString("nombre_cliente"));
                    venta.put("nitCliente", rs.getString("nit_cliente"));

                    // Datos del vendedor
                    venta.put("vendedor", rs.getString("vendedor"));

                    // Detalles de las computadoras vendidas (concatenados en una cadena)
                    venta.put("detallesComputadoras", rs.getString("detalles_computadoras"));

                    // Añadir la venta a la lista de resultados
                    ventas.add(venta);
                }
            }
        }

        return ventas;
    }

        // Obtener una venta específica por su ID lo usa reporteDevoluciones.jsp
     public static Venta obtenerVenta(int idVenta) throws SQLException {
         Venta venta = null;
         String query = "SELECT * FROM Ventas WHERE id_venta = ?";
         
         try (Connection con = ConexionDB.getConnection(); 
              PreparedStatement ps = con.prepareStatement(query)) {
             ps.setInt(1, idVenta);
             
             try (ResultSet rs = ps.executeQuery()) {
                 if (rs.next()) {
                     venta = new Venta();
                     venta.setIdVenta(rs.getInt("id_venta"));
                     venta.setIdCliente(rs.getInt("id_cliente"));
                     venta.setIdUsuario(rs.getInt("id_usuario"));
                     venta.setFechaVenta(rs.getDate("fecha_venta"));
                     venta.setTotalVenta(rs.getDouble("total_venta"));
                 }
             }
         }
         return venta;
     }
}
