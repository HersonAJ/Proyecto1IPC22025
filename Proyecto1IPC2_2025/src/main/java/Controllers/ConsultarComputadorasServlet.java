/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import backendDB.ModelosDB.ComputadoraDB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author herson
 */
@WebServlet(name = "ConsultarComputadorasServlet", urlPatterns = {"/ConsultarComputadorasServlet"})
public class ConsultarComputadorasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Servlet ConsultarComputadorasServlet llamado."); // Indicador inicial de llamada al servlet
        
        try {
            // Llamar al método para obtener computadoras disponibles con detalles
            System.out.println("Ejecutando el método obtenerComputadorasConDetalles...");
            List<Map<String, Object>> computadorasDisponibles = ComputadoraDB.obtenerComputadorasConDetalles();

            // Establecer la lista como atributo en la solicitud
            request.setAttribute("computadorasDisponibles", computadorasDisponibles);

            // Redirigir al JSP
            request.getRequestDispatcher("consultarComputadoras.jsp").forward(request, response);
        } catch (SQLException e) {
            // Manejo de excepciones en caso de error de base de datos
            System.out.println("Error al ejecutar el método obtenerComputadorasConDetalles: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocurrió un error al consultar las computadoras disponibles.");
        }
    }
}

