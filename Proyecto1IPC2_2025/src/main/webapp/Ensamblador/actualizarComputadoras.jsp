<%@page import="java.util.ArrayList"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelos.ComputadoraEnsamblada" %>
<%@ page import="Modelos.TipoComputadora" %>
<%@ page import="Modelos.Usuario" %>

<%
    // Obtener los datos pasados desde el servlet
    List<ComputadoraEnsamblada> computadorasEnsambladas = (List<ComputadoraEnsamblada>) request.getAttribute("computadorasEnsambladas");
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");

    // Dividir computadoras por estado
    List<ComputadoraEnsamblada> ensambladas = new ArrayList<>();
    List<ComputadoraEnsamblada> salaDeVenta = new ArrayList<>();

    for (ComputadoraEnsamblada computadora : computadorasEnsambladas) {
        if ("Ensamblada".equals(computadora.getEstado())) {
            ensambladas.add(computadora);
        } else if ("En Sala de Venta".equals(computadora.getEstado())) {
            salaDeVenta.add(computadora);
        }
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Computadoras</title>
    <%@ include file="/resources/resources.jsp" %>
    <%@ include file="/resources/header.jsp" %>
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
                    <h2 class="mb-4">Gestionar Computadoras</h2>

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

                    <!-- Tabla de computadoras ensambladas -->
                    <h4>Computadoras Ensambladas</h4>
                    <table class="table table-striped table-hover">
                        <thead class="thead-dark">
                            <tr>
                                <th>ID</th>
                                <th>Tipo de Computadora</th>
                                <th>Costo Total</th>
                                <th>Fecha Ensamblaje</th>
                                <th>Usuario Ensamblador</th>
                                <th>Estado</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (!ensambladas.isEmpty()) {
                                for (ComputadoraEnsamblada computadora : ensambladas) { %>
                                <tr>
                                    <td><%= computadora.getIdComputadora() %></td>
                                    <td><%= computadora.getTipoComputadora().getNombre() %></td>
                                    <td>Q<%= String.format("%.2f", computadora.getCostoTotal()) %></td>
                                    <td><%= computadora.getFechaEnsamblaje() %></td>
                                    <td><%= computadora.getUsuarioEnsamblador().getNombreUsuario() %></td>
                                    <td><%= computadora.getEstado() %></td>
                                    <td>
                                        <!-- Formulario para mover a sala de ventas -->
                                        <form action="${pageContext.request.contextPath}/ActualizarEstadoComputadoraServlet" method="post" style="display: inline;">
                                            <input type="hidden" name="idComputadora" value="<%= computadora.getIdComputadora() %>">
                                            <button type="submit" class="btn btn-primary btn-sm">Mover a Sala de Ventas</button>
                                        </form>
                                    </td>
                                </tr>
                            <% } 
                            } else { %>
                            <tr>
                                <td colspan="7" class="text-center">No hay computadoras ensambladas disponibles.</td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>

                    <!-- Tabla de computadoras en sala de venta -->
                    <h4>Computadoras en Sala de Venta</h4>
                    <table class="table table-striped table-hover">
                        <thead class="thead-light">
                            <tr>
                                <th>ID</th>
                                <th>Tipo de Computadora</th>
                                <th>Costo Total</th>
                                <th>Fecha Ensamblaje</th>
                                <th>Usuario Ensamblador</th>
                                <th>Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (!salaDeVenta.isEmpty()) {
                                for (ComputadoraEnsamblada computadora : salaDeVenta) { %>
                                <tr>
                                    <td><%= computadora.getIdComputadora() %></td>
                                    <td><%= computadora.getTipoComputadora().getNombre() %></td>
                                    <td>Q<%= String.format("%.2f", computadora.getCostoTotal()) %></td>
                                    <td><%= computadora.getFechaEnsamblaje() %></td>
                                    <td><%= computadora.getUsuarioEnsamblador().getNombreUsuario() %></td>
                                    <td><%= computadora.getEstado() %></td>
                                </tr>
                            <% } 
                            } else { %>
                            <tr>
                                <td colspan="6" class="text-center">No hay computadoras en sala de ventas disponibles.</td>
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
