<%@page import="java.util.List"%>
<%@page import="backendDB.ModelosDB.RolDB"%>
<%@page import="Modelos.*"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Usuario</title>
    <jsp:include page="/resources/resources.jsp" />
</head>
<body>
    <jsp:include page="/resources/header.jsp" />
    <div class="container-fluid">
        <div class="row">
            <aside class="col-md-3 p-0">
                <jsp:include page="/resources/sidebar.jsp" />
            </aside>
            <main class="col-md-9">
                <div class="container">
                    <h2 class="mt-5">Registro de Usuario</h2>
                    <form action="${pageContext.request.contextPath}/RegistroServlet" method="post" class="mt-3">
                        <div class="form-group">
                            <label for="username">Nombre de Usuario:</label>
                            <input type="text" class="form-control" id="username" name="username" required>
                        </div>
                        <div class="form-group">
                            <label for="password">Contraseña:</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <div class="form-group">
                            <label for="rol">Rol:</label>
                            <select class="form-control" id="rol" name="rol">
                                <% 
                                    List<Rol> roles = RolDB.obtenerRoles();
                                    for (Rol rol : roles) {
                                %>
                                    <option value="<%= rol.getIdRol() %>"><%= rol.getNombreRol() %></option>
                                <% } %>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Registrar</button>
                    </form>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
