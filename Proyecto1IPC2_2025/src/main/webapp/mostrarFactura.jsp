<%@page import="backendDB.ModelosDB.Vendedor.VendedorComputadoraDB"%>
<%@page import="Modelos.Venta"%>
<%@page import="Modelos.Cliente"%>
<%@page import="Modelos.DetalleVenta"%>
<%@page import="Modelos.ComputadoraEnsamblada"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Factura</title>
    <jsp:include page="/resources/resources.jsp" />
    <style>
        .numero-factura {
            text-align: right;
            font-weight: bold;
            font-size: 1.2em;
        }
        .total-rojo {
            color: red;
            font-weight: bold;
        }
        /* Estilos específicos para impresión */
        @media print {
            body * {
                visibility: hidden; /* Ocultar todo */
            }
            #factura-container, #factura-container * {
                visibility: visible; /* Mostrar solo la factura */
            }
            #factura-container {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
            }
            .btn-imprimir {
                display: none; /* Ocultar el botón de imprimir */
            }
        }
    </style>
</head>
<body>
    <jsp:include page="/resources/header.jsp" />
    <div class="container-fluid">
        <div class="row">
            <aside class="col-md-3 p-0">
                <jsp:include page="/resources/sidebar.jsp" />
            </aside>
            <main class="col-md-9">
                <div id="factura-container" class="container mt-5">
                    <div class="d-flex justify-content-between align-items-center">
                        <h2>Factura</h2>
                        <% Venta venta = (Venta) request.getAttribute("venta"); %>
                        <% if (venta != null) { %>
                            <div class="numero-factura">
                                <p>Número de Factura: <%= venta.getNumeroFactura() %></p>
                            </div>
                        <% } %>
                    </div>
                    <hr>

                    <!-- Información del cliente -->
                    <% Cliente cliente = (Cliente) request.getAttribute("cliente");
                       List<DetalleVenta> detalleVenta = (List<DetalleVenta>) request.getAttribute("detalleVenta");
                       if (cliente != null) { %>
                        <div class="mt-4">
                            <h4>Datos del Cliente</h4>
                            <p><strong>NIT:</strong> <%= cliente.getNit() %></p>
                            <p><strong>Nombre:</strong> <%= cliente.getNombre() %></p>
                            <p><strong>Dirección:</strong> <%= cliente.getDireccion() %></p>
                        </div>
                    <% } %>

                    <!-- Detalles de la venta -->
                    <% if (venta != null) { %>
                        <div class="mt-4">
                            <h4>Detalles de la Venta</h4>
                            <p><strong>Fecha de Venta:</strong> <%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(venta.getFechaVenta()) %></p>
                            <p><strong>Total:</strong> Q<%= String.format("%.2f", venta.getTotalVenta()) %></p>
                        </div>
                    <% } %>

                    <!-- Tabla de artículos comprados -->
                    <div class="mt-4">
                        <h4>Artículos Comprados</h4>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Nombre</th>
                                    <th>Cantidad</th>
                                    <th>Precio Unitario</th>
                                    <th>Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                    if (detalleVenta != null && !detalleVenta.isEmpty()) { 
                                        int contador = 1;
                                        for (DetalleVenta detalle : detalleVenta) { 
                                            // Obtener la computadora ensamblada desde el método VendedorComputadoraDB
                                            ComputadoraEnsamblada computadora = VendedorComputadoraDB.obtenerComputadora(detalle.getIdComputadora());
                                %>
                                    <tr>
                                        <td><%= contador++ %></td>
                                        <td><%= computadora != null ? computadora.getTipoComputadora().getNombre() : "Desconocido" %></td>
                                        <td><%= detalle.getCantidad() %></td>
                                        <td>Q<%= String.format("%.2f", detalle.getSubtotal() / detalle.getCantidad()) %></td>
                                        <td>Q<%= String.format("%.2f", detalle.getSubtotal()) %></td>
                                    </tr>
                                <% 
                                        } 
                                    } else { 
                                %>
                                    <tr>
                                        <td colspan="5" class="text-center">No hay artículos comprados.</td>
                                    </tr>
                                <% } %>
                                <!-- Fila de total en rojo -->
                                <tr>
                                    <td colspan="4" class="text-right total-rojo">Total:</td>
                                    <td class="total-rojo">Q<%= String.format("%.2f", venta != null ? venta.getTotalVenta() : 0) %></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <!-- Botones para imprimir y aceptar -->
                    <div class="mt-5 text-center">
                        <button onclick="window.print()" class="btn btn-primary btn-imprimir">Imprimir / Descargar PDF</button>
                        <form action="buscarCliente.jsp" method="post" class="d-inline" onsubmit="limpiarSesion(event);">
                            <button type="submit" class="btn btn-success">Aceptar</button>
                        </form>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script>
        // Función para limpiar los datos de sesión excepto el usuario logeado
        function limpiarSesion(event) {
            <% 
                // Eliminar los datos de sesión relacionados con la venta
                session.removeAttribute("cliente");
                session.removeAttribute("detalleVenta");
            %>
            console.log("Datos de la sesión relacionados con la venta han sido eliminados.");
        }
    </script>
</body>
</html>
