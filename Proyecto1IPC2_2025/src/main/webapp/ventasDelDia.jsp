<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Ventas del Día</title>
        <jsp:include page="/resources/resources.jsp" />
        <style>
            .table-container {
                margin-top: 20px;
            }
            .table thead th {
                background-color: #f8f9fa;
            }
            .details-header {
                text-align: center;
                margin-top: 20px;
                margin-bottom: 20px;
            }
            .hidden {
                display: none;
            }
            .details-container {
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 5px;
                border: 1px solid #ddd;
                margin-top: 10px;
            }
        </style>
        <script>
            function toggleDetails(id) {
                const detailsRow = document.getElementById("ventaDetalle" + id);
                if (detailsRow.classList.contains("hidden")) {
                    detailsRow.classList.remove("hidden");
                } else {
                    detailsRow.classList.add("hidden");
                }
            }
        </script>
    </head>
    <body>
        <jsp:include page="/resources/header.jsp" />
        <div class="container-fluid">
            <div class="row">
                <aside class="col-md-3 p-0">
                    <jsp:include page="/resources/sidebar.jsp" />
                </aside>
                <main class="col-md-9">
                    <div class="container table-container">
                        <h2 class="details-header">Consultar Ventas del Día</h2>

                        <!-- Formulario para ingresar la fecha -->
                        <form action="ConsultarVentasDelDiaServlet" method="post" class="mb-4">
                            <div class="input-group">
                                <input type="date" name="fechaVenta" class="form-control" required>
                                <button type="submit" class="btn btn-primary">Consultar</button>
                            </div>
                        </form>

                        <!-- Validación para mostrar resultados o mensaje de advertencia -->
                        <%
                            List<Map<String, Object>> ventas = 
                                (List<Map<String, Object>>) request.getAttribute("ventas");
                            if (ventas != null && !ventas.isEmpty()) {
                        %>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>ID Venta</th>
                                    <th>Fecha</th>
                                    <th>Cliente</th>
                                    <th>NIT</th>
                                    <th>Vendedor</th>
                                    <th>Total</th>
                                    <th>Número Factura</th>
                                    <th>Detalles</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Map<String, Object> venta : ventas) { %>
                                <tr>
                                    <td><%= venta.get("idVenta") %></td>
                                    <td><%= venta.get("fechaVenta") %></td>
                                    <td><%= venta.get("nombreCliente") %></td>
                                    <td><%= venta.get("nitCliente") %></td>
                                    <td><%= venta.get("vendedor") %></td>
                                    <td>Q<%= String.format("%.2f", (Double) venta.get("totalVenta")) %></td>
                                    <td><%= venta.get("numeroFactura") %></td>
                                    <td>
                                        <button class="btn btn-info btn-sm" type="button" 
                                                onclick="toggleDetails(<%= venta.get("idVenta") %>)">
                                            Ver Detalles
                                        </button>
                                    </td>
                                </tr>
                                <tr id="ventaDetalle<%= venta.get("idVenta") %>" class="hidden">
                                    <td colspan="8">
                                        <!-- Mostrar los detalles de las computadoras en una tabla diferenciada -->
                                        <div class="details-container">
                                            <h5>Detalles de la Venta</h5>
                                            <table class="table table-striped table-sm">
                                                <thead>
                                                    <tr>
                                                        <th>#</th>
                                                        <th>Nombre de Computadora</th>
                                                        <th>Cantidad</th>
                                                        <th>Subtotal</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <% 
                                                        String detallesComputadoras = (String) venta.get("detallesComputadoras");
                                                        String[] listaDetalles = detallesComputadoras.split(" \\| "); // Separar detalles por cada computadora
                                                        for (int i = 0; i < listaDetalles.length; i++) {
                                                            String detalle = listaDetalles[i];
                                                            String[] partes = detalle.split(" \\("); // Dividir nombre y resto de datos
                                                            String nombreComputadora = partes[0]; // Nombre de la computadora
                                                            String datos = partes[1].replace(")", ""); // Eliminar el cierre del paréntesis
                                                            String[] datosDivididos = datos.split(", "); // Dividir cantidad y subtotal
                                                            String cantidad = datosDivididos[0].replaceAll("\\D+", ""); // Extraer cantidad
                                                            String subtotal = datosDivididos[1].split(": Q")[1]; // Extraer subtotal
                                                    %>
                                                    <tr>
                                                        <td><%= (i + 1) %></td>
                                                        <td><%= nombreComputadora %></td>
                                                        <td><%= cantidad %></td>
                                                        <td>Q<%= subtotal %></td>
                                                    </tr>
                                                    <% } %>
                                                </tbody>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                        <% } else if (ventas != null) { %>
                        <div class="alert alert-warning" role="alert">
                            No se encontraron ventas para la fecha seleccionada.
                        </div>
                        <% } %>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>
