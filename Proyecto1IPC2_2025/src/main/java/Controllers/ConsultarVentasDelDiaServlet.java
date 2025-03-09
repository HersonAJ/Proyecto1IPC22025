/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import backendDB.ModelosDB.VentaDB;
import java.io.IOException;
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
@WebServlet(name = "ConsultarVentasDelDiaServlet", urlPatterns = {"/ConsultarVentasDelDiaServlet"})
public class ConsultarVentasDelDiaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fechaVenta = request.getParameter("fechaVenta"); // Recibir la fecha ingresada por el usuario

        try {
            // Llamar al método en VentaDB para obtener las ventas
            List<Map<String, Object>> ventas = VentaDB.obtenerVentasPorFecha(fechaVenta);
            
            // Establecer las ventas como atributo en la solicitud
            request.setAttribute("ventas", ventas);
            
            // Redirigir al JSP para mostrar los resultados
            request.getRequestDispatcher("ventasDelDia.jsp").forward(request, response);
        } catch (SQLException e) {
            // Enviar un error HTTP 500 en caso de problemas
            System.out.println("Error al obtener las ventas: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocurrió un error al obtener las ventas.");
        }
    }
}

