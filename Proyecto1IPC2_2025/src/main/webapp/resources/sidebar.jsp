<%@ include file="resources.jsp" %>

<!<!doctype html>
<html lang="es">

    <%
        String rol = (String) session.getAttribute("rolNombre");
    %>
    <div class="d-flex">
        <div class="bg-primary text-white border-right" id="sidebar-wrapper" style="min-height: 100vh; width: 250px;">
            <div class="sidebar-heading text-center py-4 primary-text fs-4 fw-bold text-uppercase border-bottom">Panel de Control</div>
            <div class="list-group list-group-flush">


                <% if (rol.equals("Administrador")) { %>
                <!-- Opciones para Administrador -->
                <a href="${pageContext.request.contextPath}/Administrador/registro.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-person-plus-fill me-2"></i>Crear Usuario
                </a>
                <a href="${pageContext.request.contextPath}/Administrador/gestionarUsuarios.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-gear-fill me-2"></i>Gestionar Usuarios
                </a>
                <a href="${pageContext.request.contextPath}/Administrador/reportesAdmin.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-file-earmark-bar-graph-fill me-2"></i>Generar Reportes
                </a>
                <a href="${pageContext.request.contextPath}/GestionComputadorasServlet" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-pc-display-horizontal me-2"></i>Crear Tipo de Computadora
                </a>
                <a href="${pageContext.request.contextPath}/Administrador/cargarArchivo.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-cloud-upload-fill me-2"></i>Cargar Archivo
                </a>



                <% } else if (rol.equals("Ensamblador")) { %>
                <!-- Opciones para Encargado de Ensamblaje -->
                <a href="${pageContext.request.contextPath}/Ensamblador/gestionarComponentes.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-tools me-2"></i>Gestionar Componentes
                </a>
                <a href="${pageContext.request.contextPath}/EnsamblarComputadora2Servlet" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-cpu me-2"></i>Ensamblar Computadoras
                </a>
                <a href="${pageContext.request.contextPath}/ActualizarEstadoComputadoraServlet" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-card-list me-2"></i>Registrar Computadoras Ensambladas
                </a>
                <a href="${pageContext.request.contextPath}/ConsultarComponentesServlet" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-search me-2"></i>Consultar Componentes
                </a>
                <a href="${pageContext.request.contextPath}/ConsultarComputadorasServlet" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-display me-2"></i>Consultar Computadoras Ensambladas
                </a>


                <% } else if (rol.equals("Vendedor")) { %>
                <!-- Opciones para Encargado de Ventas -->
                <a href="${pageContext.request.contextPath}/Vendedor/buscarCliente.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-cart-fill me-2"></i>Registrar Venta
                </a>
                <a href="${pageContext.request.contextPath}/Vendedor/registrarDevolucion.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-arrow-counterclockwise me-2"></i>Registrar Devoluci�n
                </a>
                <a href="${pageContext.request.contextPath}/Vendedor/consultarComprasCliente.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-journal-check me-2"></i>Consultar Compras de Clientes
                </a>
                <a href="${pageContext.request.contextPath}/Vendedor/consultarDevoluciones.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-journal-x me-2"></i>Consultar Devoluciones de Clientes
                </a>
                <a href="${pageContext.request.contextPath}/ConsultarComputadorasEnSalaDeVentaServlet" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-display me-2"></i>Consultar Computadoras Disponibles
                </a>
                <a href="${pageContext.request.contextPath}/Vendedor/mostrarNumeroFactura.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-calendar-day me-2"></i>Consultar Detalles Factura
                </a>
                <a href="${pageContext.request.contextPath}/Vendedor/ventasDelDia.jsp" class="list-group-item list-group-item-action bg-primary text-white">
                    <i class="bi bi-calendar-day me-2"></i>Consultar Ventas del D�a
                </a>
                <% }%>
            </div>
        </div>
        <div id="page-content-wrapper" style="width: calc(100% - 250px);">
            <!-- Aqu� va el contenido de la p�gina seg�n las opciones seleccionadas en la barra lateral -->
        </div>
    </div>
