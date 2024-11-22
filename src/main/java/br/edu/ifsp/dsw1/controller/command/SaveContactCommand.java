package br.edu.ifsp.dsw1.controller.command;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import br.edu.ifsp.dsw1.model.dao.ContactDao;
import br.edu.ifsp.dsw1.model.entity.Contact;


public class SaveContactCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		var name = request.getParameter("textName");
		var fone = request.getParameter("textFone");
		var email = request.getParameter("textEmail");
		ContactDao dao = new ContactDao();
		
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

}