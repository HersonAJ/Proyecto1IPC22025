/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB.Vendedor;

import Modelos.ComputadoraEnsamblada;
import Modelos.TipoComputadora;
import backendDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author herson
 */
public class VendedorConsultaComprasCliente {

    // Método para obtener una computadora ensamblada completa por su ID
    public static ComputadoraEnsamblada obtenerComputadoraEnsamblada(int idComputadora) throws SQLException {
        ComputadoraEnsamblada computadora = null;
        String query = "SELECT ce.id_computadora, ce.costo_total, ce.fecha_ensamblaje, ce.estado, " +
                       "tc.id_tipo_computadora, tc.nombre AS nombre_tipo, tc.precio_venta " +
                       "FROM ComputadorasEnsambladas ce " +
                       "JOIN TiposComputadoras tc ON ce.id_tipo_computadora = tc.id_tipo_computadora " +
                       "WHERE ce.id_computadora = ?";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idComputadora);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    computadora = new ComputadoraEnsamblada();
                    computadora.setIdComputadora(rs.getInt("id_computadora"));
                    computadora.setCostoTotal(rs.getDouble("costo_total"));
                    computadora.setFechaEnsamblaje(rs.getDate("fecha_ensamblaje"));
                    computadora.setEstado(rs.getString("estado"));

                    // Mapear el tipo de computadora
                    TipoComputadora tipo = new TipoComputadora();
                    tipo.setIdTipoComputadora(rs.getInt("id_tipo_computadora"));
                    tipo.setNombre(rs.getString("nombre_tipo"));
                    tipo.setPrecioVenta(rs.getDouble("precio_venta"));
                    computadora.setTipoComputadora(tipo);
                }
            }
        }
        return computadora;
    }

    // Método para obtener únicamente el nombre de la computadora ensamblada por su ID
    //se usa en consultarComprasCliente.jsp
    public static String obtenerNombreComputadoraEnsamblada(int idComputadora) {
        String query = "SELECT tc.nombre AS nombre_tipo " +
                       "FROM ComputadorasEnsambladas ce " +
                       "JOIN TiposComputadoras tc ON ce.id_tipo_computadora = tc.id_tipo_computadora " +
                       "WHERE ce.id_computadora = ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idComputadora);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre_tipo"); // Devuelve el nombre del tipo de computadora
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Nombre no encontrado"; // Valor predeterminado si no se encuentra la computadora
    }
}

