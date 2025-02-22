/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos.ProcesadorArchivo;

import Modelos.Componente;
import backendDB.ModelosDB.ComponenteDB;
import java.util.List;

/**
 *
 * @author herson
 */
public class ProcesarComponente {

    public static void procesarComponente(String linea, List<String> logProcesamiento) {
        // Formato esperado: PIEZA("tipoPieza", costo)
        try {
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            String[] partes = datos.split(", ");

            String nombre = partes[0].replace("\"", "").trim();
            double costo = Double.parseDouble(partes[1].trim());

            Componente componente = new Componente();
            componente.setNombre(nombre);
            componente.setCosto(costo);
            componente.setCantidadDisponible(0); // Inicialmente, la cantidad disponible puede ser 0

            if (ComponenteDB.registrarComponente(componente)) {
                logProcesamiento.add("Componente registrado correctamente: " + nombre);
            } else {
                logProcesamiento.add("Error al registrar el componente: " + nombre);
            }
        } catch (Exception e) {
            logProcesamiento.add("Error al procesar la instrucción PIEZA: " + linea + " - " + e.getMessage());
        }
    }
}