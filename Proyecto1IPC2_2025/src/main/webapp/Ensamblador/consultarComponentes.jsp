<%@ page import="java.util.List"%>
<%@ page import="Modelos.Componente"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Consultar Componentes</title>
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
                        <h2>Consultar Componentes</h2>

                        <!-- Formulario de búsqueda -->
                        <form action="${pageContext.request.contextPath}/ConsultarComponentesServlet" method="get" class="mb-4">
                            <div class="input-group">
                                <input type="text" name="busqueda" class="form-control" placeholder="Buscar componente..." />
                                <button type="submit" class="btn btn-primary">Buscar</button>
                            </div>
                        </form>

                        <% 
                            List<Componente> componentes = 
                                (List<Componente>) request.getAttribute("componentes");
                            String error = (String) request.getAttribute("error");
                        %>

                        <!-- Mostrar mensaje de error si ocurre -->
                        <% if (error != null) { %>
                            <div class="alert alert-danger">
                                <%= error %>
                            </div>
                        <% } %>

                        <!-- Tabla de resultados -->
                        <% if (componentes != null && !componentes.isEmpty()) { %>
                        <table class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Nombre</th>
                                    <th>Costo</th>
                                    <th>Cantidad Disponible</th>
                                    <th>Estado</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Componente componente : componentes) { %>
                                <tr>
                                    <td><%= componente.getIdComponente() %></td>
                                    <td><%= componente.getNombre() %></td>
                                    <td>Q<%= String.format("%.2f", componente.getCosto()) %></td>
                                    <td><%= componente.getCantidadDisponible() %></td>
                                    <td><%= componente.getEstado() %></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                        <% } else { %>
                        <div class="alert alert-info">
                            No se encontraron componentes.
                        </div>
                        <% } %>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>
