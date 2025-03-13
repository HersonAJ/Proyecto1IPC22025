/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

/**
 *
 * @author herson
 */
import Modelos.DetalleVenta;
import Modelos.Venta;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GeneradorCsv {
    private String tituloReporte;
    private String nombreUsuarioActivo;
    private String fechaInicio;
    private String fechaFin;

    // Constructor para inicializar los valores del reporte
    public GeneradorCsv(String tituloReporte, String nombreUsuarioActivo, String fechaInicio, String fechaFin) {
        this.tituloReporte = tituloReporte;
        this.nombreUsuarioActivo = nombreUsuarioActivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Método para generar el CSV
    public void generarReporteVentas(PrintWriter writer, List<Venta> ventas) {
        try {
            // Encabezado del reporte
            writer.println(tituloReporte);
            writer.println("Generado por: " + nombreUsuarioActivo);
            writer.println("Fecha de generación: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            writer.println("Rango de fechas: " + fechaInicio + " a " + fechaFin);
            writer.println();

            // Encabezados de las columnas
            writer.println("ID Venta,Cliente,Vendedor,Fecha Venta,Total Venta,ID Computadora,Cantidad,Subtotal");

            // Iterar sobre las ventas y sus detalles para generar el contenido
            for (Venta venta : ventas) {
                for (DetalleVenta detalle : venta.getDetallesVenta()) {
                    writer.printf("%d,%s,%s,%s,\"%.2f\",%d,%d,\"%.2f\"%n",
                            venta.getIdVenta(),
                            venta.getNombreCliente(),
                            venta.getNombreVendedor(),
                            new SimpleDateFormat("yyyy-MM-dd").format(venta.getFechaVenta()),
                            venta.getTotalVenta(), // Total formateado con dos decimales
                            detalle.getIdComputadora(),
                            detalle.getCantidad(),
                            detalle.getSubtotal()); // Subtotal formateado con dos decimales
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el archivo CSV", e);
        }
    }
}

