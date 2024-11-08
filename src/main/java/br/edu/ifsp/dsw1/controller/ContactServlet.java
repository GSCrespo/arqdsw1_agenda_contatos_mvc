package br.edu.ifsp.dsw1.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		
		var dispatcher = request.getRequestDispatcher("contact_form.jsp");
		dispatcher.forward(request, response);
	}

}