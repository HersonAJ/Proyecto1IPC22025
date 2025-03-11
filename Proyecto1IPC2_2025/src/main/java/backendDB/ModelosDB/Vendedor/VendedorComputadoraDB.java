/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB.Vendedor;

import Modelos.ComputadoraEnsamblada;
import Modelos.DetalleVenta;
import Modelos.TipoComputadora;
import Modelos.Usuario;
import Modelos.Venta;
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
public class VendedorComputadoraDB {
    //este metodo mostrara todas las computadoras en el selector de seleccionarComputadora.jsp
    public static List<ComputadoraEnsamblada> obtenerComputadorasEnSalaDeVentas() throws SQLException {
    List<ComputadoraEnsamblada> computadoras = new ArrayList<>();
    String consultaSQL = "SELECT ce.id_computadora, ce.costo_total, ce.fecha_ensamblaje, ce.estado, " +
                         "tc.id_tipo_computadora, tc.nombre AS nombre_tipo, tc.precio_venta, " +
                         "u.id_usuario, u.nombre_usuario " +
                         "FROM ComputadorasEnsambladas ce " +
                         "JOIN TiposComputadoras tc ON ce.id_tipo_computadora = tc.id_tipo_computadora " +
                         "JOIN Usuarios u ON ce.usuario_ensamblador = u.id_usuario " +
                         "WHERE ce.estado = 'En Sala de Venta'";

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(consultaSQL);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            // Crear una nueva instancia de ComputadoraEnsamblada
            ComputadoraEnsamblada computadora = new ComputadoraEnsamblada();
            computadora.setIdComputadora(rs.getInt("id_computadora"));
            computadora.setCostoTotal(rs.getDouble("costo_total"));
            computadora.setFechaEnsamblaje(rs.getDate("fecha_ensamblaje"));
            computadora.setEstado(rs.getString("estado"));

            // Mapear TipoComputadora
            TipoComputadora tipo = new TipoComputadora();
            tipo.setIdTipoComputadora(rs.getInt("id_tipo_computadora"));
            tipo.setNombre(rs.getString("nombre_tipo"));
            tipo.setPrecioVenta(rs.getDouble("precio_venta"));
            computadora.setTipoComputadora(tipo);

            // Mapear Usuario (ensamblador)
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(rs.getInt("id_usuario"));
            usuario.setNombreUsuario(rs.getString("nombre_usuario"));
            computadora.setUsuarioEnsamblador(usuario);

            // Agregar a la lista
            computadoras.add(computadora);
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener computadoras en sala de ventas: " + e.getMessage());
        e.printStackTrace();
    }

    return computadoras;
}
    
    //lo utiliza VentaServlet 
    public static ComputadoraEnsamblada obtenerComputadora(int idComputadora) throws SQLException {
    ComputadoraEnsamblada computadora = null;
    String query = "SELECT ce.id_computadora, ce.costo_total, ce.fecha_ensamblaje, ce.estado, " +
                   "tc.id_tipo_computadora, tc.nombre AS nombre_tipo, tc.precio_venta, " +
                   "u.id_usuario, u.nombre_usuario " +
                   "FROM ComputadorasEnsambladas ce " +
                   "JOIN TiposComputadoras tc ON ce.id_tipo_computadora = tc.id_tipo_computadora " +
                   "JOIN Usuarios u ON ce.usuario_ensamblador = u.id_usuario " +
                   "WHERE ce.id_computadora = ?";

    try (Connection con = ConexionDB.getConnection(); 
         PreparedStatement ps = con.prepareStatement(query)) {
        ps.setInt(1, idComputadora);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Crear instancia de ComputadoraEnsamblada
                computadora = new ComputadoraEnsamblada();
                computadora.setIdComputadora(rs.getInt("id_computadora"));
                computadora.setCostoTotal(rs.getDouble("costo_total"));
                computadora.setFechaEnsamblaje(rs.getDate("fecha_ensamblaje"));
                computadora.setEstado(rs.getString("estado"));

                // Mapear TipoComputadora
                TipoComputadora tipo = new TipoComputadora();
                tipo.setIdTipoComputadora(rs.getInt("id_tipo_computadora"));
                tipo.setNombre(rs.getString("nombre_tipo"));
                tipo.setPrecioVenta(rs.getDouble("precio_venta"));
                computadora.setTipoComputadora(tipo);

                // Mapear Usuario (ensamblador)
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                computadora.setUsuarioEnsamblador(usuario);
            }
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener la computadora ensamblada: " + e.getMessage());
        e.printStackTrace();
    }

    return computadora;
}

// Método para registrar una venta y actualizar el estado de todas las computadoras ensambladas asociadas
public static boolean registrarVenta(Venta venta, List<DetalleVenta> detalleVenta) {
    String sql = "INSERT INTO Ventas (id_cliente, id_usuario, fecha_venta, total_venta, numero_factura) VALUES (?, ?, ?, ?, ?)";
    String actualizarEstadoSQL = "UPDATE ComputadorasEnsambladas SET estado = 'Vendida' WHERE id_computadora = ?";

    try (Connection conn = ConexionDB.getConnection(); 
         PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

        System.out.println("Conectado a la base de datos para registrar una venta.");

        // Configurar los valores para la consulta de inserción
        System.out.println("Datos de la venta: idCliente=" + venta.getIdCliente() + ", idUsuario=" + venta.getIdUsuario() +
                           ", fechaVenta=" + venta.getFechaVenta() + ", totalVenta=" + venta.getTotalVenta() + 
                           ", numeroFactura=" + venta.getNumeroFactura());
        stmt.setInt(1, venta.getIdCliente());
        stmt.setInt(2, venta.getIdUsuario());
        stmt.setDate(3, new java.sql.Date(venta.getFechaVenta().getTime()));
        stmt.setDouble(4, venta.getTotalVenta());
        stmt.setInt(5, venta.getNumeroFactura()); // Inicialmente será 0

        // Ejecutar la inserción de la venta
        int filasInsertadas = stmt.executeUpdate();
        if (filasInsertadas > 0) {
            // Obtener el ID generado automáticamente para la venta
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idVentaGenerado = generatedKeys.getInt(1);
                venta.setIdVenta(idVentaGenerado);
                venta.setNumeroFactura(idVentaGenerado);
                System.out.println("Venta registrada con ID: " + idVentaGenerado);
            }

            // Actualizar el campo numero_factura en la base de datos
            String updateQuery = "UPDATE Ventas SET numero_factura = ? WHERE id_venta = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, venta.getNumeroFactura());
                updateStmt.setInt(2, venta.getIdVenta());
                int updateRows = updateStmt.executeUpdate();
                System.out.println("Actualización del número de factura completada. Filas afectadas: " + updateRows);
            }

            // Iterar sobre todos los detalles de venta para actualizar el estado de las computadoras ensambladas
            try (PreparedStatement actualizarEstadoStmt = conn.prepareStatement(actualizarEstadoSQL)) {
                for (DetalleVenta detalle : detalleVenta) {
                    System.out.println("Intentando actualizar el estado de la computadora ensamblada con ID: " + detalle.getIdComputadora());
                    actualizarEstadoStmt.setInt(1, detalle.getIdComputadora());
                    int filasActualizadas = actualizarEstadoStmt.executeUpdate();
                    if (filasActualizadas == 0) {
                        System.out.println("No se pudo actualizar el estado de la computadora ensamblada con ID: " + detalle.getIdComputadora());
                        return false; // Falló al actualizar una computadora
                    } else {
                        System.out.println("Estado de la computadora ensamblada actualizado correctamente para ID: " + detalle.getIdComputadora());
                    }
                }
            }

            return true; // Venta registrada y estados actualizados
        }

        System.out.println("No se pudo registrar la venta.");
        return false; // Falló al registrar la venta
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}



        
public static boolean registrarDetalleVenta(DetalleVenta detalle) {
    String sql = "INSERT INTO Detalle_Ventas (id_venta, id_computadora, cantidad, subtotal) VALUES (?, ?, ?, ?)";
    try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        System.out.println("Conectado a la base de datos para registrar el detalle de venta.");
        System.out.println("Datos del detalle: idVenta=" + detalle.getIdVenta() + ", idComputadora=" + detalle.getIdComputadora() +
                           ", cantidad=" + detalle.getCantidad() + ", subtotal=" + detalle.getSubtotal());

        stmt.setInt(1, detalle.getIdVenta());
        stmt.setInt(2, detalle.getIdComputadora());
        stmt.setInt(3, detalle.getCantidad());
        stmt.setDouble(4, detalle.getSubtotal());

        int filasInsertadas = stmt.executeUpdate();
        System.out.println("Detalle registrado. Filas insertadas: " + filasInsertadas);
        return filasInsertadas > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


}
