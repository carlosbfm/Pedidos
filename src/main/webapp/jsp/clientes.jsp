<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="mtw" uri="http://www.mentaframework.org/tags-mtw/"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Pedidos</title>
</head>
<body>

	<mtw:form action="novo-cliente.cadastro.mtw" method="POST">
		<h2>Cadastro de Cliente</h2>

		<label>Nome do cliente:</label>
		<mtw:input type="text" name="nomeCliente" size="30" maxlength="255" />
		
		<label>Data de Nascimento:</label>
		<input type="date" name="dataNascimento" value="${dataNascimento}" />
		
		<input type="submit" value="Cadastrar" />

		<c:if test="${not empty erro}">
			<div style="color: white; font-weight: bold; background-color: red; padding: 5px; margin-top: 5px;">
				<mtw:out value="erro" />
			</div>
		</c:if>

		<c:if test="${not empty mensagem}">
			<div style="color: white; font-weight: bold; background-color: green; padding: 5px; margin-top: 5px;">
				<mtw:out value="mensagem" />
			</div>
		</c:if>
	</mtw:form>

	<hr/>

	<table border="1" cellpadding="5">
		<thead>
			<tr>
				<th>ID</th>
				<th>Nome</th>
				<th>Data de Nascimento</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${lista}" var="cliente">
				<tr>
					<td>${cliente.id}</td>
					<td>${cliente.nomeCliente}</td>
					<td>${cliente.dataNascimentoFormatada}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</body>
</html>