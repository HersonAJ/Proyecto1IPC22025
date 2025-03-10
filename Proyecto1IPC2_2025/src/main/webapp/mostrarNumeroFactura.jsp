<%@page import="backendDB.ModelosDB.Vendedor.VendedorConsultaComprasCliente"%>
<%@page import="java.util.List"%>
<%@page import="Modelos.Venta"%>
<%@page import="Modelos.Cliente"%>
<%@page import="Modelos.DetalleVenta"%>
<%@page import="backendDB.ModelosDB.ComputadoraDB"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consultar Factura</title>
    <jsp:include page="/resources/resources.jsp" />
    <style>
        .factura-header {
            text-align: center;
            margin-bottom: 20px;
        }
        .factura-container {
            background-color: #f9f9f9;
            border-radius: 5px;
            padding: 15px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .numero-factura {
            text-align: right;
            font-weight: bold;
            font-size: 1.2em;
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
            .btn-imprimir, form {
                display: none; /* Ocultar el botón de imprimir y el formulario */
            }
        }
        .button-group {
            display: flex;
            gap: 10px;
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
                <div id="factura-container" class="container mt-5 factura-container">
                    <div class="factura-header">
                        <h2>Factura</h2>
                    </div>
                    <form action="ConsultarFacturaServlet" method="post" class="mt-4">
                        <div class="form-group">
                            <label for="numeroFactura">Número de Factura</label>
                            <input type="number" class="form-control" id="numeroFactura" name="numeroFactura" placeholder="Ingrese el número de factura" required>
                        </div>
                        <div class="button-group">
                            <button type="submit" class="btn btn-primary">Buscar Factura</button>
                            <button type="button" onclick="window.print()" class="btn btn-success btn-imprimir">Imprimir / Descargar PDF</button>
                        </div>
                    </form>
                    <hr>
                    <% 
                        String error = (String) request.getAttribute("error");
                        Venta venta = (Venta) request.getAttribute("venta");
                        Cliente cliente = (Cliente) request.getAttribute("cliente");
                        List<DetalleVenta> detalleVenta = (List<DetalleVenta>) request.getAttribute("detalleVenta");
                    %>
                    <% if (error != null && (venta == null && cliente == null)) { %>
                        <div class="alert alert-warning mt-4">
                            <%= error %>
                        </div>
                    <% } %>
                    <% if (venta != null && cliente != null) { %>
                        <div class="numero-factura">
                            <p>Número de Factura: <%= venta.getNumeroFactura() %></p>
                        </div>
                        <div class="mt-4">
                            <h4>Datos del Cliente</h4>
                            <p><strong>NIT:</strong> <%= cliente.getNit() %></p>
                            <p><strong>Nombre:</strong> <%= cliente.getNombre() %></p>
                            <p><strong>Dirección:</strong> <%= cliente.getDireccion() %></p>
                        </div>
                        <div class="mt-4">
                            <h4>Detalles de la Venta</h4>
                            <p><strong>Fecha de Venta:</strong> <%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(venta.getFechaVenta()) %></p>
                            <p><strong>Total de la Venta:</strong> Q<%= String.format("%.2f", venta.getTotalVenta()) %></p>
                            <p><strong>Vendedor:</strong> <%= request.getAttribute("vendedor") %></p>
                        </div>
                        <div class="mt-4">
                            <h4>Artículos Comprados</h4>
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Producto</th>
                                        <th>Cantidad</th>
                                        <th>Precio Unitario</th>
                                        <th>Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% int contador = 1;
                                       double total = 0;
                                       for (DetalleVenta detalle : detalleVenta) { 
                                           total += detalle.getSubtotal();
                                    %>
                                        <tr>
                                            <td><%= contador++ %></td>
                                            <td><%= VendedorConsultaComprasCliente.obtenerNombreComputadoraEnsamblada(detalle.getIdComputadora()) %></td>

                                            <td><%= detalle.getCantidad() %></td>
                                            <td>Q<%= String.format("%.2f", detalle.getSubtotal() / detalle.getCantidad()) %></td>
                                            <td>Q<%= String.format("%.2f", detalle.getSubtotal()) %></td>
                                        </tr>
                                    <% } %>
                                    <tr>
                                        <td colspan="4" class="text-right"><strong>Total:</strong></td>
                                        <td>Q<%= String.format("%.2f", total) %></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    <% } %>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
