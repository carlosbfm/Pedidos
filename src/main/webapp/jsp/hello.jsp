<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="mtw" uri="http://www.mentaframework.org/tags-mtw/" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Resultado do Cadastro</title>
</head>
<body>
    <h2>Teste:</h2>

    <p style="color: green;">
        <mtw:out value="mensagem" />
    </p>

    <div>
        <p><strong>Nome:</strong> <mtw:out value="item.nomeItem" /></p>
        <p><strong>Preço:</strong> R$ <mtw:out value="item.precoItem" /></p>
        <p><strong>Quantidade:</strong> <mtw:out value="item.quantidadeDoItem" /></p>
        <p><strong>Unidade:</strong> <mtw:out value="item.tipoUnidadeDeMedida" /></p>
    </div>
</body>
</html>