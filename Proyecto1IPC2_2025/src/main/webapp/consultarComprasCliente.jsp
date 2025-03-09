<%@page import="backendDB.ModelosDB.ComprasClienteDB"%>
<%@page import="backendDB.ModelosDB.DetalleVentaDB"%>
<%@page import="backendDB.ModelosDB.ComputadoraDB"%> 
<%@page import="java.util.List"%>
<%@page import="Modelos.Venta"%>
<%@page import="Modelos.Cliente"%>
<%@page import="Modelos.DetalleVenta"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consultar Compras del Cliente</title>
    <jsp:include page="/resources/resources.jsp" />
    <style>
        .details-row {
            display: none; /* Oculta inicialmente las filas de detalles */
            background-color: #f9f9f9;
        }
    </style>
    <script>
        function toggleDetails(rowId) {
            const detailsRow = document.getElementById("details-" + rowId);
            if (detailsRow.style.display === "none") {
                detailsRow.style.display = "table-row";
            } else {
                detailsRow.style.display = "none";
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
                <div class="container mt-5">
                    <h2>Consultar Compras del Cliente</h2>
                    <hr>

                    <!-- Formulario de búsqueda -->
                    <form action="ComprasClientServlet" method="post" class="mt-4">
                        <div class="form-row">
                            <div class="form-group col-md-4">
                                <label for="nit">NIT del Cliente</label>
                                <input type="text" class="form-control" id="nit" name="nit" placeholder="Ingrese el NIT" required>
                            </div>
                            <div class="form-group col-md-3">
                                <label for="fechaInicio">Fecha Inicio (Opcional)</label>
                                <input type="date" class="form-control" id="fechaInicio" name="fechaInicio">
                            </div>
                            <div class="form-group col-md-3">
                                <label for="fechaFin">Fecha Fin (Opcional)</label>
                                <input type="date" class="form-control" id="fechaFin" name="fechaFin">
                            </div>
                            <div class="form-group col-md-2 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary btn-block">Buscar</button>
                            </div>
                        </div>
                    </form>

                    <!-- Mensajes de error o éxito -->
                    <% String mensaje = (String) request.getAttribute("mensaje");
                       if (mensaje != null) { %>
                        <div class="alert alert-info mt-4" role="alert">
                            <%= mensaje %>
                        </div>
                    <% } %>

                    <% String error = (String) request.getAttribute("error");
                       if (error != null) { %>
                        <div class="alert alert-danger mt-4" role="alert">
                            <%= error %>
                        </div>
                    <% } %>

                    <!-- Mostrar resultados de las compras -->
                    <% Cliente cliente = (Cliente) request.getAttribute("cliente");
                       List<Venta> compras = (List<Venta>) request.getAttribute("compras");
                       if (cliente != null) { %>
                        <div class="mt-4">
                            <h4>Datos del Cliente</h4>
                            <p><strong>NIT:</strong> <%= cliente.getNit() %></p>
                            <p><strong>Nombre:</strong> <%= cliente.getNombre() %></p>
                            <p><strong>Dirección:</strong> <%= cliente.getDireccion() %></p>
                        </div>
                    <% } %>

                    <% if (compras != null && !compras.isEmpty()) { %>
                        <div class="mt-4">
                            <h4>Historial de Compras</h4>
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Fecha</th>
                                        <th>Número de Factura</th>
                                        <th>Total</th>
                                        <th>Detalles</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% int contador = 1;
                                       for (Venta compra : compras) { %>
                                        <tr>
                                            <td><%= contador %></td>
                                            <td><%= compra.getFechaVenta() %></td>
                                            <td><%= compra.getNumeroFactura() %></td>
                                            <td>Q<%= String.format("%.2f", compra.getTotalVenta()) %></td>
                                            <td>
                                                <button class="btn btn-info btn-sm" type="button" onclick="toggleDetails('<%= contador %>')">Ver Detalles</button>
                                            </td>
                                        </tr>
                                        <tr id="details-<%= contador %>" class="details-row">
                                            <td colspan="5">
                                                <h5>Detalles de la Venta</h5>
                                                <table class="table">
                                                    <thead>
                                                        <tr>
                                                            <th>Producto</th>
                                                            <th>Cantidad</th>
                                                            <th>Precio Unitario</th>
                                                            <th>Subtotal</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <% 
                                                            List<DetalleVenta> detalles = DetalleVentaDB.obtenerDetallesVenta(compra.getIdVenta());
                                                            double totalVenta = 0;
                                                            if (detalles != null && !detalles.isEmpty()) {
                                                                for (DetalleVenta detalle : detalles) {
                                                                    totalVenta += detalle.getSubtotal();
                                                                    String productoNombre = ComputadoraDB.obtenerNombreComputadora(detalle.getIdComputadora());
                                                        %>
                                                                    <tr>
                                                                        <td><%= productoNombre %></td>
                                                                        <td><%= detalle.getCantidad() %></td>
                                                                        <td>Q<%= String.format("%.2f", detalle.getSubtotal() / detalle.getCantidad()) %></td>
                                                                        <td>Q<%= String.format("%.2f", detalle.getSubtotal()) %></td>
                                                                    </tr>
                                                        <% 
                                                                } 
                                                            } else { 
                                                        %>
                                                                <tr>
                                                                    <td colspan="4">No se encontraron detalles para esta venta.</td>
                                                                </tr>
                                                        <% } %>
                                                    </tbody>
                                                    <tfoot>
                                                        <tr>
                                                            <td colspan="3"><strong>Total de la Venta</strong></td>
                                                            <td>Q<%= String.format("%.2f", totalVenta) %></td>
                                                        </tr>
                                                    </tfoot>
                                                </table>
                                            </td>
                                        </tr>
                                        <% contador++; %>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    <% } else if (cliente != null && (compras == null || compras.isEmpty())) { %>
                        <div class="alert alert-warning mt-4" role="alert">
                            No se encontraron compras para este cliente en el intervalo proporcionado.
                        </div>
                    <% } %>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
