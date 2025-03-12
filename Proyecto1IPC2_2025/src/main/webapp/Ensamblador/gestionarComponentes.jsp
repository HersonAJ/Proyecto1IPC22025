<%@page import="backendDB.ModelosDB.ComponentesDB.ComponenteConsultaDB"%>
<%@ page import="java.util.List"%>
<%@ page import="Modelos.Componente"%>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<%
    List<Componente> componentes = null;
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
    try {
        componentes = ComponenteConsultaDB.obtenerComponentes();
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
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <%= mensaje %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    <% } %>
                    <% if (error != null) { %>
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <%= error %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    <% } %>

                    <!-- Botón para mostrar/ocultar formulario de agregar -->
                    <button class="btn btn-primary mb-3" type="button" data-bs-toggle="collapse" data-bs-target="#formularioAgregar" aria-expanded="false" aria-controls="formularioAgregar">
                        Agregar Componente
                    </button>

                    <!-- Formulario para agregar un nuevo componente -->
                    <div class="collapse" id="formularioAgregar">
                        <form action="${pageContext.request.contextPath}/GestionComponentesServlet" method="post">
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
                                        <!-- Botón para mostrar/ocultar formulario de edición -->
                                        <button class="btn btn-warning btn-sm" type="button" data-bs-toggle="collapse" data-bs-target="#formularioEditar_<%= componente.getIdComponente() %>" aria-expanded="false" aria-controls="formularioEditar_<%= componente.getIdComponente() %>">
                                            Editar
                                        </button>

                                        <!-- Formulario para confirmar eliminación -->
                                        <form action="${pageContext.request.contextPath}/GestionComponentesServlet" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="eliminar">
                                            <input type="hidden" name="id" value="<%= componente.getIdComponente() %>">
                                            <button type="submit" class="btn btn-danger btn-sm">
                                                Eliminar
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                                <!-- Formulario de edición -->
                                <tr class="collapse" id="formularioEditar_<%= componente.getIdComponente() %>">
                                    <td colspan="5">
                                        <h5>Editar Componente: <%= componente.getNombre() %></h5>
                                        <form action="${pageContext.request.contextPath}/GestionComponentesServlet" method="post">
                                            <input type="hidden" name="idComponente" value="<%= componente.getIdComponente() %>">
                                            <div class="form-group">
                                                <label for="nombreEditar">Nombre:</label>
                                                <input type="text" class="form-control" name="nombre" value="<%= componente.getNombre() %>" required>
                                            </div>
                                            <div class="form-group">
                                                <label for="costoEditar">Costo:</label>
                                                <input type="number" step="0.01" class="form-control" name="costo" value="<%= componente.getCosto() %>" required>
                                            </div>
                                            <div class="form-group">
                                                <label for="cantidadDisponibleEditar">Cantidad Disponible:</label>
                                                <input type="number" class="form-control" name="cantidadDisponible" value="<%= componente.getCantidadDisponible() %>" required>
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
