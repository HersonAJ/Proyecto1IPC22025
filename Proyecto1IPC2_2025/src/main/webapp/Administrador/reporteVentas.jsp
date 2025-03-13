<%@page import="java.util.List"%>
<%@page import="Modelos.Venta"%>
<%@page import="Modelos.DetalleVenta"%>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reporte de Ventas</title>
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
                    <h2 class="mt-4">Reporte de Ventas</h2>

                    <!-- Formulario para generar el reporte -->
                    <form method="post" action="${pageContext.request.contextPath}/reporteVentas">
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

                    <!-- Botón para exportar el reporte a CSV -->
                    <form method="post" action="${pageContext.request.contextPath}/reporteVentas">
                        <input type="hidden" name="fechaInicio" value="${param.fechaInicio}">
                        <input type="hidden" name="fechaFin" value="${param.fechaFin}">
                        <input type="hidden" name="export" value="csv">
                        <button type="submit" class="btn btn-success mt-3">Exportar a CSV</button>
                    </form>

                    <!-- Tabla de reporte de ventas -->
                    <%
                        List<Venta> ventas = (List<Venta>) request.getAttribute("ventas");
                        if (ventas != null && !ventas.isEmpty()) {
                    %>
                    <table class="table table-striped mt-4">
                        <thead>
                            <tr>
                                <th>ID Venta</th>
                                <th>Fecha Venta</th>
                                <th>Cliente</th>
                                <th>Vendedor</th>
                                <th>Total Venta</th>
                                <th>Detalles</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Venta venta : ventas) { %>
                            <tr>
                                <td><%= venta.getIdVenta() %></td>
                                <td><%= venta.getFechaVenta() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(venta.getFechaVenta()) : "N/A" %></td>
                                <td><%= venta.getNombreCliente() %></td>
                                <td><%= venta.getNombreVendedor() %></td>
                                <td>Q<%= String.format("%.2f", venta.getTotalVenta()) %></td>
                                <td>
                                    <button class="btn btn-info btn-sm" type="button" onclick="toggleDetalles(<%= venta.getIdVenta() %>)">Ver Detalles</button>
                                </td>
                            </tr>
                            <tr id="detalles_<%= venta.getIdVenta() %>" style="display: none;">
                                <td colspan="6">
                                    <table class="table table-bordered mt-2">
                                        <thead>
                                            <tr>
                                                <th>ID Computadora</th>
                                                <th>Cantidad</th>
                                                <th>Subtotal</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (DetalleVenta detalle : venta.getDetallesVenta()) { %>
                                            <tr>
                                                <td><%= detalle.getIdComputadora() %></td>
                                                <td><%= detalle.getCantidad() %></td>
                                                <td>Q<%= String.format("%.2f", detalle.getSubtotal()) %></td>
                                            </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                    <% } else { %>
                    <p class="mt-4">No se encontraron ventas en el intervalo de tiempo especificado.</p>
                    <% } %>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
