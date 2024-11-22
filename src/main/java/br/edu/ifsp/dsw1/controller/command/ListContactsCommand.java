package br.edu.ifsp.dsw1.controller.command;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.edu.ifsp.dsw1.model.dao.ContactDao;
import br.edu.ifsp.dsw1.model.entity.Contact;


public class ListContactsCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ContactDao dao = new ContactDao();
		List<Contact> contacts = dao.retrieve();
		request.setAttribute("contacts", contacts);
		
		return "contacts.jsp";
	}

}