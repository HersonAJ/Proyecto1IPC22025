/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos.ProcesadorArchivo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author herson
 */
public class ProcesadorArchivo {
    
    public List<String> procesarArchivo(List<String> lineasArchivo) {
        List<String> logProcesamiento = new ArrayList<>();
        
        for (String linea : lineasArchivo) {
            try {
                if (linea.startsWith("USUARIO")) {
                    // Delegar el procesamiento de usuario a la clase ProcesarUsuario
                    ProcesarUsuario.procesarUsuario(linea, logProcesamiento);
                } else if (linea.startsWith("PIEZA")) {
                    ProcesarComponente.procesarComponente(linea, logProcesamiento);
                } else if (linea.startsWith("COMPUTADORA")) {
                    ProcesarComputadora.procesarComputadora(linea, logProcesamiento);
                } else if (linea.startsWith("CLIENTE")) {
                    ProcesarCliente.procesarCliente(linea, logProcesamiento);
                } else {
                    logProcesamiento.add("Instrucción no reconocida: " + linea);
                }
            } catch (Exception e) {
                logProcesamiento.add("Error al procesar la línea: " + linea + " - " + e.getMessage());
            }
        }
        
        return logProcesamiento;
    }
}
