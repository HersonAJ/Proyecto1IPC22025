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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author herson
 */
@WebServlet(name = "RegistrarVentaServlet", urlPatterns = {"/RegistrarVentaServlet"})
public class RegistrarVentaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Se ejecutó el RegistrarVentaServlet");

        // Obtener la lista de detalles de la venta desde la sesión
        List<DetalleVenta> detalleVenta = (List<DetalleVenta>) request.getSession().getAttribute("detalleVenta");
        if (detalleVenta == null || detalleVenta.isEmpty()) {
            request.setAttribute("error", "No hay artículos en el carrito para procesar la venta.");
            request.getRequestDispatcher("seleccionarComputadora.jsp").forward(request, response);
            return;
        }

        // Obtener el cliente y el usuario desde la sesión
        Cliente cliente = (Cliente) request.getSession().getAttribute("cliente");
        int idUsuario = (int) request.getSession().getAttribute("idUsuario");

        // Validar que el cliente esté seleccionado
        if (cliente == null) {
            request.setAttribute("error", "Debe seleccionar un cliente antes de confirmar la venta.");
            request.getRequestDispatcher("seleccionarComputadora.jsp").forward(request, response);
            return;
        }

        // Obtener la fecha ingresada por el usuario
        String fechaVentaStr = request.getParameter("fechaVenta");
        Date fechaVenta;
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            fechaVenta = formatoFecha.parse(fechaVentaStr);
        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("error", "Formato de fecha inválido.");
            request.getRequestDispatcher("seleccionarComputadora.jsp").forward(request, response);
            return;
        }

        // Calcular el total de la venta
        double totalVenta = detalleVenta.stream()
                .mapToDouble(DetalleVenta::getSubtotal)
                .sum();
        System.out.println("Total de la venta: " + totalVenta);

        // Crear el objeto venta
        Venta venta = new Venta();
        venta.setIdCliente(cliente.getIdCliente());
        venta.setIdUsuario(idUsuario);
        venta.setFechaVenta(fechaVenta); // Usar la fecha ingresada
        venta.setTotalVenta(totalVenta);

        // Registrar la venta en la base de datos
        boolean ventaRegistrada = VentaDB.registrarVenta(venta);
        if (ventaRegistrada) {
            System.out.println("Venta registrada con ID: " + venta.getIdVenta() + " y número de factura: " + venta.getNumeroFactura());

            // Registrar los detalles de la venta
            for (DetalleVenta detalle : detalleVenta) {
                detalle.setIdVenta(venta.getIdVenta());
                DetalleVentaDB.registrarDetalleVenta(detalle);
            }

            // Redirigir al JSP para mostrar la factura
            request.setAttribute("venta", venta);
            request.setAttribute("cliente", cliente);
            request.setAttribute("detalleVenta", detalleVenta);
            request.getRequestDispatcher("mostrarFactura.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "No se pudo registrar la venta.");
            request.getRequestDispatcher("seleccionarComputadora.jsp").forward(request, response);
        }
    }
}
