/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos.ProcesadorArchivo;

import Modelos.Computadora;
import Modelos.TipoComputadora;
import backendDB.ModelosDB.ComputadoraDB;
import backendDB.ModelosDB.TipoComputadoraDB;
import java.util.List;

/**
 *
 * @author herson
 */
public class ProcesarComputadora {
        
    //nuevo metodo con correciones en la division de la entidad computadora
    public static void procesarComputadora(String linea, List<String> logProcesamiento) {
        // Formato esperado: COMPUTADORA("Nombre de la computadora", precioVenta)
        try {
            // Extraer los datos dentro de los paréntesis
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")")).trim();
            String[] partes = datos.split(",\\s*"); // Divide los datos por la coma y elimina espacios adicionales

            // Validar y extraer el nombre
            String nombre = partes[0].replace("\"", "").trim();
            if (nombre.isEmpty()) {
                logProcesamiento.add("Error: El nombre de la computadora está vacío en la instrucción: " + linea);
                return;
            }

            // Validar y extraer el precio de venta
            double precioVenta;
            try {
                precioVenta = Double.parseDouble(partes[1].trim());
                if (precioVenta <= 0) {
                    logProcesamiento.add("Error: El precio de venta debe ser mayor a 0 en la instrucción: " + linea);
                    return;
                }
            } catch (NumberFormatException e) {
                logProcesamiento.add("Error: Formato inválido para el precio de venta en la instrucción: " + linea);
                return;
            }

            // Crear instancia de TipoComputadora
            TipoComputadora tipoComputadora = new TipoComputadora();
            tipoComputadora.setNombre(nombre);
            tipoComputadora.setPrecioVenta(precioVenta);

            // Registrar el tipo de computadora en la base de datos
            if (TipoComputadoraDB.registrarTipoComputadora(tipoComputadora)) {
                logProcesamiento.add("Tipo de computadora registrada correctamente: " + nombre);
            } else {
                logProcesamiento.add("Error: El tipo de computadora ya existe o no se pudo registrar: " + nombre);
            }

        } catch (Exception e) {
            logProcesamiento.add("Error inesperado al procesar la instrucción COMPUTADORA: " + linea + " - " + e.getMessage());
        }
    }
}