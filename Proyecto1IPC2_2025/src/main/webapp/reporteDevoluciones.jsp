<%@page import="Modelos.Computadora"%>
<%@page import="Modelos.Venta"%>
<%@page import="backendDB.ModelosDB.VentaDB"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelos.Devolucion" %>
<%@ page import="Modelos.Cliente" %>
<%@ page import="Modelos.Usuario" %>
<%@ page import="Modelos.DetalleDevolucion" %>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reporte de Devoluciones</title>
    <script>
        function toggleDetalles(id) {
            var detalles = document.getElementById("detalles_" + id);
            if (detalles.style.display === "none") {
                detalles.style.display = "table-row-group";
            } else {
                detalles.style.display = "none";
            }
        }
    </script>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <aside class="col-md-3 p-0">
                <jsp:include page="/resources/sidebar.jsp" />
            </aside>
            <main class="col-md-9">
                <div class="container">
                    <h2 class="mt-4">Reporte de Devoluciones</h2>
                    <form method="post" action="ReporteDevoluciones">
                        <div class="form-group">
                            <label for="fechaInicio">Fecha de Inicio:</label>
                            <input type="date" class="form-control" id="fechaInicio" name="fechaInicio" required>
                        </div>
                        <div class="form-group">
                            <label for="fechaFin">Fecha de Fin:</label>
                            <input type="date" class="form-control" id="fechaFin" name="fechaFin" required>
                        </div>
                        <button type="submit" class="btn btn-primary mt-3">Generar Reporte</button>
                    </form>

                    <%
                        List<Devolucion> devoluciones = (List<Devolucion>) request.getAttribute("devoluciones");
                        if (devoluciones != null && !devoluciones.isEmpty()) {
                    %>
                    <table class="table table-striped mt-4">
                        <thead>
                            <tr>
                                <th>ID Devolución</th>
                                <th>ID Venta</th>
                                <th>Fecha Venta</th>
                                <th>Cliente</th>
                                <th>Usuario</th>
                                <th>Fecha Devolución</th>
                                <th>Monto Pérdida</th>
                                <th>Detalles</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Devolucion devolucion : devoluciones) { %>
                            <tr>
                                <td><%= devolucion.getIdDevolucion() %></td>
                                <td><%= devolucion.getIdVenta() %></td>
                                <td>
                                    <%
                                        // Obtener la fecha de la venta original
                                        Venta venta = VentaDB.obtenerVenta(devolucion.getIdVenta());
                                        if (venta != null) {
                                            out.print(venta.getFechaVenta());
                                        }
                                    %>
                                </td>
                                <td>
                                    <%
                                        Cliente cliente = (Cliente) request.getAttribute("cliente_" + devolucion.getIdDevolucion());
                                        if (cliente != null) {
                                            out.print(cliente.getNombre());
                                        }
                                    %>
                                </td>
                                <td>
                                    <%
                                        Usuario usuarioDevolucion = (Usuario) request.getAttribute("usuario_" + devolucion.getIdDevolucion());
                                        if (usuarioDevolucion != null) {
                                            out.print(usuarioDevolucion.getNombreUsuario());
                                        }
                                    %>
                                </td>
                                <td><%= devolucion.getFechaDevolucion() %></td>
                                <td><%= devolucion.getMontoPerdida() %></td>
                                <td>
                                    <button class="btn btn-info btn-sm" type="button" onclick="toggleDetalles(<%= devolucion.getIdDevolucion() %>)">Ver Detalles</button>
                                </td>
                            </tr>
                            <tr id="detalles_<%= devolucion.getIdDevolucion() %>" style="display: none;">
                                <td colspan="8">
                                    <table class="table table-bordered mt-2">
                                        <thead>
                                            <tr>
                                                <th>ID Computadora</th>
                                                <th>Nombre</th>
                                                <th>Cantidad</th>
                                                <th>Subtotal</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                List<DetalleDevolucion> detallesDevolucion = (List<DetalleDevolucion>) request.getAttribute("detallesDevolucion_" + devolucion.getIdDevolucion());
                                                if (detallesDevolucion != null) {
                                                    for (DetalleDevolucion detalle : detallesDevolucion) {
                                            %>
                                            <tr>
                                                <td><%= detalle.getIdComputadora() %></td>
                                                <td>
                                                    <%
                                                        Computadora computadora = VentaDB.obtenerComputadora(detalle.getIdComputadora());
                                                        if (computadora != null) {
                                                            out.print(computadora.getNombre());
                                                        }
                                                    %>
                                                </td>
                                                <td><%= detalle.getCantidad() %></td>
                                                <td><%= detalle.getSubtotal() %></td>
                                            </tr>
                                            <% 
                                                    }
                                                }
                                            %>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                    <% } else { %>
                    <p class="mt-4">No se encontraron devoluciones en el intervalo de tiempo especificado.</p>
                    <% } %>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
