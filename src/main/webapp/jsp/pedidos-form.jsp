<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	padding: 40px 20px;
	min-height: 100vh;
	background-color: #f0f2f5;
	box-sizing: border-box;
	font-family: Arial, Helvetica, sans-serif;
}

.page-container {
	width: 100%;
	max-width: 1200px;
	margin: 0 auto;
}

.page-title {
	text-align: left;
	margin: 0 0 20px 0;
	color: #333333;
	font-size: 24px;
}

.grid-container {
	display: grid;
	grid-template-columns: 1fr 1.4fr;
	gap: 24px;
	width: 100%;
	align-items: start;
}

.forms, .painel {
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

.forms input[type="submit"], button {
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

.forms input[type="submit"]:hover, button:hover {
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

.alert-success {
	color: #155724;
	background-color: #d4edda;
	border: 1px solid #c3e6cb;
	font-weight: bold;
	padding: 8px 12px;
	margin-bottom: 15px;
	border-radius: 4px;
	box-sizing: border-box;
}

.cabecalho-info {
	display: flex;
	justify-content: space-between;
	align-items: center;
	border-bottom: 1px solid #eee;
	padding-bottom: 10px;
	margin-bottom: 15px;
	font-size: 14px;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 10px;
}

table th, table td {
	padding: 10px;
	text-align: left;
	border-bottom: 1px solid #ddd;
	font-size: 14px;
}

table th {
	background-color: #f8f9fa;
	font-weight: 600;
}

.btn-icone-excluir {
	background-color: transparent;
	color: #d9534f;
	padding: 5px;
	margin: 0;
	border: none;
	cursor: pointer;
}

.btn-icone-excluir:hover {
	background-color: transparent;
	color: #c9302c;
}

.empty-state {
	text-align: center;
	padding: 40px 20px;
	color: #888888;
	font-size: 14px;
}
</style>
</head>
<body>

	<div class="page-container">
		
		<h2 class="page-title">Novo Pedido</h2>

		<div class="grid-container">

			<div class="forms">

				<form action="tabela.exibir.mtw" method="GET" style="display: inline;">
					<button type="submit" style="margin-top: 0; margin-bottom: 15px; width: 100%;">Voltar para a Tabela</button>
				</form>

				<mtw:form action="form.criarPedido.mtw" method="POST">

					<label for="clienteId">Cliente Responsável:</label>
					<select name="clienteId" id="clienteId" required>
						<option value="">Selecione um cliente...</option>
						<c:forEach var="cliente" items="${clientes}">
							<option value="${cliente.id}">
								${cliente.nomeCliente} (ID: #${cliente.id})
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

					<input type="submit" value="Salvar Pedido" />

				</mtw:form>

				<mtw:hasError>
					<div class="alert-error">
						<mtw:error field="erro" />
					</div>
				</mtw:hasError>
			</div>

			<div class="painel">

				<h2 style="margin-top: 0;">Resumo do Pedido</h2>

				<c:if test="${not empty mensagem}">
					<div class="alert-success">${mensagem}</div>
				</c:if>

				<c:choose>
					<c:when test="${not empty pedido}">
						<div class="cabecalho-info">
							<div>
								<p style="margin: 0;">
									<strong>Cliente:</strong> ${pedido.cliente.nomeCliente} (ID: #${pedido.cliente.id})
								</p>
							</div>
							<div>
								<p style="margin: 0;">
									<strong>Pagamento:</strong> ${pedido.formaPagamento.formaDePagamento}
								</p>
							</div>
						</div>

						<h3 style="margin-top: 15px; margin-bottom: 5px; font-size: 16px;">Itens Registrados</h3>
						<table>
							<thead>
								<tr>
									<th>Item</th>
									<th>Preço Unitário</th>
									<th>Qtd</th>
									<th>Subtotal</th>
									<th style="width: 35px; text-align: center;"></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pedido.itens}" var="item">
									<tr>
										<td>${item.nomeItem}</td>
										<td>R$ ${item.precoItem}</td>
										<td>${item.quantidadeDoItem}</td>
										<td>R$ ${item.precoItem * item.quantidadeDoItem}</td>
										<td style="text-align: center;">
											<form action="form.excluirItem.mtw" method="POST" style="display: inline; margin: 0;">
												<input type="hidden" name="idPedido" value="${pedido.id}">
												<input type="hidden" name="itemId" value="${item.id}">
												<button type="submit" class="btn-icone-excluir"
													title="Excluir item"
													onclick="return confirm('Deseja retirar este item do pedido?');">
													&times;
												</button>
											</form>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:when>
					<c:otherwise>
						<div class="empty-state">
							<p>Nenhum pedido gerado nesta requisição.</p>
							<small>Preencha os dados à esquerda e clique em "Salvar Pedido" para visualizar os detalhes aqui.</small>
						</div>
					</c:otherwise>
				</c:choose>

			</div>

		</div>
	</div>

</body>
</html>