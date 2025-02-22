/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos.ProcesadorArchivo;

import Modelos.Computadora;
import backendDB.ModelosDB.ComputadoraDB;
import java.util.List;

/**
 *
 * @author herson
 */
public class ProcesarComputadora {
    
    public static void procesarComputadora(String linea, List<String> logProcesamiento) {
        //formato esperado: COMPUTADORA("Nombre de la computadora", precioVenta, costoTotal)
        try {
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            String[] partes = datos.split(", ");
            
            String nombre = partes[0].replace("\"", "").trim();
            double precioVenta = Double.parseDouble(partes[1].trim());
            double costoTotal = Double.parseDouble(partes[2].trim());
            
            Computadora computadora = new Computadora();
            computadora.setNombre(nombre);
            computadora.setPrecioVenta(precioVenta);
            computadora.setCostoTotal(costoTotal);
            
            if (ComputadoraDB.registrarComputadora(computadora)) {
                logProcesamiento.add("Computadora registrada correctamente " + nombre);
            } else {
                logProcesamiento.add("Error al registrar la computadora: " + nombre);
            }
        } catch (Exception e) {
            logProcesamiento.add("Error al procesar la instruccion COMPUTADORA: " + linea + " - " + e.getMessage());
        }
    }
}
