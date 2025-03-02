/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.Cliente;
import Modelos.DetalleDevolucion;
import Modelos.Devolucion;
import Modelos.Usuario;
import backendDB.ModelosDB.DetalleDevolucionDB;
import backendDB.ModelosDB.DevolucionDB;
import backendDB.ModelosDB.VentaDB;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "ReporteDevolucionesServlet", urlPatterns = {"/ReporteDevoluciones"})
public class ReporteDevolucionesServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parámetros del formulario
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");

        try {
            // Obtener las devoluciones en el intervalo de tiempo especificado
            List<Devolucion> devoluciones = DevolucionDB.obtenerDevoluciones(fechaInicio, fechaFin);

            // Recorrer las devoluciones para obtener los detalles adicionales
            for (Devolucion devolucion : devoluciones) {
                Cliente cliente = VentaDB.obtenerCliente(devolucion.getIdCliente());
                Usuario usuarioDevolucion = VentaDB.obtenerUsuario(devolucion.getIdUsuario());
                List<DetalleDevolucion> detallesDevolucion = DetalleDevolucionDB.obtenerDetallesDevolucion(devolucion.getIdDevolucion());

                // Agregar la información adicional a la solicitud
                request.setAttribute("cliente_" + devolucion.getIdDevolucion(), cliente);
                request.setAttribute("usuario_" + devolucion.getIdDevolucion(), usuarioDevolucion);
                request.setAttribute("detallesDevolucion_" + devolucion.getIdDevolucion(), detallesDevolucion);
            }

            // Pasar la lista de devoluciones a la vista (JSP)
            request.setAttribute("devoluciones", devoluciones);
            request.getRequestDispatcher("reporteDevoluciones.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error al obtener los datos del reporte de devoluciones", e);
        }
    }
}


