/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.Componente;
import backendDB.ModelosDB.ComponenteDB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author herson
 */
@WebServlet(name = "GestionComponentesServlet", urlPatterns = {"/GestionComponentesServlet"})
public class GestionComponentesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            listarComponentes(request, response);
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idComponenteStr = request.getParameter("idComponente");
        String nombre = request.getParameter("nombre");
        double costo = Double.parseDouble(request.getParameter("costo"));
        int cantidadDisponible = Integer.parseInt(request.getParameter("cantidadDisponible"));

        Componente componente = new Componente();
        componente.setNombre(nombre);
        componente.setCosto(costo);
        componente.setCantidadDisponible(cantidadDisponible);

        try {
            if (idComponenteStr == null || idComponenteStr.isEmpty()) {
                // Agregar nuevo componente
                boolean resultado = ComponenteDB.registrarComponente(componente);
                if (resultado) {
                    System.out.println("Component successfully added");
                } else {
                    System.out.println("Failed to add component");
                }
            } else {
                // Editar componente existente
                int idComponente = Integer.parseInt(idComponenteStr);
                componente.setIdComponente(idComponente);
                System.out.println("Editando componente: " + idComponente);
                boolean resultado = ComponenteDB.actualizarComponente(componente);
                if (resultado) {
                    System.out.println("actualizacion completada");
                } else {
                    System.out.println("actualizacion fallida");
                }
            }

            listarComponentes(request, response);
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.getMessage());
            throw new ServletException(ex);
        }
    }

    private void listarComponentes(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Componente> componentes = ComponenteDB.obtenerComponentes();
        request.setAttribute("componentes", componentes);
        request.getRequestDispatcher("gestionarComponentes.jsp").forward(request, response);
    }
}
