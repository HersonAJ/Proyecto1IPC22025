/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.Cliente;
import Modelos.Computadora;
import backendDB.ModelosDB.ClienteDB;
import backendDB.ModelosDB.ComputadoraDB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author herson
 */
@WebServlet(name = "ConsultarClienteServlet", urlPatterns = {"/ConsultarClienteServlet"})
public class ConsultarClienteServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nit = request.getParameter("nit");
        System.out.println("NIT recibido: " + nit);

        try {
            Cliente cliente = ClienteDB.obtenerClientePorNit(nit);
            if (cliente != null) {
                System.out.println("Cliente encontrado: " + cliente.getNombre());
            } else {
                System.out.println("Cliente no encontrado.");
            }

            List<Computadora> computadoras = ComputadoraDB.obtenerComputadoras();
            System.out.println("Número de computadoras encontradas: " + computadoras.size());

            HttpSession session = request.getSession();
            session.setAttribute("computadoras", computadoras);

            if (cliente != null) {
                session.setAttribute("cliente", cliente);
                response.sendRedirect("registrarVenta.jsp?clienteExiste=true");
            } else {
                response.sendRedirect("registrarVenta.jsp?clienteExiste=false");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}


