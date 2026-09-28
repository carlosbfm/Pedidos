<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="mtw" uri="http://www.mentaframework.org/tags-mtw/"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Editar Item</title>
<!-- Bootstrap Icons para os símbolos de + e - -->
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
.forms input[type="text"], .forms select {
	width: 100%;
	padding: 8px 10px;
	font-size: 14px;
	border: 1px solid #cccccc;
	border-radius: 4px;
	background-color: #ffffff;
	box-sizing: border-box;
	transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.forms input[type="text"]:focus, .forms select:focus {
	border-color: #0275d8;
	box-shadow: 0 0 0 2px rgba(2, 117, 216, 0.2);
	outline: none;
}

/* Container do Stepper de Quantidade */
.quantidade-stepper {
	display: flex;
	align-items: center;
	justify-content: center;
	gap: 8px;
	width: 100%;
	margin: 4px 0 4px 0;
}

.quantidade-stepper input[type="number"] {
	flex: 1;
	text-align: center;
	padding: 8px 10px;
	width: 85px;
	font-size: 14px;
	border: 1px solid #cccccc;
	border-radius: 4px;
	box-sizing: border-box;
	margin-top: 15px
}

.btn-stepper {
	background-color: #e9ecef;
	border: 1px solid #ced4da;
	color: #495057;
	border-radius: 4px;
	width: 38px;
	height: 38px;
	display: flex;
	align-items: center;
	justify-content: center;
	cursor: pointer;
	font-size: 14px;
	transition: all 0.2s ease;
}

.btn-stepper:hover {
	background-color: #dde2e6;
	color: #212529;
}

.btn-stepper:active {
	background-color: #ced4da;
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

<script defer>
	function ajustarQuantidade(delta) {
		var input = document.getElementById("campoQtd");
		var valorAtual = parseInt(input.value, 10);
		if (isNaN(valorAtual)) {
			valorAtual = 1;
		}
		var novoValor = valorAtual + delta;
		var min = parseInt(input.getAttribute("min"), 10) || 1;
		
		if (novoValor >= min) {
			input.value = novoValor;
		}
	}
</script>
</head>
<body>
	<h2>Editar Item</h2>
	<div class="forms">

		<form action="itens.exibir.mtw" method="GET" style="display: inline;">
			<button type="submit">Voltar</button>
		</form>
		

		<mtw:form action="itens.atualizarItem.mtw" method="POST">

			<mtw:input type="hidden" name="id" />

			<label>Nome do Item:</label>
			<mtw:input type="text" name="nomeItem" size="30" maxlength="30" />

			<label>Preço por unidade do item:</label>
			<mtw:input type="text" name="precoItem" />

			<label for="quantidade">Quantidade:</label>
			<div class="quantidade-stepper">
				<button type="button" class="btn-stepper"
					onclick="ajustarQuantidade(-1)" title="Diminuir">
					<i class="bi bi-dash-lg"></i>
				</button>

				<input type="number" id="campoQtd" name="quantidadeDoItem" min="1"
					step="1"
					value="${not empty item ? item.quantidadeDoItem : quantidadeDoItem}" />

				<button type="button" class="btn-stepper"
					onclick="ajustarQuantidade(1)" title="Aumentar">
					<i class="bi bi-plus-lg"></i>
				</button>
			</div>

			<label for="tipoUnidade">Unidade de Medida:</label>
			<mtw:select name="tipoUnidadeDeMedida" list="listaTipoUnd" />

			<input type="submit" value="Salvar Alterações" />

			<c:if test="${not empty erro}">
				<div class="alert-error">${erro}</div>
			</c:if>

		</mtw:form>

	</div>
</body>
</html>