<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="d-flex justify-content-center align-items-center vh-100">
    <div class="container">
        <div class="card shadow-lg p-4" style="max-width: 400px; margin: auto;">
            <h2 class="text-center mb-4">Iniciar Sesión</h2>
            
            <!-- Mostrar mensaje de error si hay una variable en la sesión -->
            <% String error = (String) session.getAttribute("error");
               if (error != null) { %>
                <div class="alert alert-danger text-center"><%= error %></div>
                <% session.removeAttribute("error"); %> <!-- Limpiar el error después de mostrarlo -->
            <% } %>
            
            <form action="LoginServlet" method="POST">
                <div class="mb-3">
                    <label for="usuario" class="form-label">Usuario</label>
                    <input type="text" class="form-control" id="usuario" name="usuario" required>
                </div>
                <div class="mb-3">
                    <label for="contraseña" class="form-label">Contraseña</label>
                    <input type="password" class="form-control" id="contraseña" name="contraseña" required>
                </div>
                <button type="submit" class="btn btn-primary w-100">Ingresar</button>
            </form>
        </div>
    </div>
</body>
</html>
