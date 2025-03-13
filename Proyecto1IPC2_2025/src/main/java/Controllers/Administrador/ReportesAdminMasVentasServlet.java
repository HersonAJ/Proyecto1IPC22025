/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Administrador;

import Controllers.GeneradorCsv;
import Modelos.Usuario;
import backendDB.ModelosDB.ReportesAdminDB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
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
        String export = request.getParameter("export"); // Parámetro para exportar CSV

        // Validar que ambos parámetros estén presentes
        if (fechaInicio == null || fechaInicio.isEmpty() || fechaFin == null || fechaFin.isEmpty()) {
            request.setAttribute("error", "Por favor, selecciona un rango de fechas válido para generar el reporte.");
            request.getRequestDispatcher("Administrador/reportesAdminMasVentas.jsp").forward(request, response);
            return;
        }

        try {
            // Llamar al método del modelo para obtener los usuarios con más ventas
            List<Usuario> reporteUsuarios = ReportesAdminDB.obtenerUsuariosConVentas(fechaInicio, fechaFin);

            // Verificar si se solicita la exportación a CSV
            if ("csv".equalsIgnoreCase(export)) {
                exportarCSV(request, response, reporteUsuarios, fechaInicio, fechaFin);
                return; // Terminar después de generar el archivo
            }

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

    private void exportarCSV(HttpServletRequest request, HttpServletResponse response, List<Usuario> reporteUsuarios, String fechaInicio, String fechaFin) throws IOException {
        // Obtener el usuario activo de la sesión
        HttpSession session = request.getSession();
        String nombreUsuarioActivo = (String) session.getAttribute("nombreUsuarioActivo");

        // Configurar las cabeceras HTTP para el archivo CSV
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"reporteVentasPorUsuario.csv\"");

        // Crear una instancia
        GeneradorCsv generadorCsv = new GeneradorCsv("Reporte de Ventas por Usuario", nombreUsuarioActivo, fechaInicio, fechaFin);

        // generar el reporte
        try (PrintWriter writer = response.getWriter()) {
            generadorCsv.generarReporteVentasPorUsuario(writer, reporteUsuarios);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir al formulario del reporte para evitar acceso directo
        request.getRequestDispatcher("Administrador/reportesAdminMasVentas.jsp").forward(request, response);
    }
}


