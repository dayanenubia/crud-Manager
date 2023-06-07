<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
	<head>
		<%@include file="base-head.jsp"%>
	</head>
	<body>
		<%@include file="nav-menu.jsp"%>
		
		<div id="container" class="container-fluid">
			<h3 class="page-header">${not empty dependente ? 'Atualizar' : 'Cadastrar'} Dependente</h3>
			
			<form action="${pageContext.request.contextPath}/dependente/${action}" method="POST">
			
				<input type="hidden" value="${dependente.getId()}" name="dependenteId">
				
				<div class="row">
					
					<div class="form-group col-md-6">
						<label for="name">Nome</label>
						<input type="text" class="form-control" id="nome" name="nome" 
							   autofocus="autofocus" placeholder="Nome Dependente" 
							   required oninvalid="this.setCustomValidity('Por favor, informe o nome do dependente.')"
							   oninput="setCustomValidity('')" value="${dependente.getNome()}"/>
					</div>
					
					<div class="form-group col-md-6">
						<label for="parentesco">Parentesco</label>
						<input type="text" class="form-control" id="parentesco" name="parentesco" 
							   autofocus="autofocus" placeholder="Grau de Parentesco" 
							   required oninvalid="this.setCustomValidity('Por favor, informe o parentesco.')"
							   oninput="setCustomValidity('')" value="${dependente.getParentesco()}"/>
					</div>
					
				</div>
				
				<div class="row">
					
					<div class="form-group col-md-4">
						<label for="dataNasc">Data de Nascimento</label>
						<input type="date" class="form-control" id="dataNasc" name="dataNasc" 
							   autofocus="autofocus" placeholder="Data de nascimento" 
							   required oninvalid="this.setCustomValidity('Por favor, informe a data de nascimento.')"
							   oninput="setCustomValidity('')" value="${dependente.getDataNasc()}"/>
					</div>
					
					<div class="form-group col-md-4">
						<label for="infoSaude">Informações sobre a Saúde</label>
						<input type="text" class="form-control" id="infoSaude" name="infoSaude" 
							   autofocus="autofocus" placeholder="Informacoes sobre a saude" 
							   required oninvalid="this.setCustomValidity('Por favor, informe informacoes sobre a saude.')"
							   oninput="setCustomValidity('')" value="${dependente.getInfoSaude()}"/>
					</div>
					
					<div class="form-group col-md-4">
						<label for="user">Usuário</label>
						<select id="user" class="form-control selectpicker" name="user" 
							    required oninvalid="this.setCustomValidity('Por favor, informe o usuário.')"
							    oninput="setCustomValidity('')">
							    
						  <option value="" ${not empty dependente ? "" : 'selected' } >Selecione um usuario</option>
						  
						  <c:forEach var="user" items="${users}">
						  	<option value="${user.getId()}" 
						  	${dependente.getUser().getId() == user.getId() ? 'selected' : ""}>
						  		${user.getName()}
						  	</option>	
						  </c:forEach>
						</select>
					</div>
					
				</div>
				
				<hr />
				
				<div id="actions" class="row pull-right">
					<div class="col-md-12">
						<a href="${pageContext.request.contextPath}/dependentes" class="btn btn-default">Cancelar</a>
						<button type="submit" class="btn btn-primary">${not empty dependente ? 'Atulizar' : 'Cadastrar'} Dependente</button>
					</div>
				</div>
				
			</form>
		</div>
				
	</body>
</html>