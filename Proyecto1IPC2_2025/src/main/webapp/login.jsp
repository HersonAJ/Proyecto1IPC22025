
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio de Sesión</title>
        <jsp:include page="/resources/resources.jsp" />
    
</head>
<body>
    <div class="container">
        <h2 class="mt-5">Inicio de Sesión</h2>
        <form action="LoginServlet" method="post" class="mt-3">
            <div class="form-group">
                <label for="username">Nombre de Usuario:</label>
                <input type="text" class="form-control" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Contraseña:</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary">Iniciar Sesión</button>
        </form>
        <% if (request.getAttribute("errorMensaje") != null) { %>
            <div class="alert alert-danger mt-3"><%= request.getAttribute("errorMensaje") %></div>
        <% } %>
    </div>

</body>
</html>
