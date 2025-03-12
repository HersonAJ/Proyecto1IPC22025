<%@page import="Modelos.Usuario"%>
<%@ include file="resources.jsp" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
%>
<header>
    <div class="px-3 py-2 text-bg-dark border-bottom">
        <div class="container">
            <div class="d-flex flex-wrap align-items-center justify-content-between">
                <a href="inicio.jsp" class="d-flex align-items-center my-2 my-lg-0 text-white text-decoration-none">
                    <i class="bi bi-bootstrap-fill me-2" width="40" height="32" role="img" aria-label="Bootstrap"></i>
                    La Computadora Feliz
                </a>
                <ul class="nav col-12 col-lg-auto my-2 justify-content-center my-md-0 text-small">
                    <li>
                        <a href="inicio.jsp" class="nav-link text-secondary">
                            <i class="bi bi-house-fill d-block mx-auto mb-1" width="24" height="24"></i>
                            Inicio
                        </a>
                    </li>
                    <li>
                        <a href="perfil.jsp" class="nav-link text-white">
                            <i class="bi bi-person-circle d-block mx-auto mb-1" width="24" height="24"></i>
                            Perfil
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/LogoutServlet" class="nav-link text-white">
                            <i class="bi bi-box-arrow-right d-block mx-auto mb-1" width="24" height="24"></i>
                            Logout
                        </a>
                    </li>
                </ul>
                <div class="text-end text-white">
                    <p class="mb-0">Bienvenido, <%= usuario.getNombreUsuario() %></p>
                    <p class="mb-0">Rol: <%= usuario.getRolNombre() %></p>
                </div>
            </div>
        </div>
    </div>
</header>

