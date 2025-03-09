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
        // Verificar si ya existe un componente con el mismo nombre
        System.out.println("Verificando si el componente ya existe: " + componente.getNombre());
        try (PreparedStatement verificarStmt = conn.prepareStatement(verificarComponenteSQL)) {
            verificarStmt.setString(1, componente.getNombre());
            try (ResultSet rs = verificarStmt.executeQuery()) {
                if (rs.next()) {
                    // Si el componente ya existe, obtener sus datos
                    double costoExistente = rs.getDouble("costo");
                    int idComponenteExistente = rs.getInt("id_componente");
                    int nuevaCantidad = componente.getCantidadDisponible();

                    System.out.println("Componente ya existe con ID: " + idComponenteExistente + ", Costo actual: " + costoExistente);

                    // Actualizar el precio y la cantidad
                    System.out.println("Actualizando el precio del componente a: " + componente.getCosto() + 
                                       " y aumentando la cantidad.");
                    try (PreparedStatement actualizarStmt = conn.prepareStatement(actualizarComponenteSQL)) {
                        actualizarStmt.setDouble(1, componente.getCosto()); // Actualizar el costo al nuevo valor
                        actualizarStmt.setInt(2, nuevaCantidad); // Incrementar la cantidad
                        actualizarStmt.setInt(3, idComponenteExistente);
                        boolean resultado = actualizarStmt.executeUpdate() > 0;

                        if (resultado) {
                            System.out.println("Precio y cantidad actualizados correctamente para el componente: " + componente.getNombre());
                        } else {
                            System.out.println("Error al actualizar el precio y la cantidad del componente: " + componente.getNombre());
                        }
                        return resultado;
                    }
                }
            }
        }

        // Si no existe, insertar el nuevo componente
        System.out.println("Componente no existe. Insertando nuevo componente: " + componente.getNombre());
        try (PreparedStatement insertarStmt = conn.prepareStatement(insertarComponenteSQL)) {
            insertarStmt.setString(1, componente.getNombre());
            insertarStmt.setDouble(2, componente.getCosto());
            insertarStmt.setInt(3, componente.getCantidadDisponible());
            boolean resultado = insertarStmt.executeUpdate() > 0;

            if (resultado) {
                System.out.println("Componente registrado correctamente: " + componente.getNombre());
            } else {
                System.out.println("Error al registrar el nuevo componente: " + componente.getNombre());
            }
            return resultado;
        }
    } catch (SQLException e) {
        System.out.println("Error en la base de datos al procesar el componente: " + componente.getNombre());
        e.printStackTrace();
        return false;
    }
}


    // Método para obtener todos los componentes activos
    //lo utiliza GestionComponenteServlet
    public static List<Componente> obtenerComponentes() throws SQLException {
        List<Componente> componentes = new ArrayList<>();
        String query = "SELECT * FROM Componentes WHERE estado = 'activo'";

        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query); 
             ResultSet rs = ps.executeQuery()) {
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
        
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
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

        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idComponente);
            int resultado = ps.executeUpdate();
            return resultado > 0;
        }
    }
    
    // lo usa EnsamblarComputadoraServlet
    public static int obtenerCantidadDisponible(int idComponente) throws SQLException {
        int cantidadDisponible = 0;
        String query = "SELECT cantidad_disponible FROM Componentes WHERE id_componente = ?";

        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
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

        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
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

        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
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
    String query = "SELECT nombre, SUM(cantidad_disponible) AS total_disponible " +
                   "FROM Componentes " +
                   "WHERE estado = 'activo' " +
                   "GROUP BY nombre";

    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            inventario.put(rs.getString("nombre"), rs.getInt("total_disponible"));
        }
    }
    return inventario;
}


}
