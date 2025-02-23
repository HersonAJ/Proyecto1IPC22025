/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos.ProcesadorArchivo;

import Modelos.Cliente;
import backendDB.ModelosDB.ClienteDB;
import java.util.List;

/**
 *
 * @author herson
 */
public class ProcesarCliente {
    
    public static void procesarCliente(String linea, List<String> logProcesamiento) {
        //formaro para insertar cliente: CLIENTE("nit", "nombre", "direccion")
        try {
            String datos = linea.substring(linea.indexOf("(") + 1, linea.lastIndexOf(")"));
            String[] partes = datos.split(", ");
            
            String nit = partes[0].replace("\"", "").trim();
            String nombre = partes[1].replace("\"", "").trim();
            String direccion = partes[2].replace("\"", "").trim();
            
            Cliente cliente = new Cliente();
            cliente.setNit(nit);
            cliente.setNombre(nombre);
            cliente.setDireccion(direccion);
            
            if(ClienteDB.registrarCliente(cliente)) {
                logProcesamiento.add("Cliente registrado correctamente" + nombre);
            } else {
                logProcesamiento.add("Error al registrar cliente: " + nombre);
            }
        } catch (Exception e) {
            logProcesamiento.add("Error al procesar la instruccion CLIENTE: " + linea + "- " + e.getMessage());
        }
    }
}
