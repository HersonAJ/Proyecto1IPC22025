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
        // Limpiar la línea para eliminar espacios iniciales y finales
        linea = linea.trim();

        // Verificar el formato de entrada
        if (!linea.startsWith("PIEZA(") || !linea.endsWith(")")) {
            logProcesamiento.add("Formato incorrecto en la instrucción PIEZA: " + linea);
            return;
        }

        try {
            // Extraer los datos dentro de los paréntesis y limpiar espacios extra
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")")).trim();
            String[] partes = datos.split(",\\s*"); // Divide por coma y elimina espacios extra

            // Validar y procesar los datos
            String nombre = partes[0].replace("\"", "").trim();
            if (nombre.isEmpty()) {
                logProcesamiento.add("Nombre vacío en la instrucción PIEZA: " + linea);
                return;
            }

            double costo = Double.parseDouble(partes[1].trim());
            if (costo <= 0) {
                logProcesamiento.add("Costo inválido en la instrucción PIEZA: " + linea);
                return;
            }

            // Crear el componente con los datos proporcionados
            Componente componente = new Componente();
            componente.setNombre(nombre);
            componente.setCosto(costo);
            componente.setCantidadDisponible(1); // Cada instrucción representa una unidad

            // Llamar al DB para registrar o actualizar el componente (incluyendo lógica para actualizar el precio)
            if (ComponenteDB.registrarComponente(componente)) {
                logProcesamiento.add("Componente procesado correctamente: " + nombre + " (Precio actualizado: " + costo + ")");
            } else {
                logProcesamiento.add("Error al procesar el componente: " + nombre);
            }
        } catch (Exception e) {
            logProcesamiento.add("Error al procesar la instrucción PIEZA: " + linea +
                                 " - Tipo de error: " + e.getClass().getSimpleName() +
                                 " - Mensaje: " + e.getMessage());
        }
    }
}
