/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Administrador;

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

        try {
            // Obtener las ventas en el intervalo de tiempo
            List<Venta> ventas = VentaDB.obtenerVentas(fechaInicio, fechaFin);

            // Recorrer las ventas para obtener los detalles adicionales
            for (Venta venta : ventas) {
                Cliente cliente = ClienteDB.obtenerCliente(venta.getIdCliente());
                Usuario usuarioVenta = UsuarioDB.obtenerUsuario(venta.getIdUsuario());
                List<DetalleVenta> detallesVenta = DetalleVentaDB.obtenerDetallesVentaReportes(venta.getIdVenta());

                // Agregar la información adicional a la solicitud
                request.setAttribute("cliente_" + venta.getIdVenta(), cliente);
                request.setAttribute("usuarioVenta_" + venta.getIdVenta(), usuarioVenta); 
                request.setAttribute("detallesVenta_" + venta.getIdVenta(), detallesVenta);
            }

            // Pasar la lista de ventas a la vista
            request.setAttribute("ventas", ventas);
            request.getRequestDispatcher("Administrador/reporteVentas.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al obtener los datos del reporte de ventas", e);
        }
    }
}




