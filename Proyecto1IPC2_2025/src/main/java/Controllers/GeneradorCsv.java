/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

/**
 *
 * @author herson
 */
import Modelos.Cliente;
import Modelos.ComputadoraEnsamblada;
import Modelos.DetalleVenta;
import Modelos.Usuario;
import Modelos.Venta;
import backendDB.ModelosDB.ClienteDB;
import backendDB.ModelosDB.UsuarioDB;
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

    public void generarReporteGanancias(PrintWriter writer, List<Venta> ventas,
            double sumaTotalVentas, double sumaTotalGastos,
            double totalGanancia, double totalPerdida, double gananciaNeta) {
        try {
            // Encabezado del reporte
            writer.println(tituloReporte);
            writer.println("Generado por: " + nombreUsuarioActivo);
            writer.println("Fecha de generación: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            writer.println("Rango de fechas: " + fechaInicio + " a " + fechaFin);
            writer.println();

            // Sección: Detalles de Ventas
            writer.println("Detalles de Ventas");
            writer.println("ID Venta,Cliente,Vendedor,Fecha Venta,Total Venta");
            for (Venta venta : ventas) {
                String clienteNombre = ClienteDB.obtenerCliente(venta.getIdCliente()).getNombre();
                String vendedorNombre = UsuarioDB.obtenerUsuario(venta.getIdUsuario()).getNombreUsuario();

                writer.printf("%d,%s,%s,%s,\"%.2f\"%n",
                        venta.getIdVenta(),
                        clienteNombre,
                        vendedorNombre,
                        new SimpleDateFormat("yyyy-MM-dd").format(venta.getFechaVenta()),
                        venta.getTotalVenta());
            }
            writer.println();

            // Sección: Resumen Financiero
            writer.println("Resumen Financiero");
            writer.println("Concepto,Valor");
            writer.printf("Suma Total de Ventas,\"%.2f\"%n", sumaTotalVentas);
            writer.printf("Suma Total de Gastos de Ensamblaje,\"%.2f\"%n", sumaTotalGastos);
            writer.printf("Total Ganancia,\"%.2f\"%n", totalGanancia);
            writer.printf("Total Pérdida,\"%.2f\"%n", totalPerdida);
            writer.printf("Ganancia Neta,\"%.2f\"%n", gananciaNeta);
            writer.println();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte de ganancias", e);
        }
    }

    public void generarReporteVentasPorUsuario(PrintWriter writer, List<Usuario> reporteUsuarios) {
        try {
            // Encabezado del reporte
            writer.println(tituloReporte);
            writer.println("Generado por: " + nombreUsuarioActivo);
            writer.println("Fecha de generación: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            writer.println("Rango de fechas: " + fechaInicio + " a " + fechaFin);
            writer.println();

            // Encabezados de las columnas
            writer.println("Usuario,Rol,Total de Ventas");

            // Escribir los datos del reporte
            for (Usuario usuario : reporteUsuarios) {
                writer.printf("%s,%s,%d%n",
                        usuario.getNombreUsuario(),
                        usuario.getRolNombre(),
                        usuario.getTotalVentas());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte de ventas por usuario", e);
        }
    }

    public void generarReporteGananciasPorUsuario(PrintWriter writer, List<Usuario> reporteUsuarios) {
        try {
            // Encabezado del reporte
            writer.println(tituloReporte);
            writer.println("Generado por: " + nombreUsuarioActivo);
            writer.println("Fecha de generación: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            writer.println("Rango de fechas: " + fechaInicio + " a " + fechaFin);
            writer.println();

            // Encabezados de las columnas
            writer.println("Usuario,Rol,Total de Ganancias");

            // Escribir los datos del reporte
            for (Usuario usuario : reporteUsuarios) {
                writer.printf("%s,%s,\"%.2f\"%n",
                        usuario.getNombreUsuario(),
                        usuario.getRolNombre(),
                        (double) usuario.getTotalVentas());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte de ganancias por usuario", e);
        }
    }

}
