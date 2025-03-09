<%@page import="backendDB.ModelosDB.ComponenteDB"%>
<%@ page import="java.util.List" %>
<%@ page import="Modelos.TipoComputadora" %>
<%@ page import="Modelos.Componente" %>
<%@ page import="backendDB.ModelosDB.TipoComputadoraDB" %>
<%@ page import="backendDB.ModelosDB.EnsamblajePiezaDB" %>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<%
    List<TipoComputadora> tiposComputadoras = null;
    List<String> detallesComponentes = null;
    List<Componente> componentes = null;
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");

    try {
        tiposComputadoras = TipoComputadoraDB.obtenerTiposComputadoras();
        componentes = ComponenteDB.obtenerComponentes();
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
        // Función para mostrar/ocultar el formulario de creación
        function toggleFormularioCrear() {
            const formulario = document.getElementById("formularioCrear");
            formulario.style.display = (formulario.style.display === "none" || formulario.style.display === "") ? "block" : "none";
        }

        // Función para mostrar/ocultar el listado de componentes de un tipo de computadora
        function toggleDetallesComponentes(id) {
            const detalles = document.getElementById("detallesComponentes_" + id);
            detalles.style.display = (detalles.style.display === "none" || detalles.style.display === "") ? "table-row-group" : "none";
        }

        function agregarFilaPieza() {
            // Validar que la última fila tenga una cantidad válida
            const filas = document.querySelectorAll("#tablaPiezasSeleccionadas tr");
            const ultimaFila = filas[filas.length - 1];
            const cantidadInput = ultimaFila.querySelector("input[name='cantidad[]']");
            const cantidad = cantidadInput.value;

            if (!cantidad || cantidad <= 0) {
                alert("Por favor, ingresa una cantidad válida (mayor a 0) antes de agregar otra pieza.");
                return;
            }

            // Agregar nueva fila si la validación pasa
            const tbody = document.getElementById("tablaPiezasSeleccionadas");
            const filaHTML = `
                <tr>
                    <td>
                        <select class="form-control" name="componente[]" required>
                            <option value="" disabled selected>Selecciona una pieza</option>
                            <% if (componentes != null) { 
                                for (Componente componente : componentes) { %>
                                    <option value="<%= componente.getNombre() %>"><%= componente.getNombre() %></option>
                            <% } } %>
                        </select>
                    </td>
                    <td>
                        <input type="number" class="form-control" name="cantidad[]" placeholder="Cantidad requerida" min="1" required>
                    </td>
                    <td>
                        <button type="button" class="btn btn-danger btn-sm" onclick="eliminarFilaPieza(this)">Eliminar</button>
                    </td>
                </tr>
            `;
            tbody.insertAdjacentHTML("beforeend", filaHTML);
        }

        function eliminarFilaPieza(boton) {
            const fila = boton.parentNode.parentNode;
            fila.parentNode.removeChild(fila);
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
                    <h2>Gestionar Tipos de Computadoras</h2>

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

                    <!-- Botón para mostrar/ocultar formulario de creación -->
                    <button class="btn btn-primary mb-3" onclick="toggleFormularioCrear()">Crear Nueva Computadora</button>

                    <!-- Formulario para crear una nueva computadora -->
                    <div id="formularioCrear" style="display:none;">
                        <form action="GestionComputadorasServlet" method="post">
                            <div class="form-group">
                                <label for="nombre">Nombre de la Computadora:</label>
                                <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Ingrese el nombre de la computadora" required>
                            </div>
                            <div class="form-group">
                                <label for="precioVenta">Precio de Venta:</label>
                                <input type="number" step="0.01" class="form-control" id="precioVenta" name="precioVenta" placeholder="Ingrese el precio de venta" required>
                            </div>

                            <!-- Tabla para seleccionar las piezas -->
                            <h5>Seleccionar Piezas para la Computadora</h5>
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Componente</th>
                                        <th>Cantidad</th>
                                        <th>Acción</th>
                                    </tr>
                                </thead>
                                <tbody id="tablaPiezasSeleccionadas">
                                    <tr>
                                        <td>
                                            <select class="form-control" name="componente[]" required>
                                                <option value="" disabled selected>Selecciona una pieza</option>
                                                <% if (componentes != null) { 
                                                    for (Componente componente : componentes) { %>
                                                        <option value="<%= componente.getNombre() %>"><%= componente.getNombre() %></option>
                                                <% } } %>
                                            </select>
                                        </td>
                                        <td>
                                            <input type="number" class="form-control" name="cantidad[]" placeholder="Cantidad requerida" min="1" required>
                                        </td>
                                        <td>
                                            <button type="button" class="btn btn-danger btn-sm" onclick="eliminarFilaPieza(this)">Eliminar</button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            <button type="button" class="btn btn-success" onclick="agregarFilaPieza()">Agregar Pieza</button>

                            <button type="submit" class="btn btn-primary mt-3">Guardar Computadora</button>
                        </form>
                    </div>

                    <!-- Tabla de tipos de computadoras existentes -->
                    <h5>Tipos de Computadoras Registrados</h5>
                    <table class="table table-striped table-hover mt-4">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Precio de Venta</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (tiposComputadoras != null && !tiposComputadoras.isEmpty()) {
                                for (TipoComputadora tipo : tiposComputadoras) { %>
                                <tr>
                                    <td><%= tipo.getIdTipoComputadora() %></td>
                                    <td><%= tipo.getNombre() %></td>
                                    <td>Q<%= String.format("%.2f", tipo.getPrecioVenta()) %></td>
                                    <td>
                                        <button class="btn btn-info btn-sm" onclick="toggleDetallesComponentes(<%= tipo.getIdTipoComputadora() %>)">
                                            Ver Detalles
                                        </button>
                                    </td>
                                </tr>
                                <tbody id="detallesComponentes_<%= tipo.getIdTipoComputadora() %>" style="display:none;">
                                    <% if (detallesComponentes != null) {
                                        for (String detalle : detallesComponentes) {
                                            String[] partes = detalle.split(",");
                                            String computadora = partes[0].split(":")[1].trim();
                                            if (computadora.equals(tipo.getNombre())) {
                                                String componente = partes[1].split(":")[1].trim();
                                                String cantidad = partes[2].split(":")[1].trim();
                                    %>
                                    <tr>
                                        <td colspan="2"><%= componente %></td>
                                        <td><%= cantidad %></td>
                                    </tr>
                                    <%      }
                                        }
                                    } %>
                                </tbody>
                            <% }
                            } else { %>
                            <tr>
                                <td colspan="4" class="text-center">No hay tipos de computadoras disponibles</td>
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
