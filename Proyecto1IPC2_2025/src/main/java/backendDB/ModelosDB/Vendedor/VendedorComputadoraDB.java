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

    //metodo para registrar la venta
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
        
            public static boolean registrarDetalleVenta(DetalleVenta detalle) {
        String sql = "INSERT INTO Detalle_Ventas (id_venta, id_computadora, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getIdVenta());
            stmt.setInt(2, detalle.getIdComputadora());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getSubtotal());

            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
