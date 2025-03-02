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

        String action = request.getParameter("action");
        if (action != null && action.equals("eliminar")) {
            int idComponente = Integer.parseInt(request.getParameter("id"));
            try {
                eliminarComponente(request, response, idComponente);
            } catch (SQLException ex) {
                throw new ServletException(ex);
            }
        } else {
            try {
                listarComponentes(request, response);
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());
                throw new ServletException(ex);
            }
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
                    System.out.println("Componente agregado con exito");
                } else {
                    System.out.println("Fallo agregar nuevo componente");
                }
            } else {
                // Editar componente existente
                int idComponente = Integer.parseInt(idComponenteStr);
                componente.setIdComponente(idComponente);
                boolean resultado = ComponenteDB.actualizarComponente(componente);
                if (resultado) {
                    System.out.println("Componente actualizado");
                } else {
                    System.out.println("Fallo la actualizacion del componente");
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

    private void eliminarComponente(HttpServletRequest request, HttpServletResponse response, int idComponente) throws SQLException, ServletException, IOException {
  
        boolean resultado = ComponenteDB.eliminarComponente(idComponente);
        if (resultado) {
            System.out.println("Componente eliminado correctamente con soft delete");
        } else {
            System.out.println("Fallo la eliminacion ");
        }
        listarComponentes(request, response);
    }
}
