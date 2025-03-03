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
    //metodo para registrar un apieza en el ensamblaje de una computadora
    public static boolean registrarEnsamblajePieza(EnsamblajePieza ensamblajePieza) throws SQLException {
        String sql = "INSERT INTO Ensamblaje_Piezas (id_computadora, id_componente, cantidad) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
                PreparedStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, ensamblajePieza.getIdComputadora());
            stmt.setInt(2, ensamblajePieza.getIdComponente());
            stmt.setInt(3, ensamblajePieza.getCantidad());
            int resultado = stmt.executeUpdate();
            return resultado > 0;
        }
    }
    
    //metodo para obtener todas las piezas de un ensamblaje de computadora
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
 }
