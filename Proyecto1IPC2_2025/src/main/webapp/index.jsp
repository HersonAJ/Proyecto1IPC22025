<%@page import="java.sql.Connection"%>
<%@page import="java.sql.SQLException"%>
<%@page import="backendDB.ConexionDB"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>inicio</title>
    </head>
    <body>
        <h1>prueba de base de datos</h1>
        
        <%
            Connection connection = null;
            try {
            connection = ConexionDB.getConnection();
            if(connection != null && !connection.isClosed()) {
            out.println("<p> Conexion exitosa a la base de datos. </p>");
            } else {
            out.println("<p> No se pudo establecer la conexion. </p>");
            }
            } catch (SQLException e) {
            out.println("<p> Error al conectar a la base de datos: " + e.getMessage() + "</p>");
            }
            %>
        
    </body>
</html>
