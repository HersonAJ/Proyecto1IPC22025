/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Modelos.ProcesadorArchivo.ProcesadorArchivo;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author herson
 */
@MultipartConfig
@WebServlet(name = "CargarArchivoServlet", urlPatterns = {"/CargarArchivoServlet"})
public class CargarArchivoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part archivo = request.getPart("archivo");

        List<String> lineasArchivo = new ArrayList<>();
        StringBuilder logProcesamiento = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(archivo.getInputStream()))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineasArchivo.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logProcesamiento.append("Error al leer el archivo: ").append(e.getMessage());
        }

        // Procesar el archivo usando ProcesadorArchivo
        ProcesadorArchivo procesador = new ProcesadorArchivo();
        List<String> resultadoProcesamiento = procesador.procesarArchivo(lineasArchivo);

        // Construir el log de procesamiento
        for (String logEntry : resultadoProcesamiento) {
            logProcesamiento.append(logEntry).append("\n");
        }

        request.setAttribute("logProcesamiento", logProcesamiento.toString());
        request.getRequestDispatcher("cargarArchivo.jsp").forward(request, response);
    }
}
