package br.edu.ifsp.dsw1.controller.command;

import java.io.IOException;
import java.util.List;

import br.edu.ifsp.dsw1.model.dao.ContactDaoFactory;
import br.edu.ifsp.dsw1.model.entity.Contact;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SearchContactCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		var email = request.getParameter("textEmail");
		var dao = new ContactDaoFactory().factory();
		
		List<Contact> list;
		var contact = dao.retrieve(email);
		if (contact != null) { 
			// Contato encontrado, apresenta a lista com apenas um contato.
			list = List.of(contact);
		} else {
			// Contato não encontrado, apresenta-se a lista completa e mensagem de erro.
			list = dao.retrieve();
			request.setAttribute("errorMessage", "E-mail " + email + " não localizado.");
		}
		request.setAttribute("contacts", list);
		
		return "contacts.jsp";
	}

}