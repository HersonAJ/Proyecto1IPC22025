/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Ensamblador;

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
@WebServlet("/ActualizarEstadoComputadoraServlet")
public class ActualizarEstadoComputadoraServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener todas las computadoras con estado "Ensamblada" y "En Sala de Venta"
            List<ComputadoraEnsamblada> computadorasEnsambladas = ComputadorasEnsambladasDB.obtenerComputadorasEnsambladas();
            request.setAttribute("computadorasEnsambladas", computadorasEnsambladas);

            // Redirigir al JSP para mostrar la lista
            request.getRequestDispatcher("Ensamblador/actualizarComputadoras.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al cargar las computadoras ensambladas.");
            request.setAttribute("computadorasEnsambladas", null); // Asegurar que la lista esté vacía en caso de error
            request.getRequestDispatcher("Ensamblador/actualizarComputadoras.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener el ID de la computadora seleccionada desde el formulario
            int idComputadora = Integer.parseInt(request.getParameter("idComputadora"));

            // Actualizar el estado de la computadora a "En Sala de Venta"
            boolean estadoActualizado = ComputadorasEnsambladasDB.actualizarEstadoComputadora(idComputadora);
            if (estadoActualizado) {
                request.setAttribute("mensaje", "La computadora con ID " + idComputadora + " se movió a 'En Sala de Venta' correctamente.");
            } else {
                request.setAttribute("error", "No se pudo actualizar el estado de la computadora con ID " + idComputadora + ".");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al procesar la actualización.");
        }

        // Volver a cargar la lista de computadoras ensambladas
        doGet(request, response);
    }
}


