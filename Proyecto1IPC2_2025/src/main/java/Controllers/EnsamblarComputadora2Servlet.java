/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.TipoComputadora;
import Modelos.Usuario;
import backendDB.ModelosDB.ComponenteDB;
import backendDB.ModelosDB.ComputadorasEnsambladasDB;
import backendDB.ModelosDB.TipoComputadoraDB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 *
 * @author herson
 */
@WebServlet("/EnsamblarComputadora2Servlet")
public class EnsamblarComputadora2Servlet extends HttpServlet {
 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener todos los tipos de computadoras disponibles
            List<TipoComputadora> tiposComputadoras = TipoComputadoraDB.obtenerTiposComputadoras();
            request.setAttribute("tiposComputadoras", tiposComputadoras);

            // Verificar si se seleccionó una computadora
            String computadoraSeleccionada = request.getParameter("computadora");
            if (computadoraSeleccionada != null && !computadoraSeleccionada.isEmpty()) {
                // Obtener componentes y cantidades (requeridas y en inventario)
                List<String[]> componentes = ComponenteDB.obtenerComponentesConCantidad(computadoraSeleccionada);
                request.setAttribute("componentes", componentes);

                // Enviar la computadora seleccionada al JSP
                request.setAttribute("computadoraSeleccionada", computadoraSeleccionada);
            }

            // Redirigir al JSP correspondiente
            request.getRequestDispatcher("ensamblarComputadora1.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al cargar los datos.");
            request.getRequestDispatcher("/vistas/Error.jsp").forward(request, response);
        }
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
        // Obtener datos del formulario
        String computadoraSeleccionada = request.getParameter("computadora");
        String fecha = request.getParameter("fecha");

        // Formatear la fecha al formato esperado si es necesario (dd/MM/yyyy)
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = LocalDate.parse(fecha, formatoEntrada).format(formatoSalida);

        // Obtener el usuario ensamblador de la sesión
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String nombreUsuario = usuario.getNombreUsuario();

        // Validar inventario
        List<String> componentesInsuficientes = ComponenteDB.validarInventarioComponentes(computadoraSeleccionada);
        if (!componentesInsuficientes.isEmpty()) {
            request.setAttribute("error", "No hay suficiente inventario para los siguientes componentes: " + componentesInsuficientes);
            doGet(request, response); // Volver a cargar la página
            return;
        }

        // Registrar ensamblaje
        boolean ensamblajeRegistrado = ComputadorasEnsambladasDB.registrarEnsamblaje(computadoraSeleccionada, nombreUsuario, fechaFormateada);
        if (ensamblajeRegistrado) {
            // Actualizar inventario
            boolean inventarioActualizado = ComponenteDB.actualizarInventario(computadoraSeleccionada);
            if (inventarioActualizado) {
                request.setAttribute("mensaje", "La computadora '" + computadoraSeleccionada + "' se ensambló correctamente.");
            } else {
                request.setAttribute("error", "Ocurrió un error al actualizar el inventario.");
            }
        } else {
            request.setAttribute("error", "Ocurrió un error al registrar el ensamblaje.");
        }

        // Volver a cargar los datos para la vista
        doGet(request, response);
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("error", "Ocurrió un error al procesar el ensamblaje.");
        request.getRequestDispatcher("/vistas/Error.jsp").forward(request, response);
    }
}


}
