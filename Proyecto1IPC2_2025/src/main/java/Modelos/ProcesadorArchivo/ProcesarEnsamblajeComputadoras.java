/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos.ProcesadorArchivo;

import backendDB.ModelosDB.ComponentesDB.InventarioDB;
import backendDB.ModelosDB.ComputadorasEnsambladasDB;
import backendDB.ModelosDB.TipoComputadoraDB;
import backendDB.ModelosDB.UsuarioDB;
import java.util.List;

/**
 *
 * @author herson
 */
public class ProcesarEnsamblajeComputadoras {
    
    public static void procesarEnsamblarComputadora(String linea, List<String> logProcesamiento) {
        try {
            // Extraer datos dentro de los paréntesis
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            String[] partes = datos.split(",\\s*");

            // Limpiar el nombre de la computadora y otros datos
            String nombreComputadora = partes[0].replace("\"", "").replace("“", "").replace("”", "").trim();
            String nombreUsuario = partes[1].trim();
            String fecha = partes[2].replace("\"", "").replace("“", "").replace("”", "").trim();

            // Validaciones
            if (!TipoComputadoraDB.existeTipoComputadora(nombreComputadora)) {
                logProcesamiento.add("Error: El tipo de computadora '" + nombreComputadora + "' no existe.");
                return;
            }
            if (!UsuarioDB.existeUsuario(nombreUsuario)) {
                logProcesamiento.add("Error: El usuario '" + nombreUsuario + "' no existe.");
                return;
            }
            List<String> componentesInsuficientes = InventarioDB.validarInventarioComponentes(nombreComputadora);
            if (!componentesInsuficientes.isEmpty()) {
                logProcesamiento.add("Error: Insuficiencia de los siguientes componentes: " + componentesInsuficientes);
                return;
            }

            // Registrar ensamblaje y actualizar inventario
            if (ComputadorasEnsambladasDB.registrarEnsamblaje(nombreComputadora, nombreUsuario, fecha)) {
                if (InventarioDB.actualizarInventario(nombreComputadora)) {
                    logProcesamiento.add("Éxito: Ensamblaje de '" + nombreComputadora + "' registrado correctamente.");
                } else {
                    logProcesamiento.add("Error: No se pudo actualizar el inventario para '" + nombreComputadora + "'.");
                }
            } else {
                logProcesamiento.add("Error: No se pudo registrar el ensamblaje para '" + nombreComputadora + "'.");
            }
        } catch (Exception e) {
            logProcesamiento.add("Error inesperado al procesar ensamblaje: " + linea + " - " + e.getMessage());
        }
    }
}
