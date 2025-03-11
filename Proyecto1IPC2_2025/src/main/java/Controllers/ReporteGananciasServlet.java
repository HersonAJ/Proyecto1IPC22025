/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

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

        try {
            // Inicializar variables
            double totalGanancia = 0.0;
            double totalPerdida = 0.0;
            double gananciaNeta = 0.0;

            // Obtener las ventas en el intervalo de tiempo especificado
            List<Venta> ventas = VentaDB.obtenerVentas(fechaInicio, fechaFin);

            // Calcular las ganancias de las ventas
            List<ComputadoraEnsamblada> computadorasVendidas = new ArrayList<>();
            for (Venta venta : ventas) {
                List<DetalleVenta> detallesVenta = DetalleVentaDB.obtenerDetallesVentaReportes(venta.getIdVenta());
                for (DetalleVenta detalle : detallesVenta) {
                    // Utilizar el método actualizado para obtener la computadora ensamblada
                    ComputadoraEnsamblada computadora = VendedorConsultaComprasCliente.obtenerComputadoraEnsamblada(detalle.getIdComputadora());
                    
                    // Calcular la ganancia utilizando precio de venta y costo total
                    double ganancia = (computadora.getTipoComputadora().getPrecioVenta() - computadora.getCostoTotal()) * detalle.getCantidad();
                    totalGanancia += ganancia;
                    
                    // Agregar la computadora ensamblada a la lista de computadoras vendidas
                    computadorasVendidas.add(computadora);
                }
            }

            // Obtener las devoluciones en el intervalo de tiempo especificado
            List<Devolucion> devoluciones = DevolucionDB.obtenerDevoluciones(fechaInicio, fechaFin);

            // Calcular las pérdidas por devoluciones
            for (Devolucion devolucion : devoluciones) {
                totalPerdida += devolucion.getMontoPerdida();
            }

            // Calcular la ganancia neta
            gananciaNeta = totalGanancia - totalPerdida;

            // Pasar la lista de computadoras vendidas y las ganancias a la vista (JSP)
            request.setAttribute("ventas", ventas);
            request.setAttribute("devoluciones", devoluciones);
            request.setAttribute("totalGanancia", totalGanancia);
            request.setAttribute("totalPerdida", totalPerdida);
            request.setAttribute("gananciaNeta", gananciaNeta);
            request.setAttribute("computadorasVendidas", computadorasVendidas);

            request.getRequestDispatcher("reporteGanancias.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error al obtener los datos del reporte de ganancias", e);
        }
    }
}



