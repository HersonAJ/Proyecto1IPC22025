/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.Cliente;
import Modelos.Computadora;
import Modelos.ComputadoraEnsamblada;
import Modelos.DetalleVenta;
import backendDB.ModelosDB.ClienteDB;
import backendDB.ModelosDB.ComputadoraDB;
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
        // Obtener la acción enviada desde el JSP
        String action = request.getParameter("action");
        System.out.println("la acción que se ejecutó fue: " + action);

        // Obtener la lista de detalles de la venta desde la sesión
        List<DetalleVenta> detalleVenta = (List<DetalleVenta>) request.getSession().getAttribute("detalleVenta");
        if (detalleVenta == null) {
            detalleVenta = new ArrayList<>();
        }

        System.out.println("lista detalleVenta al iniciar: " + detalleVenta.size());

        if ("cargarComputadoras".equals(action)) {

            try {
                System.out.println("Se está ejecutando la acción de cargar computadoras");

                // Acción para cargar la información inicial
                String nit = request.getParameter("nit");
                System.out.println("El NIT que se ingresó fue: " + nit);
                Cliente cliente = ClienteDB.obtenerClientePorNit(nit);
                request.getSession().setAttribute("cliente", cliente); // Guardar cliente en sesión
                request.setAttribute("cliente", cliente); // Pasar cliente al JSP

                // Cargar computadoras desde la base de datos
                List<ComputadoraEnsamblada> computadoras = null;
                try {
                    computadoras = VendedorComputadoraDB.obtenerComputadorasEnSalaDeVentas(); // Método actualizado
                } catch (Exception e) {
                    e.printStackTrace();
                }
                request.setAttribute("computadoras", computadoras);

                // Redirigir al JSP de selección
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
                    // Obtener los datos de la computadora desde la base de datos
                    ComputadoraEnsamblada computadora = VendedorComputadoraDB.obtenerComputadora(idComputadora); // Método actualizado
                    if (computadora != null) {
                        // Crear una nueva fila para cada computadora agregada
                        DetalleVenta nuevoDetalle = new DetalleVenta();
                        nuevoDetalle.setIdDetalleVenta(detalleVenta.size() + 1); // Asignar un ID único basado en el tamaño de la lista
                        nuevoDetalle.setIdComputadora(idComputadora);
                        nuevoDetalle.setCantidad(1); // Siempre inicia con 1
                        nuevoDetalle.setSubtotal(computadora.getTipoComputadora().getPrecioVenta()); // Precio desde TipoComputadora
                        detalleVenta.add(nuevoDetalle);
                        System.out.println("Detalle agregado con ID único: " + nuevoDetalle.getIdDetalleVenta());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Ocurrió un error al agregar la computadora.");
                }
            }

            // Continuar con la lógica para recargar datos
            List<ComputadoraEnsamblada> computadoras = null;
            try {
                computadoras = VendedorComputadoraDB.obtenerComputadorasEnSalaDeVentas(); // Método actualizado
            } catch (Exception e) {
                e.printStackTrace();
            }

            request.setAttribute("cliente", request.getSession().getAttribute("cliente")); // Recuperar cliente
            request.setAttribute("computadoras", computadoras); // Lista de computadoras
            request.setAttribute("detalleVenta", detalleVenta); // Lista de detalles actualizada
            request.getSession().setAttribute("detalleVenta", detalleVenta); // Actualizar en sesión
            request.getRequestDispatcher("seleccionarComputadora.jsp").forward(request, response);
        } else if ("confirmarVenta".equals(action)) {
            System.out.println("Se está ejecutando la acción de confirmarVenta");

            // Redirigir al RegistrarVentaServlet
            request.getRequestDispatcher("RegistrarVentaServlet").forward(request, response);
        }
    }
}
