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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author herson
 */
@WebServlet("/EnsamblarComputadoraServlet")
public class EnsamblarComputadoraServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idComputadoraStr = request.getParameter("tipoComputadora");
        
        if (idComputadoraStr != null) {
            int idComputadora = Integer.parseInt(idComputadoraStr);
            try {
                List<EnsamblajePieza> piezas = EnsamblajePiezaDB.obtenerEnsamblajePiezas(idComputadora);
                request.setAttribute("componentes", piezas);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher("ensamblajeComputadora.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
