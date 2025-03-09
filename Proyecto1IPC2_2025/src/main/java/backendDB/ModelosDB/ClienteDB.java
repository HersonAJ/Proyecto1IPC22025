/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.Cliente;
import backendDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author herson
 */
public class ClienteDB {
    
    public static boolean registrarCliente(Cliente cliente) {
        String sql = "INSERT INTO Clientes (nit, nombre, direccion) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNit());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getDireccion());
            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    /*metodo utilizado por buscarCliente.jsp
                            ComprasClienteDB
                            ComprasClienteServlet
*/
    public static Cliente obtenerClientePorNit(String nit) throws SQLException {
        Cliente cliente = null;
        String sql = "SELECT * FROM Clientes WHERE nit = ?";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nit);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    cliente.setNit(rs.getString("nit"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setDireccion(rs.getString("direccion"));
                }
            }
        }
        return cliente;
    }

        // Obtener información del cliente por su id
    //se usa en VentaServlet cuando se hace una compra
    public static Cliente obtenerCliente(int idCliente) throws SQLException {
        Cliente cliente = null;
        String query = "SELECT * FROM Clientes WHERE id_cliente = ?";

        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    cliente.setNit(rs.getString("nit"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setDireccion(rs.getString("direccion"));
                }
            }
        }
        return cliente;
    }
}
