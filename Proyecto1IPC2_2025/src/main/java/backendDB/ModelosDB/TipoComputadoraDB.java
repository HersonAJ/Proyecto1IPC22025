/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backendDB.ModelosDB;

import Modelos.TipoComputadora;
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
public class TipoComputadoraDB {

    // Método para registrar un nuevo tipo de computadora
    public static boolean registrarTipoComputadora(TipoComputadora tipoComputadora) {
        String verificarNombreSQL = "SELECT COUNT(*) AS contador FROM TiposComputadoras WHERE nombre = ?";
        String insertarSQL = "INSERT INTO TiposComputadoras (nombre, precio_venta) VALUES (?, ?)";

        try (Connection conn = ConexionDB.getConnection()) {
            // Verificar si ya existe un tipo de computadora con el mismo nombre
            try (PreparedStatement verificarStmt = conn.prepareStatement(verificarNombreSQL)) {
                verificarStmt.setString(1, tipoComputadora.getNombre());
                try (ResultSet rs = verificarStmt.executeQuery()) {
                    if (rs.next() && rs.getInt("contador") > 0) {
                        System.out.println("Error: El nombre del tipo de computadora ya existe: " + tipoComputadora.getNombre());
                        return false; // Si el nombre ya existe, no se permite registrar
                    }
                }
            }

            // Insertar el nuevo tipo de computadora
            try (PreparedStatement insertarStmt = conn.prepareStatement(insertarSQL)) {
                insertarStmt.setString(1, tipoComputadora.getNombre());
                insertarStmt.setDouble(2, tipoComputadora.getPrecioVenta());

                int filasInsertadas = insertarStmt.executeUpdate();
                if (filasInsertadas > 0) {
                    System.out.println("Tipo de computadora registrado correctamente: " + tipoComputadora.getNombre());
                    return true;
                } else {
                    System.out.println("Error: No se pudo registrar el tipo de computadora.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos al registrar el tipo de computadora: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //metodo para obtener todos los tipos de computadoras existentes
    public static List<TipoComputadora> obtenerTiposComputadoras() {
        String consultaSQL = "SELECT id_tipo_computadora, nombre, precio_venta FROM TiposComputadoras";
        List<TipoComputadora> listaTipos = new ArrayList<>();

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(consultaSQL); ResultSet rs = stmt.executeQuery()) {

            // Recorrer los resultados
            while (rs.next()) {
                TipoComputadora tipo = new TipoComputadora();
                tipo.setIdTipoComputadora(rs.getInt("id_tipo_computadora"));
                tipo.setNombre(rs.getString("nombre"));
                tipo.setPrecioVenta(rs.getDouble("precio_venta"));
                listaTipos.add(tipo);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener los tipos de computadoras: " + e.getMessage());
            e.printStackTrace();
        }

        return listaTipos;
    }

    //metodo que se utilizada para la instruccion de ensamblar computadora con el archivo
    public static boolean existeTipoComputadora(String nombre) {
        String consultaSQL = "SELECT COUNT(*) AS contador FROM TiposComputadoras WHERE nombre = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(consultaSQL)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt("contador") > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar el tipo de computadora: " + e.getMessage());
        }
        return false;
    }

}
