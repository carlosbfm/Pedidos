<%@page pageEncoding="UTF-8" %>
<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="mtw" uri="http://www.mentaframework.org/tags-mtw/"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Teste</title>
</head>
<body>
	<mtw:form action="cadastro-item.cadastrar.mtw" method="post">
		<h1>cadastro item</h1>
			<label>Nome do Item:</label>
			<mtw:input type="text" name="item.nome" size="30"
			 maxlength="30" />
			 <label>Preço por unidade do item:</label>
			 <mtw:input type="text" name="item.preco" />
			 <label for="quantidade">Quantidade:</label>
			<input type="number" name="item.quantidade" min="1" step="1" value="<mtw:out value="item.quantidade" />" />
			 <label>Tipo de unidade do item: </label>
			 <label for="tipo_unidade">Unidade de Medida:</label>
			<mtw:select name="tipo_unidade" list="listaTipoUnd" />	
			
  			<input type="submit" value="Enviar" />
  			
  			<div style="color: red; font-weight: bold;">
    <mtw:out value="erro" />
</div>
	</mtw:form>
</body>
</html>