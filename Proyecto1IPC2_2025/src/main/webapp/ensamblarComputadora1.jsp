<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="Modelos.TipoComputadora" %>

<%
    // Obtener los datos pasados desde el Servlet
    List<TipoComputadora> tiposComputadoras = (List<TipoComputadora>) request.getAttribute("tiposComputadoras");
    List<String[]> componentes = (List<String[]>) request.getAttribute("componentes");
    String computadoraSeleccionada = (String) request.getAttribute("computadoraSeleccionada");
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ensamblar Computadora</title>
    <%@ include file="/resources/resources.jsp" %> <!-- Recursos (Bootstrap, CSS, JS) -->
    <script>
        // Enviar el formulario para actualizar los componentes según la computadora seleccionada
        function actualizarComponentes() {
            document.getElementById("selectorComputadora").submit();
        }
    </script>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Barra lateral -->
            <aside class="col-md-3 p-0">
                <jsp:include page="/resources/sidebar.jsp" />
            </aside>
            
            <!-- Contenido principal -->
            <main class="col-md-9">
                <div class="container mt-4">
                    <h2 class="mb-4">Ensamblar Computadora</h2>

                    <!-- Mensajes de éxito o error -->
                    <% if (mensaje != null) { %>
                        <div class="alert alert-success">
                            <%= mensaje %>
                        </div>
                    <% } %>
                    <% if (error != null) { %>
                        <div class="alert alert-danger">
                            <%= error %>
                        </div>
                    <% } %>

                    <!-- Selector de computadoras -->
                    <form id="selectorComputadora" action="EnsamblarComputadora2Servlet" method="get">
                        <div class="form-group">
                            <label for="computadora">Selecciona una computadora:</label>
                            <select class="form-control" id="computadora" name="computadora" onchange="actualizarComponentes()">
                                <option value="">-- Selecciona una computadora --</option>
                                <% if (tiposComputadoras != null && !tiposComputadoras.isEmpty()) {
                                    for (TipoComputadora tipo : tiposComputadoras) { %>
                                        <option value="<%= tipo.getNombre() %>" <%= (computadoraSeleccionada != null && computadoraSeleccionada.equals(tipo.getNombre())) ? "selected" : "" %>>
                                            <%= tipo.getNombre() %>
                                        </option>
                                <% } } %>
                            </select>
                        </div>
                    </form>

                    <!-- Formulario para ensamblar la computadora seleccionada -->
                    <% if (computadoraSeleccionada != null) { %>
                    <h4 class="mt-4">Componentes de la Computadora: <%= computadoraSeleccionada %></h4>

                    <!-- Tabla de componentes -->
                    <form action="EnsamblarComputadora2Servlet" method="post">
                        <input type="hidden" name="computadora" value="<%= computadoraSeleccionada %>">
                        <div class="form-group">
                            <label for="fecha">Selecciona la fecha de ensamblaje:</label>
                            <input type="date" class="form-control" id="fecha" name="fecha" required>
                        </div>
                        <table class="table table-striped">
                            <thead class="thead-dark">
                                <tr>
                                    <th>Componente</th>
                                    <th>Cantidad Requerida</th>
                                    <th>Cantidad Disponible</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (componentes != null && !componentes.isEmpty()) {
                                    for (String[] componente : componentes) { %>
                                    <tr>
                                        <td><%= componente[0] %></td>
                                        <td><%= componente[1] %></td>
                                        <td><%= componente[2] %></td>
                                    </tr>
                                <% } 
                                } else { %>
                                <tr>
                                    <td colspan="3" class="text-center">No hay componentes disponibles para esta computadora.</td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                        <button type="submit" class="btn btn-primary mt-3">Ensamblar Computadora</button>
                    </form>
                    <% } %>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
