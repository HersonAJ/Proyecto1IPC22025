/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

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
public class ComputadoraDB {

        //metodo usado por el vendedor para obtener las computadoras disponibles 
    public static List<Map<String, Object>> obtenerComputadorasConDetalles() throws SQLException {
        List<Map<String, Object>> computadorasConDetalles = new ArrayList<>();
        String query
                = "SELECT "
                + "    c.id_computadora, "
                + "    c.nombre AS nombre_computadora, "
                + "    c.precio_venta, "
                + "    c.costo_total, "
                + "    c.estado, "
                + "    e.fecha_ensamblaje, "
                + "    u.nombre_usuario AS ensamblador, "
                + "    GROUP_CONCAT(CONCAT(cmp.nombre, ' (', ep.cantidad, ')') SEPARATOR ', ') AS componentes "
                + "FROM "
                + "    Computadoras c "
                + "LEFT JOIN "
                + "    Ensamblaje e ON c.id_computadora = e.id_computadora "
                + "LEFT JOIN "
                + "    Usuarios u ON e.id_usuario = u.id_usuario "
                + "LEFT JOIN "
                + "    Ensamblaje_Piezas ep ON c.id_computadora = ep.id_computadora "
                + "LEFT JOIN "
                + "    Componentes cmp ON ep.id_componente = cmp.id_componente "
                + "WHERE "
                + "    c.estado = 'En Sala de Venta' "
                + "GROUP BY "
                + "    c.id_computadora, c.nombre, c.precio_venta, c.costo_total, c.estado, e.fecha_ensamblaje, u.nombre_usuario";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> computadora = new HashMap<>();
                computadora.put("idComputadora", rs.getInt("id_computadora"));
                computadora.put("nombre", rs.getString("nombre_computadora"));
                computadora.put("precioVenta", rs.getDouble("precio_venta"));
                computadora.put("estado", rs.getString("estado"));
                computadora.put("ensamblador", rs.getString("ensamblador"));
                computadora.put("fechaEnsamblaje", rs.getDate("fecha_ensamblaje"));
                computadora.put("componentes", rs.getString("componentes")); // Lista de componentes concatenados
                computadorasConDetalles.add(computadora);
            }
        }

        return computadorasConDetalles;
    }
    
}
