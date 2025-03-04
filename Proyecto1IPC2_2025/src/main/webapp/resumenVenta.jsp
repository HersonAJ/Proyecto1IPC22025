<%@page import="Modelos.Cliente"%>
<%@page import="Modelos.DetalleVenta"%>
<%@page import="Modelos.Computadora"%>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resumen de la Venta</title>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <aside class="col-md-3 p-0">
                <jsp:include page="/resources/sidebar.jsp" />
            </aside>
            <main class="col-md-9">
                <div class="container">
                    <h2 class="mt-5">Resumen de la Venta</h2>
                    <p>Cliente: ${cliente.nombre}</p>
                    <p>Dirección: ${cliente.direccion}</p>
                    
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
                                Computadora computadoraSeleccionada = (Computadora) session.getAttribute("computadoraSeleccionada");
                                if (computadoraSeleccionada != null) {
                            %>
                                <tr>
                                    <td><%= computadoraSeleccionada.getNombre() %></td>
                                    <td>1</td>
                                    <td><%= computadoraSeleccionada.getPrecioVenta() %></td>
                                </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                    
                    <p>Total: ${totalVenta}</p>
                    <form action="ConfirmarVentaServlet" method="POST">
                        <button type="submit" class="btn btn-success mt-3">Confirmar Venta</button>
                    </form>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
