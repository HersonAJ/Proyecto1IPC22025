/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Vendedor;

import Modelos.ComputadoraEnsamblada;
import backendDB.ModelosDB.ComputadorasEnsambladasDB;
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
@WebServlet(name = "ConsultarComputadorasEnSalaDeVentaServlet", urlPatterns = {"/ConsultarComputadorasEnSalaDeVentaServlet"})
public class ConsultarComputadorasEnSalaDeVentaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Llamar al método para obtener detalles de computadoras ensambladas
            System.out.println("Ejecutando el método obtenerDetallesComputadorasEnSalaDeVentas...");
            List<ComputadoraEnsamblada> computadorasDisponibles = ComputadorasEnsambladasDB.obtenerDetallesComputadorasEnSalaDeVenta();

            // Establecer la lista como atributo en la solicitud
            request.setAttribute("computadorasDisponibles", computadorasDisponibles);

            // Redirigir al JSP
            request.getRequestDispatcher("Vendedor/consultarComputadoraSalaDeVenta.jsp").forward(request, response);
        } catch (Exception e) {
            // Manejo de excepciones en caso de error de base de datos
            System.out.println("Error al ejecutar el método obtenerDetallesComputadorasEnSalaDeVentas: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al consultar las computadoras en sala de ventas.");
            request.setAttribute("computadorasDisponibles", null); // Asegurar que la lista esté vacía en caso de error
            request.getRequestDispatcher("Vendedor/consultarComputadoraSalaDeVenta.jsp").forward(request, response);
        }
    }
}
