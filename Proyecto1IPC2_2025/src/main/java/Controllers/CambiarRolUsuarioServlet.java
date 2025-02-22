/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

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
@WebServlet(name = "CambiarRolUsuarioServlet", urlPatterns = {"/CambiarRolUsuarioServlet"})
public class CambiarRolUsuarioServlet extends HttpServlet {

    //obtener los parametros del la gestion de usuarios
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        int nuevoRol = Integer.parseInt(request.getParameter("nuevoRol"));
        String usuarioActivo =(String) request.getSession().getAttribute("nombreUsuarioActivo");
        
        if(usuarioActivo.equals(request.getParameter("nombreUsuario"))) {
            System.out.println("No se puede cambiar el rol del usuario activo. ");
            response.sendRedirect("gestionarUsuarios.jsp?mensaje=noCambioRol");
        }
        
        System.out.println("Cambiar Rol - ID Usuario " + idUsuario);
        
        boolean exito = GestionarUsuariosDB.cambiarRolUsuario(idUsuario, nuevoRol);
        
        if(exito) {
            response.sendRedirect("gestionarUsuarios.jsp?mensaje=rolActualizado");
        } else {
            response.sendRedirect("gestionarUsuarios.jsp?mensaje=error");
        }
    }

}
