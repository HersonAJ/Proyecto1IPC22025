/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Administrador;

import Controllers.GeneradorCsv;
import backendDB.ModelosDB.ReportesAdminDB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 *
 * @author herson
 */
@WebServlet(name = "ReportesAdminComputadoraMenosVendidaServlet", urlPatterns = {"/ReportesAdminComputadoraMenosVendidaServlet"})
public class ReportesAdminComputadoraMenosVendidaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener parámetros del rango de fechas
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String export = request.getParameter("export"); // Parámetro para exportar CSV

        try {
            // Si las fechas no están presentes, tratarlas como nulas
            fechaInicio = (fechaInicio == null || fechaInicio.isEmpty()) ? null : fechaInicio;
            fechaFin = (fechaFin == null || fechaFin.isEmpty()) ? null : fechaFin;

            // Llamar al método para obtener la lista de computadoras menos vendidas
            List<Map<String, Object>> listaComputadoras = ReportesAdminDB.obtenerComputadorasMenosVendidas(fechaInicio, fechaFin);

            // Verificar si se solicita exportación a CSV
            if ("csv".equalsIgnoreCase(export)) {
                exportarCSV(request, response, listaComputadoras, fechaInicio, fechaFin);
                return; // Finalizar después de la exportación
            }

            // Enviar los resultados al JSP
            request.setAttribute("listaComputadoras", listaComputadoras);
            request.setAttribute("fechaInicio", fechaInicio);
            request.setAttribute("fechaFin", fechaFin);

            // Redirigir a la vista (JSP)
            request.getRequestDispatcher("Administrador/reporteAdminComputadoraMenosVendida.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al generar el reporte: " + e.getMessage());
            request.getRequestDispatcher("Administrador/reporteAdminComputadoraMenosVendida.jsp").forward(request, response);
        }
    }

    private void exportarCSV(HttpServletRequest request, HttpServletResponse response, List<Map<String, Object>> listaComputadoras, String fechaInicio, String fechaFin) throws IOException {
        // Obtener el usuario activo de la sesión
        HttpSession session = request.getSession();
        String nombreUsuarioActivo = (String) session.getAttribute("nombreUsuarioActivo");

        // Configurar las cabeceras HTTP para el archivo CSV
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"reporteComputadorasMenosVendidas.csv\"");

        // Crear una instancia
        GeneradorCsv generadorCsv = new GeneradorCsv("Reporte - Computadoras Menos Vendidas", nombreUsuarioActivo, fechaInicio, fechaFin);

        // generar el reporte
        try (PrintWriter writer = response.getWriter()) {
            generadorCsv.generarReporteComputadorasMenosVendidas(writer, listaComputadoras);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir al JSP inicial del formulario del reporte
        request.getRequestDispatcher("Administrador/reporteAdminComputadoraMenosVendida.jsp").forward(request, response);
    }
}
