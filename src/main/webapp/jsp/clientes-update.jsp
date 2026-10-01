<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="mtw" uri="http://www.mentaframework.org/tags-mtw/"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Editar Item</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

<style>
body {
	margin: 0;
	padding: 20px;
	min-height: 100vh;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	background-color: #f0f2f5;
	box-sizing: border-box;
	font-family: Arial, Helvetica, sans-serif;
}

h2 {
	margin-bottom: 16px;
	color: #333333;
}

.forms {
	width: 100%;
	max-width: 450px;
	background-color: #ffffff;
	padding: 24px;
	border: 1px solid #e0e0e0;
	border-radius: 8px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
	box-sizing: border-box;
}

.forms form {
	display: flex;
	flex-direction: column;
	gap: 10px;
	width: 100%;
}

.forms label {
	display: block;
	font-size: 13px;
	font-weight: 600;
	color: #444444;
	margin-top: 14px;
	margin-bottom: 6px;
}

.forms input[type="text"] {
	width: 100%;
	padding: 8px 10px;
	font-size: 14px;
	border: 1px solid #cccccc;
	border-radius: 4px;
	background-color: #ffffff;
	box-sizing: border-box;
	transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.forms input[type="text"]:focus {
	border-color: #0275d8;
	box-shadow: 0 0 0 2px rgba(2, 117, 216, 0.2);
	outline: none;
}

.forms input[type="submit"] {
	margin-top: 14px;
	padding: 10px 16px;
	font-size: 14px;
	font-weight: bold;
	color: #ffffff;
	background-color: #0275d8;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	transition: background-color 0.2s ease;
}

.forms input[type="submit"]:hover {
	background-color: #025aa5;
}

.alert-error {
	color: #fff;
	background-color: #d9534f;
	font-weight: bold;
	padding: 8px 12px;
	margin-top: 10px;
	border-radius: 4px;
	width: 100%;
	box-sizing: border-box;
}

button {
	display: flex flex-direction: left;
	margin-top: 14px;
	padding: 10px 16px;
	font-size: 14px;
	font-weight: bold;
	color: #ffffff;
	background-color: #0275d8;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	transition: background-color 0.2s ease;
}
</style>

</head>
<body>
	<h2>Editar Cliente</h2>
	<div class="forms">

		<form action="clientes.exibir.mtw" method="GET"
			style="display: inline;">
			<button type="submit">Voltar</button>
		</form>


		<mtw:form action="clientes.atualizarCliente.mtw" method="POST">

			<mtw:input type="hidden" name="id" value="${id}" />

			<label>Nome do Cliente:</label>
			<mtw:input type="text" name="nomeCliente" value="${nomeCliente}"
				size="30" maxlength="255" />

			<label>Data Nascimento:</label>
			<mtw:input type="text" name="dataNascimento"
				value="${dataNascimento}" />


			<input type="submit" value="Salvar Alterações" />
			
			<mtw:hasError>
                <div class="alert-error">
                    <mtw:error field="erro" />
                </div>
            </mtw:hasError>

		</mtw:form>

	</div>
</body>
</html>