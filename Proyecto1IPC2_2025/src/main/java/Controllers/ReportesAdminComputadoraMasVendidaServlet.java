/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import backendDB.ModelosDB.ReportesAdminDB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *
 * @author herson
 */
@WebServlet(name = "ReportesComputadoraMasVendidaServlet", urlPatterns = {"/ReportesComputadoraMasVendidaServlet"})
public class ReportesAdminComputadoraMasVendidaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener parámetros del rango de fechas
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");

        try {
            // Llamar al método para obtener la lista de computadoras vendidas
            List<Map<String, Object>> listaComputadoras = ReportesAdminDB.obtenerComputadorasVendidas(fechaInicio, fechaFin);

            // Enviar los resultados al JSP
            request.setAttribute("listaComputadoras", listaComputadoras);
            request.setAttribute("fechaInicio", fechaInicio);
            request.setAttribute("fechaFin", fechaFin);

            // Redirigir a la vista (JSP)
            request.getRequestDispatcher("reportesAdminComputadoraMasVendida.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al generar el reporte: " + e.getMessage());
            request.getRequestDispatcher("reportesAdminComputadoraMasVendida.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir al JSP inicial del formulario del reporte
        request.getRequestDispatcher("reportesAdminComputadoraMasVendida.jsp").forward(request, response);
    }
}



