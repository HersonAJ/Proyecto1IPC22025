/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import backendDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author herson
 */
public class ComputadorasEnsambladasDB {

    // Método para registrar una computadora ensamblada
public static boolean registrarEnsamblaje(String nombreComputadora, String nombreUsuario, String fecha) {
    String consultaSQL = "INSERT INTO ComputadorasEnsambladas (id_tipo_computadora, usuario_ensamblador, costo_total, fecha_ensamblaje) " +
                         "SELECT tc.id_tipo_computadora, u.id_usuario, " +
                         "SUM(c.costo * ep.cantidad) AS costo_total, ? " +
                         "FROM TiposComputadoras tc " +
                         "JOIN Ensamblaje_Piezas ep ON tc.id_tipo_computadora = ep.id_tipo_computadora " +
                         "JOIN Componentes c ON ep.id_componente = c.id_componente " +
                         "JOIN Usuarios u ON u.nombre_usuario = ? " +
                         "WHERE tc.nombre = ? " +
                         "GROUP BY tc.id_tipo_computadora, u.id_usuario";

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

}

