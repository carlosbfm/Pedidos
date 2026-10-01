<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mtw" uri="http://www.mentaframework.org/tags-mtw/"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style>
table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 15px;
}

th, td {
	border: 1px solid #ccc;
	padding: 8px;
	text-align: left;
}

th {
	background-color: #f2f2f2;
}

.paginacao {
	margin-top: 15px;
	font-family: Arial, sans-serif;
	display: flex;
	align-items: center;
	gap: 6px;
}

.paginacao a {
	padding: 4px 8px;
	text-decoration: none;
	border: 1px solid #ccc;
	color: #0275d8;
	border-radius: 3px;
}

.paginacao a:hover {
	background-color: #f0f0f0;
}

.paginacao .atual {
	padding: 4px 8px;
	font-weight: bold;
	background-color: #0275d8;
	color: #fff;
	border: 1px solid #0275d8;
	border-radius: 3px;
}

.paginacao .desabilitado {
	padding: 4px 8px;
	border: 1px solid #e0e0e0;
	color: #a0a0a0;
	background-color: #f9f9f9;
	border-radius: 3px;
	cursor: not-allowed;
	pointer-events: none;
	user-select: none;
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

.btn-icone-excluir {
	background: transparent;
	border: 1px solid transparent;
	color: #d9534f;
	cursor: pointer;
	padding: 6px;
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
	margin-top: 10px;
	border-radius: 4px;
	width: fit-content;
	max-width: 100%;
	box-sizing: border-box;
}
</style>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

<title>Pedidos</title>
</head>
<body>


	<form action="clientes.cadastro.mtw" method="GET"
		style="display: inline;">
		<button type="submit">NOVO CLIENTE +</button>
	</form>

	<table>
		<thead>
			<tr>
				<th></th>
				<th>ID</th>
				<th>Nome</th>
				<th>Data de Nascimento</th>
				<th></th>
			</tr>
		</thead>
		<tbody>

			<c:forEach items="${lista}" var="cliente">
				<tr>
					<td><a href="clientes.exibirCliente.mtw?id=${cliente.id}"
						font-weight: bold;" class="btn-icone-editar"
						title="Editar Cliente"> <i class="bi bi-pencil-square"></i>
					</a></td>
					<td>${cliente.id}</td>
					<td>${cliente.nomeCliente}</td>
					<td>${cliente.dataNascimentoFormatada}</td>
					<td><a
						href="clientes.excluir.mtw?id=${cliente.id}&page=${page}"
						onclick="return confirm('Atenção: Esta ação não pode ser desfeita. Deseja mesmo excluir este registro?');"
						style="color: red; font-weight: bold;" class="btn-icone-excluir"
						title="Excluir item"> <i class="bi bi-trash"></i>
					</a></td>

				</tr>
			</c:forEach>


			<c:choose>
				<c:when test="${not empty erro}">
					<tr>
						<td colspan="5" >
							<div class="alert-error" >
								<c:out value="${erro}" />
							</div>
						</td>
					</tr>
				</c:when>

				<c:when test="${empty lista}">
					<tr>
						<td colspan="5" style="text-align: center;">Nenhum item
							cadastrado até o momento.</td>
					</tr>
				</c:when>
			</c:choose>

		</tbody>
	</table>

	<div class="paginacao">
		<c:choose>
			<c:when test="${paginaAtual > 1}">
				<a href="clientes.exibir.mtw?page=${paginaAtual - 1}">&laquo;
					Anterior</a>
			</c:when>
			<c:otherwise>
				<span class="desabilitado">&laquo; Anterior</span>
			</c:otherwise>
		</c:choose>

		<c:forEach begin="1" end="${totalPaginas}" var="p">
			<c:choose>
				<c:when test="${p == paginaAtual}">
					<span class="atual">${p}</span>
				</c:when>
				<c:otherwise>
					<a href="clientes.exibir.mtw?page=${p}">${p}</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>

		<c:choose>
			<c:when test="${paginaAtual < totalPaginas}">
				<a href="clientes.exibir.mtw?page=${paginaAtual + 1}">Próxima
					&raquo;</a>
			</c:when>
			<c:otherwise>
				<span class="desabilitado">Próxima &raquo;</span>
			</c:otherwise>
		</c:choose>
	</div>

</body>
</html>