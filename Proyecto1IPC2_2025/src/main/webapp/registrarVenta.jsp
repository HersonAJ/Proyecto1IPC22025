<%@page import="Modelos.DetalleVenta"%>
<%@page import="backendDB.ModelosDB.ComputadoraDB"%>
<%@page import="Modelos.Computadora"%>
<%@page import="java.util.List"%>
<%@page import="Modelos.Cliente"%>
<%@page import="Modelos.Computadora"%>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar Venta</title>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <aside class="col-md-3 p-0">
                <jsp:include page="/resources/sidebar.jsp" />
            </aside>
            <main class="col-md-9">
                <div class="container">
                    <h2 class="mt-5">Registrar Venta</h2>
                    
                    <!-- Formulario para ingresar el NIT del cliente -->
                    <form action="ConsultarClienteServlet" method="POST">
                        <div class="form-group">
                            <label for="nit">NIT del Cliente:</label>
                            <input type="text" id="nit" name="nit" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary mt-3">Consultar Cliente</button>
                    </form>

                    <!-- Formulario para registrar la venta (se llenará después de consultar el cliente) -->
                    <form action="RegistrarVentaServlet" method="POST" id="formularioVenta" style="display:none;">
                        <div class="form-group">
                            <label for="nitCliente">NIT del Cliente:</label>
                            <input type="text" id="nitCliente" name="nitCliente" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label for="nombreCliente">Nombre del Cliente:</label>
                            <input type="text" id="nombreCliente" name="nombreCliente" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label for="direccionCliente">Dirección del Cliente:</label>
                            <input type="text" id="direccionCliente" name="direccionCliente" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label for="idComputadora">Seleccionar Computadora:</label>
                            <select id="idComputadora" name="idComputadora" class="form-control">
                                <option value="">Seleccione una computadora</option>
                                <%
                                    try {
                                        List<Computadora> computadoras = ComputadoraDB.obtenerComputadoras();
                                        for (Computadora computadora : computadoras) {
                                %>
                                <option value="<%= computadora.getIdComputadora() %>"><%= computadora.getNombre() %></option>
                                <%
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                %>
                            </select>
                        </div>
                        <button type="submit" name="action" value="add" class="btn btn-primary mt-3">Agregar a la Compra</button>
                        <button type="submit" name="action" value="checkout" class="btn btn-success mt-3">Proceder al Pago</button>
                    </form>

                    <!-- Tabla de computadoras agregadas -->
                    <div id="tablaComputadorasAgregadas">
                        <h3>Computadoras Seleccionadas</h3>
                        <table class="table table-striped mt-3">
                            <thead>
                                <tr>
                                    <th>Nombre de la Computadora</th>
                                    <th>Cantidad</th>
                                    <th>Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    List<DetalleVenta> detallesVenta = (List<DetalleVenta>) session.getAttribute("detallesVenta");
                                    if (detallesVenta != null) {
                                        for (DetalleVenta detalle : detallesVenta) {
                                            Computadora computadora = ComputadoraDB.obtenerComputadora(detalle.getIdComputadora());
                                %>
                                <tr>
                                    <td><%= computadora.getNombre() %></td>
                                    <td><%= detalle.getCantidad() %></td>
                                    <td><%= detalle.getSubtotal() %></td>
                                </tr>
                                <%
                                        }
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </main>
        </div>
    </div>
    <script>
        // Mostrar el formulario de venta si el cliente existe
        <% 
            Cliente cliente = (Cliente) session.getAttribute("cliente");
            if ("true".equals(request.getParameter("clienteExiste"))) {
                if (cliente != null) {
        %>
                    document.getElementById("formularioVenta").style.display = 'block';
                    document.getElementById("nitCliente").value = "<%= cliente.getNit() %>";
                    document.getElementById("nitCliente").readOnly = true;
                    document.getElementById("nombreCliente").value = "<%= cliente.getNombre() %>";
                    document.getElementById("nombreCliente").readOnly = true;
                    document.getElementById("direccionCliente").value = "<%= cliente.getDireccion() %>";
                    document.getElementById("direccionCliente").readOnly = true;
        <% 
                }
            } else if ("false".equals(request.getParameter("clienteExiste"))) { 
        %>
                alert("El cliente no existe. Por favor, ingrese los datos del cliente.");
                document.getElementById("formularioVenta").style.display = 'block';
                document.getElementById("nitCliente").readOnly = false;
                document.getElementById("nombreCliente").readOnly = false;
                document.getElementById("direccionCliente").readOnly = false;
        <% 
            } 
        %>
    </script>
</body>
</html>
