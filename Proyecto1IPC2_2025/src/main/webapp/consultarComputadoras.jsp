<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Computadoras Disponibles</title>
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
        </style>
        <script>
            function toggleDetails(id) {
                const detailsRow = document.getElementById("componentes" + id);
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
                        <h2 class="details-header">Computadoras Disponibles</h2>

                        <%
                            List<Map<String, Object>> computadoras
                                    = (List<Map<String, Object>>) request.getAttribute("computadorasDisponibles");
                        %>

                        <% if (computadoras != null && !computadoras.isEmpty()) { %>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Nombre</th>
                                    <th>Precio de Venta</th>
                                    <th>Estado</th>
                                    <th>Ensamblador</th>
                                    <th>Fecha Ensamblaje</th>
                                    <th>Componentes</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Map<String, Object> computadora : computadoras) {%>
                                <tr>
                                    <td><%= computadora.get("idComputadora") %></td>
                                    <td><%= computadora.get("nombre") %></td>
                                    <td>Q<%= String.format("%.2f", (Double) computadora.get("precioVenta")) %></td>
                                    <td><%= computadora.get("estado") %></td>
                                    <td><%= computadora.get("ensamblador") != null ? computadora.get("ensamblador") : "No registrado" %></td>
                                    <td><%= computadora.get("fechaEnsamblaje") != null ? computadora.get("fechaEnsamblaje").toString() : "No registrada" %></td>
                                    <td>
                                        <button class="btn btn-info btn-sm" type="button" onclick="toggleDetails(<%= computadora.get("idComputadora") %>)">
                                            Ver Componentes
                                        </button>
                                    </td>
                                </tr>
                                <tr class="hidden" id="componentes<%= computadora.get("idComputadora") %>">
                                    <td colspan="7">
                                        <% 
                                            String componentes = (String) computadora.get("componentes");
                                            if (componentes != null && !componentes.isEmpty()) {
                                                String[] listaComponentes = componentes.split(", ");
                                        %>
                                        <table class="table table-sm">
                                            <thead>
                                                <tr>
                                                    <th>Componente</th>
                                                    <th>Cantidad</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% for (String componente : listaComponentes) {
                                                    String[] detalles = componente.split(" \\(");
                                                    String nombre = detalles[0];
                                                    String cantidad = detalles[1].replace(")", "");
                                                %>
                                                <tr>
                                                    <td><%= nombre %></td>
                                                    <td><%= cantidad %></td>
                                                </tr>
                                                <% } %>
                                            </tbody>
                                        </table>
                                        <% } else { %>
                                        <p>Sin componentes asociados</p>
                                        <% } %>
                                    </td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                        <% } else { %>
                        <div class="alert alert-warning" role="alert">
                            No hay computadoras disponibles para la venta.
                        </div>
                        <% } %>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>
