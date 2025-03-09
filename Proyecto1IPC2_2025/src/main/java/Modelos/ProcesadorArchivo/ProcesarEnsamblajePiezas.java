/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos.ProcesadorArchivo;

import backendDB.ModelosDB.EnsamblajePiezaDB;
import java.util.List;

/**
 *
 * @author herson
 */
public class ProcesarEnsamblajePiezas {

    public static void procesarEnsamblaje(String linea, List<String> logProcesamiento) {
        // Formato esperado: ENSAMBLE_PIEZAS("NombreComputadora", "NombrePieza", Cantidad)
        try {
            // Extraer los datos entre los paréntesis
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")")).trim();
            String[] partes = datos.split(",\\s*"); // Divide por coma y elimina espacios extra

            // Validar y extraer el nombre de la computadora
            String nombreComputadora = partes[0].replace("\"", "").trim();
            if (nombreComputadora.isEmpty()) {
                logProcesamiento.add("Error: El nombre de la computadora está vacío en la instrucción: " + linea);
                return;
            }

            // Validar y extraer el nombre de la pieza
            String nombrePieza = partes[1].replace("\"", "").trim();
            if (nombrePieza.isEmpty()) {
                logProcesamiento.add("Error: El nombre de la pieza está vacío en la instrucción: " + linea);
                return;
            }

            // Validar y extraer la cantidad
            int cantidad;
            try {
                cantidad = Integer.parseInt(partes[2].trim());
                if (cantidad <= 0) {
                    logProcesamiento.add("Error: La cantidad debe ser un número entero positivo en la instrucción: " + linea);
                    return;
                }
            } catch (NumberFormatException e) {
                logProcesamiento.add("Error: Formato inválido para la cantidad en la instrucción: " + linea);
                return;
            }

            // Llamar al método del DAO para registrar la relación
            if (EnsamblajePiezaDB.crearRelacion(nombreComputadora, nombrePieza, cantidad)) {
                logProcesamiento.add("Relación creada correctamente: Computadora = " + nombreComputadora +
                                     ", Componente = " + nombrePieza + ", Cantidad = " + cantidad);
            } else {
                logProcesamiento.add("Error: No se pudo crear la relación para la instrucción: " + linea);
            }

        } catch (Exception e) {
            logProcesamiento.add("Error inesperado al procesar la instrucción ENSAMBLE_PIEZAS: " + linea + " - " + e.getMessage());
        }
    }
}

