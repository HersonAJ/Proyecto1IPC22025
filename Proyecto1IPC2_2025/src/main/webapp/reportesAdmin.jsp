<%@ page import="Modelos.Usuario" %>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Opciones de Reportes</title>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <aside class="col-md-3 p-0">
                <jsp:include page="/resources/sidebar.jsp" />
            </aside>
            <main class="col-md-6">
                <div class="container">
                    <h2 class="mt-4">Opciones de Reportes</h2>
                    <p>Seleccione una opción de reporte para generar el informe correspondiente.</p>
                    <div class="list-group">
                        <a href="reporteVentas.jsp" class="list-group-item list-group-item-action">Reporte de Ventas</a>
                        <a href="reporteDevoluciones.jsp" class="list-group-item list-group-item-action">Reporte de Devoluciones</a>
                        <a href="reporteGanancias.jsp" class="list-group-item list-group-item-action">Reporte de Ganancias</a>
                        <a href="reportesAdminMasVentas.jsp" class="list-group-item list-group-item-action">Reporte de Usuario con Más Ventas</a>
                        <a href="reportesAdminVendedorMasGanancia.jsp" class="list-group-item list-group-item-action">Reporte de Usuario con Más Ganancias</a>
                        <a href="reporteComputadoraMasVendida.jsp" class="list-group-item list-group-item-action">Reporte de Computadora Más Vendida</a>
                        <a href="reporteComputadoraMenosVendida.jsp" class="list-group-item list-group-item-action">Reporte de Computadora Menos Vendida</a>
                    </div>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
