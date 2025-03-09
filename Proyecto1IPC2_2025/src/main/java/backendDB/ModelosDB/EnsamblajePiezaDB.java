/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.EnsamblajePieza;
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
public class EnsamblajePiezaDB {

    
    //metodo para obtener todas las piezas de un ensamblaje de computadora
    //lo esta usando EnsamblarComputadoraServlet pero luego se tiene que eliminar al igual que ese servlet
    public static List<EnsamblajePieza> obtenerEnsamblajePiezas (int idComputadora) throws SQLException {
        List<EnsamblajePieza> piezas = new ArrayList<>();
        String sql = "SELECT * FROM Ensamblaje_Piezas WHERE id_computadora = ?";
        
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idComputadora);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EnsamblajePieza pieza = new EnsamblajePieza();
                    pieza.setIdEnsamblajePieza(rs.getInt("id_ensamblaje_pieza"));
                    pieza.setIdComputadora(rs.getInt("id_computadora"));
                    pieza.setIdComponente(rs.getInt("id_componente"));
                    pieza.setCantidad(rs.getInt("cantidad"));
                    piezas.add(pieza);
                }
            }
        }
        return piezas;
    }
    
    //metodo nuevo para crear la relacion validando que exista la computadora y la pieza sin validar la cantidad disponible
    // Método para crear la relación entre un tipo de computadora y un componente
    public static boolean crearRelacion(String nombreComputadora, String nombreComponente, int cantidad) {
        String verificarTipoSQL = "SELECT id_tipo_computadora FROM TiposComputadoras WHERE nombre = ?";
        String verificarComponenteSQL = "SELECT id_componente FROM Componentes WHERE nombre = ?";
        String insertarRelacionSQL = "INSERT INTO Ensamblaje_Piezas (id_tipo_computadora, id_componente, cantidad) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection()) {
            // Verificar que el tipo de computadora exista
            int idTipoComputadora = -1;
            try (PreparedStatement verificarTipoStmt = conn.prepareStatement(verificarTipoSQL)) {
                verificarTipoStmt.setString(1, nombreComputadora);
                try (ResultSet rs = verificarTipoStmt.executeQuery()) {
                    if (rs.next()) {
                        idTipoComputadora = rs.getInt("id_tipo_computadora");
                    } else {
                        System.out.println("Error: El tipo de computadora no existe: " + nombreComputadora);
                        return false; // Tipo de computadora no encontrado
                    }
                }
            }

            // Verificar que el componente exista
            int idComponente = -1;
            try (PreparedStatement verificarComponenteStmt = conn.prepareStatement(verificarComponenteSQL)) {
                verificarComponenteStmt.setString(1, nombreComponente);
                try (ResultSet rs = verificarComponenteStmt.executeQuery()) {
                    if (rs.next()) {
                        idComponente = rs.getInt("id_componente");
                    } else {
                        System.out.println("Error: El componente no existe: " + nombreComponente);
                        return false; // Componente no encontrado
                    }
                }
            }

            // Insertar la relación en la tabla Ensamblaje_Piezas
            try (PreparedStatement insertarRelacionStmt = conn.prepareStatement(insertarRelacionSQL)) {
                insertarRelacionStmt.setInt(1, idTipoComputadora);
                insertarRelacionStmt.setInt(2, idComponente);
                insertarRelacionStmt.setInt(3, cantidad);

                int filasInsertadas = insertarRelacionStmt.executeUpdate();
                if (filasInsertadas > 0) {
                    System.out.println("Relación creada correctamente: TipoComputadora = " + nombreComputadora +
                                       ", Componente = " + nombreComponente + ", Cantidad = " + cantidad);
                    return true;
                } else {
                    System.out.println("Error: No se pudo crear la relación.");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error de base de datos al crear la relación: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    //metodo que devuelve el tipo de computadora y sus componentes 
    //lo usara gestionarTipoComputadoras.jsp

    public static List<String> obtenerComponentesPorTipoComputadora() {
        String consultaSQL = "SELECT tc.nombre AS Tipo_Computadora, " +
                             "c.nombre AS Componente, " +
                             "ep.cantidad AS Cantidad_Requerida " +
                             "FROM Ensamblaje_Piezas ep " +
                             "JOIN TiposComputadoras tc ON ep.id_tipo_computadora = tc.id_tipo_computadora " +
                             "JOIN Componentes c ON ep.id_componente = c.id_componente " +
                             "ORDER BY tc.nombre, c.nombre";

        List<String> listaResultados = new ArrayList<>();

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(consultaSQL);
             ResultSet rs = stmt.executeQuery()) {

            // Procesar los resultados
            while (rs.next()) {
                String resultado = "Computadora: " + rs.getString("Tipo_Computadora") +
                                   ", Componente: " + rs.getString("Componente") +
                                   ", Cantidad: " + rs.getInt("Cantidad_Requerida");
                listaResultados.add(resultado);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener los componentes por tipo de computadora: " + e.getMessage());
            e.printStackTrace();
        }

        return listaResultados;
    }
}

