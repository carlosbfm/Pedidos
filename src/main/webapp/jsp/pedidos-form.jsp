<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="mtw" uri="http://www.mentaframework.org/tags-mtw/"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Novo Pedido</title>
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
	font-size: 13px;
	font-weight: 600;
	color: #444444;
	margin-top: 4px;
}

.forms input[type="text"], .forms input[type="number"], .forms select {
	width: 100%;
	padding: 8px 10px;
	font-size: 14px;
	border: 1px solid #cccccc;
	border-radius: 4px;
	background-color: #ffffff;
	box-sizing: border-box;
	transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.forms input[type="text"]:focus, .forms input[type="number"]:focus,
	.forms select:focus {
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
	width: fit-content;
	max-width: 100%;
	box-sizing: border-box;
}

button {
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

button:hover {
	background-color: #025aa5;
}
</style>
</head>
<body>
	<h2>Novo Pedido</h2>
	<div class="forms">

		<form action="tabela.exibir.mtw" method="GET" style="display: inline;">
			<button type="submit">Voltar</button>
		</form>

		<mtw:form action="form.criarPedido.mtw" method="POST">

			<label for="clienteId">Cliente Responsável:</label>
			<select name="clienteId" id="clienteId" required>
				<option value="">Selecione um cliente...</option>
				<c:forEach var="cliente" items="${clientes}">
					<option value="${cliente.id}">
						${cliente.nomeCliente} (ID: ${cliente.id})
					</option>
				</c:forEach>
			</select>

			<label for="itemId">Item do Pedido:</label>
			<select name="itemId" id="itemId" required>
				<option value="">Selecione um produto...</option>
				<c:forEach var="item" items="${itens}">
					<option value="${item.id}">
						${item.nomeItem} - R$ ${item.precoItem} (Disponível: ${item.quantidadeDoItem} ${item.tipoUnidadeDeMedida})
					</option>
				</c:forEach>
			</select>

			<label for="quantidade">Quantidade:</label>
			<input type="number" id="quantidade" name="quantidade" min="1"
				step="1" value="<mtw:out value="quantidade" />" required />

			<label for="formaPagamento">Forma de Pagamento:</label>
			<select name="formaPagamento" id="formaPagamento" required>
				<option value="">Selecione...</option>
				<c:forEach var="fp" items="${formasPagamento}">
					<option value="${fp.name()}">${fp.name()}</option>
				</c:forEach>
			</select>

			<input type="submit" value="Enviar" />

		</mtw:form>

		<mtw:hasError>
			<div class="alert-error">
				<mtw:error field="erro" />
			</div>
		</mtw:hasError>

	</div>
</body>
</html>