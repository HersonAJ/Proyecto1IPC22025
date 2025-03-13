<%@page import="java.util.List"%>
<%@page import="Modelos.Usuario"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reporte de Ganancias por Usuario</title>
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
                        <h2 class="mt-5">Reporte de Ganancias por Usuario</h2>

                        <!-- Mostrar mensaje de error si existe -->
                        <%
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                        %>
                        <div class="alert alert-danger mt-3" role="alert">
                            <%= error%>
                        </div>
                        <% } %>

                        <!-- Mostrar mensaje si no hay resultados -->
                        <%
                            String mensaje = (String) request.getAttribute("mensaje");
                            if (mensaje != null) {
                        %>
                        <div class="alert alert-info mt-3" role="alert">
                            <%= mensaje%>
                        </div>
                        <% }%>

                        <!-- Formulario para seleccionar el rango de fechas -->
                        <form action="${pageContext.request.contextPath}/ReportesAdminGananciasServlet" method="post" class="mt-4">
                            <div class="form-group">
                                <label for="fechaInicio">Fecha de Inicio:</label>
                                <input type="date" id="fechaInicio" name="fechaInicio" class="form-control" 
                                       value="<%= request.getAttribute("fechaInicio") != null ? request.getAttribute("fechaInicio") : ""%>" required>
                            </div>
                            <div class="form-group mt-3">
                                <label for="fechaFin">Fecha de Fin:</label>
                                <input type="date" id="fechaFin" name="fechaFin" class="form-control" 
                                       value="<%= request.getAttribute("fechaFin") != null ? request.getAttribute("fechaFin") : ""%>" required>
                            </div>
                            <button type="submit" class="btn btn-primary mt-4">Generar Reporte</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/ReportesAdminGananciasServlet" method="post">
                            <input type="hidden" name="fechaInicio" value="<%= request.getAttribute("fechaInicio") != null ? request.getAttribute("fechaInicio") : ""%>">
                            <input type="hidden" name="fechaFin" value="<%= request.getAttribute("fechaFin") != null ? request.getAttribute("fechaFin") : ""%>">
                            <input type="hidden" name="export" value="csv">
                            <button type="submit" class="btn btn-success mt-4">Exportar a CSV</button>
                        </form>

                        <!-- Tabla para mostrar los resultados -->
                        <%
                            List<Usuario> reporteUsuarios = (List<Usuario>) request.getAttribute("reporteUsuarios");
                            if (reporteUsuarios != null && !reporteUsuarios.isEmpty()) {
                        %>
                        <div class="mt-5">
                            <h4>Resultados del Reporte</h4>
                            <table class="table table-bordered table-striped mt-3">
                                <thead>
                                    <tr>
                                        <th>Usuario</th>
                                        <th>Rol</th>
                                        <th>Total de Ganancias</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (Usuario usuario : reporteUsuarios) {%>
                                    <tr>
                                        <td><%= usuario.getNombreUsuario()%></td>
                                        <td><%= usuario.getRolNombre()%></td>
                                        <td>Q<%= String.format("%.2f", (double) usuario.getTotalVentas())%></td> <!-- Mostrar ganancias -->
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                        <% }%>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>
