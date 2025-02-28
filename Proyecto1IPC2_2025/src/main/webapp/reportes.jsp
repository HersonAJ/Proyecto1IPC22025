<%@ page import="Modelos.Usuario" %>
<%@ include file="/resources/resources.jsp" %>

<%
    Usuario us = (Usuario) session.getAttribute("usuario");
%>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Opciones de Reportes</title>
</head>
<body>
<%@ include file="/resources/header.jsp" %>

<div class="d-flex">
    <div class="bg-primary text-white border-right" id="sidebar-wrapper" style="min-height: 100vh; width: 250px;">
        <div class="sidebar-heading text-center py-4 primary-text fs-4 fw-bold text-uppercase border-bottom">Panel de Control</div>
        <div class="list-group list-group-flush">
            <% 
                String rol = (String) session.getAttribute("rolNombre");
                if (rol.equals("Administrador")) { 
            %>
            <!-- Opciones para Administrador -->
                <a href="registro.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-person-plus-fill me-2"></i>Crear Usuario
                </a>
                <a href="gestionarUsuarios.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-gear-fill me-2"></i>Gestionar Usuarios
                </a>
                <a href="generarReportes.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-file-earmark-bar-graph-fill me-2"></i>Generar Reportes
                </a>
                <a href="crearTipoComputadora.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-pc-display-horizontal me-2"></i>Crear Tipo de Computadora
                </a>
                <a href="cargarArchivo.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-cloud-upload-fill me-2"></i>Cargar Archivo
                </a>
            <% 
                } else if (rol.equals("Ensamblador")) { 
            %>
                <!-- Opciones para Encargado de Ensamblaje -->
                <a href="gestionarComponentes.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-tools me-2"></i>Gestionar Componentes
                </a>
                <a href="ensamblarComputadoras.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-cpu me-2"></i>Ensamblar Computadoras
                </a>
                <a href="registrarComputadoras.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-card-list me-2"></i>Registrar Computadoras Ensambladas
                </a>
                <a href="consultarComponentes.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-search me-2"></i>Consultar Componentes
                </a>
                <a href="consultarComputadoras.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-display me-2"></i>Consultar Computadoras Ensambladas
                </a>
            <% 
                } else if (rol.equals("Vendedor")) { 
            %>
                <!-- Opciones para Encargado de Ventas -->
                <a href="registrarVenta.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-cart-fill me-2"></i>Registrar Venta
                </a>
                <a href="registrarDevolucion.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-arrow-counterclockwise me-2"></i>Registrar Devoluci�n
                </a>
                <a href="consultarCompras.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-journal-check me-2"></i>Consultar Compras de Clientes
                </a>
                <a href="consultarDevoluciones.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-journal-x me-2"></i>Consultar Devoluciones de Clientes
                </a>
                <a href="consultarDisponibles.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-display me-2"></i>Consultar Computadoras Disponibles
                </a>
                <a href="consultarVentasDia.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-calendar-day me-2"></i>Consultar Ventas del D�a
                </a>
            <% } %>
        </div>
    </div>
    <div id="page-content-wrapper" style="width: calc(100% - 250px);">
        <div class="container-fluid">
            <h1 class="mt-4">Opciones de Reportes</h1>
            <p>Seleccione una opci�n de reporte para generar el informe correspondiente.</p>
            <div class="list-group">
                <a href="reporteVentas.jsp" class="list-group-item list-group-item-action">Reporte de Ventas</a>
                <a href="reporteDevoluciones.jsp" class="list-group-item list-group-item-action">Reporte de Devoluciones</a>
                <a href="reporteGanancias.jsp" class="list-group-item list-group-item-action">Reporte de Ganancias</a>
                <a href="reporteUsuarioMasVentas.jsp" class="list-group-item list-group-item-action">Reporte de Usuario con M�s Ventas</a>
                <a href="reporteUsuarioMasGanancias.jsp" class="list-group-item list-group-item-action">Reporte de Usuario con M�s Ganancias</a>
                <a href="reporteComputadoraMasVendida.jsp" class="list-group-item list-group-item-action">Reporte de Computadora M�s Vendida</a>
                <a href="reporteComputadoraMenosVendida.jsp" class="list-group-item list-group-item-action">Reporte de Computadora Menos Vendida</a>
            </div>
        </div>
    </div>
</div>

<%@ include file="/resources/resources.jsp" %>
</body>
</html>
