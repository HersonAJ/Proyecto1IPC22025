<%@page import="backendDB.ModelosDB.Vendedor.VendedorConsultaComprasCliente"%>
<%@page import="backendDB.ModelosDB.DetalleVentaDB"%>
<%@page import="backendDB.ModelosDB.UsuarioDB"%>
<%@page import="backendDB.ModelosDB.ClienteDB"%>
<%@page import="Modelos.DetalleVenta"%>
<%@page import="Modelos.ComputadoraEnsamblada"%>
<%@page import="backendDB.ModelosDB.VentaDB"%>
<%@page import="Modelos.Cliente"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="Modelos.Venta"%>
<%@page import="Modelos.Devolucion"%>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reporte de Ganancias</title>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <aside class="col-md-3 p-0">
                <jsp:include page="/resources/sidebar.jsp" />
            </aside>
            <main class="col-md-9">
                <div class="container">
                    <h2 class="mt-4">Reporte de Ganancias</h2>
                    <form method="post" action="ReporteGananciasServlet">
                        <div class="form-group">
                            <label for="fechaInicio">Fecha de Inicio:</label>
                            <input type="date" class="form-control" id="fechaInicio" name="fechaInicio" required>
                        </div>
                        <div class="form-group">
                            <label for="fechaFin">Fecha de Fin:</label>
                            <input type="date" class="form-control" id="fechaFin" name="fechaFin" required>
                        </div>
                        <button type="submit" class="btn btn-primary mt-3">Generar Reporte</button>
                    </form>

                    <%
                        Double totalGanancia = (Double) request.getAttribute("totalGanancia");
                        Double totalPerdida = (Double) request.getAttribute("totalPerdida");
                        Double gananciaNeta = (Double) request.getAttribute("gananciaNeta");
                        List<Venta> ventas = (List<Venta>) request.getAttribute("ventas");
                        List<Devolucion> devoluciones = (List<Devolucion>) request.getAttribute("devoluciones");
                        List<ComputadoraEnsamblada> computadorasVendidas = (List<ComputadoraEnsamblada>) request.getAttribute("computadorasVendidas");

                        // Manejar posibles valores null
                        totalGanancia = totalGanancia != null ? totalGanancia : 0.0;
                        totalPerdida = totalPerdida != null ? totalPerdida : 0.0;
                        gananciaNeta = gananciaNeta != null ? gananciaNeta : 0.0;
                        ventas = ventas != null ? ventas : new ArrayList<>();
                        devoluciones = devoluciones != null ? devoluciones : new ArrayList<>();
                        computadorasVendidas = computadorasVendidas != null ? computadorasVendidas : new ArrayList<>();
                    %>

                    <h3 class="mt-4">Detalles de Ventas</h3>
                    <table class="table table-striped mt-4">
                        <thead>
                            <tr>
                                <th>ID Venta</th>
                                <th>Fecha Venta</th>
                                <th>Cliente</th>
                                <th>Usuario</th>
                                <th>Total Venta</th>
                                <th>Computadoras Vendidas</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Venta venta : ventas) { %>
                            <tr>
                                <td><%= venta.getIdVenta() %></td>
                                <td><%= venta.getFechaVenta() %></td>
                                <td>
                                    <%
                                        Cliente cliente = ClienteDB.obtenerCliente(venta.getIdCliente());
                                        if (cliente != null) {
                                            out.print(cliente.getNombre());
                                        }
                                    %>
                                </td>
                                <td>
                                    <%
                                        Usuario usuarioGanancia = UsuarioDB.obtenerUsuario(venta.getIdUsuario());
                                        if (usuarioGanancia != null) {
                                            out.print(usuarioGanancia.getNombreUsuario());
                                        }
                                    %>
                                </td>
                                <td><%= venta.getTotalVenta() %></td>
                                <td>
                                    <ul>
                                    <%
                                        List<DetalleVenta> detallesVenta = DetalleVentaDB.obtenerDetallesVenta(venta.getIdVenta());
                                        for (DetalleVenta detalle : detallesVenta) {
                                            ComputadoraEnsamblada computadora = VendedorConsultaComprasCliente.obtenerComputadoraEnsamblada(detalle.getIdComputadora());
                                            if (computadora != null) {
                                    %>
                                    <li><%= computadora.getTipoComputadora().getNombre() %> (<%= detalle.getCantidad() %> unidades)</li>
                                    <%
                                            }
                                        }
                                    %>
                                    </ul>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>

                    <h3 class="mt-4">Detalles de Devoluciones</h3>
                    <table class="table table-striped mt-4">
                        <thead>
                            <tr>
                                <th>ID Devolución</th>
                                <th>Fecha Devolución</th>
                                <th>Cliente</th>
                                <th>Usuario</th>
                                <th>Computadoras Devueltas</th>
                                <th>Monto Pérdida</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Devolucion devolucion : devoluciones) { %>
                            <tr>
                                <td><%= devolucion.getIdDevolucion() %></td>
                                <td><%= devolucion.getFechaDevolucion() %></td>
                                <td>
                                    <%
                                        Cliente cliente = ClienteDB.obtenerCliente(devolucion.getIdCliente());
                                        if (cliente != null) {
                                            out.print(cliente.getNombre());
                                        }
                                    %>
                                </td>
                                <td>
                                    <%
                                        Usuario usuarioGanancia = UsuarioDB.obtenerUsuario(devolucion.getIdCliente());
                                        if (usuarioGanancia != null) {
                                            out.print(usuarioGanancia.getNombreUsuario());
                                        }
                                    %>
                                </td>
                                <td>
                                    <%
                                        ComputadoraEnsamblada computadora = VendedorConsultaComprasCliente.obtenerComputadoraEnsamblada(devolucion.getIdComputadora());
                                        if (computadora != null) {
                                            out.print(computadora.getTipoComputadora().getNombre());
                                        }
                                    %>
                                </td>
                                <td><%= devolucion.getMontoPerdida() %></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>

                    <h3 class="mt-4">Resumen Financiero</h3>
                    <table class="table table-bordered mt-4">
                        <tbody>
                            <tr>
                                <th>Total Ganancia</th>
                                <td><%= totalGanancia %></td>
                            </tr>
                            <tr>
                                <th>Total Pérdida</th>
                                <td><%= totalPerdida %></td>
                            </tr>
                            <tr>
                                <th>Ganancia Neta</th>
                                <td><%= gananciaNeta %></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
