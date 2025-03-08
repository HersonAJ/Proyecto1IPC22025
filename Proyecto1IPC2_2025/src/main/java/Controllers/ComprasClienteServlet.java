/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.Cliente;
import Modelos.Venta;
import backendDB.ModelosDB.ComprasClienteDB;
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
@WebServlet(name = "ComprasClientServlet", urlPatterns = {"/ComprasClientServlet"})
public class ComprasClienteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Parámetros recibidos del JSP
        String nit = request.getParameter("nit");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");

        try {
            // Validar que se haya ingresado un NIT
            if (nit == null || nit.trim().isEmpty()) {
                request.setAttribute("error", "Debe ingresar un NIT para buscar las compras.");
                request.getRequestDispatcher("buscarCliente.jsp").forward(request, response);
                return;
            }

            // Obtener el cliente por NIT
            Cliente cliente = ComprasClienteDB.obtenerClientePorNit(nit);
            if (cliente == null) {
                request.setAttribute("error", "No se encontró un cliente con el NIT proporcionado.");
                request.getRequestDispatcher("buscarCliente.jsp").forward(request, response);
                return;
            }

            // Obtener las compras del cliente
            List<Venta> compras = ComprasClienteDB.obtenerComprasCliente(nit, fechaInicio, fechaFin);

            // Verificar si se encontraron compras
            if (compras.isEmpty()) {
                request.setAttribute("mensaje", "No se encontraron compras para el cliente en el intervalo de tiempo indicado.");
            } else {
                request.setAttribute("compras", compras);
            }

            // Pasar datos al JSP
            request.setAttribute("cliente", cliente);
            request.getRequestDispatcher("consultarComprasCliente.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al procesar la solicitud. Inténtelo más tarde.");
            request.getRequestDispatcher("buscarCliente.jsp").forward(request, response);
        }
    }
}

