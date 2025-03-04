/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.Cliente;
import Modelos.DetalleVenta;
import Modelos.Venta;
import backendDB.ModelosDB.DetalleVentaDB;
import backendDB.ModelosDB.VentaDB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 *
 * @author herson
 */
@WebServlet(name = "ConfirmarVentaServlet", urlPatterns = {"/ConfirmarVentaServlet"})
public class ConfirmarVentaServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        List<DetalleVenta> detallesVenta = (List<DetalleVenta>) session.getAttribute("detallesVenta");
        
        if (cliente == null || detallesVenta == null) {
            response.sendRedirect("error.jsp");
            return;
        }

        try {
            // Crear la venta
            Venta venta = new Venta();
            venta.setIdCliente(cliente.getIdCliente());
            venta.setIdUsuario((Integer) session.getAttribute("idUsuario")); // ID del vendedor
            venta.setFechaVenta(new java.sql.Date(new Date().getTime()));
            double totalVenta = detallesVenta.stream().mapToDouble(DetalleVenta::getSubtotal).sum();
            venta.setTotalVenta(totalVenta);
            
            // Registrar la venta
            if (VentaDB.registrarVenta(venta)) {
                for (DetalleVenta detalle : detallesVenta) {
                    detalle.setIdVenta(venta.getIdVenta());
                    DetalleVentaDB.registrarDetalleVenta(detalle);
                }
                
                // Limpiar sesión
                session.removeAttribute("cliente");
                session.removeAttribute("detallesVenta");
                
                //pendiente
                // Generar factura en PDF 
                // Código para generar factura

                response.sendRedirect("success.jsp"); // Redirigir a una página de éxito
            } else {
                response.sendRedirect("error.jsp");
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
