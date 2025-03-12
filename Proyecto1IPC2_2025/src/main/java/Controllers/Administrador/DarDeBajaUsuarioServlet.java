/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers.Administrador;

import backendDB.ModelosDB.GestionarUsuariosDB;
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
@WebServlet(name = "DarDeBajaUsuarioServlet", urlPatterns = {"/DarDeBajaUsuarioServlet"})
public class DarDeBajaUsuarioServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //obtner los parametros de la gestion de usuarios
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        String usuarioActivo =(String) request.getSession().getAttribute("nombreUsuarioActivo");
        
        if(usuarioActivo.equals(request.getParameter("nombreUsuario"))) {
            System.out.println("No se puede dar de baja al usuario activo");
            response.sendRedirect("Administrador/gestionarUsuarios.jsp?mensaje=noBaja");
        }
        
        
        System.out.println("Dar de baja - ID Usuario " + idUsuario);
        
        boolean exito = GestionarUsuariosDB.darDeBajaUsuario(idUsuario);
        
        if (exito) {
            response.sendRedirect("Administrador/gestionarUsuarios.jsp?mensaje=usuarioInactivado");
        } else {
            response.sendRedirect("Administrador/gestionarUsuarios.jsp?mensaje=error");
        }
    }

}
