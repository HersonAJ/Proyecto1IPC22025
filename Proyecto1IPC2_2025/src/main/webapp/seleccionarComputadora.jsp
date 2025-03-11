<%@page import="java.util.List"%>
<%@page import="Modelos.Cliente"%>
<%@page import="Modelos.ComputadoraEnsamblada"%>
<%@page import="Modelos.DetalleVenta"%>

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
                                    List<ComputadoraEnsamblada> listaComputadoras = 
                                        (List<ComputadoraEnsamblada>) request.getAttribute("computadoras");
                                    List<DetalleVenta> detalleVenta = 
                                        (List<DetalleVenta>) request.getAttribute("detalleVenta");

                                    if (listaComputadoras != null) {
                                        for (ComputadoraEnsamblada computadora : listaComputadoras) {
                                            boolean yaEnVenta = false;

                                            // Verificar si la computadora ya está en detalleVenta
                                            if (detalleVenta != null) {
                                                for (DetalleVenta detalle : detalleVenta) {
                                                    if (computadora.getIdComputadora() == detalle.getIdComputadora()) {
                                                        yaEnVenta = true;
                                                        break;
                                                    }
                                                }
                                            }

                                            // Mostrar solo las computadoras que no estén ya en venta
                                            if (!yaEnVenta) {
                                %>
                                    <option value="<%= computadora.getIdComputadora() %>">
                                        <%= computadora.getTipoComputadora().getNombre() %> - Q<%= computadora.getTipoComputadora().getPrecioVenta() %>
                                    </option>
                                <% 
                                            }
                                        }
                                    } 
                                %>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Agregar a la Venta</button>
                    </form>

                    <!-- Lista de computadoras en la venta -->
                    <% 
                        double totalVenta = 0; // Variable para calcular el total
                        if (detalleVenta != null && !detalleVenta.isEmpty()) { 
                    %>
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
                                    <% 
                                        for (DetalleVenta detalle : detalleVenta) { 
                                            ComputadoraEnsamblada computadora = null;

                                            // Buscar la computadora correspondiente al idComputadora
                                            if (listaComputadoras != null) {
                                                for (ComputadoraEnsamblada c : listaComputadoras) {
                                                    if (c.getIdComputadora() == detalle.getIdComputadora()) {
                                                        computadora = c;
                                                        break;
                                                    }
                                                }
                                            }

                                            // Sumar al total de la venta
                                            totalVenta += detalle.getSubtotal();
                                    %>
                                        <tr>
                                            <td><%= computadora != null 
                                                    ? computadora.getTipoComputadora().getNombre() 
                                                    : "Desconocido" %></td>
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

                        <!-- Mostrar el total -->
                        <div class="mt-3">
                            <h4>Total: Q<%= String.format("%.2f", totalVenta) %></h4>
                        </div>
                    <% } %>

                    <!-- Botón para continuar si hay artículos en la venta -->
                    <% if (detalleVenta != null && !detalleVenta.isEmpty()) { %>
                    <form action="VentaServlet" method="post" class="mt-4" onsubmit="confirmarVenta(event)">
                        <input type="hidden" name="action" value="confirmarVenta">
                        
                        <!-- Campo para ingresar la fecha de la venta -->
                        <div class="form-group">
                            <label for="fechaVenta">Fecha de la venta:</label>
                            <input type="date" id="fechaVenta" name="fechaVenta" class="form-control" required>
                        </div>

                        <!-- Botón para confirmar la venta -->
                        <button type="submit" class="btn btn-success mt-4">Confirmar Venta</button>
                    </form>
                    <% } %>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
