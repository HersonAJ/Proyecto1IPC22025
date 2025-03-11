/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB.ComponentesDB;

import Modelos.Componente;
import backendDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author herson
 */
public class ComponenteRegistroDB {

    /*metodo que toma en cuenta de que si ya hay un componente con el mismo nombre lo ingresara pero sumara y si el precio varia esto sera una
    actualizacion al precio
    se usa en procesar componente y en GestionComponentesServlet
    */
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
}
