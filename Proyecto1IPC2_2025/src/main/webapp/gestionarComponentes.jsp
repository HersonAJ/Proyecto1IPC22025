<%@ page import="java.util.List" %>
<%@ page import="Modelos.Componente" %>
<%@ page import="backendDB.ModelosDB.ComponenteDB" %>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<%
    List<Componente> componentes = ComponenteDB.obtenerComponentes();
    request.setAttribute("componentes", componentes);
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Componentes</title>
    <script>
        function toggleFormulario() {
            var formulario = document.getElementById("formularioAgregar");
            if (formulario.style.display === "none") {
                formulario.style.display = "block";
            } else {
                formulario.style.display = "none";
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
                    <h2 class="mt-4">Gestionar Componentes</h2>
                    <button class="btn btn-primary mb-3" onclick="toggleFormulario()">Agregar Componente</button>
                    <div id="formularioAgregar" style="display:none;">
                        <form action="GestionComponentesServlet" method="post">
                            <div class="form-group">
                                <label for="nombre">Nombre:</label>
                                <input type="text" class="form-control" id="nombre" name="nombre" required>
                            </div>
                            <div class="form-group">
                                <label for="costo">Costo:</label>
                                <input type="number" step="0.01" class="form-control" id="costo" name="costo" required>
                            </div>
                            <div class="form-group">
                                <label for="cantidadDisponible">Cantidad Disponible:</label>
                                <input type="number" class="form-control" id="cantidadDisponible" name="cantidadDisponible" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Guardar</button>
                        </form>
                    </div>
                    <table class="table table-striped mt-4">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Costo</th>
                                <th>Cantidad Disponible</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (componentes != null) {
                                    for (Componente componente : componentes) {
                            %>
                            <tr>
                                <td><%= componente.getIdComponente() %></td>
                                <td><%= componente.getNombre() %></td>
                                <td><%= componente.getCosto() %></td>
                                <td><%= componente.getCantidadDisponible() %></td>
                                <td>
                                    <a href="editarComponente.jsp?id=<%= componente.getIdComponente() %>" class="btn btn-warning">Editar</a>
                                    <a href="eliminarComponente?id=<%= componente.getIdComponente() %>" class="btn btn-danger">Eliminar</a>
                                </td>
                            </tr>
                            <% } 
                               } else { %>
                            <tr>
                                <td colspan="5" class="text-center">No hay componentes disponibles</td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
