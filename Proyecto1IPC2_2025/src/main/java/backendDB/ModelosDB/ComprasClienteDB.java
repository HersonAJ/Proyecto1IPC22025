/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Cliente;
import Modelos.ComputadoraEnsamblada;
import Modelos.DetalleVenta;
import Modelos.Venta;
import backendDB.ConexionDB;
import backendDB.ModelosDB.Vendedor.VendedorComputadoraDB;
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


    // Método para obtener las compras de un cliente en un intervalo de tiempo (o todas si no se especifican fechas)
    //el metodo lo usa el ComprasClienteServlet
    public static List<Venta> obtenerComprasCliente(String nit, String fechaInicio, String fechaFin) throws SQLException {
        List<Venta> compras = new ArrayList<>();

        // Obtener el cliente por su NIT
        Cliente cliente = ClienteDB.obtenerClientePorNit(nit);
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
 
    ///obtener todos los detalles de una factura
    //lo usa ConsultarFacturaServlet
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
