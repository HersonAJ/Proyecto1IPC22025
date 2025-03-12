/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Ensamblador;

import Modelos.TipoComputadora;
import Modelos.Usuario;
import backendDB.ModelosDB.ComponentesDB.InventarioDB;
import backendDB.ModelosDB.ComputadorasEnsambladasDB;
import backendDB.ModelosDB.TipoComputadoraDB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

            // Redirigir al JSP correspondiente
            request.getRequestDispatcher("Ensamblador/ensamblarComputadora1.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al cargar los datos.");
            request.getRequestDispatcher("Ensamblador/ensamblarComputadora1.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener la sesión y verificar si el usuario está autenticado
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                request.setAttribute("error", "La sesión ha expirado o no hay un usuario autenticado. Por favor, inicie sesión nuevamente.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            String nombreUsuario = usuario.getNombreUsuario();

            // Obtener acción del formulario
            String accion = request.getParameter("accion");

            if ("actualizarComponentes".equals(accion)) {
                // Lógica para actualizar los componentes en el selector
                List<TipoComputadora> tiposComputadoras = TipoComputadoraDB.obtenerTiposComputadoras();
                request.setAttribute("tiposComputadoras", tiposComputadoras);

                String computadoraSeleccionada = request.getParameter("computadora");
                if (computadoraSeleccionada != null && !computadoraSeleccionada.isEmpty()) {
                    List<String[]> componentes = InventarioDB.obtenerComponentesConCantidad(computadoraSeleccionada);
                    request.setAttribute("componentes", componentes);
                    request.setAttribute("computadoraSeleccionada", computadoraSeleccionada);
                }

                request.getRequestDispatcher("Ensamblador/ensamblarComputadora1.jsp").forward(request, response);
                return;
            }

            // Lógica de ensamblaje
            String computadoraSeleccionada = request.getParameter("computadora");
            String fecha = request.getParameter("fecha");

            if (fecha == null || fecha.isEmpty()) {
                request.setAttribute("error", "La fecha es obligatoria.");
                List<TipoComputadora> tiposComputadoras = TipoComputadoraDB.obtenerTiposComputadoras();
                request.setAttribute("tiposComputadoras", tiposComputadoras);
                request.getRequestDispatcher("Ensamblador/ensamblarComputadora1.jsp").forward(request, response);
                return;
            }

            DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFormateada = LocalDate.parse(fecha, formatoEntrada).format(formatoSalida);

            List<String> componentesInsuficientes = InventarioDB.validarInventarioComponentes(computadoraSeleccionada);
            if (!componentesInsuficientes.isEmpty()) {
                request.setAttribute("error", "No hay suficiente inventario para los siguientes componentes: " + String.join(", ", componentesInsuficientes));
                List<TipoComputadora> tiposComputadoras = TipoComputadoraDB.obtenerTiposComputadoras();
                request.setAttribute("tiposComputadoras", tiposComputadoras);
                request.getRequestDispatcher("EnsambladorensamblarComputadora1.jsp").forward(request, response);
                return;
            }

            boolean ensamblajeRegistrado = ComputadorasEnsambladasDB.registrarEnsamblaje(computadoraSeleccionada, nombreUsuario, fechaFormateada);
            if (ensamblajeRegistrado) {
                boolean inventarioActualizado = InventarioDB.actualizarInventario(computadoraSeleccionada);
                if (inventarioActualizado) {
                    request.setAttribute("mensaje", "La computadora '" + computadoraSeleccionada + "' se ensambló correctamente.");
                } else {
                    request.setAttribute("error", "Ocurrió un error al actualizar el inventario.");
                }
            } else {
                request.setAttribute("error", "Ocurrió un error al registrar el ensamblaje.");
            }

            // Recargar los tipos de computadoras y redirigir al JSP después del ensamblaje
            List<TipoComputadora> tiposComputadoras = TipoComputadoraDB.obtenerTiposComputadoras();
            request.setAttribute("tiposComputadoras", tiposComputadoras);
            request.getRequestDispatcher("Ensamblador/ensamblarComputadora1.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al procesar la solicitud.");
            try {
                List<TipoComputadora> tiposComputadoras = TipoComputadoraDB.obtenerTiposComputadoras();
                request.setAttribute("tiposComputadoras", tiposComputadoras);
            } catch (Exception ex) {
                ex.printStackTrace(); // Manejo de error adicional en caso de falla al cargar tipos
            }
            request.getRequestDispatcher("Ensamblador/ensamblarComputadora1.jsp").forward(request, response);
        }
    }

}
