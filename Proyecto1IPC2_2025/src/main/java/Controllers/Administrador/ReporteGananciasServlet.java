/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Administrador;

import Controllers.GeneradorCsv;
import Modelos.ComputadoraEnsamblada;
import Modelos.DetalleVenta;
import Modelos.Devolucion;
import Modelos.Venta;
import backendDB.ModelosDB.DetalleVentaDB;
import backendDB.ModelosDB.DevolucionDB;
import backendDB.ModelosDB.Vendedor.VendedorConsultaComprasCliente;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author herson
 */
@WebServlet(name = "ReporteGananciasServlet", urlPatterns = {"/ReporteGananciasServlet"})
public class ReporteGananciasServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parámetros del formulario
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String export = request.getParameter("export"); // Parámetro para exportar CSV

        // Obtener el usuario activo de la sesión
        HttpSession session = request.getSession();
        String nombreUsuarioActivo = (String) session.getAttribute("nombreUsuarioActivo");

        try {
            // Inicializar variables
            double sumaTotalVentas = 0.0;
            double sumaTotalGastos = 0.0;
            double totalGanancia = 0.0;
            double totalPerdida = 0.0;
            double gananciaNeta = 0.0;

            // Obtener las ventas en el intervalo de tiempo especificado
            List<Venta> ventas = VentaDB.obtenerVentas(fechaInicio, fechaFin);

            // Calcular las ganancias de las ventas
            List<ComputadoraEnsamblada> computadorasVendidas = new ArrayList<>();
            for (Venta venta : ventas) {
                sumaTotalVentas += venta.getTotalVenta();

                List<DetalleVenta> detallesVenta = DetalleVentaDB.obtenerDetallesVentaReportes(venta.getIdVenta());
                for (DetalleVenta detalle : detallesVenta) {
                    // Obtener la computadora ensamblada y calcular los gastos
                    ComputadoraEnsamblada computadora = VendedorConsultaComprasCliente.obtenerComputadoraEnsamblada(detalle.getIdComputadora());
                    sumaTotalGastos += computadora.getCostoTotal();
                    computadorasVendidas.add(computadora);
                }
            }

            // Calcular los totales financieros
            totalGanancia = sumaTotalVentas - sumaTotalGastos;
            gananciaNeta = totalGanancia - totalPerdida; // Perdida actualmente = 0 porque no logre manejar las devoluciones

            // Verificar si se solicita la exportación a CSV
            if ("csv".equalsIgnoreCase(export)) {
                exportarCSV(response, ventas, sumaTotalVentas, sumaTotalGastos, totalGanancia, totalPerdida, gananciaNeta, fechaInicio, fechaFin, nombreUsuarioActivo, computadorasVendidas);
                return; // Terminar después de generar el archivo
            }

            // Pasar los datos a la vista para mostrarlos
            request.setAttribute("ventas", ventas);
            request.setAttribute("totalGanancia", totalGanancia);
            request.setAttribute("totalPerdida", totalPerdida);
            request.setAttribute("gananciaNeta", gananciaNeta);
            request.setAttribute("computadorasVendidas", computadorasVendidas);
            request.setAttribute("sumaTotalVentas", sumaTotalVentas);
            request.setAttribute("sumaTotalGastos", sumaTotalGastos);
            request.getRequestDispatcher("Administrador/reporteGanancias.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error al obtener los datos del reporte de ganancias", e);
        }
    }

    private void exportarCSV(HttpServletResponse response, List<Venta> ventas, double sumaTotalVentas, double sumaTotalGastos,
            double totalGanancia, double totalPerdida, double gananciaNeta, String fechaInicio, String fechaFin,
            String nombreUsuarioActivo, List<ComputadoraEnsamblada> computadorasVendidas) throws IOException {
        // Configurar las cabeceras HTTP para el archivo CSV
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"reporteGanancias.csv\"");

        // Crear una instancia
        GeneradorCsv generadorCsv = new GeneradorCsv("Reporte de Ganancias", nombreUsuarioActivo, fechaInicio, fechaFin);

        // generar el reporte
        try (PrintWriter writer = response.getWriter()) {
            generadorCsv.generarReporteGanancias(writer, ventas, sumaTotalVentas, sumaTotalGastos, totalGanancia, totalPerdida, gananciaNeta);
        }
    }
}
