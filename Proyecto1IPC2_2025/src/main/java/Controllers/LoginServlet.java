/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.Usuario;
import backendDB.ModelosDB.UsuarioDB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author herson
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombreUsuario = request.getParameter("username");
        String contraseña = request.getParameter("password");
        
        Usuario usuario = UsuarioDB.verificarCredenciales(nombreUsuario, contraseña);
        if(usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            session.setAttribute("rolNombre", usuario.getRolNombre()); //setear el tipo de rol en la sesion 
            response.sendRedirect("inicio.jsp");
        } else {
            request.setAttribute("errorMensaje", "Nombre de usuario o contraseña incorrecta");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
}
