/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Vendedor;

import Modelos.Cliente;
import Modelos.ComputadoraEnsamblada;
import Modelos.DetalleVenta;
import backendDB.ModelosDB.Vendedor.VendedorComputadoraDB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author herson
 */
@WebServlet(name = "EliminarDetalleServlet", urlPatterns = {"/EliminarDetalleServlet"})
public class EliminarDetalleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Se ejecutó el EliminarDetalleServlet");

        // Recuperar la lista de detalles de la sesión
        List<DetalleVenta> detalleVenta = (List<DetalleVenta>) request.getSession().getAttribute("detalleVenta");
        if (detalleVenta == null) {
            detalleVenta = new ArrayList<>();
        }

        // Obtener el ID del detalle a eliminar
        String idDetalleVentaStr = request.getParameter("idDetalleVenta");
        System.out.println("ID Detalle Venta recibido para eliminar: " + idDetalleVentaStr);

        if (idDetalleVentaStr != null && !idDetalleVentaStr.isEmpty()) {
            try {
                int idDetalleVenta = Integer.parseInt(idDetalleVentaStr);

                // Imprimir la lista antes de eliminar
                System.out.println("Lista detalleVenta antes de eliminar:");
                for (DetalleVenta detalle : detalleVenta) {
                    System.out.println("ID Detalle: " + detalle.getIdDetalleVenta() + ", ID Computadora: " + detalle.getIdComputadora());
                }

                // Eliminar el detalle especificado
                boolean eliminado = detalleVenta.removeIf(detalle -> detalle.getIdDetalleVenta() == idDetalleVenta);
                System.out.println("¿Se eliminó el detalle? " + eliminado);

                // Imprimir la lista después de eliminar
                System.out.println("Lista detalleVenta después de eliminar:");
                for (DetalleVenta detalle : detalleVenta) {
                    System.out.println("ID Detalle: " + detalle.getIdDetalleVenta() + ", ID Computadora: " + detalle.getIdComputadora());
                }
            } catch (NumberFormatException e) {
                System.out.println("Error al convertir ID Detalle Venta: " + idDetalleVentaStr);
                e.printStackTrace();
                request.setAttribute("error", "ID no válido.");
            }
        } else {
            System.out.println("ID Detalle Venta es nulo o vacío.");
            request.setAttribute("error", "ID inválido para eliminación.");
        }

        // Volver a cargar la lista de computadoras para el selector
        List<ComputadoraEnsamblada> computadoras = null;
        try {
            computadoras = VendedorComputadoraDB.obtenerComputadorasEnSalaDeVentas(); // Método actualizado
            System.out.println("Computadoras cargadas para el selector:");
            for (ComputadoraEnsamblada computadora : computadoras) {
                System.out.println("ID: " + computadora.getIdComputadora() + ", Tipo: " + computadora.getTipoComputadora().getNombre());
            }
        } catch (Exception e) {
            System.out.println("Error al cargar computadoras para el selector.");
            e.printStackTrace();
        }

        // Recuperar los datos del cliente de la sesión
        Cliente cliente = (Cliente) request.getSession().getAttribute("cliente");
        if (cliente != null) {
            System.out.println("Datos del cliente cargados: NIT = " + cliente.getNit() + ", Nombre = " + cliente.getNombre());
        } else {
            System.out.println("No se encontraron datos del cliente en la sesión.");
        }

        // Actualizar datos en sesión y reenviar al JSP
        request.getSession().setAttribute("detalleVenta", detalleVenta); // Actualizar lista en la sesión
        request.setAttribute("cliente", cliente); // Pasar cliente al JSP
        request.setAttribute("computadoras", computadoras); // Pasar computadoras al JSP
        request.setAttribute("detalleVenta", detalleVenta); // Pasar detalles al JSP

        // Reenviar al JSP
        request.getRequestDispatcher("Vendedor/seleccionarComputadora.jsp").forward(request, response);
    }
}
