/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Administrador;

import Controllers.GeneradorCsv;
import Modelos.Cliente;
import Modelos.DetalleVenta;
import Modelos.Usuario;
import Modelos.Venta;
import backendDB.ModelosDB.ClienteDB;
import backendDB.ModelosDB.DetalleVentaDB;
import backendDB.ModelosDB.UsuarioDB;
import backendDB.ModelosDB.VentaDB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author herson
 */
@WebServlet(name = "ReporteVentasServlet", urlPatterns = {"/reporteVentas"})
public class ReporteVentasServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parámetros del formulario
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String export = request.getParameter("export"); // Parámetro para determinar si es exportación a CSV

        // Obtener el usuario activo de la sesión
        HttpSession session = request.getSession();
        String nombreUsuarioActivo = (String) session.getAttribute("nombreUsuarioActivo");

        try {
            // Obtener las ventas en el intervalo de tiempo
            List<Venta> ventas = VentaDB.obtenerVentas(fechaInicio, fechaFin);

            // Procesar cada venta para completar su información adicional
            for (Venta venta : ventas) {
                Cliente cliente = ClienteDB.obtenerCliente(venta.getIdCliente());
                Usuario usuarioVenta = UsuarioDB.obtenerUsuario(venta.getIdUsuario());
                List<DetalleVenta> detallesVenta = DetalleVentaDB.obtenerDetallesVentaReportes(venta.getIdVenta());

                // Asignar los detalles y nombres al modelo de Venta
                venta.setDetallesVenta(detallesVenta);
                venta.setNombreCliente(cliente != null ? cliente.getNombre() : "N/A");
                venta.setNombreVendedor(usuarioVenta != null ? usuarioVenta.getNombreUsuario() : "N/A");
            }

            // Revisar si se solicita la exportación a CSV
            if ("csv".equalsIgnoreCase(export)) {
                exportarCSV(response, ventas, fechaInicio, fechaFin, nombreUsuarioActivo);
                return; // Salir después de generar el archivo
            }

            // Pasar la lista de ventas a la vista para mostrarlas en la tabla
            request.setAttribute("ventas", ventas);
            request.getRequestDispatcher("Administrador/reporteVentas.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al obtener los datos del reporte de ventas", e);
        }
    }

    private void exportarCSV(HttpServletResponse response, List<Venta> ventas, String fechaInicio, String fechaFin, String nombreUsuarioActivo) throws IOException {
        // Configurar las cabeceras HTTP para el archivo CSV
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"reporteVentas.csv\"");

        // Crear una instancia 
        GeneradorCsv generadorCsv = new GeneradorCsv("Reporte de Ventas", nombreUsuarioActivo, fechaInicio, fechaFin);

        // generar el reporte
        try (PrintWriter writer = response.getWriter()) {
            generadorCsv.generarReporteVentas(writer, ventas);
        }
    }

}
