/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.ComputadoraEnsamblada;
import backendDB.ModelosDB.ComputadorasEnsambladasDB;
import java.io.IOException;
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
@WebServlet(name = "ConsultarComputadorasServlet", urlPatterns = {"/ConsultarComputadorasServlet"})
public class ConsultarComputadorasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Llamar al método para obtener detalles de computadoras ensambladas
            System.out.println("Ejecutando el método obtenerDetallesComputadorasEnsambladas...");
            List<ComputadoraEnsamblada> computadorasDisponibles = ComputadorasEnsambladasDB.obtenerDetallesComputadorasEnsambladas1();

            // Establecer la lista como atributo en la solicitud
            request.setAttribute("computadorasDisponibles", computadorasDisponibles);

            // Redirigir al JSP
            request.getRequestDispatcher("consultarComputadoras.jsp").forward(request, response);
        } catch (Exception e) {
            // Manejo de excepciones en caso de error de base de datos
            System.out.println("Error al ejecutar el método obtenerDetallesComputadorasEnsambladas: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al consultar las computadoras ensambladas.");
            request.setAttribute("computadorasDisponibles", null); // Asegurar que la lista esté vacía en caso de error
            request.getRequestDispatcher("consultarComputadoras.jsp").forward(request, response);
        }
    }
}
