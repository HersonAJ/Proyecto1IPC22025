<%@ page import="java.util.List" %>
<%@ page import="Modelos.Componente" %>
<%@ page import="backendDB.ModelosDB.ComponenteDB" %>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<%
    List<Componente> componentes = null;
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
    try {
        componentes = ComponenteDB.obtenerComponentes();
    } catch (Exception e) {
        error = "No se pudieron cargar los componentes: " + e.getMessage();
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Componentes</title>
    <script>
        function toggleFormularioAgregar() {
            var formulario = document.getElementById("formularioAgregar");
            if (formulario.style.display === "none") {
                formulario.style.display = "block";
            } else {
                formulario.style.display = "none";
            }
        }

        function toggleFormularioEditar(id) {
            var formulario = document.getElementById("formularioEditar_" + id);
            if (formulario.style.display === "none") {
                formulario.style.display = "block";
            } else {
                formulario.style.display = "none";
            }
        }

        function confirmarEliminacion(id) {
            var confirmar = confirm("¿Estás seguro de que quieres eliminar este componente?");
            if (confirmar) {
                window.location.href = "GestionComponentesServlet?action=eliminar&id=" + id;
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
                <div class="container mt-4">
                    <h2>Gestionar Componentes</h2>

                    <!-- Mostrar mensajes de éxito o error -->
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

                    <!-- Botón para mostrar/ocultar formulario de agregar -->
                    <button class="btn btn-primary mb-3" onclick="toggleFormularioAgregar()">Agregar Componente</button>

                    <!-- Formulario para agregar un nuevo componente -->
                    <div id="formularioAgregar" style="display:none;">
                        <form action="GestionComponentesServlet" method="post">
                            <div class="form-group">
                                <label for="nombre">Nombre:</label>
                                <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Ingrese el nombre del componente" required>
                            </div>
                            <div class="form-group">
                                <label for="costo">Costo:</label>
                                <input type="number" step="0.01" class="form-control" id="costo" name="costo" placeholder="Ingrese el costo del componente" required>
                            </div>
                            <div class="form-group">
                                <label for="cantidadDisponible">Cantidad Disponible:</label>
                                <input type="number" class="form-control" id="cantidadDisponible" name="cantidadDisponible" placeholder="Ingrese la cantidad inicial" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Guardar</button>
                        </form>
                    </div>

                    <!-- Tabla de componentes -->
                    <table class="table table-striped table-hover mt-4">
                        <thead class="thead-dark">
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Costo</th>
                                <th>Cantidad Disponible</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (componentes != null && !componentes.isEmpty()) {
                                for (Componente componente : componentes) { %>
                                <tr>
                                    <td><%= componente.getIdComponente() %></td>
                                    <td><%= componente.getNombre() %></td>
                                    <td>Q<%= String.format("%.2f", componente.getCosto()) %></td>
                                    <td><%= componente.getCantidadDisponible() %></td>
                                    <td>
                                        <button class="btn btn-warning btn-sm" onclick="toggleFormularioEditar(<%= componente.getIdComponente() %>)">Editar</button>
                                        <button class="btn btn-danger btn-sm" onclick="confirmarEliminacion(<%= componente.getIdComponente() %>)">Eliminar</button>
                                    </td>
                                </tr>
                                <tr id="formularioEditar_<%= componente.getIdComponente() %>" style="display:none;">
                                    <td colspan="5">
                                        <!-- Formulario para editar un componente existente -->
                                        <h5>Editar Componente: <%= componente.getNombre() %></h5>
                                        <form action="GestionComponentesServlet" method="post">
                                            <input type="hidden" id="idComponenteEditar" name="idComponente" value="<%= componente.getIdComponente() %>">
                                            <div class="form-group">
                                                <label for="nombreEditar">Nombre:</label>
                                                <input type="text" class="form-control" id="nombreEditar" name="nombre" value="<%= componente.getNombre() %>" required>
                                            </div>
                                            <div class="form-group">
                                                <label for="costoEditar">Costo:</label>
                                                <input type="number" step="0.01" class="form-control" id="costoEditar" name="costo" value="<%= componente.getCosto() %>" required>
                                            </div>
                                            <div class="form-group">
                                                <label for="cantidadDisponibleEditar">Cantidad Disponible:</label>
                                                <input type="number" class="form-control" id="cantidadDisponibleEditar" name="cantidadDisponible" value="<%= componente.getCantidadDisponible() %>" required>
                                            </div>
                                            <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                                        </form>
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
