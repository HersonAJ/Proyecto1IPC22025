/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Ensamblador;

import Modelos.Componente;
import backendDB.ModelosDB.ComponentesDB.ComponenteConsultaDB;
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
@WebServlet(name = "ConsultarComponentesServlet", urlPatterns = {"/ConsultarComponentesServlet"})
public class ConsultarComponentesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Obtener el parámetro de búsqueda ingresado por el usuario
            String criterioBusqueda = request.getParameter("busqueda");

            // Llamar al método del modelo para obtener los componentes según el criterio
            List<Componente> componentes;
            if (criterioBusqueda == null || criterioBusqueda.trim().isEmpty()) {
                componentes = ComponenteConsultaDB.obtenerTodosLosComponentes(); // Obtener todo
            } else {
                componentes = ComponenteConsultaDB.buscarComponentesPorNombre(criterioBusqueda.trim()); // Búsqueda parcial
            }

            // Pasar los componentes como atributo a la vista
            request.setAttribute("componentes", componentes);

            // Redirigir al JSP
            request.getRequestDispatcher("Ensamblador/consultarComponentes.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al consultar los componentes.");
            request.getRequestDispatcher("Ensamblador/consultarComponentes.jsp").forward(request, response);
        }
    }
}
