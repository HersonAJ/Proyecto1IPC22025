
<%@page import="backendDB.comprobarAdmin"%>

<%
    if (comprobarAdmin.existeAdministrador()) {
        response.sendRedirect("login.jsp");
    } else {
        response.sendRedirect("registro_admin.jsp");
    }
%>
