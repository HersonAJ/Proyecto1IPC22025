/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.Cliente;
import Modelos.Computadora;
import Modelos.DetalleVenta;
import backendDB.ModelosDB.ClienteDB;
import backendDB.ModelosDB.ComputadoraDB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author herson
 */
@WebServlet(name = "RegistrarVentaServlet", urlPatterns = {"/RegistrarVentaServlet"})
public class RegistrarVentaServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nit = request.getParameter("nitCliente");
        String nombreCliente = request.getParameter("nombreCliente");
        String direccionCliente = request.getParameter("direccionCliente");
        String idComputadoraStr = request.getParameter("idComputadora");
        String action = request.getParameter("action");

        if (idComputadoraStr == null || idComputadoraStr.isEmpty()) {
            response.sendRedirect("error.jsp");
            return;
        }

        int idComputadora = Integer.parseInt(idComputadoraStr);
        
        HttpSession session = request.getSession();
        List<DetalleVenta> detallesVenta = (List<DetalleVenta>) session.getAttribute("detallesVenta");
        if (detallesVenta == null) {
            detallesVenta = new ArrayList<>();
        }
        
        try {
            Cliente cliente = ClienteDB.obtenerClientePorNit(nit);
            if (cliente == null) {
                cliente = new Cliente();
                cliente.setNit(nit);
                cliente.setNombre(nombreCliente);
                cliente.setDireccion(direccionCliente);
                ClienteDB.registrarCliente(cliente);
                cliente = ClienteDB.obtenerClientePorNit(nit); // Obtener el ID del cliente registrado
            }
            
            Computadora computadora = ComputadoraDB.obtenerComputadora(idComputadora);
            if (computadora != null) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setIdVenta(0); // Este valor se actualizará cuando se confirme la venta
                detalle.setIdComputadora(computadora.getIdComputadora());
                detalle.setCantidad(1);
                detalle.setSubtotal(computadora.getPrecioVenta());
                detallesVenta.add(detalle);
            }
            
            session.setAttribute("cliente", cliente);
            session.setAttribute("detallesVenta", detallesVenta);
            
            if ("add".equals(action)) {
                response.sendRedirect("registrarVenta.jsp?clienteExiste=true");
            } else if ("checkout".equals(action)) {
                response.sendRedirect("resumenVenta.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
