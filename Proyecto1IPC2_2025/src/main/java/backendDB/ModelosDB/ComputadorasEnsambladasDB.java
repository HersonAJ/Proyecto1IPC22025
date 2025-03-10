/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.ComputadoraEnsamblada;
import Modelos.TipoComputadora;
import Modelos.Usuario;
import backendDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author herson
 */
public class ComputadorasEnsambladasDB {

    // Método para registrar una computadora ensamblada y desde la base de datos se configura como "Ensamblada"
    public static boolean registrarEnsamblaje(String nombreComputadora, String nombreUsuario, String fecha) {
        String consultaSQL = "INSERT INTO ComputadorasEnsambladas (id_tipo_computadora, usuario_ensamblador, costo_total, fecha_ensamblaje) "
                + "SELECT tc.id_tipo_computadora, u.id_usuario, "
                + "SUM(c.costo * ep.cantidad) AS costo_total, ? "
                + "FROM TiposComputadoras tc "
                + "JOIN Ensamblaje_Piezas ep ON tc.id_tipo_computadora = ep.id_tipo_computadora "
                + "JOIN Componentes c ON ep.id_componente = c.id_componente "
                + "JOIN Usuarios u ON u.nombre_usuario = ? "
                + "WHERE tc.nombre = ? "
                + "GROUP BY tc.id_tipo_computadora, u.id_usuario";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(consultaSQL)) {
            // Convertir la fecha al formato correcto
            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechaFormateada = LocalDate.parse(fecha, formatoEntrada).format(formatoSalida);

            stmt.setString(1, fechaFormateada); // Fecha de ensamblaje en formato 'yyyy-MM-dd'
            stmt.setString(2, nombreUsuario); // Usuario ensamblador
            stmt.setString(3, nombreComputadora); // Tipo de computadora

            int filasInsertadas = stmt.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Ensamblaje registrado correctamente para la computadora: " + nombreComputadora);
                return true;
            } else {
                System.out.println("No se pudo registrar el ensamblaje para la computadora: " + nombreComputadora);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error al registrar ensamblaje: " + e.getMessage());
            return false;
        }
    }

    //este metodo se usara por el ensamblador para registras las computadoras ensambladas a la sala de ventas
    public static boolean actualizarEstadoComputadora(int idComputadora) {
        String consultaSQL = "UPDATE ComputadorasEnsambladas SET estado = 'En Sala de Venta' WHERE id_computadora = ? AND estado = 'Ensamblada'";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(consultaSQL)) {
            stmt.setInt(1, idComputadora); // ID de la computadora a actualizar
            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0; // Retorna true si al menos una fila fue actualizada
        } catch (SQLException e) {
            System.out.println("Error al actualizar el estado de la computadora: " + e.getMessage());
            return false;
        }
    }

    
    //este metodo muetra las computadoras ensambladas
public static List<ComputadoraEnsamblada> obtenerComputadorasEnsambladas() {
    String consultaSQL = "SELECT ce.id_computadora, ce.costo_total, ce.fecha_ensamblaje, ce.estado, " +
                         "tc.id_tipo_computadora, tc.nombre AS nombre_tipo, tc.precio_venta, " +
                         "u.id_usuario, u.nombre_usuario, r.nombre_rol " +
                         "FROM ComputadorasEnsambladas ce " +
                         "JOIN TiposComputadoras tc ON ce.id_tipo_computadora = tc.id_tipo_computadora " +
                         "JOIN Usuarios u ON ce.usuario_ensamblador = u.id_usuario " +
                         "JOIN Roles r ON u.id_rol = r.id_rol " +
                         "WHERE ce.estado IN ('Ensamblada', 'En Sala de Venta')"; // Ambos estados
    List<ComputadoraEnsamblada> lista = new ArrayList<>();

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(consultaSQL);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            ComputadoraEnsamblada computadora = new ComputadoraEnsamblada();

            // Asignar atributos simples
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

            // Mapear Usuario
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(rs.getInt("id_usuario"));
            usuario.setNombreUsuario(rs.getString("nombre_usuario"));
            usuario.setRolNombre(rs.getString("nombre_rol"));
            computadora.setUsuarioEnsamblador(usuario);

            // Agregar a la lista
            lista.add(computadora);
        }

    } catch (SQLException e) {
        System.out.println("Error al obtener computadoras ensambladas y en sala de venta: " + e.getMessage());
        e.printStackTrace();
    }

    return lista;
}

//metodo para consultar computadoras ensambladas del ensamblador
public static List<ComputadoraEnsamblada> obtenerDetallesComputadorasEnsambladas1() {
    String consultaSQL = "SELECT ce.id_computadora, ce.costo_total, ce.fecha_ensamblaje, ce.estado, " +
                         "tc.id_tipo_computadora, tc.nombre AS nombre_tipo, tc.precio_venta, " +
                         "u.id_usuario, u.nombre_usuario, r.nombre_rol, " +
                         "c.nombre AS componente_nombre, ep.cantidad AS componente_cantidad " +
                         "FROM ComputadorasEnsambladas ce " +
                         "JOIN TiposComputadoras tc ON ce.id_tipo_computadora = tc.id_tipo_computadora " +
                         "JOIN Usuarios u ON ce.usuario_ensamblador = u.id_usuario " +
                         "JOIN Roles r ON u.id_rol = r.id_rol " +
                         "JOIN Ensamblaje_Piezas ep ON tc.id_tipo_computadora = ep.id_tipo_computadora " +
                         "JOIN Componentes c ON ep.id_componente = c.id_componente " +
                         "WHERE ce.estado = 'Ensamblada' " +  // Solo ensambladas
                         "ORDER BY ce.id_computadora";
    List<ComputadoraEnsamblada> lista = new ArrayList<>();

    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(consultaSQL);
         ResultSet rs = stmt.executeQuery()) {

        // Mapeo para agrupar componentes por computadora
        int idComputadoraActual = -1; // Para rastrear el ID actual
        ComputadoraEnsamblada computadoraActual = null;

        while (rs.next()) {
            int idComputadora = rs.getInt("id_computadora");

            // Si cambiamos a una nueva computadora, agregamos la anterior a la lista
            if (idComputadora != idComputadoraActual) {
                if (computadoraActual != null) {
                    lista.add(computadoraActual);
                }

                // Crear nueva computadora ensamblada
                computadoraActual = new ComputadoraEnsamblada();
                computadoraActual.setIdComputadora(idComputadora);
                computadoraActual.setCostoTotal(rs.getDouble("costo_total"));
                computadoraActual.setFechaEnsamblaje(rs.getDate("fecha_ensamblaje"));
                computadoraActual.setEstado(rs.getString("estado"));

                // Mapear TipoComputadora
                TipoComputadora tipo = new TipoComputadora();
                tipo.setIdTipoComputadora(rs.getInt("id_tipo_computadora"));
                tipo.setNombre(rs.getString("nombre_tipo"));
                tipo.setPrecioVenta(rs.getDouble("precio_venta"));
                computadoraActual.setTipoComputadora(tipo);

                // Mapear Usuario
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                usuario.setRolNombre(rs.getString("nombre_rol"));
                computadoraActual.setUsuarioEnsamblador(usuario);

                // Inicializar lista de componentes
                computadoraActual.setComponentes(new ArrayList<>());

                idComputadoraActual = idComputadora; // Actualizar el ID actual
            }

            // Mapear componente y agregarlo a la lista de componentes de la computadora actual
            String nombreComponente = rs.getString("componente_nombre");
            int cantidadComponente = rs.getInt("componente_cantidad");
            computadoraActual.getComponentes().add(new String[] {nombreComponente, String.valueOf(cantidadComponente)});
        }

        // Agregar la última computadora a la lista si no se agregó antes
        if (computadoraActual != null) {
            lista.add(computadoraActual);
        }

    } catch (SQLException e) {
        System.out.println("Error al obtener detalles de computadoras ensambladas: " + e.getMessage());
        e.printStackTrace();
    }

    return lista;
}



}
