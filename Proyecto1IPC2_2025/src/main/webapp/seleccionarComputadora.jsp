<%@page import="java.util.List"%>
<%@page import="Modelos.Cliente"%>
<%@page import="Modelos.Computadora"%>
<%@page import="Modelos.DetalleVenta"%>
<%@page import="backendDB.ModelosDB.ComputadoraDB"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Venta - Selección de Computadoras</title>
    <jsp:include page="/resources/resources.jsp" />
</head>
<body>
    <jsp:include page="/resources/header.jsp" />
    <div class="container-fluid">
        <div class="row">
            <aside class="col-md-3 p-0">
                <jsp:include page="/resources/sidebar.jsp" />
            </aside>
            <main class="col-md-9">
                <div class="container">
                    <h2 class="mt-5">Venta - Selección de Computadoras</h2>

                    <!-- Mostrar información del cliente -->
                    <% Cliente cliente = (Cliente) request.getAttribute("cliente");
                       if (cliente != null) { %>
                        <div class="mt-3">
                            <h4>Información del Cliente</h4>
                            <p><strong>NIT:</strong> <%= cliente.getNit() %></p>
                            <p><strong>Nombre:</strong> <%= cliente.getNombre() %></p>
                            <p><strong>Dirección:</strong> <%= cliente.getDireccion() %></p>
                        </div>
                    <% } %>

                    <!-- Mostrar mensajes de error -->
                    <% String error = (String) request.getAttribute("error");
                       if (error != null) { %>
                        <div class="alert alert-danger mt-3" role="alert">
                            <%= error %>
                        </div>
                    <% } %>

                    <!-- Selector de computadoras -->
                    <form action="VentaServlet" method="post" class="mt-4">
                        <input type="hidden" name="action" value="agregarComputadora">
                        <div class="form-group">
                            <label for="computadora">Seleccione una Computadora:</label>
                            <select class="form-control" id="computadora" name="idComputadora" required>
                                <option value="">-- Seleccionar --</option>
                                <% 
                                    List<Computadora> computadoras = (List<Computadora>) request.getAttribute("computadoras");
                                    if (computadoras != null) {
                                        for (Computadora computadora : computadoras) { 
                                %>
                                    <option value="<%= computadora.getIdComputadora() %>">
                                        <%= computadora.getNombre() %> - Q<%= computadora.getPrecioVenta() %>
                                    </option>
                                <% 
                                        } 
                                    } 
                                %>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Agregar a la Venta</button>
                    </form>

                    <!-- Lista de computadoras en la venta -->
                    <% List<DetalleVenta> detalleVenta = (List<DetalleVenta>) request.getAttribute("detalleVenta");
                       if (detalleVenta != null && !detalleVenta.isEmpty()) { %>
                        <div class="mt-5">
                            <h4>Computadoras en la Venta</h4>
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Nombre</th>
                                        <th>Precio</th>
                                        <th>Cantidad</th>
                                        <th>Subtotal</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (DetalleVenta detalle : detalleVenta) { 
                                           Computadora computadora = ComputadoraDB.obtenerComputadora(detalle.getIdComputadora());
                                    %>
                                        <tr>
                                            <td><%= computadora != null ? computadora.getNombre() : "Desconocido" %></td>
                                            <td>Q<%= detalle.getSubtotal() / detalle.getCantidad() %></td>
                                            <td><%= detalle.getCantidad() %></td>
                                            <td>Q<%= detalle.getSubtotal() %></td>
                                            <td>
                                                <form action="EliminarDetalleServlet" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="eliminarComputadora">
                                                    <input type="hidden" name="idDetalleVenta" value="<%= detalle.getIdDetalleVenta() %>">
                                                    <button type="submit" class="btn btn-danger btn-sm">Eliminar</button>
                                                </form>
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    <% } %>

                    <!-- Botón para continuar si hay artículos en la venta -->
                    <% if (detalleVenta != null && !detalleVenta.isEmpty()) { %>
                        <form action="VentaServlet" method="post" class="mt-4">
                            <input type="hidden" name="action" value="continuar">
                            <button type="submit" class="btn btn-success">Continuar</button>
                        </form>
                    <% } %>
                </div>
            </main>
        </div>
    </div>
</body>
</html>




