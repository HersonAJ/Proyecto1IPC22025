/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.Cliente;
import Modelos.ComputadoraEnsamblada;
import Modelos.DetalleVenta;
import backendDB.ModelosDB.ClienteDB;
import backendDB.ModelosDB.Vendedor.VendedorComputadoraDB;
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
@WebServlet(name = "VentaServlet", urlPatterns = {"/VentaServlet"})
public class VentaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        System.out.println("la acción que se ejecutó fue: " + action);

        List<DetalleVenta> detalleVenta = (List<DetalleVenta>) request.getSession().getAttribute("detalleVenta");
        if (detalleVenta == null) {
            detalleVenta = new ArrayList<>();
        }

        System.out.println("lista detalleVenta al iniciar: " + detalleVenta.size());

        if ("cargarComputadoras".equals(action)) {
            try {
                System.out.println("Se está ejecutando la acción de cargar computadoras");
                String nit = request.getParameter("nit");
                System.out.println("El NIT que se ingresó fue: " + nit);

                Cliente cliente = ClienteDB.obtenerClientePorNit(nit);
                request.getSession().setAttribute("cliente", cliente);
                request.setAttribute("cliente", cliente);

                List<ComputadoraEnsamblada> computadoras = VendedorComputadoraDB.obtenerComputadorasEnSalaDeVentas();
                request.setAttribute("computadoras", computadoras);

                request.getRequestDispatcher("seleccionarComputadora.jsp").forward(request, response);

            } catch (SQLException ex) {
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            }

        } else if ("agregarComputadora".equals(action)) {
            System.out.println("Se está ejecutando la acción de agregarComputadora");
            String idComputadoraStr = request.getParameter("idComputadora");
            System.out.println("La computadora que se agregó tiene el siguiente ID: " + idComputadoraStr);

            if (idComputadoraStr == null || idComputadoraStr.isEmpty()) {
                request.setAttribute("error", "Debe seleccionar una computadora.");
            } else {
                int idComputadora = Integer.parseInt(idComputadoraStr);
                try {
                    ComputadoraEnsamblada computadora = VendedorComputadoraDB.obtenerComputadora(idComputadora);
                    if (computadora != null) {
                        DetalleVenta nuevoDetalle = new DetalleVenta();
                        nuevoDetalle.setIdDetalleVenta(detalleVenta.size() + 1);
                        nuevoDetalle.setIdComputadora(idComputadora);
                        nuevoDetalle.setCantidad(1);
                        nuevoDetalle.setSubtotal(computadora.getTipoComputadora().getPrecioVenta());
                        detalleVenta.add(nuevoDetalle);
                        System.out.println("Detalle agregado con ID único: " + nuevoDetalle.getIdDetalleVenta());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Ocurrió un error al agregar la computadora.");
                }
            }

            List<ComputadoraEnsamblada> computadoras = null;
            try {
                computadoras = VendedorComputadoraDB.obtenerComputadorasEnSalaDeVentas();
            } catch (Exception e) {
                e.printStackTrace();
            }

            request.setAttribute("cliente", request.getSession().getAttribute("cliente"));
            request.setAttribute("computadoras", computadoras);
            request.setAttribute("detalleVenta", detalleVenta);
            request.getSession().setAttribute("detalleVenta", detalleVenta);
            request.getRequestDispatcher("seleccionarComputadora.jsp").forward(request, response);
        } else if ("confirmarVenta".equals(action)) {
            System.out.println("Se está ejecutando la acción de confirmarVenta");

            // Pasar el ID de la primera computadora de la lista de detalles a la sesión para usarlo en el RegistrarVentaServlet
            if (!detalleVenta.isEmpty()) {
                int idComputadoraEnsamblada = detalleVenta.get(0).getIdComputadora();
                request.getSession().setAttribute("idComputadoraEnsamblada", idComputadoraEnsamblada);
                System.out.println("ID Computadora Ensamblada seleccionado: " + idComputadoraEnsamblada);
            }

            request.getRequestDispatcher("RegistrarVentaServlet").forward(request, response);
        }
    }
}

