<%@page import="java.util.List"%>
<%@page import="Modelos.ComputadoraEnsamblada"%>
<%@page import="Modelos.TipoComputadora"%>
<%@page import="Modelos.Usuario"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Computadoras Ensambladas</title>
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
        </style>
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
                        <h2 class="details-header">Computadoras Ensambladas</h2>

                        <% 
                            List<ComputadoraEnsamblada> computadorasDisponibles = 
                                (List<ComputadoraEnsamblada>) request.getAttribute("computadorasDisponibles");
                        %>

                        <% if (computadorasDisponibles != null && !computadorasDisponibles.isEmpty()) { %>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Tipo de Computadora</th>
                                    <th>Costo Total</th>
                                    <th>Precio de Venta</th>
                                    <th>Estado</th>
                                    <th>Ensamblador</th>
                                    <th>Fecha Ensamblaje</th>
                                    <th>Componentes</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (ComputadoraEnsamblada computadora : computadorasDisponibles) { %>
                                <tr>
                                    <td><%= computadora.getIdComputadora() %></td>
                                    <td><%= computadora.getTipoComputadora().getNombre() %></td>
                                    <td>Q<%= String.format("%.2f", computadora.getCostoTotal()) %></td>
                                    <td>Q<%= String.format("%.2f", computadora.getTipoComputadora().getPrecioVenta()) %></td>
                                    <td><%= computadora.getEstado() %></td>
                                    <td><%= computadora.getUsuarioEnsamblador().getNombreUsuario() %></td>
                                    <td><%= computadora.getFechaEnsamblaje() %></td>
                                    <td>
                                        <ul>
                                            <% for (String[] componente : computadora.getComponentes()) { %>
                                            <li><%= componente[0] %> (Cantidad: <%= componente[1] %>)</li>
                                            <% } %>
                                        </ul>
                                    </td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                        <% } else { %>
                        <div class="alert alert-warning" role="alert">
                            No hay computadoras ensambladas disponibles.
                        </div>
                        <% } %>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>
