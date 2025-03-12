/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Administrador;

import Modelos.Usuario;
import backendDB.ModelosDB.ReportesAdminDB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author herson
 */
@WebServlet(name = "ReportesAdminMasVentasServlet", urlPatterns = {"/ReportesAdminMasVentasServlet"})
public class ReportesAdminMasVentasServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener los parámetros del rango de fechas enviados desde el formulario JSP
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");

        // Validar que ambos parámetros estén presentes
        if (fechaInicio == null || fechaInicio.isEmpty() || fechaFin == null || fechaFin.isEmpty()) {
            request.setAttribute("error", "Por favor, selecciona un rango de fechas válido para generar el reporte.");
            request.getRequestDispatcher("Administrador/reportesAdminMasVentas.jsp").forward(request, response);
            return;
        }

        try {
            // Llamar al método del modelo para obtener los usuarios con más ventas
            List<Usuario> reporteUsuarios = ReportesAdminDB.obtenerUsuariosConVentas(fechaInicio, fechaFin);

            // Verificar si el reporte tiene resultados
            if (reporteUsuarios.isEmpty()) {
                request.setAttribute("mensaje", "No se encontraron ventas en el rango de fechas seleccionado.");
            } else {
                request.setAttribute("reporteUsuarios", reporteUsuarios);
            }

            // Enviar el rango de fechas y el reporte a la vista
            request.setAttribute("fechaInicio", fechaInicio);
            request.setAttribute("fechaFin", fechaFin);

            // Redirigir a la vista JSP del reporte
            request.getRequestDispatcher("Administrador/reportesAdminMasVentas.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al generar el reporte: " + e.getMessage());
            request.getRequestDispatcher("Administrador/reportesAdminMasVentas.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir al formulario del reporte para evitar acceso directo
        request.getRequestDispatcher("Administrador/reportesAdminMasVentas.jsp").forward(request, response);
    }
}

