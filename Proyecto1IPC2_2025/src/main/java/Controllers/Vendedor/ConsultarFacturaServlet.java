/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Vendedor;

import Modelos.Cliente;
import Modelos.DetalleVenta;
import Modelos.Venta;
import backendDB.ModelosDB.ComprasClienteDB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author herson
 */
@WebServlet(name = "ConsultarFacturaServlet", urlPatterns = {"/ConsultarFacturaServlet"})
public class ConsultarFacturaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Solo redirige al JSP vacío (sin procesar datos)
        request.getRequestDispatcher("${pageContext.request.contextPath}/Vendedor/mostrarNumeroFactura.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener el número de factura ingresado por el usuario
        String numeroFacturaStr = request.getParameter("numeroFactura");

        // Validar que el número de factura sea válido
        if (numeroFacturaStr == null || numeroFacturaStr.trim().isEmpty()) {
            request.setAttribute("error", "Debe ingresar un número de factura.");
            request.getRequestDispatcher("Vendedor/mostrarNumeroFactura.jsp").forward(request, response);
            return;
        }

        try {
            int numeroFactura = Integer.parseInt(numeroFacturaStr);

            // Obtener los datos de la factura usando ComprasClienteDB
            Map<String, Object> facturaCompleta = ComprasClienteDB.obtenerFacturaPorNumero(numeroFactura);

            // Manejar caso en el que no se encuentra la factura
            if (facturaCompleta == null) {
                request.setAttribute("error", "No se encontró una factura con el número proporcionado.");
                request.getRequestDispatcher("Vendedor/mostrarNumeroFactura.jsp").forward(request, response);
                return;
            }

            // Extraer y preparar los datos para el JSP
            Venta venta = new Venta();
            venta.setIdVenta((Integer) facturaCompleta.get("id_venta"));
            venta.setFechaVenta((java.util.Date) facturaCompleta.get("fecha_venta"));
            venta.setTotalVenta((Double) facturaCompleta.get("total_venta"));
            venta.setNumeroFactura((Integer) facturaCompleta.get("numero_factura"));

            Cliente cliente = new Cliente();
            cliente.setNit((String) facturaCompleta.get("cliente_nit"));
            cliente.setNombre((String) facturaCompleta.get("cliente_nombre"));
            cliente.setDireccion((String) facturaCompleta.get("cliente_direccion"));

            @SuppressWarnings("unchecked")
            List<DetalleVenta> detalleVenta = (List<DetalleVenta>) facturaCompleta.get("detalles");

            // Establecer atributos para el JSP
            request.setAttribute("venta", venta);
            request.setAttribute("cliente", cliente);
            request.setAttribute("detalleVenta", detalleVenta);
            request.setAttribute("vendedor", facturaCompleta.get("vendedor_nombre"));

            // Redirigir al JSP para mostrar la factura
            request.getRequestDispatcher("Vendedor/mostrarNumeroFactura.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Manejar caso donde el número de factura no es un número válido
            request.setAttribute("error", "El número de factura ingresado no es válido.");
            request.getRequestDispatcher("Vendedor/mostrarNumeroFactura.jsp").forward(request, response);
        } catch (SQLException e) {
            // Manejar errores de la base de datos
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error al consultar la factura: " + e.getMessage());
            request.getRequestDispatcher("Vendedor/mostrarNumeroFactura.jsp").forward(request, response);
        } catch (Exception e) {
            // Manejar cualquier otra excepción
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error inesperado: " + e.getMessage());
            request.getRequestDispatcher("Vendedor/mostrarNumeroFactura.jsp").forward(request, response);
        }
    }
}

