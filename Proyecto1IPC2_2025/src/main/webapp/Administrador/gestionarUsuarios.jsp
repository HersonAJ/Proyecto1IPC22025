<%@page import="java.util.List"%>
<%@page import="backendDB.ModelosDB.UsuarioDB"%>
<%@page import="Modelos.Usuario"%>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Usuarios</title>

</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <aside class="col-md-3 p-0">
                <jsp:include page="/resources/sidebar.jsp" />
            </aside>
            <main class="col-md-9">
                <div class="container">
                    <h2 class="mt-5">Gestionar Usuarios</h2>
                    
                    <h4>Usuarios Activos</h4>
                    <table class="table table-striped mt-3">
                        <thead>
                            <tr>
                                <th>Nombre de Usuario</th>
                                <th>Rol</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                                String usuarioActivo = (String) session.getAttribute("nombreUsuarioActivo");
                                List<Usuario> usuarios = UsuarioDB.obtenerUsuarios();
                                for (Usuario u : usuarios) {
                                    if (!u.getNombreUsuario().equals(usuarioActivo) && u.getEstado().equals("Activo")) {
                            %>
                            <tr>
                                <td><%= u.getNombreUsuario() %></td>
                                <td><%= u.getRolNombre() %></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/CambiarRolUsuarioServlet" method="post" style="display:inline;">
                                        <input type="hidden" name="idUsuario" value="<%= u.getIdUsuario() %>">
                                        <select name="nuevoRol" class="form-control mb-2">
                                            <option value="2" <%= u.getRolNombre().equals("Ensamblador") ? "selected" : "" %>>Ensamblador</option>
                                            <option value="3" <%= u.getRolNombre().equals("Vendedor") ? "selected" : "" %>>Vendedor</option>
                                            <option value="1" <%= u.getRolNombre().equals("Administrador") ? "selected" : "" %>>Administrador</option>
                                        </select>
                                        <button type="submit" class="btn btn-warning btn-sm">Cambiar Rol</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/DarDeBajaUsuarioServlet" method="post" style="display:inline;">
                                        <input type="hidden" name="idUsuario" value="<%= u.getIdUsuario() %>">
                                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('¿Estás seguro de que quieres dar de baja a este usuario?');">Dar de Baja</button>
                                    </form>
                                </td>
                            </tr>
                            <% 
                                    }
                                }
                            %>
                        </tbody>
                    </table>

                    <h4>Usuarios Inactivos</h4>
                    <table class="table table-striped mt-3">
                        <thead>
                            <tr>
                                <th>Nombre de Usuario</th>
                                <th>Rol</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                                for (Usuario u : usuarios) {
                                    if (u.getEstado().equals("Inactivo")) {
                            %>
                            <tr>
                                <td><%= u.getNombreUsuario() %></td>
                                <td><%= u.getRolNombre() %></td>
                            </tr>
                            <% 
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
