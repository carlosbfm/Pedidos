
<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="mtw" uri="http://www.mentaframework.org/tags-mtw/"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Teste</title>
</head>
<body>
	<mtw:form action="cadastro-item.cadastrar.mtw" method="POST">
		<h1>cadastro item</h1>
		<label>Nome do Item:</label>
		<mtw:input type="text" name="itemNome" size="30" maxlength="30" />
		<label>Preço por unidade do item:</label>
		<mtw:input type="text" name="itemPreco" />
		<label for="quantidade">Quantidade:</label>
		<input type="number" name="itemQuantidade" min="1" step="1"
			value="<mtw:out value="itemQuantidade" />" />
		<label>Tipo de unidade do item: </label>
		<label for="tipoUnidade">Unidade de Medida:</label>
		<mtw:select name="tipoUnidade" list="listaTipoUnd" />

		<input type="submit" value="Enviar" />

		<div style="color: red; font-weight: bold;">
			<mtw:out value="erro" />
		</div>
	</mtw:form>
</body>
</html>