/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Ensamblador;

import Modelos.Componente;
import backendDB.ModelosDB.ComponentesDB.ComponenteConsultaDB;
import backendDB.ModelosDB.ComponentesDB.ComponenteRegistroDB;
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
            try {
                int idComponente = Integer.parseInt(request.getParameter("id"));
                // Eliminar componente por ID
                eliminarComponente(request, response, idComponente);
            } catch (NumberFormatException ex) {
                System.out.println("Error al procesar la eliminación: " + ex.getMessage());
                request.setAttribute("error", "Error al eliminar el componente.");
                listarComponentes(request, response);
            }
        } else {
            // Listar todos los componentes
            listarComponentes(request, response);
        }
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");

    if (action != null && action.equals("eliminar")) {
        try {
            int idComponente = Integer.parseInt(request.getParameter("id"));
            eliminarComponente(request, response, idComponente);
        } catch (NumberFormatException ex) {
            System.out.println("Error al procesar la eliminación: " + ex.getMessage());
            request.setAttribute("error", "Error al eliminar el componente.");
            listarComponentes(request, response);
        }
        return; // Termina aquí, ya no ejecuta más validaciones
    }

    // Código actual para agregar/editar un componente
    String idComponenteStr = request.getParameter("idComponente");
    String nombre = request.getParameter("nombre");
    String costoStr = request.getParameter("costo");
    String cantidadDisponibleStr = request.getParameter("cantidadDisponible");

    try {
        // Validar campos enviados desde el formulario
        if (nombre == null || nombre.trim().isEmpty() || costoStr == null || cantidadDisponibleStr == null) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            listarComponentes(request, response);
            return;
        }

        double costo = Double.parseDouble(costoStr);
        int cantidadDisponible = Integer.parseInt(cantidadDisponibleStr);

        if (costo <= 0 || cantidadDisponible < 0) {
            request.setAttribute("error", "El costo debe ser mayor a 0 y la cantidad no puede ser negativa.");
            listarComponentes(request, response);
            return;
        }

        // Crear objeto Componente con los datos enviados
        Componente componente = new Componente();
        componente.setNombre(nombre.trim());
        componente.setCosto(costo);
        componente.setCantidadDisponible(cantidadDisponible);

        if (idComponenteStr == null || idComponenteStr.isEmpty()) {
            // Intentar registrar un nuevo componente
            boolean resultado = ComponenteRegistroDB.registrarComponente(componente);
            if (resultado) {
                request.setAttribute("mensaje", "Componente registrado correctamente.");
                System.out.println("Componente registrado correctamente: " + componente.getNombre());
            } else {
                request.setAttribute("error", "No se pudo registrar el componente: ya existe uno con un costo diferente.");
                System.out.println("Error: No se pudo registrar el componente, ya existe con un costo diferente.");
            }
        } else {
            // Editar un componente existente
            int idComponente = Integer.parseInt(idComponenteStr);
            componente.setIdComponente(idComponente);

            boolean resultado = ComponenteRegistroDB.actualizarComponente(componente);
            if (resultado) {
                request.setAttribute("mensaje", "Componente actualizado correctamente.");
                System.out.println("Componente actualizado correctamente: " + componente.getNombre());
            } else {
                request.setAttribute("error", "No se pudo actualizar el componente.");
                System.out.println("Error: No se pudo actualizar el componente.");
            }
        }

        listarComponentes(request, response);
    } catch (NumberFormatException ex) {
        System.out.println("Error en el formato de los datos ingresados: " + ex.getMessage());
        request.setAttribute("error", "Formato de datos inválido.");
        listarComponentes(request, response);
    } catch (SQLException ex) {
        System.out.println("Error al procesar la solicitud: " + ex.getMessage());
        request.setAttribute("error", "Error al procesar la solicitud.");
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud.");
    }
}


    private void listarComponentes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener todos los componentes de la base de datos
            List<Componente> componentes = ComponenteConsultaDB.obtenerComponentes();
            request.setAttribute("componentes", componentes);

            // Redirigir al JSP para mostrar los componentes
            request.getRequestDispatcher("Ensamblador/gestionarComponentes.jsp").forward(request, response);
        } catch (SQLException e) {
            System.out.println("Error al obtener componentes para listar: " + e.getMessage());
            request.setAttribute("error", "Error al listar los componentes.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar los componentes.");
        }
    }

    private void eliminarComponente(HttpServletRequest request, HttpServletResponse response, int idComponente) throws ServletException, IOException {
        try {
            // Eliminar el componente por ID
            boolean resultado = ComponenteRegistroDB.eliminarComponente(idComponente);
            if (resultado) {
                request.setAttribute("mensaje", "Componente eliminado correctamente.");
                System.out.println("Componente eliminado correctamente con ID: " + idComponente);
            } else {
                request.setAttribute("error", "No se pudo eliminar el componente.");
                System.out.println("Error: No se pudo eliminar el componente con ID: " + idComponente);
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar componente con ID " + idComponente + ": " + e.getMessage());
            request.setAttribute("error", "Error al eliminar el componente.");
        }

        listarComponentes(request, response);
    }
}
