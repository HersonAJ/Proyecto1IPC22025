<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Ventas del Día</title>
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
                    <div class="container mt-4">
                        <h2 class="mb-4 text-center">Consultar Ventas del Día</h2>

                        <!-- Formulario para ingresar la fecha -->
                        <form action="${pageContext.request.contextPath}/ConsultarVentasDelDiaServlet" method="post" class="mb-4">
                            <div class="input-group">
                                <input type="date" name="fechaVenta" class="form-control" required>
                                <button type="submit" class="btn btn-primary">Consultar</button>
                            </div>
                        </form>

                        <!-- Validación para mostrar resultados o mensaje de advertencia -->
                        <%
                            List<Map<String, Object>> ventas = (List<Map<String, Object>>) request.getAttribute("ventas");
                            if (ventas != null && !ventas.isEmpty()) {
                        %>
                        <div class="accordion" id="ventasAccordion">
                            <% for (Map<String, Object> venta : ventas) {%>
                            <div class="accordion-item">
                                <h2 class="accordion-header" id="headingVenta<%= venta.get("idVenta")%>">
                                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseVenta<%= venta.get("idVenta")%>" aria-expanded="false" aria-controls="collapseVenta<%= venta.get("idVenta")%>">
                                        <strong>Venta ID:</strong> <%= venta.get("idVenta")%> | <strong>Cliente:</strong> <%= venta.get("nombreCliente")%> | <strong>Total:</strong> Q<%= String.format("%.2f", (Double) venta.get("totalVenta"))%>
                                    </button>
                                </h2>
                                <div id="collapseVenta<%= venta.get("idVenta")%>" class="accordion-collapse collapse" aria-labelledby="headingVenta<%= venta.get("idVenta")%>" data-bs-parent="#ventasAccordion">
                                    <div class="accordion-body">
                                        <h5>Detalles de la Venta</h5>
                                        <p><strong>Fecha:</strong> <%= venta.get("fechaVenta")%></p>
                                        <p><strong>Cliente:</strong> <%= venta.get("nombreCliente")%></p>
                                        <p><strong>NIT:</strong> <%= venta.get("nitCliente")%></p>
                                        <p><strong>Vendedor:</strong> <%= venta.get("vendedor")%></p>
                                        <p><strong>Número de Factura:</strong> <%= venta.get("numeroFactura")%></p>
                                        <hr>
                                        <h6>Artículos Comprados</h6>
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
                                                    String[] listaDetalles = detallesComputadoras.split(" \\| ");
                                                    for (int i = 0; i < listaDetalles.length; i++) {
                                                        String detalle = listaDetalles[i];
                                                        String[] partes = detalle.split(" \\(");
                                                        String nombreComputadora = partes[0];
                                                        String datos = partes[1].replace(")", "");
                                                        String[] datosDivididos = datos.split(", ");
                                                        String cantidad = datosDivididos[0].replaceAll("\\D+", "");
                                                        String subtotal = datosDivididos[1].split(": Q")[1];
                                                %>
                                                <tr>
                                                    <td><%= (i + 1)%></td>
                                                    <td><%= nombreComputadora%></td>
                                                    <td><%= cantidad%></td>
                                                    <td>Q<%= subtotal%></td>
                                                </tr>
                                                <% } %>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <% } %>
                        </div>
                        <% } else if (ventas != null) { %>
                        <div class="alert alert-warning" role="alert">
                            No se encontraron ventas para la fecha seleccionada.
                        </div>
                        <% }%>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>
