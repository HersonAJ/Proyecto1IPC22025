<%@ page import="java.util.List" %>
<%@ page import="Modelos.Computadora" %>
<%@ page import="Modelos.Componente" %>
<%@ page import="Modelos.EnsamblajePieza" %>
<%@ page import="backendDB.ModelosDB.ComputadoraDB" %>
<%@ page import="backendDB.ModelosDB.ComponenteDB" %>
<%@ page import="backendDB.ModelosDB.EnsamblajePiezaDB" %>
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ensamblar Computadora</title>
    <script>
        function mostrarComponentes() {
            var tipoComputadora = document.getElementById("tipoComputadora").value;
            if (tipoComputadora) {
                document.getElementById("componentesForm").submit();
            }
        }

        function mostrarAlerta(mensaje) {
            alert(mensaje);
        }
    </script>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <aside class="col-md-3 p-0">
                <jsp:include page="/resources/sidebar.jsp" />
            </aside>
            <main class="col-md-9">
                <div class="container">
                    <h2 class="mt-4">Ensamblar Computadora</h2>
                    <form id="componentesForm" action="EnsamblarComputadoraServlet" method="post">
                        <div class="form-group">
                            <label for="tipoComputadora">Tipo de Computadora:</label>
                            <select class="form-control" id="tipoComputadora" name="tipoComputadora" onchange="mostrarComponentes()" required>
                                <option value="">Selecciona un tipo de computadora</option>
                                <% 
                                    List<Computadora> computadoras = ComputadoraDB.obtenerComputadoras();
                                    for (Computadora computadora : computadoras) {
                                %>
                                <option value="<%= computadora.getIdComputadora() %>"><%= computadora.getNombre() %></option>
                                <% } %>
                            </select>
                        </div>
                    </form>
                    
                    <div id="componentesDiv" style="<%= (request.getAttribute("componentes") == null) ? "display:none;" : "display:block;" %>">
                        <h3 class="mt-4">Componentes Necesarios</h3>
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Componente</th>
                                    <th>Cantidad Necesaria</th>
                                    <th>Cantidad Disponible</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                    List<EnsamblajePieza> piezas = (List<EnsamblajePieza>) request.getAttribute("componentes");
                                    if (piezas != null) {
                                        for (EnsamblajePieza pieza : piezas) {
                                            Componente componente = ComponenteDB.obtenerComponentePorId(pieza.getIdComponente());
                                %>
                                <tr>
                                    <td><%= componente.getNombre() %></td>
                                    <td><%= pieza.getCantidad() %></td>
                                    <td><%= componente.getCantidadDisponible() %></td>
                                </tr>
                                <% 
                                        }
                                    } 
                                %>
                            </tbody>
                        </table>
                        <div class="form-group">
                            <label for="fechaEnsamblaje">Fecha de Ensamblaje:</label>
                            <input type="date" class="form-control" id="fechaEnsamblaje" name="fechaEnsamblaje" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Ensamblar Computadora</button>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <%
        String mensaje = (String) request.getAttribute("mensaje");
        if (mensaje != null) {
    %>
    <script>
        mostrarAlerta("<%= mensaje %>");
    </script>
    <% 
        }
    %>
</body>
</html>
