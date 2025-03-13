<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reporte - Computadoras Menos Vendidas</title>
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
                    <div class="container mt-5">
                        <h2>Reporte - Computadoras Menos Vendidas</h2>

                        <!-- Formulario de rango de fechas -->
                        <form action="${pageContext.request.contextPath}/ReportesAdminComputadoraMenosVendidaServlet" method="post" class="mt-4">
                            <div class="form-group">
                                <label for="fechaInicio">Fecha de Inicio:</label>
                                <input type="date" id="fechaInicio" name="fechaInicio" class="form-control" 
                                       value="<%= request.getAttribute("fechaInicio") != null ? request.getAttribute("fechaInicio") : ""%>">
                            </div>
                            <div class="form-group mt-3">
                                <label for="fechaFin">Fecha de Fin:</label>
                                <input type="date" id="fechaFin" name="fechaFin" class="form-control" 
                                       value="<%= request.getAttribute("fechaFin") != null ? request.getAttribute("fechaFin") : ""%>">
                            </div>
                            <button type="submit" class="btn btn-primary mt-4">Generar Reporte</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/ReportesAdminComputadoraMenosVendidaServlet" method="post">
                            <input type="hidden" name="fechaInicio" value="<%= request.getAttribute("fechaInicio") != null ? request.getAttribute("fechaInicio") : ""%>">
                            <input type="hidden" name="fechaFin" value="<%= request.getAttribute("fechaFin") != null ? request.getAttribute("fechaFin") : ""%>">
                            <input type="hidden" name="export" value="csv">
                            <button type="submit" class="btn btn-success mt-4">Exportar a CSV</button>
                        </form>


                        <!-- Mostrar mensaje de error si existe -->
                        <%
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                        %>
                        <div class="alert alert-danger mt-3" role="alert">
                            <%= error%>
                        </div>
                        <% } else { %>
                        <!-- Mostrar la tabla con los resultados -->
                        <div class="mt-5">
                            <h4>Resultados del Reporte</h4>
                            <table class="table table-bordered table-striped mt-3">
                                <thead>
                                    <tr>
                                        <th>Tipo de Computadora</th>
                                        <th>Total Vendidas</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        List<Map<String, Object>> listaComputadoras = (List<Map<String, Object>>) request.getAttribute("listaComputadoras");
                                        if (listaComputadoras != null && !listaComputadoras.isEmpty()) {
                                            for (Map<String, Object> computadora : listaComputadoras) {
                                    %>
                                    <tr>
                                        <td><%= computadora.get("tipo_computadora")%></td>
                                        <td><%= computadora.get("total_vendidas")%></td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr>
                                        <td colspan="2" class="text-center">No se encontraron resultados para el rango de fechas seleccionado.</td>
                                    </tr>
                                    <%
                                        }
                                    %>
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
