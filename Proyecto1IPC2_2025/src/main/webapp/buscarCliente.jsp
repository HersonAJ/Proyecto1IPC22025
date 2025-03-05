<%@page import="Modelos.Cliente"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Cliente</title>
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
                    <h2 class="mt-5">Gestión de Cliente</h2>

                    <!-- Mostrar errores, si los hay -->
                    <% String error = (String) request.getAttribute("error");
                       if (error != null) { %>
                        <div class="alert alert-danger" role="alert">
                            <%= error %>
                        </div>
                    <% } %>

                    <!-- Formulario para buscar cliente por NIT -->
                    <form action="ClienteServlet" method="post" class="mt-3">
                        <input type="hidden" name="action" value="buscar">
                        <div class="form-group">
                            <label for="nit">Ingrese el NIT del Cliente:</label>
                            <input type="text" class="form-control" id="nit" name="nit" 
                                   placeholder="Ejemplo: 1234567" 
                                   required>
                        </div>
                        <button type="submit" class="btn btn-primary">Buscar Cliente</button>
                    </form>

                    <!-- Mostrar datos del cliente existente -->
                    <% Cliente cliente = (Cliente) request.getAttribute("cliente");
                       if (cliente != null) { %>
                        <div id="clienteExistente" class="mt-5">
                            <h4>Datos del Cliente Encontrado:</h4>
                            <p><strong>NIT:</strong> <%= cliente.getNit() %></p>
                            <p><strong>Nombre:</strong> <%= cliente.getNombre() %></p>
                            <p><strong>Dirección:</strong> <%= cliente.getDireccion() %></p>
                            <!-- Botón para continuar con la venta -->
                            <form action="VentaServlet" method="post" class="mt-3">
                                <input type="hidden" name="action" value="cargarComputadoras">
                                <input type="hidden" name="nit" value="<%= cliente.getNit() %>">
                                <button type="submit" class="btn btn-success">Continuar con la Venta</button>
                            </form>
                        </div>
                    <% } %>

                    <!-- Formulario para registrar cliente nuevo -->
                    <% String clienteNoEncontrado = (String) request.getAttribute("clienteNoEncontrado");
                       if (clienteNoEncontrado != null) { %>
                        <div id="clienteNuevo" class="mt-5">
                            <h4>Registrar Nuevo Cliente:</h4>
                            <form action="ClienteServlet" method="post">
                                <input type="hidden" name="action" value="registrar">
                                <div class="form-group">
                                    <label for="nitNuevo">NIT:</label>
                                    <input type="text" class="form-control" id="nitNuevo" 
                                           name="nit" value="<%= clienteNoEncontrado %>" readonly>
                                </div>
                                <div class="form-group">
                                    <label for="nombre">Nombre:</label>
                                    <input type="text" class="form-control" id="nombre" 
                                           name="nombre" required>
                                </div>
                                <div class="form-group">
                                    <label for="direccion">Dirección:</label>
                                    <input type="text" class="form-control" id="direccion" 
                                           name="direccion" required>
                                </div>
                                <button type="submit" class="btn btn-success">Registrar Cliente</button>
                            </form>
                        </div>
                    <% } %>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
