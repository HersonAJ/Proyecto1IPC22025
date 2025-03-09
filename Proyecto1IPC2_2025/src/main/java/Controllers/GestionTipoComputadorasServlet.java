/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.TipoComputadora;
import backendDB.ModelosDB.EnsamblajePiezaDB;
import backendDB.ModelosDB.TipoComputadoraDB;
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
@WebServlet(name = "GestionComputadorasServlet", urlPatterns = {"/GestionComputadorasServlet"})
public class GestionTipoComputadorasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener la lista de tipos de computadoras desde la base de datos
            List<TipoComputadora> tiposComputadoras = TipoComputadoraDB.obtenerTiposComputadoras();
            request.setAttribute("tiposComputadoras", tiposComputadoras);
        } catch (Exception e) {
            // Manejar posibles errores al cargar los datos
            request.setAttribute("error", "No se pudieron cargar los tipos de computadoras: " + e.getMessage());
            e.printStackTrace();
        }

        // Redirigir al JSP para mostrar la lista
        request.getRequestDispatcher("gestionarTipoComputadoras.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los datos del formulario
        String nombre = request.getParameter("nombre");
        String precioVentaStr = request.getParameter("precioVenta");
        String[] componentes = request.getParameterValues("componente[]");
        String[] cantidadesStr = request.getParameterValues("cantidad[]");

        try {
            // Validar nombre
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new Exception("El nombre de la computadora es obligatorio.");
            }

            // Validar precio de venta
            double precioVenta;
            try {
                precioVenta = Double.parseDouble(precioVentaStr);
                if (precioVenta <= 0) {
                    throw new Exception("El precio de venta debe ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                throw new Exception("El precio de venta debe ser un número válido.");
            }

            // Crear el tipo de computadora
            TipoComputadora tipoComputadora = new TipoComputadora();
            tipoComputadora.setNombre(nombre.trim());
            tipoComputadora.setPrecioVenta(precioVenta);

            boolean registrado = TipoComputadoraDB.registrarTipoComputadora(tipoComputadora);
            if (!registrado) {
                throw new Exception("El tipo de computadora ya existe o no se pudo registrar.");
            }

            // Asociar componentes al nuevo tipo de computadora
            if (componentes != null && cantidadesStr != null) {
                for (int i = 0; i < componentes.length; i++) {
                    String componente = componentes[i].trim();
                    int cantidad;
                    try {
                        cantidad = Integer.parseInt(cantidadesStr[i].trim());
                        if (cantidad <= 0) throw new Exception("La cantidad debe ser mayor a 0.");
                    } catch (NumberFormatException e) {
                        throw new Exception("La cantidad debe ser un número válido para el componente: " + componente);
                    }

                    // Crear la relación entre el tipo de computadora y el componente
                    boolean relacionCreada = EnsamblajePiezaDB.crearRelacion(tipoComputadora.getNombre(), componente, cantidad);
                    if (!relacionCreada) {
                        throw new Exception("No se pudo crear la relación para el componente: " + componente);
                    }
                }
            }

            request.setAttribute("mensaje", "El tipo de computadora y sus componentes se registraron correctamente.");
        } catch (Exception e) {
            // Manejo de errores durante la validación o registro
            request.setAttribute("error", "Error al registrar el tipo de computadora: " + e.getMessage());
        }

        // Redirigir al JSP con los mensajes y datos actualizados
        doGet(request, response);
    }
}

