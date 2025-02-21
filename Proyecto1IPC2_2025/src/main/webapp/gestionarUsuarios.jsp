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
                                List<Usuario> usuarios = UsuarioDB.obtenerUsuarios();
                                for (Usuario u : usuarios) {
                            %>
                            <tr>
                                <td><%= u.getNombreUsuario() %></td>
                                <td><%= u.getRolNombre() %></td>
                                <td>
                                    <button class="btn btn-warning btn-sm">Cambiar Rol</button>
                                    <button class="btn btn-danger btn-sm">Dar de Baja</button>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
