/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Cliente;
import Modelos.DetalleVenta;
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
public class ComprasClienteDB {

    // Método para obtener un cliente por su NIT
    public static Cliente obtenerClientePorNit(String nit) throws SQLException {
        Cliente cliente = null;
        String sql = "SELECT * FROM Clientes WHERE nit = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nit);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    cliente.setNit(rs.getString("nit"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setDireccion(rs.getString("direccion"));
                }
            }
        }
        return cliente;
    }

    // Método para obtener las compras de un cliente en un intervalo de tiempo (o todas si no se especifican fechas)
    public static List<Venta> obtenerComprasCliente(String nit, String fechaInicio, String fechaFin) throws SQLException {
        List<Venta> compras = new ArrayList<>();

        // Obtener el cliente por su NIT
        Cliente cliente = obtenerClientePorNit(nit);
        if (cliente == null) {
            throw new SQLException("No se encontró un cliente con el NIT proporcionado.");
        }

        // Construir la consulta SQL dinámicamente
        String query = "SELECT * FROM Ventas WHERE id_cliente = ?";
        boolean filtrarPorFechas = (fechaInicio != null && !fechaInicio.isEmpty()) && (fechaFin != null && !fechaFin.isEmpty());
        if (filtrarPorFechas) {
            query += " AND fecha_venta BETWEEN ? AND ?";
        }

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, cliente.getIdCliente());

            // Solo agregar parámetros de fechas si están disponibles
            if (filtrarPorFechas) {
                ps.setString(2, fechaInicio);
                ps.setString(3, fechaFin);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Venta venta = new Venta();
                    venta.setIdVenta(rs.getInt("id_venta"));
                    venta.setIdCliente(rs.getInt("id_cliente"));
                    venta.setIdUsuario(rs.getInt("id_usuario"));
                    venta.setFechaVenta(rs.getDate("fecha_venta"));
                    venta.setTotalVenta(rs.getDouble("total_venta"));
                    venta.setNumeroFactura(rs.getInt("numero_factura"));
                    compras.add(venta);
                }

            }
        }
        return compras;
    }

    // Método para obtener los detalles de una compra específica
    public static List<DetalleVenta> obtenerDetallesDeCompra(int idVenta) throws SQLException {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM Detalle_Ventas WHERE id_venta = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                    detalle.setIdVenta(rs.getInt("id_venta"));
                    detalle.setIdComputadora(rs.getInt("id_computadora"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setSubtotal(rs.getDouble("subtotal"));
                    detalles.add(detalle);
                }
            }
        }
        return detalles;
    }

    // Método para obtener el nombre de una computadora por su ID
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

    
    
    ///obtener todos los detalles de una factura
    public static Map<String, Object> obtenerFacturaPorNumero(int numeroFactura) throws SQLException {
        Map<String, Object> facturaCompleta = new HashMap<>();
        List<DetalleVenta> detalles = new ArrayList<>();

        String sqlFactura
                = "SELECT v.id_venta, v.fecha_venta, v.total_venta, v.numero_factura, "
                + "       c.nit AS cliente_nit, c.nombre AS cliente_nombre, c.direccion AS cliente_direccion, "
                + "       u.nombre_usuario AS vendedor_nombre "
                + "FROM Ventas v "
                + "JOIN Clientes c ON v.id_cliente = c.id_cliente "
                + "JOIN Usuarios u ON v.id_usuario = u.id_usuario "
                + "WHERE v.numero_factura = ?";

        String sqlDetalles
                = "SELECT dv.id_detalle_venta, dv.id_venta, dv.id_computadora, dv.cantidad, dv.subtotal "
                + "FROM Detalle_Ventas dv "
                + "JOIN Ventas v ON dv.id_venta = v.id_venta "
                + "WHERE v.numero_factura = ?";

        try (Connection conn = ConexionDB.getConnection()) {
            // Obtener datos generales de la factura
            try (PreparedStatement stmtFactura = conn.prepareStatement(sqlFactura)) {
                stmtFactura.setInt(1, numeroFactura);
                try (ResultSet rsFactura = stmtFactura.executeQuery()) {
                    if (rsFactura.next()) {
                        facturaCompleta.put("id_venta", rsFactura.getInt("id_venta"));
                        facturaCompleta.put("fecha_venta", rsFactura.getDate("fecha_venta"));
                        facturaCompleta.put("total_venta", rsFactura.getDouble("total_venta"));
                        facturaCompleta.put("numero_factura", rsFactura.getInt("numero_factura"));
                        facturaCompleta.put("cliente_nit", rsFactura.getString("cliente_nit"));
                        facturaCompleta.put("cliente_nombre", rsFactura.getString("cliente_nombre"));
                        facturaCompleta.put("cliente_direccion", rsFactura.getString("cliente_direccion"));
                        facturaCompleta.put("vendedor_nombre", rsFactura.getString("vendedor_nombre"));
                    } else {
                        return null; // No se encontró la factura
                    }
                }
            }

            // Obtener los detalles de la factura
            try (PreparedStatement stmtDetalles = conn.prepareStatement(sqlDetalles)) {
                stmtDetalles.setInt(1, numeroFactura);
                try (ResultSet rsDetalles = stmtDetalles.executeQuery()) {
                    while (rsDetalles.next()) {
                        DetalleVenta detalle = new DetalleVenta();
                        detalle.setIdDetalleVenta(rsDetalles.getInt("id_detalle_venta"));
                        detalle.setIdVenta(rsDetalles.getInt("id_venta"));
                        detalle.setIdComputadora(rsDetalles.getInt("id_computadora"));
                        detalle.setCantidad(rsDetalles.getInt("cantidad"));
                        detalle.setSubtotal(rsDetalles.getDouble("subtotal"));
                        detalles.add(detalle);
                    }
                }
            }

            facturaCompleta.put("detalles", detalles);
        }

        return facturaCompleta;
    }

}
