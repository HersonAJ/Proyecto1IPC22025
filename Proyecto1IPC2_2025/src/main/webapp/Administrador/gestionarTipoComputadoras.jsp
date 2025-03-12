<%@page import="backendDB.ModelosDB.ComponentesDB.ComponenteConsultaDB"%>
<%@page import="java.util.List"%>
<%@page import="Modelos.TipoComputadora"%>
<%@page import="Modelos.Componente"%>
<%@page import="backendDB.ModelosDB.TipoComputadoraDB"%>
<%@page import="backendDB.ModelosDB.EnsamblajePiezaDB"%>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<%    List<TipoComputadora> tiposComputadoras = null;
    List<String> detallesComponentes = null;
    List<Componente> componentes = null;
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");

    try {
        tiposComputadoras = TipoComputadoraDB.obtenerTiposComputadoras();
        componentes = ComponenteConsultaDB.obtenerComponentes();
        detallesComponentes = EnsamblajePiezaDB.obtenerComponentesPorTipoComputadora();
    } catch (Exception e) {
        error = "No se pudieron cargar los datos: " + e.getMessage();
    }
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestionar Tipos de Computadoras</title>
        <script>
            // Función para mostrar/ocultar los detalles de componentes
            function toggleDetallesComponentes(id) {
                const detalles = document.getElementById("detallesComponentes_" + id);
                if (detalles.style.display === "none" || detalles.style.display === "") {
                    detalles.style.display = "table-row-group";
                } else {
                    detalles.style.display = "none";
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
                        <h2 class="mb-3">Gestionar Tipos de Computadoras</h2>

                        <!-- Mostrar mensajes de éxito o error -->
                        <% if (mensaje != null) {%>
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <%= mensaje%>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <% } %>
                        <% if (error != null) {%>
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <%= error%>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <% } %>

                        <!-- Formulario para crear un nuevo tipo de computadora -->
                        <div class="card mb-4">
                            <div class="card-header">
                                <strong>Crear Nueva Computadora</strong>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/GestionComputadorasServlet" method="post">
                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <label for="nombre" class="form-label">Nombre de la Computadora:</label>
                                            <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Ingrese el nombre de la computadora" required>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="precioVenta" class="form-label">Precio de Venta:</label>
                                            <input type="number" step="0.01" class="form-control" id="precioVenta" name="precioVenta" placeholder="Ingrese el precio de venta" required>
                                        </div>
                                    </div>
                                    <h5>Seleccionar Piezas para la Computadora</h5>
                                    <table class="table table-striped mt-3">
                                        <thead>
                                            <tr>
                                                <th>Componente</th>
                                                <th>Cantidad</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% if (componentes != null) {
                                                    for (Componente componente : componentes) {%>
                                            <tr>
                                                <td>
                                                    <input type="checkbox" class="form-check-input me-2" name="componente[]" value="<%= componente.getNombre()%>" 
                                                           onchange="document.getElementById('cantidad_<%= componente.getNombre().replace(' ', '_')%>').disabled = !this.checked;">
                                                    <%= componente.getNombre()%>
                                                </td>
                                                <td>
                                                    <input type="number" class="form-control" id="cantidad_<%= componente.getNombre().replace(' ', '_')%>" 
                                                           name="cantidad[]" min="1" placeholder="Cantidad" disabled>
                                                </td>
                                            </tr>
                                            <% }
                                                } %>
                                        </tbody>
                                    </table>

                                    <button type="submit" class="btn btn-primary">Guardar Computadora</button>
                                </form>

                            </div>
                        </div>

                        <!-- Tabla de tipos de computadoras existentes -->
                        <h5>Tipos de Computadoras Registrados</h5>
                        <div class="table-responsive">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Nombre</th>
                                        <th>Precio de Venta</th>
                                        <th>Detalles</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (tiposComputadoras != null && !tiposComputadoras.isEmpty()) {
                                            for (TipoComputadora tipo : tiposComputadoras) {%>
                                    <tr>
                                        <td><%= tipo.getIdTipoComputadora()%></td>
                                        <td><%= tipo.getNombre()%></td>
                                        <td>Q<%= String.format("%.2f", tipo.getPrecioVenta())%></td>
                                        <td>
                                            <!-- Botón para mostrar detalles -->
                                            <button class="btn btn-info btn-sm" onclick="toggleDetallesComponentes(<%= tipo.getIdTipoComputadora()%>)">
                                                Ver Detalles
                                            </button>
                                        </td>
                                    </tr>
                                    <!-- Detalles dinámicos de los componentes -->
                                <tbody id="detallesComponentes_<%= tipo.getIdTipoComputadora()%>" style="display:none;">
                                    <tr>
                                        <th colspan="2">Componente</th>
                                        <th>Cantidad</th>
                                    </tr>
                                    <% if (detallesComponentes != null) {
                                            for (String detalle : detallesComponentes) {
                                                String[] partes = detalle.split(",");
                                                String computadora = partes[0].split(":")[1].trim();
                                                if (computadora.equals(tipo.getNombre())) {
                                                    String componente = partes[1].split(":")[1].trim();
                                                    String cantidad = partes[2].split(":")[1].trim();
                                    %>
                                    <tr>
                                        <td colspan="2"><%= componente%></td>
                                        <td><%= cantidad%></td>
                                    </tr>
                                    <%          }
                                            }
                                        } %>
                                </tbody>
                                <% }
                                } else { %>
                                <tr>
                                    <td colspan="4" class="text-center">No hay tipos de computadoras disponibles</td>
                                </tr>
                                <% }%>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>

