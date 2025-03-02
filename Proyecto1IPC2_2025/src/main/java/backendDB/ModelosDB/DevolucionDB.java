/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Devolucion;
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
public class DevolucionDB {

    // Obtener todas las devoluciones en un intervalo de tiempo
    public static List<Devolucion> obtenerDevoluciones(String fechaInicio, String fechaFin) throws SQLException {
        List<Devolucion> devoluciones = new ArrayList<>();
        String query = "SELECT id_devolucion, id_venta, id_computadora, id_cliente, fecha_devolucion, monto_perdida FROM Devoluciones WHERE fecha_devolucion BETWEEN ? AND ?";

        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Devolucion devolucion = new Devolucion();
                    devolucion.setIdDevolucion(rs.getInt("id_devolucion"));
                    devolucion.setIdVenta(rs.getInt("id_venta"));
                    devolucion.setIdComputadora(rs.getInt("id_computadora"));
                    devolucion.setIdCliente(rs.getInt("id_cliente"));
                    devolucion.setFechaDevolucion(rs.getString("fecha_devolucion"));
                    devolucion.setMontoPerdida(rs.getDouble("monto_perdida"));
                    devoluciones.add(devolucion);
                }
            }
        }
        return devoluciones;
    }

    // Insertar una nueva devolución
    public static void insertarDevolucion(Devolucion devolucion) throws SQLException {
        String query = "INSERT INTO Devoluciones (id_venta, id_computadora, id_cliente, fecha_devolucion, monto_perdida) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, devolucion.getIdVenta());
            ps.setInt(2, devolucion.getIdComputadora());
            ps.setInt(3, devolucion.getIdCliente());
            ps.setString(4, devolucion.getFechaDevolucion());
            ps.setDouble(5, devolucion.getMontoPerdida());
            ps.executeUpdate();
        }
    }
}

