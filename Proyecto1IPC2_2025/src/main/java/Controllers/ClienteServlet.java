/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.Cliente;
import backendDB.ModelosDB.ClienteDB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author herson
 */
@WebServlet(name = "ClienteServlet", urlPatterns = {"/ClienteServlet"})
public class ClienteServlet extends HttpServlet {

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener la acción desde el formulario
        String action = request.getParameter("action");

        if ("buscar".equals(action)) {
            // Lógica para buscar cliente por NIT
            String nit = request.getParameter("nit");
            if (nit == null || nit.trim().isEmpty()) {
                request.setAttribute("error", "Debe ingresar un NIT válido.");
            } else {
                Cliente cliente = ClienteDB.obtenerClientePorNit(nit);
                if (cliente != null) {
                    request.setAttribute("cliente", cliente);
                } else {
                    request.setAttribute("clienteNoEncontrado", nit);
                }
            }
            // Redirigir a la vista
            request.getRequestDispatcher("buscarCliente.jsp").forward(request, response);

        } else if ("registrar".equals(action)) {
            // Lógica para registrar un nuevo cliente
            String nit = request.getParameter("nit");
            String nombre = request.getParameter("nombre");
            String direccion = request.getParameter("direccion");

            if (nit != null && !nit.trim().isEmpty() && 
                nombre != null && !nombre.trim().isEmpty() && 
                direccion != null && !direccion.trim().isEmpty()) {
                
                Cliente nuevoCliente = new Cliente();
                nuevoCliente.setNit(nit);
                nuevoCliente.setNombre(nombre);
                nuevoCliente.setDireccion(direccion);
                
                boolean registrado = ClienteDB.registrarCliente(nuevoCliente);
                if (registrado) {
                    // Recuperar el cliente recién creado
                    Cliente cliente = ClienteDB.obtenerClientePorNit(nit);
                    request.setAttribute("cliente", cliente);
                } else {
                    request.setAttribute("error", "No se pudo registrar al cliente.");
                }
            } else {
                request.setAttribute("error", "Todos los campos son obligatorios.");
            }
            // Redirigir a la vista
            request.getRequestDispatcher("buscarCliente.jsp").forward(request, response);
        }
    }
}


