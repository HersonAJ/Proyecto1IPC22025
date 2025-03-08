/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.DetalleVenta;
import backendDB.ModelosDB.DetalleVentaDB;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "DetallesComprasClienteServlet", urlPatterns = {"/DetallesComprasClienteServlet"})
public class DetallesComprasClienteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idVentaStr = request.getParameter("idVenta");

        if (idVentaStr == null || idVentaStr.isEmpty()) {
            request.setAttribute("error", "No se proporcionó un ID de venta válido.");
            request.getRequestDispatcher("consultarComprasCliente.jsp").forward(request, response);
            return;
        }

        try {
            int idVenta = Integer.parseInt(idVentaStr);

            // Obtener los detalles de la venta
            List<DetalleVenta> detalles = DetalleVentaDB.obtenerDetallesVenta(idVenta);

            // Pasar los detalles como atributo
            request.setAttribute("detallesVenta", detalles);
            request.getRequestDispatcher("consultarComprasCliente.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al obtener los detalles de la venta.");
            request.getRequestDispatcher("consultarComprasCliente.jsp").forward(request, response);
        }
    }
}

