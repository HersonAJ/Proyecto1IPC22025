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
    
    public static boolean registrarCliente (Cliente cliente) {
        String sql = "INSERT INTO Clientes (nit, nombre, direccion) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,  cliente.getNit());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getDireccion());
            int filasInsertadas = stmt.executeUpdate();
            return filasInsertadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static Cliente obtenerClientePorNit(String nit) {
        String sql = "SELECT * FROM Cliente WHERE nit = ?";
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nit);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_clente"));
                cliente.setNit(rs.getString("nit"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setDireccion(rs.getString("direccion"));
                return cliente;
            } else {
                return null; //si el cliente no existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
