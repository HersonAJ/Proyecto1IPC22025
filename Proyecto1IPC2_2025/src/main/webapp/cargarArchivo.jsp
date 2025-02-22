
<%@ include file="/resources/resources.jsp" %>
<%@ include file="/resources/header.jsp" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cargar Archivo</title>
    
    <style>
        .log {
            height: 300px;
            overflow-y: scroll;
            background-color: #f8f9fa;
            padding: 10px;
            border: 1px solid #ced4da;
            white-space: pre-wrap;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <aside class="col-md-3 p-0">
                <jsp:include page="/resources/sidebar.jsp" />
            </aside>
            <main class="col-md-9">
                <div class="container mt-5">
                    <h2>Cargar Archivo de Datos</h2>
                    <form action="CargarArchivoServlet" method="post" enctype="multipart/form-data">
                        <div class="form-group">
                            <label for="archivo">Seleccionar archivo:</label>
                            <input type="file" class="form-control-file" id="archivo" name="archivo" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Cargar Archivo</button>
                    </form>

                    <h3 class="mt-5">Log de Procesamiento</h3>
                    <div class="log" id="logProcesamiento">
                        <pre>
                            <%= request.getAttribute("logProcesamiento") != null ? request.getAttribute("logProcesamiento") : "" %>
                            <%= request.getAttribute("erroresProcesamiento") != null ? request.getAttribute("erroresProcesamiento") : "" %>
                        </pre>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script>
        // Scroll hacia abajo para mantener el foco en la última línea
        var log = document.getElementById('logProcesamiento');
        log.scrollTop = log.scrollHeight;
    </script>
</body>
</html>
