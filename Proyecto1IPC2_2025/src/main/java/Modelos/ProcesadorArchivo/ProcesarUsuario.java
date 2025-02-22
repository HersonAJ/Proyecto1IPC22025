/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos.ProcesadorArchivo;

import Modelos.Usuario;
import backendDB.ModelosDB.UsuarioDB;
import java.util.List;

/**
 *
 * @author herson
 */
public class ProcesarUsuario {
 
    public static void procesarUsuario(String linea, List<String> logProcesamiento) {
        // Formato esperado: USUARIO("nombreUsuario", "contraseña", tipo)
        try {
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            String[] partes = datos.split(", ");

            String nombreUsuario = partes[0].replace("\"", "").trim();
            String contraseña = partes[1].replace("\"", "").trim();
            int idRol = Integer.parseInt(partes[2].trim());

            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setContraseña(contraseña);
            usuario.setRol(idRol);

            if (UsuarioDB.registrarUsuario(usuario)) {
                logProcesamiento.add("Usuario registrado correctamente: " + nombreUsuario);
            } else {
                logProcesamiento.add("Error al registrar el usuario: " + nombreUsuario);
            }
        } catch (Exception e) {
            logProcesamiento.add("Error al procesar la instrucción USUARIO: " + linea + " - " + e.getMessage());
        }
    }
}