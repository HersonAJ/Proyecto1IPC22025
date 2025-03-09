/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Componente;
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
public class ComponenteDB {

    //metodo que toma en cuenta de que si ya hay un componente con el mismo nombre lo ingresara pero sumara y si el precio varia esto sera una
    //actualizacion al precio
public static boolean registrarComponente(Componente componente) {
    String verificarComponenteSQL = "SELECT id_componente, costo, cantidad_disponible FROM Componentes WHERE nombre = ? AND estado = 'activo'";
    String actualizarComponenteSQL = "UPDATE Componentes SET costo = ?, cantidad_disponible = cantidad_disponible + ? WHERE id_componente = ?";
    String insertarComponenteSQL = "INSERT INTO Componentes (nombre, costo, cantidad_disponible) VALUES (?, ?, ?)";

    try (Connection conn = ConexionDB.getConnection()) {
        // Limpiar el nombre del componente
        String nombreLimpio = componente.getNombre().replace("\"", "").replace("“", "").replace("”", "").trim();
        componente.setNombre(nombreLimpio);

        // Verificar si ya existe un componente con el mismo nombre limpio
        try (PreparedStatement verificarStmt = conn.prepareStatement(verificarComponenteSQL)) {
            verificarStmt.setString(1, nombreLimpio);
            try (ResultSet rs = verificarStmt.executeQuery()) {
                if (rs.next()) {
                    // Si el componente ya existe, actualizar precio y cantidad
                    int idComponenteExistente = rs.getInt("id_componente");
                    double costoExistente = rs.getDouble("costo");

                    // Actualizar costo solo si el nuevo costo es diferente
                    if (costoExistente != componente.getCosto()) {
                        System.out.println("Costo del componente actualizado de " + costoExistente + " a " + componente.getCosto());
                    }

                    try (PreparedStatement actualizarStmt = conn.prepareStatement(actualizarComponenteSQL)) {
                        actualizarStmt.setDouble(1, componente.getCosto()); // Establece el nuevo costo
                        actualizarStmt.setInt(2, componente.getCantidadDisponible()); // Incrementa la cantidad
                        actualizarStmt.setInt(3, idComponenteExistente);

                        if (actualizarStmt.executeUpdate() > 0) {
                            System.out.println("Componente actualizado correctamente: " + nombreLimpio);
                            return true;
                        } else {
                            System.out.println("Error al actualizar el componente: " + nombreLimpio);
                            return false;
                        }
                    }
                }
            }
        }

        // Si no existe, insertar el nuevo componente
        try (PreparedStatement insertarStmt = conn.prepareStatement(insertarComponenteSQL)) {
            insertarStmt.setString(1, nombreLimpio);
            insertarStmt.setDouble(2, componente.getCosto());
            insertarStmt.setInt(3, componente.getCantidadDisponible());

            if (insertarStmt.executeUpdate() > 0) {
                System.out.println("Componente registrado correctamente: " + nombreLimpio);
                return true;
            } else {
                System.out.println("Error al registrar el nuevo componente: " + nombreLimpio);
                return false;
            }
        }
    } catch (SQLException e) {
        System.out.println("Error en la base de datos al procesar el componente: " + componente.getNombre());
        e.printStackTrace();
        return false;
    }
}



    // Método para obtener todos los componentes activos
    //lo utiliza GestionComponenteServlet
    //lo utiliza gestionTipoComputadoras.jsp
    public static List<Componente> obtenerComponentes() throws SQLException {
        List<Componente> componentes = new ArrayList<>();
        String query = "SELECT * FROM Componentes WHERE estado = 'activo'";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Componente componente = new Componente();
                componente.setIdComponente(rs.getInt("id_componente"));
                componente.setNombre(rs.getString("nombre"));
                componente.setCosto(rs.getDouble("costo"));
                componente.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                componente.setEstado(rs.getString("estado"));
                componentes.add(componente);
            }
        }
        return componentes;
    }

    // Método para actualizar un componente
    //lo utiliza GestionComponentesServlet
    public static boolean actualizarComponente(Componente componente) throws SQLException {
        String query = "UPDATE Componentes SET nombre = ?, costo = ?, cantidad_disponible = ? WHERE id_componente = ?";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, componente.getNombre());
            ps.setDouble(2, componente.getCosto());
            ps.setInt(3, componente.getCantidadDisponible());
            ps.setInt(4, componente.getIdComponente());
            int resultado = ps.executeUpdate();
            return resultado > 0;
        }
    }

    // Método para eliminar un componente con soft delete
    //lo utiliza GestionComponentesSrvlet
    public static boolean eliminarComponente(int idComponente) throws SQLException {
        String query = "UPDATE Componentes SET estado = 'eliminado' WHERE id_componente = ?";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idComponente);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        }
    }

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

    // Método para obtener un componente activo por su ID
    //lo usa ensamblarComputadora.jsp
    public static Componente obtenerComponentePorId(int idComponente) throws SQLException {
        Componente componente = null;
        String query = "SELECT * FROM Componentes WHERE id_componente = ? AND estado = 'activo'";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idComponente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    componente = new Componente();
                    componente.setIdComponente(rs.getInt("id_componente"));
                    componente.setNombre(rs.getString("nombre"));
                    componente.setCosto(rs.getDouble("costo"));
                    componente.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                    componente.setEstado(rs.getString("estado"));
                }
            }
        }
        return componente;
    }

    //metodo opcional para obtener el inventario agrupado
    public static Map<String, Integer> obtenerInventarioAgrupado() throws SQLException {
        Map<String, Integer> inventario = new HashMap<>();
        String query = "SELECT nombre, SUM(cantidad_disponible) AS total_disponible "
                + "FROM Componentes "
                + "WHERE estado = 'activo' "
                + "GROUP BY nombre";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                inventario.put(rs.getString("nombre"), rs.getInt("total_disponible"));
            }
        }
        return inventario;
    }

    //este metodo se usarara para verificar si hay una cantidad disponible para ensamblar una computadora usando el archivo
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

    //metodo para reducir la cantidad en el inventario de las piezas que se van a usar
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
    
    //metodo para obtener los componentes con cantidada para ya ensamblar computadoras
    public static List<String[]> obtenerComponentesConCantidad(String nombreComputadora) {
    String consultaSQL = "SELECT c.nombre AS componente, ep.cantidad AS cantidad_requerida, c.cantidad_disponible " +
                         "FROM Ensamblaje_Piezas ep " +
                         "JOIN Componentes c ON ep.id_componente = c.id_componente " +
                         "JOIN TiposComputadoras tc ON ep.id_tipo_computadora = tc.id_tipo_computadora " +
                         "WHERE tc.nombre = ?";
    List<String[]> componentes = new ArrayList<>();
    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(consultaSQL)) {
        stmt.setString(1, nombreComputadora);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String nombreComponente = rs.getString("componente");
                int cantidadRequerida = rs.getInt("cantidad_requerida");
                int cantidadDisponible = rs.getInt("cantidad_disponible");
                componentes.add(new String[] {nombreComponente, String.valueOf(cantidadRequerida), String.valueOf(cantidadDisponible)});
            }
        }
    } catch (SQLException e) {
        System.out.println("Error al obtener componentes para la computadora '" + nombreComputadora + "': " + e.getMessage());
    }
    return componentes;
}


}
