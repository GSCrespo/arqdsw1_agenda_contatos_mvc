package br.edu.ifsp.dsw1.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import br.edu.ifsp.dsw1.model.dao.ContactDao;
import br.edu.ifsp.dsw1.model.entity.Contact;


@WebServlet("/contact.do")
public class ContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ContactDao dao;
       
	@Override
	public void init() throws ServletException {
		super.init();
		dao = new ContactDao();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		String view;
		
		if ("list".equals(action)) {
			view = handleList(request, response);
		} else if ("newContact".equals(action)) {
			view = handleSaveContact(request, response);
		} else if ("getForm".equals(action)) {
			view = handleForm(request, response);
		} else {
			view = "index.jsp";
		}
		
		var dispatcher = request.getRequestDispatcher(view);
		dispatcher.forward(request, response);
	}

	private String handleForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "contact_form.jsp";
	}

	private String handleSaveContact(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		var name = request.getParameter("textName");
		var fone = request.getParameter("textFone");
		var email = request.getParameter("textEmail");
		
		Contact contact = new Contact(name, fone, email);
		boolean saved = dao.create(contact);
		
		String message;
		if (saved) {
			message = "Contato salvo com sucesso!";
		} else {
			message = "Erro ao salvar contato. Verifique se o e-mail já consta na lista de contatos.";
		}
		
		request.setAttribute("message", message);
		request.setAttribute("saved", saved);
		
		return "contact_form.jsp";		
	}

	private String handleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Contact> contacts = dao.retrieve();
		request.setAttribute("contacts", contacts);
		
		return "contacts.jsp";
	}
}