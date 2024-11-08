<%@page import="br.edu.ifsp.dsw1.model.entity.Contact"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:include page="includes/head.html" />
<body>
	<jsp:include page="includes/navBar.jsp" />

	<%
	var contacts = (List<Contact>)request.getAttribute("contacts");
	if (contacts == null) {
		response.sendRedirect("index.jsp");
	} else {
	%>
	<main class="container-sm flex-grow-1  justify-content-center">
		<h1 style="text-align: center; margin: 30px;">Contatos Cadastrados</h1>
		<table class="table">
			<thead>
				<tr>
					<th scope="col">#</th>
					<th scope="col">Nome</th>
					<th scope="col">Telefone</th>
					<th scope="col">E-mail</th>
				</tr>
			</thead>
			<tbody class="table-group-divider">
			<%
			int i = 1;
			for (var contact : contacts) {
			%>
				<tr>
					<th scope="row"><%= i %></th>
					<td><%= contact.getName() %></td>
					<td><%= contact.getFone() %></td>
					<td><%= contact.getEmail() %></td>
				</tr>
			<%} %>
			</tbody>
		</table>

	</main>
	<%} %>
	<jsp:include page="includes/scripts.html" />
</body>
</html>