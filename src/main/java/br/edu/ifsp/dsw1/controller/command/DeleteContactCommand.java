package br.edu.ifsp.dsw1.controller.command;

import java.io.IOException;

import br.edu.ifsp.dsw1.model.dao.ContactDao;
import br.edu.ifsp.dsw1.model.dao.JsonContactDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteContactCommand implements Command{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		ContactDao dao = new JsonContactDao();
		var contact = dao.retrieve(email);
		dao.delete(contact);
		return "contact.do?action=list";
	}
}
