/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Administrador;

import Modelos.Usuario;
import backendDB.ModelosDB.UsuarioDB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author herson
 */

@WebServlet("/RegistroServlet")
public class RegistroUsuariosServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int roleId = Integer.parseInt(request.getParameter("rol"));

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(username);
        usuario.setContraseña(password);
        usuario.setRol(roleId);

        boolean registrado = UsuarioDB.registrarUsuario(usuario);
        if (registrado) {
            response.sendRedirect("Administrador/registroExitoso.jsp");
        } else {
            response.sendRedirect("Administrador/error.jsp");
        }
    }
}

