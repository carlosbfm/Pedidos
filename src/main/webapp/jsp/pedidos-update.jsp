<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="mtw" uri="http://www.mentaframework.org/tags-mtw/"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Detalhes do Pedido #${pedido.id}</title>
<style>
body {
	margin: 0;
	padding: 20px;
	min-height: 100vh;
	display: flex;
	flex-direction: column;
	align-items: center;
	background-color: #f0f2f5;
	box-sizing: border-box;
	font-family: Arial, Helvetica, sans-serif;
}

h2, h3 {
	margin-top: 0;
	margin-bottom: 14px;
	color: #333333;
}

.painel {
	width: 100%;
	max-width: 800px;
	background-color: #ffffff;
	padding: 24px;
	border: 1px solid #e0e0e0;
	border-radius: 8px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
	box-sizing: border-box;
}

.cabecalho-info {
	display: flex;
	justify-content: space-between;
	background-color: #f8f9fa;
	border: 1px solid #e9ecef;
	border-radius: 6px;
	padding: 14px 18px;
	margin-bottom: 20px;
	font-size: 14px;
}

.cabecalho-info p {
	margin: 4px 0;
	color: #444444;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 10px;
	margin-bottom: 20px;
}

th, td {
	border: 1px solid #ccc;
	padding: 8px;
	text-align: left;
	font-size: 14px;
}

th {
	background-color: #f2f2f2;
}

.box-conferencia {
	background-color: #ffffff;
	border: 2px dashed #0275d8;
	border-radius: 6px;
	padding: 20px;
	margin-top: 20px;
}

.box-conferencia form {
	display: flex;
	flex-direction: column;
	gap: 12px;
	width: 100%;
}

.linha-conferencia {
	display: flex;
	gap: 15px;
}

.coluna-campo {
	flex: 1;
	display: flex;
	flex-direction: column;
}

.coluna-campo label {
	font-size: 13px;
	font-weight: 600;
	color: #444444;
	margin-bottom: 4px;
}

.coluna-campo input {
	width: 100%;
	padding: 8px 10px;
	font-size: 14px;
	font-weight: bold;
	border: 1px solid #cccccc;
	border-radius: 4px;
	background-color: #e9ecef;
	color: #495057;
	box-sizing: border-box;
	cursor: not-allowed;
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

.btn-cancelar-pedido {
	background-color: #d9534f;
}

.btn-cancelar-pedido:hover {
	background-color: #c9302c;
}

.btn-icone-excluir {
	background: transparent;
	border: 1px solid transparent;
	color: #d9534f;
	cursor: pointer;
	padding: 6px;
	margin: 0;
	border-radius: 4px;
	display: inline-flex;
	align-items: center;
	justify-content: center;
	transition: all 0.2s ease-in-out;
}

.btn-icone-excluir:hover {
	background-color: #fdf2f2;
	border-color: #d9534f;
	color: #c9302c;
}

.btn-icone-excluir:active {
	background-color: #f8d7da;
	transform: scale(0.95);
}

.alert-error {
	color: #fff;
	background-color: #d9534f;
	font-weight: bold;
	padding: 8px 12px;
	margin-bottom: 15px;
	border-radius: 4px;
	width: fit-content;
	max-width: 100%;
	box-sizing: border-box;
}

.status-badge {
	font-weight: bold;
	padding: 2px 6px;
	border-radius: 3px;
	background-color: #0275d8;
	color: #ffffff;
	font-size: 12px;
}
</style>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>

	<div class="painel">

		<form action="tabela.exibir.mtw" method="GET" style="display: inline;">
			<button type="submit">Voltar</button>
		</form>

		<h2 style="margin-top: 15px;" name="id">Conferência do Pedido
			#${idConferencia}</h2>

		<mtw:hasError>
			<div class="alert-error">
				<mtw:error field="erro" />
			</div>
		</mtw:hasError>

		<div class="cabecalho-info">
			<div>
				<p>
					<strong>Cliente:</strong> ${pedido.cliente.nomeCliente} (ID:
					#${pedido.cliente.id})
				</p>
				<p>
					<strong>Emissão:</strong> ${pedido.dataHoraEmissaoFormatada}
				</p>
			</div>
			<div style="text-align: right;">
				<p>
					<strong>Status Atual:</strong> <span class="status-badge">${pedido.status.descricao}</span>
				</p>
				<p>
					<strong>Pagamento Registrado:</strong>
					${pedido.formaPagamento.formaDePagamento}
				</p>
			</div>
		</div>

		<h3>Itens do Pedido</h3>
		<table>
			<thead>
				<tr>
					<th>Item</th>
					<th>Preço Histórico</th>
					<th>Quantidade</th>
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
						<td style="text-align: center;"><c:if
								test="${pedido.status.name() != 'CONCLUIDO' && pedido.status.name() != 'CANCELADO'}">
								<form action="form.excluirItem.mtw" method="POST"
									style="display: inline; margin: 0;">
									<input type="hidden" name="idPedido" value="${pedido.id}">
									<input type="hidden" name="itemId" value="${item.id}">
									<button type="submit" class="btn-icone-excluir"
										title="Excluir item e devolver ao estoque"
										onclick="return confirm('Deseja retirar este item do pedido? O estoque será estornado.');">
										<i class="bi bi-trash"></i>
									</button>
								</form>
							</c:if></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<c:if
			test="${pedido.status.name() != 'CONCLUIDO' && pedido.status.name() != 'CANCELADO'}">
			<div class="box-conferencia">
				<h3>Conferência e Fechamento</h3>

				<form action="form.concluir.mtw" method="POST">
					<input type="hidden" name="idPedido" value="${pedido.id}">

					<div class="linha-conferencia">
						<div class="coluna-campo">
							<label>Quantidade de Itens:</label> <input type="number"
								name="quantidadeConferida" value="${quantidadeTotal}" readonly />
						</div>

						<div class="coluna-campo">
							<label>Valor Total (R$):</label> <input type="text"
								name="valorConferido" value="${valorTotal}" readonly />
						</div>

						<div class="coluna-campo">
							<label>Forma de Pagamento:</label> <input type="text"
								name="formaPagamentoConferida"
								value="${pedido.formaPagamento.formaDePagamento}" readonly />
						</div>
					</div>
					<div style="display: flex; gap: 10px;">
						<button type="submit">Concluir Pedido</button>
					</div>

				</form>
				<div style="display: flex; gap: 10px;">


					<form action="form.cancelar.mtw?idPedido=${pedido.id}"
						method="POST" style="display: inline; margin: 0;">

						<button type="submit" class="btn-cancelar-pedido"
							onclick="return confirm('Atenção: Deseja cancelar o pedido completo? Todos os itens voltarão ao estoque.');">
							Cancelar Pedido</button>
					</form>
				</div>
			</div>
		</c:if>

		
	</div>

</body>
</html>