/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.Ensamblaje;
import Modelos.EnsamblajePieza;
import backendDB.ModelosDB.ComponenteDB;
import backendDB.ModelosDB.EnsamblajeDB;
import backendDB.ModelosDB.EnsamblajePiezaDB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author herson
 */
@WebServlet("/EnsamblarComputadoraServlet")
public class EnsamblarComputadoraServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idComputadoraStr = request.getParameter("tipoComputadora");
        String fechaEnsamblajeStr = request.getParameter("fechaEnsamblaje");
        int idUsuario = (Integer) request.getSession().getAttribute("idUsuario"); // Asumimos que el ID de usuario está en la sesión

        if (idComputadoraStr != null && fechaEnsamblajeStr != null && idUsuario != 0) {
            int idComputadora = Integer.parseInt(idComputadoraStr);
            Date fechaEnsamblaje = Date.valueOf(fechaEnsamblajeStr); // Conversión a java.sql.Date

            try {
                // Verificar disponibilidad de componentes
                List<EnsamblajePieza> piezas = EnsamblajePiezaDB.obtenerEnsamblajePiezas(idComputadora);
                boolean disponible = true;

                for (EnsamblajePieza pieza : piezas) {
                    int cantidadDisponible = ComponenteDB.obtenerCantidadDisponible(pieza.getIdComponente());
                    if (cantidadDisponible < pieza.getCantidad()) {
                        disponible = false;
                        break;
                    }
                }

                if (disponible) {
                    // Descontar las cantidades de componentes del inventario
                    for (EnsamblajePieza pieza : piezas) {
                        ComponenteDB.actualizarCantidad(pieza.getIdComponente(), -pieza.getCantidad());
                    }

                    // Registrar ensamblaje
                    Ensamblaje ensamblaje = new Ensamblaje();
                    ensamblaje.setIdComputadora(idComputadora);
                    ensamblaje.setIdUsuario(idUsuario);
                    ensamblaje.setFechaEnsamblaje(fechaEnsamblaje);
                    EnsamblajeDB.registrarEnsamblaje(ensamblaje);

                    // Establecer mensaje de éxito
                    request.setAttribute("mensaje", "Computadora ensamblada con éxito.");
                } else {
                    // Establecer mensaje de error por falta de componentes
                    request.setAttribute("mensaje", "No hay suficientes componentes disponibles para ensamblar esta computadora.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("mensaje", "Ocurrió un error durante el ensamblaje.");
            }
        } else {
            request.setAttribute("mensaje", "Por favor, complete todos los campos.");
        }
        request.getRequestDispatcher("ensamblajeComputadora.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}


